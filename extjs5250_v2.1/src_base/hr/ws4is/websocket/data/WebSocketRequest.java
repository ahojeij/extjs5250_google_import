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

package hr.ws4is.websocket.data;

import hr.ws4is.WS4ISConstants;
import hr.ws4is.ext.ExtJSDirectRequest;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * JsonNode to be converted into JSON structure.
 */
public class WebSocketRequest {
	
	public final String type = WS4ISConstants.WEBSOCKET_TYPE;

	WebSocketInstruction cmd;  // 'welcome , bye, data' ,
	int timeout;  //set only when cmd=welcome

	String errMsg ;
	int errId;
	
	//list of commands - batch
	ArrayList<ExtJSDirectRequest<JsonNode>> data;
	
	public WebSocketInstruction getCmd() {
		return cmd;
	}
	
	public void setCmd(WebSocketInstruction cmd) {
		this.cmd = cmd;
	}
	
	public int getTimeout() {
		return timeout;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public String getErrMsg() {
		return errMsg;
	}
	
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	public int getErrId() {
		return errId;
	}
	
	public void setErrId(int errId) {
		this.errId = errId;
	}
	
	public String getType() {
		return type;
	}
	
	public ArrayList<ExtJSDirectRequest<JsonNode>> getData() {
		return data;
	}
	
	public void setData(ArrayList<ExtJSDirectRequest<JsonNode>> data) {
		this.data = data;
	}

}
