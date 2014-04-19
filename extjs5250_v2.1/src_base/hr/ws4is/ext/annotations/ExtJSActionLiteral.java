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
package hr.ws4is.ext.annotations;

import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.Nonbinding;

@SuppressWarnings("all")
final  public class ExtJSActionLiteral extends AnnotationLiteral<ExtJSAction> implements ExtJSAction {

	private static final long serialVersionUID = 1L;

	final String namespace ;
	final String action ;
		
	public ExtJSActionLiteral(String namespace, String action) {
		super();
		this.action = action;
		this.namespace = namespace;			
	}

	@Override
	@Nonbinding
	public String action() {
		return action;
	}

	@Override
	@Nonbinding
	public String namespace() {
		return namespace;
	}
	
}
