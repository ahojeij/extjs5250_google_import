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


/**
 * Object to be converted into JSON structure.
 * {type :'ws' , sid : session_id , tid : transaction_id, timeout : 0 , ....}
 */
public class WebSocketResponse 
{
	public final String type = "ws";
	public final String sid; // connection unique id
	public final String tid; // transaction unique id

	public final WebSocketInstruction cmd;
	
	public String errMsg ;
	public int errId;
	public Object data;
	
	public WebSocketResponse(WebSocketRequest request, WebSocketInstruction cmd){
		this.sid=request.getSid();
		this.tid=request.getTid();
		this.cmd=cmd;
	}

	public WebSocketResponse(String sid, String tid, WebSocketInstruction cmd) {
		super();
		this.sid = sid;
		this.tid = tid;
		this.cmd = cmd;
	}	
}
