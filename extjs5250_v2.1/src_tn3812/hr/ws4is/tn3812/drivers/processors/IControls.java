/*
 * Copyright (C) 2014,  Tomislav Milkovic
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
package hr.ws4is.tn3812.drivers.processors;

import hr.ws4is.tn3812.drivers.processors.scs.SCSPageOrientation;

/**
 * Interface used inside data parsing listeners to get 
 * current print data parsing statuses like positioning instructions,
 * page setup instructions and font styling instructions 
 */
public interface IControls {
	
	public abstract void reset();

	public abstract SCSPageOrientation getPageOrientation();

	public abstract float getFontSize();

	public abstract short getLinesPerPage();
	
	public abstract int getLineSize();

	public abstract short getCharactersPerLine();

	public abstract float getMarginLeftInInches();

	public abstract float getMarginLeftInCentimeters();

	public abstract float getMarginRightInInches();

	public abstract float getMarginRightInCentimeters();

	public abstract float getMarginTopInInches();

	public abstract float getMarginTopInCentimeters();

	public abstract float getMarginBottomInInches();

	public abstract float getMarginBottomInCentimeters();

	public abstract float getPageWidthInIches();

	public abstract float getPageWidthInCentimeters();

	public abstract float getPageHeightInIches();

	public abstract float getPagHeightidthInCentimeters();

}
