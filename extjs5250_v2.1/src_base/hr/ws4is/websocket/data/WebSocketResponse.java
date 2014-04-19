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


/**
 * Object to be converted into JSON structure.
 * {type :'ws' , sid : session_id , tid : transaction_id, timeout : 0 , ....}
 */
public class WebSocketResponse {
	private String type = WS4ISConstants.WEBSOCKET_TYPE ;

	public final WebSocketInstruction cmd;
	
	public String errMsg ;
	public int errId;
	public Object data;
	
	public WebSocketResponse(WebSocketInstruction cmd){
		this.cmd=cmd;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public WebSocketInstruction getCmd() {
		return cmd;
	}
	
}
