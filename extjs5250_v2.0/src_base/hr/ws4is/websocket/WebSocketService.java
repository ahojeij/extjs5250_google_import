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

import hr.ws4is.websocket.data.WebSocketRequest;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;


public abstract class WebSocketService  {  

	@Inject 
	private WebSocketEndpoint endpoint;
	
	@OnMessage
	public void onMessage(WebSocketRequest message, Session session) {	
		endpoint.onMessage(message, session);
	}

	@OnOpen
	public void onOpen(final Session session, final EndpointConfig config) {	
		endpoint.onOpen(session, config);
	}

	@OnClose
	public void onClose(final Session session, final CloseReason reason) {
		endpoint.onClose(session, reason);
	}

	@OnError
	public void onError(Session session, Throwable t) {
		endpoint.onError(session, t);
	}

}
