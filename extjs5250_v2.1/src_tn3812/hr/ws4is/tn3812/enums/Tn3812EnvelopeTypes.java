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
*NONE ’FF’X 
*MFRTYPMDL ’00’X 
*B5 ’06’X
*MONARCH ’09’X
*NUMBER9 ’0A’X
*NUMBER10 ’0B’X
*C5 ’0C’X
*DL ’0D’X
*/

/**
 * Definitions of envelope type commands for tn3812 parameter negotiation
 */
public enum Tn3812EnvelopeTypes {
	
	NONE("*NONE", (byte)0xff),
	MFRTYPMDL("*MFRTYPMDL", (byte)0x00),
	B5("*B5", (byte)0x06),
	MONARCH("*MONARCH", (byte)0x09),
	NUMBER9("*NUMBER9", (byte)0x0a),
	NUMBER10("*NUMBER10", (byte)0x0b),
	C5("*C5", (byte)0x0c),
	DL("*DL", (byte)0x0d)
	;

    private final String name;
    private final byte value;

    private Tn3812EnvelopeTypes(String name, byte value) {
        this.name = name;
        this.value = value;
    }

	public String getName() {
		return name;
	}

	public byte getValue() {
		return value;
	}
    
}
