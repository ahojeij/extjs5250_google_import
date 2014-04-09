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
import javax.websocket.EndpointConfig;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.Session;

import com.fasterxml.jackson.databind.JsonNode;


public class WebSocketEndpoint {	
	final static String WS = "ws";
	final static String WELCO = "{type:'ws' , protocol:'ws4is' , version : '1.0' , cmd :'welco'}";
	final static String BYE = "{type:'ws' , cmd :'bye'}";
	
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
			
			if(!WS.equals(message.getType())){
				return;
			}

			wsession = new WebSocketSession(session);
			websocketContextThreadLocal.set(wsession);
			webSocketEvent.fire(new WebsocketEvent(wsession, WebSocketEventStatus.MESSAGE ));
			
			switch(message.getCmd()){
				case WELCO : processWelco(wsession,message); break;	
				case DATA : processData(wsession,message); break;
				case ECHO : processEcho(wsession,message); break;
				case BYE : processBye(wsession,message); break;
				case ERR : break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			WebSocketResponse wsResponse = new WebSocketResponse(WebSocketInstruction.DATA);
			ExtJSResponse directResponse = new ExtJSResponse(e,e.getMessage()) ; 
			wsResponse.data = directResponse;
			sendResponse(wsResponse,wsession);		
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
			webSocketEvent.fire(new WebsocketEvent(wsession,WebSocketEventStatus.START));
			
			if(!wsession.isValidHttpSession()){
				System.out.println("Websocket requires valid http session");
				WebSocketResponse response = new WebSocketResponse(WebSocketInstruction.ERR);
				response.errMsg = "Websocket requires valid http session";
				String responseString =  JsonDecoder.getJSONEngine().writeValueAsString(response);
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
			webSocketEvent.fire(new WebsocketEvent(wsession,WebSocketEventStatus.CLOSE));	
		} finally {
			websocketContextThreadLocal.remove();
		}		
	}

	public void onError(Session session, Throwable t) {
		
		final WebSocketSession wsession = new WebSocketSession(session, null);		
		try{
			websocketContextThreadLocal.set(wsession);
			webSocketEvent.fire(new WebsocketEvent(wsession,WebSocketEventStatus.ERROR,t));	
		} finally {
			websocketContextThreadLocal.remove();
		}		
	}
		
	/*
	 * PRIVATE SECTION
	 */
		
	private void processEcho(WebSocketSession session, WebSocketRequest message) {
		session.getAsyncRemote().sendText(message.toString());
	}
	
	private void processWelco(WebSocketSession session, WebSocketRequest wsMessage) {
		session.getAsyncRemote().sendText(WELCO);
	}

	private void processBye(WebSocketSession session, WebSocketRequest wsMessage) {
		session.getAsyncRemote().sendText(BYE);
	}	

	private void processData(WebSocketSession session, WebSocketRequest wsMessage)	{
		List<ExtJSDirectResponse<?>> responseList = new ArrayList<ExtJSDirectResponse<?>>();
		List<ExtJSDirectRequest<JsonNode>> requests =  wsMessage.getData();
		String wsPath = (String) session.getUserProperties().get(WS4ISConstants.WEBSOCKET_PATH);
		for(ExtJSDirectRequest<JsonNode> request : requests ){
			ExtJSDirectResponse<?> directResponse = null;
			try{
				session.setTransactionID(request.getTid());
				directResponse = directOperations.process(request,session.getHttpSession(),wsPath);
				responseList.add(directResponse);				
			}catch(Exception e){
				ExtJSResponse errorResponse = new ExtJSResponse(e, e.getMessage());
				ExtJSDirectResponse<?> errorDirectResponse = new ExtJSDirectResponse<>(request, errorResponse) ;
				responseList.add(errorDirectResponse);
			}

			if(!session.isOpen()){
				break;
			}
		}
		
		//send response
		if(session.isOpen()){			
			WebSocketResponse wsResponse = new WebSocketResponse(WebSocketInstruction.DATA);
			wsResponse.data = responseList;
			sendResponse(wsResponse,session);
		}		
	}
	
	private void sendResponse(WebSocketResponse data , WebSocketSession session){
		if(data!=null && session.isOpen()){			
			try {
				Async basic = session.getAsyncRemote();
				basic.sendObject(data);
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}	

}
