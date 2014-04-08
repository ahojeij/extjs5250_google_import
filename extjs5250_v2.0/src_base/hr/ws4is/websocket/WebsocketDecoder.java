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

import hr.ws4is.JsonDecoder;
import hr.ws4is.websocket.data.WebSocketRequest;

import javax.enterprise.inject.Vetoed;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

@Vetoed
public class WebsocketDecoder implements Decoder.Text<WebSocketRequest> {

	@Override
	public WebSocketRequest decode(String message) throws DecodeException{
		WebSocketRequest wsMessage = null;

		try{
			JsonDecoder<WebSocketRequest> jd = new JsonDecoder<>(WebSocketRequest.class, message);
			wsMessage = jd.getObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DecodeException(message, e.getMessage(), e);
		}
		return wsMessage;
	}

	@Override
	public boolean willDecode(String message){
		if(message == null) return false;
		return message.trim().startsWith("{") && message.trim().endsWith("}");
	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(EndpointConfig arg0) {

	}

}
