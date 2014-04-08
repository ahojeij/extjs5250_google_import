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
package hr.ws4is.cdi;

import hr.ws4is.WS4ISConstants;
import java.util.Locale;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


@WebListener
public final class WebProducer implements ServletRequestListener, HttpSessionListener {
	
	private final static ThreadLocal<Locale> holder = new ThreadLocal<Locale>();
	
	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		holder.remove();
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		holder.set( ((HttpServletRequest) sre.getServletRequest()).getLocale());
	}

	protected void localeEvent(@Observes final WebLocaleEvent event) {
		holder.set(event.getLocale());
	}

	@Produces
	@WebLocale
	Locale getLocaleForCDI() {
		Locale locale = holder.get();
		if(locale==null) return Locale.ENGLISH;
		return locale;
	}

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		arg0.getSession().setAttribute(WS4ISConstants.HTTP_SEESION_STATUS, "true");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		arg0.getSession().setAttribute(WS4ISConstants.HTTP_SEESION_STATUS, "false");
	}

}