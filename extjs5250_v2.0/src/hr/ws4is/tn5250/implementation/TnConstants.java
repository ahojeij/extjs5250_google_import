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

/**
 * constants used by tn5250 websocket service
 */
public enum TnConstants {
	;

	public final static String HOST_5250_CONFIG = TnConstants.class.getCanonicalName() + "_HOST_5250_CONFIG";
	
	public final static String DISPLAY_ID =  "Display name not set";
	public final static String NOT_CONNECTED =  "Session not connected";
	public final static String UNABLE_TO_CONNECT =  "Unable to connect";
	public final static String NO_DATA =  "Screen data not received";
	public final static String INVALID_DISPLAY =  "Invalid display name";
	public final static String NO_HOST_CONFIG =  "Hosts configurations is missing";
	public final static String HOST_NOT_FOUND =  "Host not found";
	public final static String SESSION_NOT_FOUND =  "Session not found";
	public final static String REQUEST_ERROR =  "Request error";
	
	public final static String SESSION_STORE = "tn5250";
	public final static String CURRENT_SESSION = "tn5250_display";
	public final static String SESSION_COUNTER = "tn5250_counter";
}
