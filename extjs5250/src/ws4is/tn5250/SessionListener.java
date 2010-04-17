/**
 * ws4is.tn5250.SessionListener.java
 *
 * Copyright (C) 2009,  Tomislav Milkovic
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
 */

package ws4is.tn5250;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.tn5250j.Session5250;



public class SessionListener implements HttpSessionListener {
	
	private transient List sessions = null;
	

	public SessionListener() {
		sessions = new Vector();
	}

	public void sessionCreated(HttpSessionEvent arg0) {
		HttpSession sess = arg0.getSession();
		sessions.add(sess);	
		sess.setAttribute("ws4is_sess", sessions);
		
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		System.out.println("Closing 5250 session as http session is destroyed!");
		HttpSession sess = arg0.getSession();
		sessions.remove(sess);
		HashMap hm = (HashMap)sess.getAttribute(Dwr5250.sessionStore);
		Iterator it = hm.values().iterator();
		while (it.hasNext()){
			Session5250 s5250 = (Session5250) it.next();
			try{
				if (s5250.isConnected()) s5250.disconnect();	
			}catch(Exception e){};
			
		}
	}

}
