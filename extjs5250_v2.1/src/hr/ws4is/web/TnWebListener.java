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
package hr.ws4is.web;

import hr.ws4is.data.TnConfigLoader;
import hr.ws4is.data.TnConstants;
import hr.ws4is.data.TnHost;
import hr.ws4is.tn3812.interfaces.ITn3812Context;
import hr.ws4is.tn5250.ITn5250Session;
import hr.ws4is.websocket.WebSocketEventStatus;
import hr.ws4is.websocket.WebSocketSession;
import hr.ws4is.websocket.WebsocketEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.tn5250j.tools.LangTool;

/**
 * HttpSession and Servlet context listener to handle tn5250 sessions on httpsession close  
 *
 */
@WebListener
public final class TnWebListener implements HttpSessionListener, ServletContextListener {
	
	private ServletContext servletContext; 
	
	@SuppressWarnings("unchecked")
	@Produces
	Map<String,TnHost> getTnHostList(){
		return (Map<String, TnHost>) servletContext.getAttribute(TnHost.class.getCanonicalName());
	}
		

	/*
	 *  closes on websocket disconnect ?? 
	 *  Maybe not... close only if http sessions are closed?!
	 *  because multiple browser windows might be opened
	 *  maybe to identify 5250 sessions opened websocket id ?  
	protected void onWSClose(@Observes WebsocketEvent wsEvent){		
		closeTnSession(wsEvent.getWebSocketSession().getHttpSession());
	}
	*/

	/*
	 *  reasign websocket session to active tn sessions;
	 *  this is in a case when browser is relaoded
	 */
	@SuppressWarnings("unchecked")
	protected void onWSClose(@Observes WebsocketEvent wsEvent){		
	  if(wsEvent.getEventStatus() == WebSocketEventStatus.START){
		  WebSocketSession wsSession = wsEvent.getWebSocketSession();
		  Map<String, ITn5250Session> tnSessions = (Map<String, ITn5250Session>) wsSession.getHttpSession().getAttribute(ITn5250Session.class.getCanonicalName());
		  Collection<ITn5250Session> sessions =  tnSessions.values();
		  for(ITn5250Session session : sessions){
			  if(session.isConnected()){
				  session.updateWebSocketSession(wsSession);
			  }
		  }
	  }
	}
	
	private void closeTn5250Session(HttpSession webSession){
		System.out.println("Closing Tn5250 sessions for Web session :" + webSession.getId());
		
		@SuppressWarnings("unchecked")
		Map<String,ITn5250Session> tnSessions = (Map<String, ITn5250Session>) webSession.getAttribute(ITn5250Session.class.getCanonicalName());
		Collection<ITn5250Session> sessions =  tnSessions.values();
		for(ITn5250Session session : sessions){
			if(session.isConnected()){
				System.out.println("   Closing Tn5250 session :" + session.getDisplayId());
				session.disconnect();
			}
		}
		tnSessions.clear();
		webSession.removeAttribute(ITn5250Session.class.getCanonicalName());
	}
	
	private void closeTn3812Session(HttpSession webSession){
		System.out.println("Closing Tn3812 sessions for Web session :" + webSession.getId());
		
		@SuppressWarnings("unchecked")
		Map<String,ITn3812Context> tnSessions = (Map<String, ITn3812Context>) webSession.getAttribute(ITn3812Context.class.getCanonicalName());
		Collection<ITn3812Context> sessions =  tnSessions.values();
		for(ITn3812Context session : sessions){
			if(session.isConnected()){
				System.out.println("   Closing Tn3812 session :" + session.getconfiguration().getDevName());
				session.disconnect();
			}
		}
		tnSessions.clear();
		webSession.removeAttribute(ITn5250Session.class.getCanonicalName());
	}

	
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		HttpSession webSession = arg0.getSession();
		webSession.setAttribute(ITn5250Session.class.getCanonicalName(), new HashMap<String,ITn5250Session>());
		webSession.setAttribute(ITn3812Context.class.getCanonicalName(), new HashMap<String,ITn3812Context>());
		webSession.setAttribute(TnConstants.SESSION_COUNTER, new AtomicInteger());
	}

	/*
	 * Clean all 5250 sessions for current session
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {		
		HttpSession webSession = arg0.getSession();
		webSession.removeAttribute(TnConstants.SESSION_COUNTER);
		closeTn5250Session(webSession);
		closeTn3812Session(webSession);
	}

	/*
	 * remove 5250 server configurations from memory 
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		arg0.getServletContext().removeAttribute(TnHost.class.getCanonicalName());
	}

	/*
	 * Load 5250 server configurations 
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		LangTool.init();
		Map<String,TnHost> hosts = TnConfigLoader.reload();
		arg0.getServletContext().setAttribute(TnHost.class.getCanonicalName(), hosts);
	}

}