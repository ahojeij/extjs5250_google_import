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

import java.util.List;
import java.util.Locale;

import javax.enterprise.inject.Vetoed;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;


@Vetoed
public class WebSocketConfigurator extends ServerEndpointConfig.Configurator{
	
	@Override
	public String getNegotiatedSubprotocol(List<String> supported, List<String> requested){
		return "ws4is";
	}
 
	// modifyHandshake() is called before getEndpointInstance()!
	@Override
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response){
		super.modifyHandshake(sec, request, response);
		HttpSession httpSession = (HttpSession)request.getHttpSession();
		sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
		sec.getUserProperties().put(Locale.class.getCanonicalName(),getLocale(request));
		sec.getUserProperties().put(WS4ISConstants.WEBSOCKET_PATH,sec.getPath());
	}

	private Locale getLocale(HandshakeRequest request){
		//Accept-Language:hr,en-US;q=0.8,en;q=0.6
		Locale locale = Locale.ENGLISH;
		List<String> params = request.getHeaders().get("Accept-Language");
	
		if(params==null) {
			return locale;
		}
		
		if(params.size()>0){
			String data = params.get(0);
			data = data.split(";")[0];
			data = data.split(",")[0];
			locale = new Locale(data);
		}
		return locale;
	}
	
}
