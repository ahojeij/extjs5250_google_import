/*
 * Copyright (C) 2010,  Tomislav Milkovic
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package hr.ws4is.websocket;

import hr.ws4is.JsonDecoder;
import hr.ws4is.WS4ISConstants;
import hr.ws4is.ext.ExtJSDirectRequest;
import hr.ws4is.ext.ExtJSDirectResponse;
import hr.ws4is.ext.ExtJSResponse;
import hr.ws4is.websocket.data.WebSocketInstruction;
import hr.ws4is.websocket.data.WebSocketRequest;
import hr.ws4is.websocket.data.WebSocketResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Internal CDI injectable object used by WebSocket endpoint instance.
 * Used to separate internal logic from WebSocketService.
 *
 */
public class WebSocketEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEndpoint.class);
    private static final ThreadLocal<WebSocketSession> websocketContextThreadLocal = new ThreadLocal<WebSocketSession>();

    @Inject
    private Event<WebsocketEvent> webSocketEvent;

    @Inject
    private WebSocketOperations<JsonNode> directOperations;

    @Produces
    private WebSocketSession sessionProducer() {
        return websocketContextThreadLocal.get();
    }

    /*
     * PUBLIC SECTION
     */
    public final void onMessage(final WebSocketRequest message, final Session session) {
        WebSocketSession wsession = null;

        try {

            if (!WS4ISConstants.WEBSOCKET_TYPE.equals(message.getType())) {
                return;
            }

            wsession = new WebSocketSession(session);
            websocketContextThreadLocal.set(wsession);
            webSocketEvent.fire(new WebsocketEvent(wsession, WebSocketEventStatus.MESSAGE));

            switch (message.getCmd()) {
            case WELCO:
                processSimple(wsession, message);
                break;
            case DATA:
                processData(wsession, message);
                break;
            case ECHO:
                processSimple(wsession, message);
                break;
            case BYE:
                processSimple(wsession, message);
                break;
            case ERR:
                break;
            default:
                break;
            }

        } catch (Exception exception) {
            LOGGER.error(exception.getMessage(), exception);
            final WebSocketResponse wsResponse = getErrorResponse(exception);
            wsession.sendResponse(wsResponse, true);
        } finally {
            websocketContextThreadLocal.remove();
        }
    }

    // allow this websocket endpoint only for clients with valid session
    // attached
    public final void onOpen(final Session session, final EndpointConfig config) {

        try {
            final HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
            final WebSocketSession wsession = new WebSocketSession(session, httpSession);

            session.getUserProperties().put(WS4ISConstants.WEBSOCKET_PATH, config.getUserProperties().get(WS4ISConstants.WEBSOCKET_PATH));

            websocketContextThreadLocal.set(wsession);
            webSocketEvent.fire(new WebsocketEvent(wsession, WebSocketEventStatus.START));

            if (!wsession.isValidHttpSession()) {
                LOGGER.error(WS4ISConstants.HTTP_SEESION_REQUIRED);
                final IllegalStateException ise = new IllegalStateException(WS4ISConstants.HTTP_SEESION_REQUIRED);
                final WebSocketResponse wsResponse = getErrorResponse(ise);
                final String responseString = JsonDecoder.getJSONEngine().writeValueAsString(wsResponse);
                session.close(new CloseReason(CloseCodes.VIOLATED_POLICY, responseString));
            }

        } catch (IOException exception) {
            LOGGER.error(exception.getMessage(), exception);
        } finally {
            websocketContextThreadLocal.remove();
        }

    }

    public final void onClose(final Session session, final CloseReason reason) {

        final WebSocketSession wsession = new WebSocketSession(session, null);
        try {
            websocketContextThreadLocal.set(wsession);
            webSocketEvent.fire(new WebsocketEvent(wsession, WebSocketEventStatus.CLOSE));
        } finally {
            websocketContextThreadLocal.remove();
        }
    }

    public final void onError(final Session session, final Throwable throwable) {

        final WebSocketSession wsession = new WebSocketSession(session, null);
        try {
            websocketContextThreadLocal.set(wsession);
            webSocketEvent.fire(new WebsocketEvent(wsession, WebSocketEventStatus.ERROR, throwable));
        } finally {
            websocketContextThreadLocal.remove();
        }
    }

    /*
     * PRIVATE SECTION
     */

    private WebSocketResponse getErrorResponse(final Exception exception) {
        final ExtJSResponse response = new ExtJSResponse(exception, exception.getMessage());
        WebSocketResponse wsResponse = new WebSocketResponse(WebSocketInstruction.ERR);
        wsResponse.setData(response);
        wsResponse.setErrMsg(exception.getMessage());
        return wsResponse;
    }

    private void processSimple(final WebSocketSession session, final WebSocketRequest message) {
        final WebSocketResponse wsResposne = new WebSocketResponse(message.getCmd());
        session.sendResponse(wsResposne, true);
    }

    private void processData(final WebSocketSession session, final WebSocketRequest wsMessage) throws IOException, EncodeException {

        final List<ExtJSDirectResponse<?>> responseList = new ArrayList<ExtJSDirectResponse<?>>();
        final Map<String, Object> map = session.getUserProperties();
        final String wsPath = (String) map.get(WS4ISConstants.WEBSOCKET_PATH);

        final List<ExtJSDirectRequest<JsonNode>> requests = wsMessage.getData();
        for (final ExtJSDirectRequest<JsonNode> request : requests) {
            processRequest(session, request, wsPath, responseList);
        }

        final WebSocketResponse wsResponse = new WebSocketResponse(WebSocketInstruction.DATA);
        wsResponse.setData(responseList);
        session.sendResponse(wsResponse, true);
    }

    private void processRequest(final WebSocketSession session, final ExtJSDirectRequest<JsonNode> request, final String wsPath, final List<ExtJSDirectResponse<?>> responseList) throws IOException,
            EncodeException {
        ExtJSDirectResponse<?> directResponse = null;
        try {
            session.setRequest(request);
            directResponse = directOperations.process(request, session.getHttpSession(), wsPath);
        } catch (Exception exception) {
            final ExtJSResponse errorResponse = new ExtJSResponse(exception, exception.getMessage());
            directResponse = new ExtJSDirectResponse<>(request, errorResponse);
        }
        responseList.add(directResponse);
    }

}
