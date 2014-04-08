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

import hr.ws4is.ext.ExtJSResponse;
import java.util.Set;

/**
 * Simple response structure with string list
 */
public class Tn5250ResponseList extends ExtJSResponse {

	private static final long serialVersionUID = 1L;

	private Set<String> data;
	
	public Tn5250ResponseList() {
		super();
	}

	public Tn5250ResponseList(boolean success, String message) {
		super(success, message);
	}

	public Tn5250ResponseList(Throwable exception, String message) {
		super(exception, message);
	}

	public Set<String> getData() {
		return data;
	}

	public void setData(Set<String> data) {
		this.data = data;
	}
		
}
