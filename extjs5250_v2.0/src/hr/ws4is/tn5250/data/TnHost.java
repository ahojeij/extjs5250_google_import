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
package hr.ws4is.tn5250.data;

import java.net.InetSocketAddress;

/**
 * Used by Property loader to prepare list of all available host configurations
 */
public class TnHost {

	private String ipAddress;
	private int port;
	private String name;
	private boolean screen132 = true;

	public InetSocketAddress getLocation(){
		InetSocketAddress ia = new InetSocketAddress(ipAddress, port);
		if(ia.isUnresolved()) return null;
		return ia;
	}

	public boolean isValid(){
		return (name!=null) && getLocation()!=null;
	}

	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public String getPort() {
		
		return Integer.toString(port);
	}
	public void setPort(String port) {
		this.port = Integer.parseInt(port);
	}	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public boolean isScreen132() {
		return screen132;
	}

	public void setScreen132(String screen) {
		
		if(screen==null){
			return;
		}
		
		if("true".equals(screen.toLowerCase()))
			this.screen132 = true;

		if(  "false".equals(screen.toLowerCase()))
			this.screen132 = false;

	}
		
}
