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
package hr.ws4is.tn5250;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import hr.ws4is.ext.ExtJSResponse;
import hr.ws4is.ext.annotations.ExtJSAction;
import hr.ws4is.ext.annotations.ExtJSDirect;
import hr.ws4is.ext.annotations.ExtJSMethod;
import hr.ws4is.tn5250.data.TnHost;
import hr.ws4is.tn5250.data.TnScreenElement;
import hr.ws4is.tn5250.data.TnScreenRequest;
import hr.ws4is.tn5250.data.TnSession;
import hr.ws4is.tn5250.implementation.TnConstants;
import hr.ws4is.tn5250.implementation.TnConfigLoader;
import hr.ws4is.tn5250.implementation.TnSessionFactory;
import hr.ws4is.websocket.WebSocketSession;

/**
 * Main websocket controller that will be invoked from browser  
 */
@ExtJSDirect(paths={"socket"})
@ExtJSAction(namespace = "hr.ws4is", action="Controller")
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
			Collection<TnSession> list =  getTnSessions().values();
			for(TnSession tnSession : list){
				tnSession.disconnect();
			}
			response = new ExtJSResponse(true,null);	
		}catch(Exception e){
			response = new ExtJSResponse(e,e.getMessage());
		}	
        return response;
	}
	
	/*
	 * Reload AS/400 server configurations
	 */
	@ExtJSMethod("reload5250Definitions")
	public ExtJSResponse reload5250Definitions() {
		ServletContext  servletContext = session.getHttpSession().getServletContext();
		ExtJSResponse response = null;
		try{
			servletContext.setAttribute(TnHost.class.getCanonicalName(), TnConfigLoader.reload());
			response = new ExtJSResponse(true,null);	
		}catch(Exception e){
			response = new ExtJSResponse(e,e.getMessage());
		}	
        return response;
	}
	
	/*
	 * List all available AS/400 servers for connections
	 */
	@ExtJSMethod("list5250Definitions")
	public Tn5250ResponseList list5250Definitions() {
		Tn5250ResponseList response = null;
		try{
			Map<String,TnHost> hosts = getTnHosts();			
			response = new Tn5250ResponseList(true,null);			
			response.setData(hosts.keySet());
		} catch (Exception e){
			response = new Tn5250ResponseList(e,e.getMessage());
		}

        return response;
	}	
	
	/*
	 * List all active 5250 sessions for current web session
	 */
	@ExtJSMethod("list5250Sessions")
	public Tn5250ResponseList list5250Sessions() {
		Tn5250ResponseList response = null;
		try{
			Map<String, TnSession> sessions = getTnSessions();		
			response = new Tn5250ResponseList(true,null);			
			response.setData(sessions.keySet());
		} catch (Exception e){
			response = new Tn5250ResponseList(e,e.getMessage());
		}		
        return response;
	}
	
	
	/*---------------------------------------------------------------------------------------------------*/
		
	/*
	 * Open new session for available AS/400 connection
	 */
	@ExtJSMethod("open5250Session")
	public Tn5250ResponseScreen open5250Session(String data) {
		Tn5250ResponseScreen response = null;
		try{
			final TnHost host = findTnHost(data);
			if(host==null){
				response = new Tn5250ResponseScreen(false,TnConstants.HOST_NOT_FOUND);	
			}else{
				final String displayId = Long.toString(System.nanoTime());				
				response = new Tn5250ResponseScreen(true,null);
				response.setLocked(true);
				response.setClearScr(true);
				response.setDisplayID(displayId);
				
				Runnable rn = new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(100);
							TnSession tnSession = TnSessionFactory.create(session, host, displayId);
							getTnSessions().put(tnSession.displayId, tnSession);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				Thread t = new Thread(rn);
				t.start();
				
			}			
			
		} catch (Exception e){
			response = getErrorResponse(e);
		}		
        return response;
	}
	
	/*
	 * Close active 5250 session 
	 */
	@ExtJSMethod("close5250Session")
	public Tn5250ResponseScreen close5250Session(String data) {
		Tn5250ResponseScreen response = null;
		try{
			Map<String, TnSession> sessions = getTnSessions(); 
			TnSession tnSession = sessions.remove(data);
			if(tnSession==null){
				response = getSessionNotFoundResponse();	
			}else{				
				response = new Tn5250ResponseScreen(true,null);
				response.setLocked(true);
				response.setClearScr(true);
				response.setMsg(TnConstants.NOT_CONNECTED);
				tnSession.disconnect();
			}			
			
		} catch (Exception e){
			response = getErrorResponse(e);
		}
		response.setDisplayID(data);
        return response;
	}

	/*
	 * Receive command for active 5250 session
	 */
	@ExtJSMethod("refresh5250Session")
	public Tn5250ResponseScreen refresh5250Session(String data) {
		Tn5250ResponseScreen response = null;
		try{
			TnSession tnSession = findTnSession(data);
			if(tnSession==null){
				response = getSessionNotFoundResponse();	
			}else{
				response = tnSession.refresh();
			}						
		} catch (Exception e){
			response = getErrorResponse(e);
		}
		response.setDisplayID(data);
        return response;		
	}
	
	/*
	 * Receive command for active 5250 session
	 */
	@ExtJSMethod("request5250Session")
	public Tn5250ResponseScreen request5250Session(TnScreenRequest data, TnScreenElement[] fields) {
		Tn5250ResponseScreen response = null;
		try{
			TnSession tnSession = findTnSession(data);			
			if(tnSession==null){
				response = getSessionNotFoundResponse();
			}else{				
				tnSession.process(data, fields);
				return null;
			}			
			
		} catch (Exception e){
			response = getErrorResponse(e);
		}
		response.setDisplayID(data.getDisplayID());
        return response;
	}
	
	/*---------------------------------------------------------------------------------------------------*/
	
	private Tn5250ResponseScreen getSessionNotFoundResponse(){		
		Tn5250ResponseScreen response = new Tn5250ResponseScreen(false,TnConstants.SESSION_NOT_FOUND);
		response.setConerr(TnConstants.SESSION_NOT_FOUND);
		response.setLocked(false);
		response.setClearScr(false);
		return response;		
	}
	
	private Tn5250ResponseScreen getErrorResponse(Exception e){		
		Tn5250ResponseScreen response = new Tn5250ResponseScreen(e, e.getMessage());
		response.setConerr(TnConstants.REQUEST_ERROR);
		response.setLocked(false);
		response.setClearScr(false);
		return response;
	}
	
	private TnHost findTnHost(String name){
		return getTnHosts().get(name);
	}	
	
	private TnSession findTnSession(TnScreenRequest data){
		return getTnSessions().get(data.getDisplayID());
	}

	private TnSession findTnSession(String name){
		return getTnSessions().get(name);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, TnHost> getTnHosts(){
		ServletContext  servletContext = session.getHttpSession().getServletContext();
		Map<String,TnHost> hosts = (Map<String, TnHost>) servletContext.getAttribute(TnHost.class.getCanonicalName());			
		return hosts;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, TnSession> getTnSessions(){
		Map<String, TnSession> sessions = (Map<String, TnSession>) session.getHttpSession().getAttribute(TnSession.class.getCanonicalName());
		return sessions;
	}
}
