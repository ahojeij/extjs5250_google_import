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
package hr.ws4is;

/**
 * Enumeration containing some internally used constants 
 */
public enum WS4ISConstants {
    ;

    public static final String HTTP_SEESION_STATUS   = "ws4is.session.status";
    public static final String HTTP_SEESION_REQUIRED = "Websocket requires valid http session";
    
    public static final String WEBSOCKET_PATH        = "ws4is.websocket.path";
    public static final String WEBSOCKET_SUBPROTOCOL = "ws4is";
    public static final String WEBSOCKET_TYPE        = "ws";

    public static final String DIRECT_SERVICE_NOT_FOUND = "Requested ExtDirect Service not found";
}
