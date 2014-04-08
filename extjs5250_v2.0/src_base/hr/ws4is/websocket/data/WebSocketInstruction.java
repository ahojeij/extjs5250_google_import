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

import com.fasterxml.jackson.annotation.JsonValue;


/**
 * WebSocket return structure {type:'ws' , cmd : * , data : *}
 */
public enum WebSocketInstruction 
{
	WELCO("welco"), BYE("bye"), ERR("err"), DATA("data"), ECHO("echo");

    private final String text;

	  /**
     * @param text
     */
    private WebSocketInstruction(final String text) {
        this.text = text;
    }
    
    @JsonValue
    public String getText() {
		return text;
	}

	/* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }	
    
}
