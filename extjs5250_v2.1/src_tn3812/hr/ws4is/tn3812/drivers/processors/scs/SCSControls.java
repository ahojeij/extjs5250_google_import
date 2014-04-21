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
package hr.ws4is.tn3812.drivers.processors.scs;

import hr.ws4is.tn3812.drivers.processors.IControls;


/**
 * Internal class to keep curent instruction from parsed SCS data stream  
 */
class SCSControls implements IControls, Cloneable {

	private static final float IN_CM_RATIO = 2.54f;
	private static final short INCH_1440 = 1440;
	
	public int currentPosition = 1;
	public int currentLine = 1;
	public int currentFeedCounter = 0;
	public int currentTabStop = 0;
	public int currentIndent = 0;
	
	//number of form feeds commands before actual page is printed
	public short SFFC = 1;
	
	//PRESENTATION PAGE SIZE - 1440th of an inch
	public short SPPS_WIDTH;
	public short SPPS_HEIGHT;
	
	//text orientation
	// 0x0000 (0) - normal
	// 0x2d00 ; 0x8700 - landscape
	// 0x5a00 - portrtait
	// 0xfffe - COR
	// 0xffff (-1) - calculate by SPPS
	public short STO;
	
	//characters per line
	public short SHF = 132;
	
	//lines per page
	public short SVF = 66;
	
	/*TODO SLD & SSLD sets single used value, different commands for diff calculations */
	//LPI - lines per inch in n/72 inch (12/72 = 6 LPI) 
	public short SLD = 12;
	
	// 1440/(240)SSLD = 6 LPI
	public short SSLD = 240;
	
	//line spacing SLS/2 (default = 2 -> 1 line)
	public short SLS = 2;
	
	//margin left in 1440'in
	public short SHM_LEFT;
	
	//margin right in 1440'in
	public short SHM_RIGHT;
	
	//margin top in 1440'in
	public short SVM_TOP;
	
	//margin bottom in 1440'in
	public short SVM_BOTTOM;
	
	//font size;  maybe not used 
	//also named SPD - print density
	public short SCD;
	
	//font width - cpi 1/(fwd/1440)
	public short FWD;
	
	// tab stop type (0 - chars; 1 - 1440' inch)
	public short STAB_TYPE;
	public short[] STAB;
	
	
	public boolean underscore = false;
	public boolean overstrike = false;
	public boolean bolding = false;
	public boolean emphasis = false;
	
	public void reset(){
		currentPosition = 1;
		currentLine = 1;
		currentFeedCounter = 0;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public SCSPageOrientation getPageOrientation(){
		SCSPageOrientation _default = SCSPageOrientation.PORTRAIT;
		if(STO == (short)0x2d00 || STO == (short)0x8700 ){
			_default = SCSPageOrientation.LANDSCAPE;
		}
		if(STO == (short)0xffff){
			if(SPPS_WIDTH > SPPS_HEIGHT ){
				_default = SCSPageOrientation.LANDSCAPE;
			}
			
			/*
			//( page width / font width = char per line ) > char per line; then rotate
			if((SHM_RIGHT / FWD) > SHF){
				_default = SCSPageOrientation.LANDSCAPE; 
			}
			*/
		}
		return _default;
	}
		
	public float getFontSize(){
		float fwd = FWD;
		float inch_1440 = INCH_1440;
		float fs = fwd/inch_1440;
		return (1/(fs));
	}
	
	public int getLineSize(){
		return SLD * (SLS/2);
	}
	
	public short getLinesPerPage(){
		return SVF;
	}
	
	public short getCharactersPerLine(){
		return SHF;
	}

	/*   HORIZONAL MARGINS     */
	public float getMarginLeftInInches(){
		return SHM_LEFT / INCH_1440;
	}
	
	public float getMarginLeftInCentimeters(){
		return getMarginLeftInInches() * IN_CM_RATIO;
	}
	
	public float getMarginRightInInches(){
		return SHM_RIGHT / INCH_1440;
	}
	
	public float getMarginRightInCentimeters(){
		return getMarginRightInInches() * IN_CM_RATIO;
	}	
	
	/*  VERTICAL MARGINS */
	
	public float getMarginTopInInches(){
		return SVM_TOP / INCH_1440;
	}
	
	public float getMarginTopInCentimeters(){
		return getMarginTopInInches() * IN_CM_RATIO;
	}
	
	public float getMarginBottomInInches(){
		return SVM_BOTTOM / 1440;
	}
	
	public float getMarginBottomInCentimeters(){
		return getMarginBottomInInches() * IN_CM_RATIO;
	}	
	
	/* PAGE SIZES SETUP */
	
	public float getPageWidthInIches(){
		return SPPS_WIDTH / INCH_1440;
	}

	public float getPageWidthInCentimeters(){
		return getPageWidthInIches() * IN_CM_RATIO;
	}
	
	public float getPageHeightInIches(){
		return SPPS_HEIGHT / INCH_1440;
	}

	public float getPagHeightidthInCentimeters(){
		return getPageHeightInIches() * IN_CM_RATIO;
	}
}
