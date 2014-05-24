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
package hr.ws4is.tn5250;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.inject.Vetoed;
import javax.servlet.http.HttpSession;

import org.tn5250j.Session5250;
import org.tn5250j.TN5250jConstants;
import org.tn5250j.framework.common.SessionManager;

import hr.ws4is.data.TnConstants;
import hr.ws4is.data.TnHost;
import hr.ws4is.websocket.WebSocketSession;

/**
 * tn5250 connection factory
 */
@Vetoed
public enum Tn5250SessionFactory {
    ;

    public static final ITn5250Session create(final WebSocketSession wsSession, final TnHost host, final String displayId) {
        final String displayName = "DSP_" + getDisplayName(wsSession);
        final String hostName = host.getName();
        final Session5250 session = createSession(wsSession, host, displayName, displayId);
        return new Tn5250Session(displayId, displayName, hostName, session);
    }

    private static String getDisplayName(final WebSocketSession wsSession) {
        final HttpSession session = wsSession.getHttpSession();
        final AtomicInteger counter = (AtomicInteger) session.getAttribute(TnConstants.SESSION_COUNTER);
        return Integer.toString(counter.incrementAndGet());
    }

    private static Session5250 createSession(final WebSocketSession wsSession, final TnHost host, final String displayName, final String displayId) {

        final Properties sesProps = new Properties();

        sesProps.put(TN5250jConstants.SESSION_HOST, host.getIpAddress());
        sesProps.put(TN5250jConstants.SESSION_HOST_PORT, host.getPort());

        // sesProps.put(TN5250jConstants.SESSION_CODE_PAGE ,"");

        sesProps.put(TN5250jConstants.SESSION_TN_ENHANCED, "1");
        sesProps.put(TN5250jConstants.SESSION_USE_GUI, "1");
        sesProps.put(TN5250jConstants.SESSION_TERM_NAME_SYSTEM, "1");
        sesProps.put(TN5250jConstants.SESSION_TN_ENHANCED, "1");

        if (host.isScreen132()) {
            sesProps.put(TN5250jConstants.SESSION_SCREEN_SIZE, TN5250jConstants.SCREEN_SIZE_27X132_STR);
        } else {
            sesProps.put(TN5250jConstants.SESSION_SCREEN_SIZE, TN5250jConstants.SCREEN_SIZE_24X80_STR);
        }

        /*
         * if (display.displayName!=null){
         * sesProps.put(TN5250jConstants.SESSION_DEVICE_NAME
         * ,display.displayName.toUpperCase()); }
         */

        final Tn5250SessionListener listener = new Tn5250SessionListener(wsSession, displayId);
        final SessionManager manager = SessionManager.instance();
        final Session5250 hostSession = manager.openSession(sesProps, "", displayName);
        hostSession.addSessionListener(listener);
        hostSession.connect();

        return hostSession;
    }

}
