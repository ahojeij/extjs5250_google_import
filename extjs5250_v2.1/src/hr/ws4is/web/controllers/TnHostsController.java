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

import hr.ws4is.data.TnHost;
import hr.ws4is.ext.ExtJSResponse;
import hr.ws4is.ext.ExtJSResponseList;
import hr.ws4is.ext.annotations.ExtJSAction;
import hr.ws4is.ext.annotations.ExtJSDirect;
import hr.ws4is.ext.annotations.ExtJSMethod;
import hr.ws4is.websocket.WebSocketSession;
import hr.ws4is.web.TnWebHelper;
import java.util.Map;
import javax.inject.Inject;

/**
 * Hosts controller that will be invoked from browser. Used for managing hosts
 * configurations.
 */
@ExtJSDirect(paths = { "socket" })
@ExtJSAction(namespace = "hr.ws4is", action = "HostsController")
public class TnHostsController {

    @Inject
    private WebSocketSession session;

    /*
     * Reload AS/400 server configurations
     */
    @ExtJSMethod("reloadDefinitions")
    public final ExtJSResponse reload5250Definitions() {

        final ExtJSResponse response = new ExtJSResponse();
        try {
            TnWebHelper.reloadConfiguration(session);
            response.setSuccess(true);
        } catch (Exception exception) {
            response.setError(exception, exception.getMessage());
        }
        return response;
    }

    /*
     * List all available AS/400 servers for connections
     */
    @ExtJSMethod("listDefinitions")
    public final ExtJSResponseList<String> list5250Definitions() {

        final ExtJSResponseList<String> response = new ExtJSResponseList<>();
        try {
            final Map<String, TnHost> hosts = TnWebHelper.getTnHosts(session);
            response.setSuccess(true);
            response.setData(hosts.keySet());
        } catch (Exception exception) {
            response.setError(exception, exception.getMessage());
        }

        return response;
    }

}
