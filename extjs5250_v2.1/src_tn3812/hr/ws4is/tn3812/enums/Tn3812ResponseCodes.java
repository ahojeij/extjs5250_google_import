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
package hr.ws4is.tn3812.enums;

/*
 The Start-Up Response Record success response codes:
 CODE DESCRIPTION
---- ------------------------------------------------------
I901 Virtual device has less function than source device
I902 Session successfully started
I906 Automatic sign-on requested, but not allowed.
Session still allowed; a sign-on screen will be coming.

The Start-Up Response Record error response codes:
CODE DESCRIPTION
---- ------------------------------------------------------
2702 Device description not found.
2703 Controller description not found.
2777 Damaged device description.
8901 Device not varied on.
8902 Device not available.
8903 Device not valid for session.
8906 Session initiation failed.
8907 Session failure.
8910 Controller not valid for session.
8916 No matching device found.
8917 Not authorized to object.
8918 Job canceled.
8920 Object partially damaged.
8921 Communications error.
8922 Negative response received.
8923 Start-up record built incorrectly.
8925 Creation of device failed.
8928 Change of device failed.
8929 Vary on or vary off failed.
8930 Message queue does not exist.
8934 Start-up for S/36 WSF received.
8935 Session rejected.
8936 Security failure on session attempt.
8937 Automatic sign-on rejected.
8940 Automatic configuration failed or not allowed.
I904 Source system at incompatible release.
 */

/**
 * Error response codes by specification
 */
public enum Tn3812ResponseCodes {
	
	//success codes
	I901("I901", new byte[] {(byte)0xC9, (byte)0xF9, (byte)0xF0, (byte)0xF1}),
	I902("I902", new byte[] {(byte)0xC9, (byte)0xF9, (byte)0xF0, (byte)0xF2}),
	I906("I906", new byte[] {(byte)0xC9, (byte)0xF9, (byte)0xF0, (byte)0xF6}),
	
	//error codes
	I904("I904", new byte[] {(byte)0xC9, (byte)0xF9, (byte)0xF0, (byte)0xF4}),
	E2702("2702", new byte[] {(byte)0xF2, (byte)0xF7, (byte)0xF0, (byte)0xF2}),
	E2703("2703", new byte[] {(byte)0xF2, (byte)0xF7, (byte)0xF0, (byte)0xF3}),
	E2777("2777", new byte[] {(byte)0xF2, (byte)0xF7, (byte)0xF7, (byte)0xF7}),
	
	E8901("8901", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF0, (byte)0xF1}),
	E8902("8902", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF0, (byte)0xF2}),
	E8903("8903", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF0, (byte)0xF3}),
	E8906("8906", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF0, (byte)0xF6}),
	E8907("8907", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF0, (byte)0xF7}),
	E8910("8910", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF1, (byte)0xF0}),
	E8916("8916", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF1, (byte)0xF6}),
	E8917("8917", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF1, (byte)0xF7}),
	E8918("8918", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF1, (byte)0xF8}),
	
	E8920("8920", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF2, (byte)0xF0}),
	E8921("8921", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF2, (byte)0xF1}),
	E8922("8922", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF2, (byte)0xF2}),
	E8923("8923", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF2, (byte)0xF3}),
	E8925("8925", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF2, (byte)0xF5}),
	E8928("8928", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF2, (byte)0xF8}),
	E8929("8929", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF2, (byte)0xF9}),
	
	E8930("8930", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF3, (byte)0xF0}),
	E8934("8934", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF3, (byte)0xF4}),
	E8935("8935", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF3, (byte)0xF5}),
	E8936("8936", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF3, (byte)0xF6}),
	E8937("8937", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF3, (byte)0xF7}),
	
	E8940("8940", new byte[] {(byte)0xF8, (byte)0xF9, (byte)0xF4, (byte)0xF0})
	;
	
    private final String name;
    private final byte[] value;

    private Tn3812ResponseCodes(String name, byte[] value) {
        this.name = name;
        this.value = value;
    }

	public String getName() {
		return name;
	}

	public byte[] getValue() {
		return value;
	}
    
}
