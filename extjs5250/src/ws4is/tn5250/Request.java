/**
 * ws4is.tn5250.Request.java
 *
 * Copyright (C) 2009,  Tomislav Milkovic
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
 */

package ws4is.tn5250;

public class Request {

	private String keyRequest;
	private int cursorField; 
	private int cursorRow;
	private String devName;
	
	public String getKeyRequest() {
		return keyRequest;
	}
	public void setKeyRequest(String keyRequest) {
		this.keyRequest = keyRequest;
	}
	public int getCursorField() {
		return cursorField;
	}
	public void setCursorField(int cursorField) {
		this.cursorField = cursorField;
	}
	public int getCursorRow() {
		return cursorRow;
	}
	public void setCursorRow(int cursorRow) {
		this.cursorRow = cursorRow;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName.toUpperCase();
	}
	
}
