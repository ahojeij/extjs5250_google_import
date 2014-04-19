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
xxxx x111 --> Reserved
xxxx 1xxx --> Last of chain
xxx1 xxxx --> First of chain
xx1x xxxx --> Printer now ready
x1xx xxxx --> Intervention Required
1xxx xxxx --> Error Indicator
*/

/**
 * Printer status flags 
 */
public enum Tn3812Flag {

	RESERVED((byte)0x07),
	LAST_OF_CHAIN((byte)0x08),
	FIRST_OF_CHAIN((byte)0x10),
	PRINTER_READY((byte)0x20),
	INTERVENTION((byte)0x40),
	ERROR((byte)0x80)
	;

    private final byte value;

    private Tn3812Flag( byte value) {
        this.value = value;
    }

	public byte getValue() {
		return value;
	}

}
