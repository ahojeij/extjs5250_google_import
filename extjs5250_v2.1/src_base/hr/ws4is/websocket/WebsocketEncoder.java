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
import hr.ws4is.websocket.data.WebSocketResponse;

import java.io.IOException;

import javax.enterprise.inject.Vetoed;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Internal encoder for WebSocket ExtJS response
 *
 */
@Vetoed
public class WebsocketEncoder implements Encoder.Text<WebSocketResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketEncoder.class);

    private ObjectMapper mapper = null;

    @Override
    public final void destroy() {

    }

    @Override
    public final void init(final EndpointConfig arg0) {
        mapper = JsonDecoder.getJSONEngine();
    }

    @Override
    public final String encode(final WebSocketResponse data) throws EncodeException {
        String response = null;
        try {
            if (mapper != null) {
                response = mapper.writeValueAsString(data);
            }
        } catch (IOException exception) {
            LOGGER.error(exception.getMessage(), exception);
            throw new EncodeException(data, exception.getMessage(), exception);
        }
        if (response == null) {
            response = "";
        }
        return response;
    }

}
