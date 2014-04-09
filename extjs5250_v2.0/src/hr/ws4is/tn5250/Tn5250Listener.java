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

import hr.ws4is.tn5250.data.TnHost;
import hr.ws4is.tn5250.data.TnSession;
import hr.ws4is.tn5250.implementation.TnConfigLoader;
import hr.ws4is.tn5250.implementation.TnConstants;
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
public final class Tn5250Listener implements HttpSessionListener, ServletContextListener {
	
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
		  Map<String, TnSession> tnSessions = (Map<String, TnSession>) wsSession.getHttpSession().getAttribute(TnSession.class.getCanonicalName());
		  Collection<TnSession> sessions =  tnSessions.values();
		  for(TnSession session : sessions){
			  if(session.isConnected()){
				  session.updateWebSocketSession(wsSession);
			  }
		  }
	  }
	}
	
	private void closeTnSession(HttpSession webSession){
		System.out.println("Closing Tn5250 sessions for Web session :" + webSession.getId());
		
		@SuppressWarnings("unchecked")
		Map<String,TnSession> tnSessions = (Map<String, TnSession>) webSession.getAttribute(TnSession.class.getCanonicalName());
		Collection<TnSession> sessions =  tnSessions.values();
		for(TnSession session : sessions){
			if(session.isConnected()){
				System.out.println("   Closing Tn5250 session :" + session.displayId);
				session.disconnect();
			}
		}
		tnSessions.clear();
		webSession.removeAttribute(TnSession.class.getCanonicalName());
	}
	
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		HttpSession webSession = arg0.getSession();
		webSession.setAttribute(TnSession.class.getCanonicalName(), new HashMap<String,TnSession>());
		webSession.setAttribute(TnConstants.SESSION_COUNTER, new AtomicInteger());
	}

	/*
	 * Clean all 5250 sessions for current session
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {		
		HttpSession webSession = arg0.getSession();
		webSession.removeAttribute(TnConstants.SESSION_COUNTER);
		closeTnSession(webSession);
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