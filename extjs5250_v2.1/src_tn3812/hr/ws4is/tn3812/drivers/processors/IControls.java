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
 * Interface used inside data parsing listeners to get current print data
 * parsing statuses like positioning instructions, page setup instructions and
 * font styling instructions
 */
public interface IControls {

    void reset();

    SCSPageOrientation getPageOrientation();

    float getFontSize();

    short getLinesPerPage();

    int getLineSize();

    short getCharactersPerLine();

    float getMarginLeftInInches();

    float getMarginLeftInCentimeters();

    float getMarginRightInInches();

    float getMarginRightInCentimeters();

    float getMarginTopInInches();

    float getMarginTopInCentimeters();

    float getMarginBottomInInches();

    float getMarginBottomInCentimeters();

    float getPageWidthInIches();

    float getPageWidthInCentimeters();

    float getPageHeightInIches();

    float getPagHeightidthInCentimeters();

}
