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
package hr.ws4is.web.controllers;

import hr.ws4is.data.TnConstants;
import hr.ws4is.data.TnHost;
import hr.ws4is.data.tn5250.Tn5250ScreenResponse;
import hr.ws4is.data.tn5250.Tn5250ScreenElement;
import hr.ws4is.data.tn5250.Tn5250ScreenRequest;
import hr.ws4is.ext.ExtJSResponse;
import hr.ws4is.ext.ExtJSResponseList;
import hr.ws4is.ext.annotations.ExtJSAction;
import hr.ws4is.ext.annotations.ExtJSDirect;
import hr.ws4is.ext.annotations.ExtJSMethod;
import hr.ws4is.tn5250.ITn5250Session;
import hr.ws4is.tn5250.Tn5250SessionFactory;
import hr.ws4is.web.TnWebHelper;
import hr.ws4is.websocket.WebSocketSession;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

/**
 * Controller for 5250 telnet that will be invoked from browser through websocket  
 * Used for managing 5250 telnet connections, data retrieval and keyboard commands processing
 */
@ExtJSDirect(paths={"socket"})
@ExtJSAction(namespace = "hr.ws4is", action="Tn5250Controller")
public class Tn5250Controller {

	@Inject 
	WebSocketSession session;		

	/*
	 * Close all 5250 sessions
	 */
	@ExtJSMethod("closeSessions")
	public ExtJSResponse closeSessions() {

		ExtJSResponse response = null;
		try{
			Collection<ITn5250Session> list =  TnWebHelper.getTn5250Sessions(session).values();
			for(ITn5250Session tnSession : list){
				tnSession.disconnect();
			}
			response = new ExtJSResponse(true,null);	
		}catch(Exception e){
			response = new ExtJSResponse(e,e.getMessage());
		}	
        return response;
	}

	
	/*
	 * List all active 5250 sessions for current web session
	 */
	@ExtJSMethod("listSessions")
	public ExtJSResponseList<String> list5250Sessions() {
		ExtJSResponseList<String> response = null;
		try{
			Map<String, ITn5250Session> sessions = TnWebHelper.getTn5250Sessions(session);		
			response = new ExtJSResponseList<>(true,null);			
			response.setData(sessions.keySet());
		} catch (Exception e){
			response = new ExtJSResponseList<>(e,e.getMessage());
		}		
        return response;
	}
	
	
	/*---------------------------------------------------------------------------------------------------*/
		
	/*
	 * Open new session for available AS/400 connection
	 */
	@ExtJSMethod("openSession")
	public Tn5250ScreenResponse open5250Session(String data) {
		Tn5250ScreenResponse response = null;
		try{
			final TnHost host = TnWebHelper.findTnHost(session, data);
			if(host==null){
				response = new Tn5250ScreenResponse(false,TnConstants.HOST_NOT_FOUND);	
			}else{
				final String displayId = Long.toString(System.nanoTime());				
				response = new Tn5250ScreenResponse(true,null);
				response.setLocked(true);
				response.setClearScr(true);
				response.setDisplayID(displayId);
				
				//little delay for WebSocket to send display id response.
				//after successful connection to tn5250, listener will send first screen data
				//with displayID so front end can find proper display to render image on it
				Runnable rn = new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(100);
							ITn5250Session tnSession = Tn5250SessionFactory.create(session, host, displayId);
							TnWebHelper.getTn5250Sessions(session).put(tnSession.getDisplayId(), tnSession);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				Thread t = new Thread(rn);
				t.start();
				
			}			
			
		} catch (Exception e){
			response = TnWebHelper.get5250ErrorResponse(e);
		}		
        return response;
	}
	
	/*
	 * Close active 5250 session 
	 */
	@ExtJSMethod("closeSession")
	public Tn5250ScreenResponse close5250Session(String data) {
		Tn5250ScreenResponse response = null;
		try{
			Map<String, ITn5250Session> sessions = TnWebHelper.getTn5250Sessions(session); 
			ITn5250Session tnSession = sessions.remove(data);
			if(tnSession==null){
				response = TnWebHelper.get5250SessionNotFoundResponse();	
			}else{				
				response = new Tn5250ScreenResponse(true,null);
				response.setLocked(true);
				response.setClearScr(true);
				response.setMsg(TnConstants.NOT_CONNECTED);
				tnSession.disconnect();
			}			
			
		} catch (Exception e){
			response = TnWebHelper.get5250ErrorResponse(e);
		}
		response.setDisplayID(data);
        return response;
	}

	/*
	 * Receive command for active 5250 session
	 */
	@ExtJSMethod("refreshSession")
	public Tn5250ScreenResponse refresh5250Session(String data) {
		Tn5250ScreenResponse response = null;
		try{
			ITn5250Session tnSession = TnWebHelper.findTn5250Session(session, data);
			if(tnSession==null){
				response = TnWebHelper.get5250SessionNotFoundResponse();	
			}else{
				response = tnSession.refresh();
			}						
		} catch (Exception e){
			response = TnWebHelper.get5250ErrorResponse(e);
		}
		response.setDisplayID(data);
        return response;		
	}
	
	/*
	 * Receive command for active 5250 session
	 */
	@ExtJSMethod("requestSession")
	public Tn5250ScreenResponse request5250Session(Tn5250ScreenRequest data, Tn5250ScreenElement[] fields) {
		Tn5250ScreenResponse response = null;
		try{
			ITn5250Session tnSession = TnWebHelper.findTn5250Session(session, data);			
			if(tnSession==null){
				response = TnWebHelper.get5250SessionNotFoundResponse();
			}else{				
				tnSession.process(data, fields);
				return null;
			}			
			
		} catch (Exception e){
			response = TnWebHelper.get5250ErrorResponse(e);
		}
		response.setDisplayID(data.getDisplayID());
        return response;
	}
	
}
