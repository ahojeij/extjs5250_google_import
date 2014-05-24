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
import hr.ws4is.data.tn3812.Tn3812Response;
import hr.ws4is.data.tn5250.Tn5250ScreenResponse;
import hr.ws4is.data.tn5250.Tn5250ScreenRequest;
import hr.ws4is.tn3812.interfaces.ITn3812Context;
import hr.ws4is.tn5250.ITn5250Session;
import hr.ws4is.websocket.WebSocketSession;

import java.util.Map;

import javax.enterprise.inject.Vetoed;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 * Helper methods used from web controllers.
 */
@Vetoed
public enum TnWebHelper {
    ;

    public static Tn5250ScreenResponse get5250SessionNotFoundResponse() {
        final Tn5250ScreenResponse response = new Tn5250ScreenResponse(false, TnConstants.SESSION_NOT_FOUND);
        response.setConerr(TnConstants.SESSION_NOT_FOUND);
        response.setLocked(false);
        response.setClearScr(false);
        return response;
    }

    public static Tn5250ScreenResponse get5250ErrorResponse(final Exception exception) {
        final Tn5250ScreenResponse response = new Tn5250ScreenResponse(exception, exception.getMessage());
        response.setConerr(TnConstants.REQUEST_ERROR);
        response.setLocked(false);
        response.setClearScr(false);
        return response;
    }

    public static Tn3812Response get3812SessionNotFoundResponse(final String printerName) {
        final Tn3812Response response = new Tn3812Response(false, TnConstants.SESSION_NOT_FOUND);
        response.setPrinterName(printerName);
        return response;
    }

    public static Tn3812Response get3812ErrorResponse(final Exception exception, final String printerName) {
        final Tn3812Response response = new Tn3812Response(exception, exception.getMessage());
        response.setMsg(TnConstants.REQUEST_ERROR);
        response.setPrinterName(printerName);
        return response;
    }

    /* * * * * * * * TN 5250 sessions * * * * * * */

    public static ITn5250Session findTn5250Session(final WebSocketSession session, final Tn5250ScreenRequest data) {
        final Map<String, ITn5250Session> map = getTn5250Sessions(session);
        return map.get(data.getDisplayID());
    }

    public static ITn5250Session findTn5250Session(final WebSocketSession session, final String name) {
        final Map<String, ITn5250Session> map = getTn5250Sessions(session);
        return map.get(name);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, ITn5250Session> getTn5250Sessions(final WebSocketSession session) {
        final HttpSession httpSession = session.getHttpSession();
        final Map<String, ITn5250Session> sessions = (Map<String, ITn5250Session>) httpSession.getAttribute(ITn5250Session.class.getCanonicalName());
        return sessions;
    }

    /* * * * * * * * TN 3812 sessions * * * * * * */

    public static ITn3812Context findTn3812Session(final WebSocketSession session, final String name) {
        final Map<String, ITn3812Context> map = getTn3812Sessions(session);
        return map.get(name);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, ITn3812Context> getTn3812Sessions(final WebSocketSession session) {
        final HttpSession httpSession = session.getHttpSession();
        final Map<String, ITn3812Context> sessions = (Map<String, ITn3812Context>) httpSession.getAttribute(ITn3812Context.class.getCanonicalName());
        return sessions;
    }

    /* * * * * * * * TN HOSTS * * * * * * */

    public static TnHost findTnHost(final WebSocketSession session, final String name) {
        Map<String, TnHost> map = getTnHosts(session);
        return map.get(name);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, TnHost> getTnHosts(final WebSocketSession session) {
        final HttpSession httpSession = session.getHttpSession();
        final ServletContext servletContext = httpSession.getServletContext();
        final Map<String, TnHost> hosts = (Map<String, TnHost>) servletContext.getAttribute(TnHost.class.getCanonicalName());
        return hosts;
    }

    public static void reloadConfiguration(final WebSocketSession session) {
        final HttpSession httpSession = session.getHttpSession();
        final ServletContext servletContext = httpSession.getServletContext();
        servletContext.setAttribute(TnHost.class.getCanonicalName(), TnConfigLoader.reload());
    }
}
