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
package hr.ws4is.tn3812;

import javax.inject.Inject;

import hr.ws4is.ext.ExtJSResponse;
import hr.ws4is.ext.ExtJSResponseList;
import hr.ws4is.ext.annotations.ExtJSAction;
import hr.ws4is.ext.annotations.ExtJSDirect;
import hr.ws4is.ext.annotations.ExtJSMethod;
import hr.ws4is.tn3812.data.Tn3812Request;
import hr.ws4is.tn3812.data.Tn3812Response;
import hr.ws4is.websocket.WebSocketSession;

/*
 * Controller used to create and controll printer session 
 */
@ExtJSDirect(paths={"socket"})
@ExtJSAction(namespace = "hr.ws4is", action="Controller3812")
public class Tn3812Controller {

	@Inject 
	WebSocketSession session;	 
	
	@ExtJSMethod("closeSessions")
	public ExtJSResponse closeSessions() {
		return null;
	}
	
	@ExtJSMethod("loadConfigurations")
	public ExtJSResponseList<String> loadConfigurations() {
		return null;
	}
	
	@ExtJSMethod("saveConfiguration")
	public ExtJSResponse saveConfiguration(Tn3812Request request) {
		return null;
	}
	
	@ExtJSMethod("loadConfiguration")
	public Tn3812Response loadConfiguration(String device) {
		return null;
	}
		
	@ExtJSMethod("openSession")
	public ExtJSResponse openSession(String device) {
		return null;
	}
	
	@ExtJSMethod("closeSession")
	public ExtJSResponse closeSession(String device) {
		return null;
	}
	
	@ExtJSMethod("refreshStatus")
	public Tn3812Response refreshStatus(String device) {
		return null;
	}
}
