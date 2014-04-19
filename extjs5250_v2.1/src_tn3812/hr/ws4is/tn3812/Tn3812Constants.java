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
package hr.ws4is.tn3812;

/**
 * Tn3812 data protocol specification commands used in negotiation process   
 */
enum Tn3812Constants {
	;

  public static final String IBM_3812_1 = "IBM-3812-1";
   
	
  //’12A0’X = GDS LU6.2 header
  public static final byte[]  GDS = new byte[]{(byte)0x12, (byte)0xA0};
	
  // ’90000560060020C0003D0000’X = fixed value
  public static final byte[]  FIXED = new byte[]{(byte)0x90,(byte)0x00,(byte)0x05,(byte)0x60,(byte)0x06,(byte)0x00,
		                                     (byte)0x20,(byte)0xC0,(byte)0x00,(byte)0x3D,(byte)0x00,(byte)0x00
		                                     };
	                       
  public static final byte[]  DATA_FLOW_SERVER = new byte[]{(byte)0x01, (byte)0x01};
  public static final byte[]  DATA_FLOW_CLIENT = new byte[]{(byte)0x01, (byte)0x02};
	
  public static final byte   PRT_OPTCODE_COMPLETE = (byte)0x01;
  public static final byte   PRT_OPTCODE_CLEAR = (byte)0x02;
	
  //client sends + eor after every data_flow_server
  public static final byte[] PRINT_COMPLETE = new byte[]{(byte)0x00, (byte)0x0A, (byte)0x12, (byte)0xA0, (byte)0x01, (byte)0x02, (byte)0x04, (byte)0x00, (byte)0x00, (byte)0x01 };
	
   
	// negotiating commands
   public static final byte IAC = (byte) -1; // 255 FF
   public static final byte DONT = (byte) -2; //254 FE
   public static final byte DO = (byte) -3; // 253 FD
   public static final byte WONT = (byte) -4; // 252 FC
   public static final byte WILL = (byte) -5; // 251 FB
   public static final byte SB = (byte) -6; // 250 Sub Begin FA
   public static final byte SE = (byte) -16; // 240 Sub End F0
   public static final byte EOR = (byte) -17; // 239 End of Record EF
   public static final byte TERMINAL_TYPE = (byte) 24; // 18
   public static final byte OPT_END_OF_RECORD = (byte) 25; // 19
   public static final byte TRANSMIT_BINARY = (byte) 0; // 0
   public static final byte QUAL_IS = (byte) 0; // 0
   public static final byte TIMING_MARK = (byte) 6; // 6
   public static final byte NEW_ENVIRONMENT = (byte) 39; // 27
   public static final byte IS = (byte) 0; // 0
   public static final byte SEND = (byte) 1; // 1
   public static final byte INFO = (byte) 2; // 2
   public static final byte VAR = (byte) 0; // 0
   public static final byte VALUE = (byte) 1; // 1
   public static final byte NEGOTIATE_ESC = (byte) 2; // 2
   public static final byte USERVAR = (byte) 3; // 3
   public static final byte ESC = 0x04; // 04

}
