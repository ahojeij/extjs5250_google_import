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
import hr.ws4is.cdi.WebLocaleEvent;
import hr.ws4is.ext.ExtJSDirectRequest;
import hr.ws4is.ext.ExtJSDirectResponse;
import hr.ws4is.ext.ExtJSResponse;
import hr.ws4is.websocket.data.WebSocketInstruction;
import hr.ws4is.websocket.data.WebSocketRequest;
import hr.ws4is.websocket.data.WebSocketResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PreDestroy;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import com.fasterxml.jackson.databind.JsonNode;


public class WebSocketEndpoint {	
	
	static final ThreadLocal<WebSocketSession> websocketContextThreadLocal = new ThreadLocal<WebSocketSession>();
			
	private Locale locale ;

	@Inject 
	private Event<WebsocketEvent> webSocketEvent;
	
	@Inject 
	private Event<WebLocaleEvent> localeEvent;
	
	@Inject 
	private WebSocketOperations<JsonNode> directOperations;

	@PreDestroy
	void preDEstroy() {
		locale = null;
		//webSocketEvent=null;
		websocketContextThreadLocal.remove();		
	}
		
	@Produces
	private WebSocketSession sessionProducer() {
		return websocketContextThreadLocal.get();
	}
	
	/*
	* PUBLIC SECTION 
	*/	
	public void onMessage(WebSocketRequest message, Session session) {	
		WebSocketSession wsession = null;
		
		try {
			
			if(!WS4ISConstants.WEBSOCKET_TYPE.equals(message.getType())){
				return;
			}

			wsession = new WebSocketSession(session);
			websocketContextThreadLocal.set(wsession);
			webSocketEvent.fire(new WebsocketEvent(wsession, WebSocketEventStatus.MESSAGE ));
			
			switch(message.getCmd()){
				case WELCO : processSimple(wsession,message); break;	
				case DATA : processData(wsession,message); break;
				case ECHO : processSimple(wsession,message); break;
				case BYE : processSimple(wsession,message); break;
				case ERR : break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			WebSocketResponse wsResponse = getErrorResponse(e);
			wsession.sendResponse(wsResponse, true);
		} finally {
			websocketContextThreadLocal.remove();
		}
	}

	//allow this websocket endpoint only for clients with valid session attached 
	public void onOpen(final Session session, final EndpointConfig config)	{
		
		try {
			final HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
			final WebSocketSession wsession = new WebSocketSession(session,httpSession);
			
			locale = (Locale) config.getUserProperties().get(Locale.class.getCanonicalName());
			localeEvent.fire(new WebLocaleEvent(locale));
			
			session.getUserProperties().put(WS4ISConstants.WEBSOCKET_PATH,config.getUserProperties().get(WS4ISConstants.WEBSOCKET_PATH));

			websocketContextThreadLocal.set(wsession);
			webSocketEvent.fire(new WebsocketEvent(wsession, WebSocketEventStatus.START));
			
			if(!wsession.isValidHttpSession()){
				System.out.println(WS4ISConstants.HTTP_SEESION_REQUIRED);
				IllegalStateException ise = new IllegalStateException(WS4ISConstants.HTTP_SEESION_REQUIRED);
				WebSocketResponse wsResponse = getErrorResponse(ise);
				String responseString =  JsonDecoder.getJSONEngine().writeValueAsString(wsResponse);
				session.close(new CloseReason(CloseCodes.VIOLATED_POLICY, responseString));
			} 

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			websocketContextThreadLocal.remove();
		}
		
	}

	public void onClose(final Session session, final CloseReason reason) {
		
		final WebSocketSession wsession = new WebSocketSession(session, null);		
		try{
			websocketContextThreadLocal.set(wsession);
			webSocketEvent.fire(new WebsocketEvent(wsession, WebSocketEventStatus.CLOSE));	
		} finally {
			websocketContextThreadLocal.remove();
		}		
	}

	public void onError(Session session, Throwable t) {
		
		final WebSocketSession wsession = new WebSocketSession(session, null);		
		try{
			websocketContextThreadLocal.set(wsession);
			webSocketEvent.fire(new WebsocketEvent(wsession, WebSocketEventStatus.ERROR,t));	
		} finally {
			websocketContextThreadLocal.remove();
		}		
	}
		
	/*
	 * PRIVATE SECTION
	 */
	
	private WebSocketResponse getErrorResponse(Exception e){
		WebSocketResponse wsResponse = null;
		ExtJSResponse response = new ExtJSResponse(e,e.getMessage()) ;
		wsResponse = new WebSocketResponse(WebSocketInstruction.ERR);
		wsResponse.setData(response);
		wsResponse.setErrMsg(e.getMessage());
		return wsResponse;
	}
		
	private void processSimple(WebSocketSession session, WebSocketRequest message) {
		WebSocketResponse wsResposne = new WebSocketResponse(message.getCmd());
		session.sendResponse(wsResposne, true);
	}
	
	private void processData(WebSocketSession session, WebSocketRequest wsMessage) throws IOException, EncodeException	{
		List<ExtJSDirectResponse<?>> responseList = new ArrayList<ExtJSDirectResponse<?>>();		
		String wsPath = (String) session.getUserProperties().get(WS4ISConstants.WEBSOCKET_PATH);
		
		List<ExtJSDirectRequest<JsonNode>> requests =  wsMessage.getData();
		for(ExtJSDirectRequest<JsonNode> request : requests ){
			processRequest(session, request, wsPath, responseList);
		}
		
		WebSocketResponse wsResponse = new WebSocketResponse(WebSocketInstruction.DATA);
		wsResponse.setData(responseList);
		session.sendResponse(wsResponse, true);	
	}
	
	private void processRequest(WebSocketSession session, ExtJSDirectRequest<JsonNode> request, String wsPath, List<ExtJSDirectResponse<?>> responseList ) throws IOException, EncodeException	{
		ExtJSDirectResponse<?> directResponse = null;
		try{
			session.setRequest(request);
			directResponse = directOperations.process(request,session.getHttpSession(),wsPath);
		}catch(Exception e){
			ExtJSResponse errorResponse = new ExtJSResponse(e, e.getMessage());
			directResponse = new ExtJSDirectResponse<>(request, errorResponse) ;			
		}		
		responseList.add(directResponse);
	}

}
