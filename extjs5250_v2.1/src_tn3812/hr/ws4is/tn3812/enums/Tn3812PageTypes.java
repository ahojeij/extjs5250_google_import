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
*LETTER ’01’X *
*LEGAL ’02’X
*EXECUTIVE ’03’X
*A4 ’04’X
*A5 ’05’X
*B5 ’06’X
*CONT80 ’07’X
*CONT132 ’08’X
*A3 ’0E’X
*B4 ’0F’X
*LEDGER ’10’X
*/

/**
 * Definitions of page type commands for tn3812 parameter negotiation *
 */
public enum Tn3812PageTypes {

	NONE("*NONE", (byte) 0xff),
	MFRTYPMDL("*MFRTYPMDL", (byte) 0x00),
	LETTER("*LETTER", (byte) 0x01),
	LEGAL("*LEGAL", (byte) 0x02),
	EXECUTIVE("*EXECUTIVE", (byte) 0x03),
	A4("*A4", (byte) 0x04),
	A5("*A5", (byte) 0x05),
	B5("*B5", (byte) 0x06),
	CONT80("*CONT80", (byte) 0x07),
	CONT132("*CONT132", (byte) 0x08),
	A3("*A3", (byte) 0x0e),
	B4("*B4", (byte) 0x0f),
	LEDGER("*LEDGER", (byte) 0x10)
	;

    private final String name;
    private final byte value;

    private Tn3812PageTypes(final String name, final byte value) {
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
