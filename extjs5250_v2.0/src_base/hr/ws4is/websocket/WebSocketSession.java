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

import hr.ws4is.WS4ISConstants;
import hr.ws4is.websocket.data.WebSocketInstruction;
import hr.ws4is.websocket.data.WebSocketResponse;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.EncodeException;
import javax.websocket.Extension;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

public class WebSocketSession implements Session {
	private final Session session;
	
	public WebSocketSession(Session session) {
		super();
		this.session = session;
	}

	public WebSocketSession(Session session, HttpSession httpSession) {
		this.session = session;
		if(httpSession!=null){
			session.getUserProperties().put(HttpSession.class.getCanonicalName(), httpSession);
		}
	}
	
	@Override
	public void addMessageHandler(MessageHandler arg0)	throws IllegalStateException {
		session.addMessageHandler(arg0);
	}

	@Override
	public void close() throws IOException {
		if(!session.isOpen()) 
		{
			return ;
		}
		
		WebSocketResponse response = new WebSocketResponse(null, null, WebSocketInstruction.BYE);
		try {
			session.getBasicRemote().sendObject(response);
		} catch (EncodeException e) {
			e.printStackTrace();
		}
		close(new CloseReason(CloseCodes.NORMAL_CLOSURE,""));
	}

	@Override
	public void close(CloseReason arg0) throws IOException {
		session.close(arg0);
	}

	@Override
	public Async getAsyncRemote() {
		return session.getAsyncRemote();
	}

	@Override
	public Basic getBasicRemote() {
		return session.getBasicRemote();
	}

	@Override
	public WebSocketContainer getContainer() {
		return session.getContainer();
	}

	@Override
	public String getId() {
		return session.getId();
	}

	@Override
	public int getMaxBinaryMessageBufferSize() {
		return session.getMaxBinaryMessageBufferSize();
	}

	@Override
	public long getMaxIdleTimeout() {
		return session.getMaxIdleTimeout();
	}

	@Override
	public int getMaxTextMessageBufferSize() {
		return session.getMaxTextMessageBufferSize();
	}

	@Override
	public Set<MessageHandler> getMessageHandlers() {
		return session.getMessageHandlers();
	}

	@Override
	public List<Extension> getNegotiatedExtensions() {
		return session.getNegotiatedExtensions();
	}

	@Override
	public String getNegotiatedSubprotocol() {
		return session.getNegotiatedSubprotocol();
	}

	@Override
	public Set<Session> getOpenSessions() {
		return session.getOpenSessions();
	}

	@Override
	public Map<String, String> getPathParameters() {
		return session.getPathParameters();
	}

	@Override
	public String getProtocolVersion() {
		return session.getProtocolVersion();
	}

	@Override
	public String getQueryString() {
		return session.getQueryString();
	}

	@Override
	public Map<String, List<String>> getRequestParameterMap() {
		return session.getRequestParameterMap();
	}

	@Override
	public URI getRequestURI() {
		return session.getRequestURI();
	}

	@Override
	public Principal getUserPrincipal() {
		return session.getUserPrincipal();
	}

	@Override
	public Map<String, Object> getUserProperties() {
		return session.getUserProperties();
	}

	@Override
	public boolean isOpen() {
		return session.isOpen();
	}

	@Override
	public boolean isSecure() {
		return session.isSecure();
	}

	@Override
	public void removeMessageHandler(MessageHandler arg0) {
		session.removeMessageHandler(arg0);
	}

	@Override
	public void setMaxBinaryMessageBufferSize(int arg0) {
		session.setMaxBinaryMessageBufferSize(arg0);
	}

	@Override
	public void setMaxIdleTimeout(long arg0) {
		session.setMaxIdleTimeout(arg0);
	}

	@Override
	public void setMaxTextMessageBufferSize(int arg0) {
		session.setMaxTextMessageBufferSize(arg0);
	}
	
	public HttpSession getHttpSession(){
		return (HttpSession) session.getUserProperties().get(HttpSession.class.getCanonicalName());
	}

	public boolean isValidHttpSession() {
		HttpSession httpSession = getHttpSession();
		if(httpSession==null) return false;
		String attr = (String)httpSession.getAttribute(WS4ISConstants.HTTP_SEESION_STATUS);		
		return "true".equals(attr);
	}

	@Override
	public boolean equals(Object obj) {
		boolean status = false;
		if(obj instanceof WebSocketSession)
		{
			try{
				Field f = WebSocketSession.class.getField("session");
				f.setAccessible(true);
				Object o = f.get(obj);
				status  = session.equals(o);			
			}catch(Exception e){
				status = false;
			}			
		}
		return status;
	}

	@Override
	public int hashCode() {
		return session.hashCode();
	}
	
}
