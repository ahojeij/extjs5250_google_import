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
package tn3812;

import hr.ws4is.tn3812.drivers.processors.IProcessor;
import hr.ws4is.tn3812.drivers.processors.ProcessorFactory;
import hr.ws4is.tn3812.drivers.processors.ProcessorType;

import java.io.IOException;
import java.nio.ByteBuffer;

/*
 * test some data stream 
 */
public class TestByteStream {
	
	static byte [] data = new byte[] {(byte)43, (byte) -47, (byte) 3, (byte) -127, (byte) -1, (byte) -1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 43, (byte) -56, (byte) 3, (byte) 64, (byte) 1, (byte) 0, (byte) 52, (byte) -60, (byte) 1, (byte) 0, (byte) 52, (byte) -64, (byte) 2, (byte) -11, (byte) -9, (byte) -14, (byte) -14, (byte) -30, (byte) -30, (byte) -15, (byte) 64, (byte) -27, (byte) -11, (byte) -39, (byte) -13, (byte) -44, (byte) -16, (byte) 64, (byte) -16, (byte) -12, (byte) -16, (byte) -11, (byte) -14, (byte) -8, (byte) 0, (byte) 52, (byte) -64, (byte) 47, (byte) 64, (byte) 64, (byte) 64, (byte) -47, (byte) -106, (byte) -126, (byte) 64, (byte) -45, (byte) -106, (byte) -121, (byte) 0, (byte) 52, (byte) -64, (byte) 86, (byte) -30, (byte) -15, (byte) -16, (byte) -12, (byte) -29, (byte) -26, (byte) -56, (byte) -44, (byte) 0, (byte) 52, (byte) -64, (byte) 95, (byte) -16, (byte) -15, (byte) 97, (byte) -14, (byte) -16, (byte) 97, (byte) -16, (byte) -10, (byte) 64, (byte) -16, (byte) -16, (byte) 122, (byte) -15, (byte) -13, (byte) 122, (byte) -16, (byte) -11, (byte) 0, (byte) 52, (byte) -64, (byte) 118, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) -41, (byte) -127, (byte) -121, (byte) -123, (byte) 0, (byte) 52, (byte) -64, (byte) 127, (byte) 64, (byte) 64, (byte) 64, (byte) -15, (byte) 0, (byte) 21, (byte) 21, (byte) 0, (byte) 52, (byte) -64, (byte) 3, (byte) -47, (byte) -106, (byte) -126, (byte) 64, (byte) -107, (byte) -127, (byte) -108, (byte) -123, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 122, (byte) 0, (byte) 52, (byte) -64, (byte) 36, (byte) -40, (byte) -29, (byte) -42, (byte) -61, (byte) -41, (byte) -41, (byte) 0, (byte) 52, (byte) -64, (byte) 52, (byte) -28, (byte) -94, (byte) -123, (byte) -103, (byte) 64, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 122, (byte) 0, (byte) 52, (byte) -64, (byte) 74, (byte) -40, (byte) -29, (byte) -61, (byte) -41, (byte) 0, (byte) 52, (byte) -64, (byte) 87, (byte) -43, (byte) -92, (byte) -108, (byte) -126, (byte) -123, (byte) -103, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 122, (byte) 0, (byte) 52, (byte) -64, (byte) 120, (byte) -16, (byte) -14, (byte) -12, (byte) -13, (byte) -13, (byte) -8, (byte) 0, (byte) 21, (byte) 0, (byte) 52, (byte) -64, (byte) 3, (byte) -47, (byte) -106, (byte) -126, (byte) 64, (byte) -124, (byte) -123, (byte) -94, (byte) -125, (byte) -103, (byte) -119, (byte) -105, (byte) -93, (byte) -119, (byte) -106, (byte) -107, (byte) 64, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 122, (byte) 0, (byte) 52, (byte) -64, (byte) 36, (byte) -40, (byte) -29, (byte) -42, (byte) -61, (byte) -29, (byte) -61, (byte) -41, (byte) -55, (byte) -41, (byte) 0, (byte) 52, (byte) -64, (byte) 52, (byte) -45, (byte) -119, (byte) -126, (byte) -103, (byte) -127, (byte) -103, (byte) -88, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 122, (byte) 0, (byte) 52, (byte) -64, (byte) 74, (byte) -40, (byte) -30, (byte) -24, (byte) -30, (byte) 0, (byte) 21, (byte) 21, (byte) 0, (byte) 21, (byte) 0, (byte) 52, (byte) -64, (byte) 1, (byte) -44, (byte) -30, (byte) -57, (byte) -55, (byte) -60, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) -29, (byte) -24, (byte) -41, (byte) -59, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) -30, (byte) -59, (byte) -27, (byte) 64, (byte) 64, (byte) -60, (byte) -63, (byte) -29, (byte) -59, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) -29, (byte) -55, (byte) -44, (byte) -59, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) -58, (byte) -39, (byte) -42, (byte) -44, (byte) 64, (byte) -41, (byte) -57, (byte) -44, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) -45, (byte) -55, (byte) -62, (byte) -39, (byte) -63, (byte) -39, (byte) -24, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) -55, (byte) -43, (byte) -30, (byte) -29, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) -29, (byte) -42, (byte) 64, (byte) -41, (byte) -57, (byte) -44, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) -45, (byte) -55, (byte) -62, (byte) -39, (byte) -63, (byte) -39, (byte) -24, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) -55, (byte) -43, (byte) -30, (byte) -29, (byte) 0, (byte) 21, (byte) 0, (byte) 21, (byte) 0, (byte) 52, (byte) -64, (byte) 1, (byte) -61, (byte) -41, (byte) -58, (byte) -15, (byte) -15, (byte) -14, (byte) -12, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) -55, (byte) -107, (byte) -122, (byte) -106, (byte) -103, (byte) -108, (byte) -127, (byte) -93, (byte) -119, (byte) -106, (byte) -107, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) -16, (byte) -16, (byte) 64, (byte) 64, (byte) 64, (byte) -16, (byte) -15, (byte) 97, (byte) -14, (byte) -16, (byte) 97, (byte) -16, (byte) -10, (byte) 64, (byte) 64, (byte) -16, (byte) -16, (byte) 122, (byte) -15, (byte) -13, (byte) 122, (byte) -16, (byte) -15, (byte) 75, (byte) -7, (byte) -8, (byte) -12, (byte) -10, (byte) -8, (byte) -8, (byte) 64, (byte) 64, (byte) -40, (byte) -26, (byte) -29, (byte) -41, (byte) -55, (byte) -55, (byte) -41, (byte) -41, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) -40, (byte) -30, (byte) -24, (byte) -30, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) -16, (byte) -10, (byte) -15, (byte) -61, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 92, (byte) -59, (byte) -25, (byte) -29, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 92, (byte) -43, (byte) 0, (byte) 21, (byte) 0, (byte) 52, (byte) -64, (byte) 1, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) -44, (byte) -123, (byte) -94, (byte) -94, (byte) -127, (byte) -121, (byte) -123, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 75, (byte) 64, (byte) 122, (byte) 64, (byte) 64, (byte) 64, (byte) -47, (byte) -106, (byte) -126, (byte) 64, (byte) -16, (byte) -14, (byte) -12, (byte) -13, (byte) -13, (byte) -8, (byte) 97, (byte) -40, (byte) -29, (byte) -61, (byte) -41, (byte) 97, (byte) -40, (byte) -29, (byte) -42, (byte) -61, (byte) -41, (byte) -41, (byte) 64, (byte) -94, (byte) -93, (byte) -127, (byte) -103, (byte) -93, (byte) -123, (byte) -124, (byte) 64, (byte) -106, (byte) -107, (byte) 64, (byte) -16, (byte) -15, (byte) 97, (byte) -14, (byte) -16, (byte) 97, (byte) -16, (byte) -10, (byte) 64, (byte) -127, (byte) -93, (byte) 64, (byte) -16, (byte) -16, (byte) 122, (byte) -15, (byte) -13, (byte) 122, (byte) -16, (byte) -15, (byte) 64, (byte) -119, (byte) -107, (byte) 0, (byte) 21, (byte) 0, (byte) 52, (byte) -64, (byte) 1, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) 64, (byte) -94, (byte) -92, (byte) -126, (byte) -94, (byte) -88, (byte) -94, (byte) -93, (byte) -123, (byte) -108, (byte) 64, (byte) -40, (byte) -30, (byte) -24, (byte) -30, (byte) -26, (byte) -39, (byte) -46, (byte) 64};

	
	public static void main(String[] args) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(data.length);
		buffer.put(data);
		
		//System.out.println(bytesToHex(data));
		IProcessor processor =  ProcessorFactory.initProcessor(ProcessorType.SCS);
		processor.start(null);
		processor.process(buffer);
	}
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
}
