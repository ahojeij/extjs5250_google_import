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
import hr.ws4is.data.tn3812.Tn3812Response;
import hr.ws4is.ext.ExtJSResponse;
import hr.ws4is.ext.ExtJSResponseList;
import hr.ws4is.ext.annotations.ExtJSAction;
import hr.ws4is.ext.annotations.ExtJSDirect;
import hr.ws4is.ext.annotations.ExtJSMethod;
import hr.ws4is.tn3812.Tn3812ClientFactory;
import hr.ws4is.tn3812.Tn3812Config;
import hr.ws4is.tn3812.drivers.Tn3812DriverFactory;
import hr.ws4is.tn3812.interfaces.ITn3812Config;
import hr.ws4is.tn3812.interfaces.ITn3812Context;
import hr.ws4is.tn3812.interfaces.ITn3812DataListener;
import hr.ws4is.web.TnWebHelper;
import hr.ws4is.websocket.WebSocketSession;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

/**
 * Controller for 3812 printers that will be invoked from browser through websocket
 * Used for managing printer sessions. Starting , stopping and report notifications. 
 */
@ExtJSDirect(paths={"socket"})
@ExtJSAction(namespace = "hr.ws4is", action="Tn3812Controller")
public class Tn3812Controller {

	@Inject 
	WebSocketSession session;	 
	
	/*
	 * close all tn3812 sessions
	 */
	@ExtJSMethod("closeSessions")
	public ExtJSResponse closeSessions() {
		ExtJSResponse response = null;
		try{
			Collection<ITn3812Context> list =  TnWebHelper.getTn3812Sessions(session).values();
			for(ITn3812Context tnSession : list){
				tnSession.disconnect();
			}
			response = new ExtJSResponse(true,null);	
		}catch(Exception e){
			response = new ExtJSResponse(e,e.getMessage());
		}	
        return response;
	}
		
	/*
	 * list all tn3812 sessions for current user
	 */
	@ExtJSMethod("listSessions")
	public ExtJSResponseList<String> listSessions() {
		ExtJSResponseList<String> response = null;
		try{
			Map<String, ITn3812Context> sessions = TnWebHelper.getTn3812Sessions(session);		
			response = new ExtJSResponseList<>(true,null);			
			response.setData(sessions.keySet());
		} catch (Exception e){
			response = new ExtJSResponseList<>(e,e.getMessage());
		}		
        return response;
	}
	
	
	/*
	 * create new tn3812 session
	 */
	@ExtJSMethod("openSession")
	public Tn3812Response openSession(String hostName, String printerName) {
		Tn3812Response response = null;
		try{
			printerName = printerName.toUpperCase();
			final TnHost host = TnWebHelper.findTnHost(session, hostName);
			if(host==null){
				response = new Tn3812Response(false,TnConstants.HOST_NOT_FOUND);	
			}else{
				final Map<String, ITn3812Context> sessions = TnWebHelper.getTn3812Sessions(session); 
				ITn3812Context tnSession = sessions.get(printerName);
				
				response = new Tn3812Response(true,null);
				response.setPrinterName(printerName);
				
				if(tnSession==null){
					
					try {
						
						ITn3812Config config = new Tn3812Config();
						config.setDevName(printerName);
						
						tnSession = Tn3812ClientFactory.createSession(host.getIpAddress(), host.getPortNumber(), config);
						
						//add pdf generator 
						ITn3812DataListener driver = Tn3812DriverFactory.create(Tn3812DriverFactory.PDF);
						tnSession.addDataListener(driver);							

						//add session listener
						tnSession.addDataListener(new Tn3812Listener(session));						
												
						tnSession.connect();
					} catch (InterruptedException | ExecutionException | IOException e) {
						e.printStackTrace();
					}
					
				}
				
			}			
			
		} catch (Exception e){
			response = TnWebHelper.get3812ErrorResponse(e, printerName);
		}		
        return response;
	}
	
	/*
	 * close existing tn3812 session
	 */
	@ExtJSMethod("closeSession")
	public Tn3812Response closeSession(String printerName) {
		Tn3812Response response = null;
		try{
			printerName = printerName.toUpperCase();
			Map<String, ITn3812Context> sessions = TnWebHelper.getTn3812Sessions(session); 
			ITn3812Context tnSession = sessions.remove(printerName);
			if(tnSession==null){
				response = TnWebHelper.get3812SessionNotFoundResponse(printerName);	
			}else{				
				response = new Tn3812Response(true,null);
				response.setMsg(TnConstants.NOT_CONNECTED);
				tnSession.disconnect();
			}			
			
		} catch (Exception e){
			response = TnWebHelper.get3812ErrorResponse(e,printerName);
		}
		response.setPrinterName(printerName);
        return response;
	}
	
	@ExtJSMethod("refreshStatus")
	public Tn3812Response refreshStatus(String printerName) {
		Tn3812Response response = null;
		try{
			printerName = printerName.toUpperCase();
			Map<String, ITn3812Context> sessions = TnWebHelper.getTn3812Sessions(session); 
			ITn3812Context tnSession = sessions.remove(printerName);
			if(tnSession==null){
				response = TnWebHelper.get3812SessionNotFoundResponse(printerName);	
			}else{				
				response = new Tn3812Response(true,null);
				//TODO fill with printer status 
			}			
			
		} catch (Exception e){
			response = TnWebHelper.get3812ErrorResponse(e,printerName);
		}
		response.setPrinterName(printerName);
        return response;
	}
	
}
