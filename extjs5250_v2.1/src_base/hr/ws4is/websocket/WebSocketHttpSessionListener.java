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
package hr.ws4is.websocket;

import hr.ws4is.WS4ISConstants;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public final class WebSocketHttpSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(final HttpSessionEvent arg0) {
        final HttpSession httpSession = arg0.getSession();
        httpSession.setAttribute(WS4ISConstants.HTTP_SEESION_STATUS, Boolean.TRUE.toString());
    }

    @Override
    public void sessionDestroyed(final HttpSessionEvent arg0) {
        final HttpSession httpSession = arg0.getSession();
        httpSession.setAttribute(WS4ISConstants.HTTP_SEESION_STATUS, Boolean.FALSE.toString());
    }

}
