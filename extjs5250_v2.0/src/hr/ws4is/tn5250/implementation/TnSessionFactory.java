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
 package hr.ws4is.tn5250.implementation;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.tn5250j.Session5250;
import org.tn5250j.TN5250jConstants;
import org.tn5250j.framework.common.SessionManager;

import hr.ws4is.tn5250.data.TnHost;
import hr.ws4is.tn5250.data.TnSession;
import hr.ws4is.websocket.WebSocketSession;

/**
 * tn5250 connection factory
 */
public enum TnSessionFactory {
	;
	
	public final static TnSession create(WebSocketSession wsSession, TnHost host, String displayId){
		String displayName = "DSP_" + getDisplayName(wsSession);
		String hostName = host.getName();		
		Session5250 session = createSession(wsSession,host,displayName, displayId);		
		TnSession hostSession = new TnSession(displayId, displayName, hostName, session);
		return hostSession;
	}
	

	private static final String getDisplayName(WebSocketSession wsSession){
		AtomicInteger counter = (AtomicInteger) wsSession.getHttpSession().getAttribute(TnConstants.SESSION_COUNTER);
		return Integer.toString(counter.incrementAndGet());
	}

	
	private static final Session5250 createSession(WebSocketSession wsSession, TnHost host, String displayName, String displayId) {
		Session5250 hostSession = null;
		
	    Properties sesProps = new Properties();
	    
	    sesProps.put(TN5250jConstants.SESSION_HOST,host.getIpAddress());
	    sesProps.put(TN5250jConstants.SESSION_HOST_PORT,host.getPort());

	    //sesProps.put(TN5250jConstants.SESSION_CODE_PAGE ,"");

	    sesProps.put(TN5250jConstants.SESSION_TN_ENHANCED,"1");
	    sesProps.put(TN5250jConstants.SESSION_USE_GUI,"1");
	    sesProps.put(TN5250jConstants.SESSION_TERM_NAME_SYSTEM, "1");
	    sesProps.put(TN5250jConstants.SESSION_TN_ENHANCED,"1");

	    if(host.isScreen132()){
        	sesProps.put(TN5250jConstants.SESSION_SCREEN_SIZE,TN5250jConstants.SCREEN_SIZE_27X132_STR);
	    }else{
        	sesProps.put(TN5250jConstants.SESSION_SCREEN_SIZE,TN5250jConstants.SCREEN_SIZE_24X80_STR);
	    }

            /*
            if (display.displayName!=null){
            	sesProps.put(TN5250jConstants.SESSION_DEVICE_NAME ,display.displayName.toUpperCase());
            }
            */

	    SessionManager manager = SessionManager.instance();
	    hostSession = manager.openSession(sesProps,"",displayName);
	    TnSessionListener listener = new TnSessionListener(wsSession, displayId);
	    hostSession.addSessionListener(listener);
	    hostSession.connect(); 
        return hostSession;
	}
	
}
