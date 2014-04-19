/*
 * Copyright (C) 2014,  Tomislav Milkovic
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
package hr.ws4is.tn5250.data;

import hr.ws4is.tn5250.Tn5250ResponseScreen;
import hr.ws4is.tn5250.implementation.TnSessionListener;
import hr.ws4is.tn5250.implementation.TnStreamProcessor;
import hr.ws4is.websocket.WebSocketSession;

import org.tn5250j.Session5250;
import org.tn5250j.TN5250jConstants;

/**
 * Represents user single telnet connection property for web client
 * Web client can have multiple 5250 connections so multiple displays will be used 
 */
public class TnSession {

	//unique display id per user session 
	//(needed because displayName can be null and it's impossible to know host generated display name)
	public String displayId;
	
	//display name of 5250 connection (can be null)
	public String displayName;
	
	//virtual host name to which real configuration is mapped
	public String hostName;
		
	private Session5250 session;
	
	public TnSession(){}
	
	public TnSession(String displayId, String displayName,String hostName, Session5250 session) {
		this.session  = session;
		this.displayId = displayId;
		this.displayName = displayName;
		this.hostName = hostName;
	}
	
	/*process key request*/
	public void process(TnScreenRequest request, TnScreenElement[] fields){		
		TnStreamProcessor.process(session, request, fields);
	}
	
	/*resends last screen*/
	public Tn5250ResponseScreen refresh(){
		Tn5250ResponseScreen response = new Tn5250ResponseScreen(true, null);
		response.setDisplayID(this.displayId);
		TnStreamProcessor.refresh(session, response);
		return response;
	}

	public String getDisplayId() {
		return displayId;
	}
	
	public void setDisplayId(String displayId) {
		this.displayId = displayId;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getHostName() {
		return hostName;
	}
	
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
		
	public synchronized void disconnect(){
		if(!session.isConnected()) return;
		session.disconnect();
	}
	
	public boolean isConnected(){
		return session.isConnected();
	}
	
	public String toString(){
		return displayId + ":" + displayName + ":" + hostName;
	}
	
	public void updateWebSocketSession(WebSocketSession wsSession){
		TnSessionListener listener = new TnSessionListener(wsSession, getDisplayId());
		session.addSessionListener(listener);
		session.fireSessionChanged(TN5250jConstants.STATE_CONNECTED);
	}
}
