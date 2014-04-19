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

/**
 * Definition of SCS data stream codes
 */
public enum SCSCodes {
	;

	public static final byte NULL = ((byte)0x00);
	public static final byte ATRN = ((byte)0x03); // ASCII TRANSPARENCY
	public static final byte HT   = ((byte)0x05); // HORIZONTAL TAB 
	public static final byte RNL  = ((byte)0x06); // REQUIRED NEW LINE 
	public static final byte SPS  = ((byte)0x09); // SUPERSCRIPT 
	public static final byte RPT  = ((byte)0x0A); // REPEAT
	public static final byte FF   = ((byte)0x0C); // FORM FEED
	public static final byte NL   = ((byte)0x15); // CARIAGE RETURN
	public static final byte BS   = ((byte)0x16); // BACKSPACE
	public static final byte UBS  = ((byte)0x1a); // UNIT BACKSPACE
	public static final byte IRS  = ((byte)0x1e); // INTERCHANGE RECORD SEPARATOR
	public static final byte WUS  = ((byte)0x23); // WORD UNDERSCORE
	public static final byte LF   = ((byte)0x25); // LINE FEED
	public static final byte SW   = ((byte)0x2a); // SWITCH
	public static final byte MBC  = ((byte)0x2b); // MULTI BYTE CONTROL
	
	public static final byte BELL = ((byte)0x2f); // BELL  
	public static final byte IRT  = ((byte)0x33); // INDEX RETURN
	public static final byte PP   = ((byte)0x34); // PRESENTATION POSITION
	public static final byte NBS  = ((byte)0x36); // NUMERIC BACKSPACE
	public static final byte SBS  = ((byte)0x38); // SUBSCRIPT
	public static final byte IT   = ((byte)0x39); // INDENT TAB
	public static final byte RFF  = ((byte)0x3a); // REQUEST FORM FEED
	public static final byte SUB  = ((byte)0x3f); // SUBSTITUTE
	public static final byte SP   = ((byte)0x40); // SPACE
	public static final byte RSP  = ((byte)0x41); // REQUIRED SPACE
	public static final byte HYP  = ((byte)0x60); // REQUIRED HYPEN
	public static final byte US   = ((byte)0x6d); // UNDERSCORE
	public static final byte SHY  = ((byte)0xca); // SYLLABLE HYPEN
	public static final byte NSP  = ((byte)0xe1); // NUMERIC SPACE
	public static final byte EO   = ((byte)0xff); // EIGHT ONES
	
	// MULTI BYTE CONTROL GROUPS
	public static final byte MBC_C1 = ((byte)0xc1);  // SET HORIZONTAL FORMAT
	public static final byte MBC_C2 = ((byte)0xc2);  // SET VERTICAL FORMAT
	public static final byte MBC_C6 = ((byte)0xc6);  // SET LINE DENSITY
	public static final byte MBC_C8 = ((byte)0xc8);  // SET GRAPHIC ERROR ACTION
	public static final byte MBC_CA = ((byte)0xca);  // EXECUTE PMP
	
	public static final byte MBC_D1 = ((byte)0xd1);  // 
	public static final byte MBC_D2 = ((byte)0xd2);  //
	public static final byte MBC_D3 = ((byte)0xd3);  //
	public static final byte MBC_D4 = ((byte)0xd4);  //	
	
	// MULTI BYTE CONSTANT COMMANDS
	
	//MBC_D1
	public static final byte SCG  = (byte) 0x01; // SET GCGID THROUGH GCID
	public static final byte SFG  = (byte) 0x05; // SET FID THROUGH GFID
	public static final byte SCGL = (byte) 0x81; // SET CGSC THROUGH LOCAL ID
	public static final byte SFFC = (byte) 0x87; // SET FORM FEE CONTROL
	public static final byte BES  = (byte) 0x8a; // BEGIN EMPHASIS
	public static final byte BAC  = (byte) 0x8b; // 00 | 01; - BOLDING; ON | OFF
	public static final byte FES  = (byte) 0x8e; // END EMPHASIS
	
	//MBC_D2
	public static final byte STAB = (byte) 0x01; //FF - SET HORIZONTAL TAB STOPS
	public static final byte JTF  = (byte) 0x03; // JUSTIFY TEXT FIELD
	public static final byte SIL  = (byte) 0x07; // SET INDENT LEVEL
	public static final byte SLS  = (byte) 0x09; // SET LINE SPACING 
	public static final byte RLM  = (byte) 0x0b; // RELEASE LEFT MARGIN
	public static final byte SJM  = (byte) 0x0d; // SET JUTIFY MODE
	public static final byte SHM  = (byte) 0x11; // SET HORISONTAL MARGINS
	public static final byte SSLD = (byte) 0x15; // SET LINE DISTANCE
	public static final byte SCD  = (byte) 0x29; // SET CHARACTER DISTANCE 
	public static final byte SPPS = (byte) 0x40; // SET PRESENTATION PAGE SIZE
	public static final byte SIC  = (byte) 0x45; // SET INITIAL CONDITIONS
	public static final byte PPM  = (byte) 0x48; // SET PRESENTATION MEDIA
	public static final byte SVM  = (byte) 0x49; // SET VERTICAL MARGINS
	public static final byte SPSU = (byte) 0x4c; // SET PRINT SETUP
	public static final byte SEA  = (byte) 0x85; // SET EXCEPTION ACTION
	
	//MBC_D3
	public static final byte STO  = (byte) 0xf6; // SET TEXT ORIENTATION
	
	//MBC_D4
	public static final byte BUS  = (byte) 0x0a; // BEGIN UNDERSCORE
	public static final byte EUS  = (byte) 0x0e; // END UNDERSCORE
	public static final byte BOS  = (byte) 0x72; // BEGIN OVERSTRKE
	public static final byte EOS  = (byte) 0x76; // END OVERSTRIKE
	
	//CURSOR MOVEMENT
	public static final byte RMD  = (byte) 0x4c; // RELATIVE MOVE DOWN
	public static final byte RMR  = (byte) 0xC8; // RELATIVE MOVE RIGHT
	public static final byte AMD  = (byte) 0xC4; // ABSOLUTE MOVE DOWN
	public static final byte AMR  = (byte) 0xC0; // ABSOLUTE MOVE RIGHT	
	
}
