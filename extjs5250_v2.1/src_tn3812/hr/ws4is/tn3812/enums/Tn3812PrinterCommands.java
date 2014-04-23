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
If BYTE 7 = xx1x xxxx then bytes 10-LL may contain:
    Printer ready C9 00 00 00 02

If BYTE 7 = x1xx xxxx then bytes 10-LL may contain:
	Command/parameter not valid C9 00 03 02 2x
	Print check                 C9 00 03 02 3x
	Forms check                 C9 00 03 02 4x
	Normal periodic condition   C9 00 03 02 5x
	Data stream error           C9 00 03 02 6x
	Machine/print/ribbon check  C9 00 03 02 8x
 
If BYTE 7 = 1xxx xxxx then bytes 10-LL may contain:
	Cancel                  08 11 02 00
	Invalid print parameter 08 11 02 29
	Invalid print command   08 11 02 28

- 5494 Remote Control Unit Functions Reference guide
	Machine check         C9 00 03 02 11
	Graphics check        C9 00 03 02 26
	Print check           C9 00 03 02 31
	Form jam              C9 00 03 02 41
	Paper jam             C9 00 03 02 47
	End of forms          C9 00 03 02 50
	Printer not ready     C9 00 03 02 51
	Data stream - class 1 C9 00 03 02 66 loss of text
	Data stream - class 2 C9 00 03 02 67 text appearance
	Data stream - class 3 C9 00 03 02 68 multibyte control error
	Data stream - class 4 C9 00 03 02 69 multibyte control parm
	Cover unexpectedly open C9 00 03 02 81
	Machine check           C9 00 03 02 86
	Machine check           C9 00 03 02 87
	Ribbon check            C9 00 03 02 88
*/

/**
 * Printer commands used to sent to the printer from the server
 */
public enum Tn3812PrinterCommands {
	
	PRINTER_READY      ( new byte[] {(byte)0xC9, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x02 }),
	
	CACNEL             ( new byte[] {(byte)0x08, (byte)0x11, (byte)0x02, (byte)0x00}),
	INVALID_PARAMETER  ( new byte[] {(byte)0x08, (byte)0x11, (byte)0x02, (byte)0x29}),
	INVALID_COMMAND    ( new byte[] {(byte)0x08, (byte)0x11, (byte)0x02, (byte)0x28}),
	
	MACHINE_CHECK      ( new byte[] {(byte)0xC9, (byte)0x00, (byte)0x03, (byte)0x02, (byte)0x11 }),
	GRAPHICS_CHECK     ( new byte[] {(byte)0xC9, (byte)0x00, (byte)0x03, (byte)0x02, (byte)0x26 }),
	PRINT_CHECK        ( new byte[] {(byte)0xC9, (byte)0x00, (byte)0x03, (byte)0x02, (byte)0x31 }),
	FORM_JAM           ( new byte[] {(byte)0xC9, (byte)0x00, (byte)0x03, (byte)0x02, (byte)0x41 }),
	PAPER_JAM          ( new byte[] {(byte)0xC9, (byte)0x00, (byte)0x03, (byte)0x02, (byte)0x47 }),
	END_OF_FORMS       ( new byte[] {(byte)0xC9, (byte)0x00, (byte)0x03, (byte)0x02, (byte)0x50 }),
	PRINTER_NOT_READY  ( new byte[] {(byte)0xC9, (byte)0x00, (byte)0x03, (byte)0x02, (byte)0x51 }),
	DATA_STREAM_CLASS_1( new byte[] {(byte)0xC9, (byte)0x00, (byte)0x03, (byte)0x02, (byte)0x66 }),
	DATA_STREAM_CLASS_2( new byte[] {(byte)0xC9, (byte)0x00, (byte)0x03, (byte)0x02, (byte)0x67 }),
	DATA_STREAM_CLASS_3( new byte[] {(byte)0xC9, (byte)0x00, (byte)0x03, (byte)0x02, (byte)0x68 }),
	DATA_STREAM_CLASS_4( new byte[] {(byte)0xC9, (byte)0x00, (byte)0x03, (byte)0x02, (byte)0x69 }),
	COVER_OPEN         ( new byte[] {(byte)0xC9, (byte)0x00, (byte)0x03, (byte)0x02, (byte)0x81 }),
	MACHINE_CHECK_2    ( new byte[] {(byte)0xC9, (byte)0x00, (byte)0x03, (byte)0x02, (byte)0x86 }),
	MACHINE_CHECK_3    ( new byte[] {(byte)0xC9, (byte)0x00, (byte)0x03, (byte)0x02, (byte)0x87 }),
	RIBBON_CHECK       ( new byte[] {(byte)0xC9, (byte)0x00, (byte)0x03, (byte)0x02, (byte)0x88 })
	;

    private final byte[]  value;

    private Tn3812PrinterCommands(byte[] value) {
        this.value = value;
    }

	public byte[] getValue() {
		return value;
	}

}
