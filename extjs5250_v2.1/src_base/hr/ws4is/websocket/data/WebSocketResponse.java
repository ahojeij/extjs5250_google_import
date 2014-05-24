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
    
    private String type = WS4ISConstants.WEBSOCKET_TYPE;

    private final WebSocketInstruction cmd;

    private String errMsg;
    private int errId;
    private Object data;

    public WebSocketResponse(final WebSocketInstruction cmd) {
        this.cmd = cmd;
    }

    public final String getType() {
        return type;
    }

    public final void setType(final String type) {
        this.type = type;
    }

    public final String getErrMsg() {
        return errMsg;
    }

    public final void setErrMsg(final String errMsg) {
        this.errMsg = errMsg;
    }

    public final int getErrId() {
        return errId;
    }

    public final void setErrId(final int errId) {
        this.errId = errId;
    }

    public final Object getData() {
        return data;
    }

    public final void setData(final Object data) {
        this.data = data;
    }

    public final WebSocketInstruction getCmd() {
        return cmd;
    }

}
