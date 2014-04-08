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
package hr.ws4is.tn5250;

import java.util.List;

import hr.ws4is.ext.ExtJSResponse;
import hr.ws4is.tn5250.data.TnScreenElement;

/**
 * Resposne structure with screen information
 */
public class Tn5250ResponseScreen extends ExtJSResponse {

	private static final long serialVersionUID = 1L;
	
	public String devName;	
	public boolean locked;		 //sends lock screen request
	public boolean clearScr = true; //sends clear screen request
	public int size = 80;		 //sends screen size 80/132
	public boolean msgw; 	 //sends message wait signal
	public String conerr; 	 //host error code
	
	public List<TnScreenElement> data;
	
	public Tn5250ResponseScreen() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Tn5250ResponseScreen(boolean success, String message) {
		super(success, message);
	}

	public Tn5250ResponseScreen(Throwable exception, String message) {
		super(exception, message);
	}
	

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isClearScr() {
		return clearScr;
	}

	public void setClearScr(boolean clearScr) {
		this.clearScr = clearScr;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isMsgw() {
		return msgw;
	}

	public void setMsgw(boolean msgw) {
		this.msgw = msgw;
	}

	public String getConerr() {
		return conerr;
	}

	public void setConerr(String conerr) {
		this.conerr = conerr;
	}

	public List<TnScreenElement> getData() {
		return data;
	}

	public void setData(List<TnScreenElement> data) {
		this.data = data;
	}

	

}
