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

import hr.ws4is.websocket.WebSocketConfigurator;
import hr.ws4is.websocket.WebSocketService;
import hr.ws4is.websocket.WebsocketDecoder;
import hr.ws4is.websocket.WebsocketEncoder;

import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/socket",
   configurator=WebSocketConfigurator.class, 
   decoders={WebsocketDecoder.class}, 
   encoders={WebsocketEncoder.class},
   subprotocols={"ws4is"})
public class Tn5250WebSocketService extends WebSocketService {
  // this is main initialization websocket service 
}
