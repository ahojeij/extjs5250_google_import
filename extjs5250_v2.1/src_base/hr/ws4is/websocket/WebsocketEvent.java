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

import javax.enterprise.inject.Vetoed;

@Vetoed
public class WebsocketEvent {
	
	private final WebSocketSession session;
	private final WebSocketEventStatus eventStatus;
	private final Throwable throwable;
	
	public WebsocketEvent(WebSocketSession session, WebSocketEventStatus eventStatus) {
		super();
		this.session = session;
		this.eventStatus = eventStatus;
		this.throwable = null;
	}
	
	public WebsocketEvent(WebSocketSession session, WebSocketEventStatus eventStatus, Throwable throwable){
		super();
		this.session = session;
		this.eventStatus = eventStatus;
		this.throwable = throwable;
	}

	public WebSocketSession getWebSocketSession() {
		return session;
	}

	public WebSocketEventStatus getEventStatus() {
		return eventStatus;
	}

	public Throwable getThrowable() {
		return throwable;
	}
			
}
