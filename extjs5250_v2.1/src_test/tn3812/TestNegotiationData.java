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

import java.io.IOException;
import java.nio.ByteBuffer;

/*
 * test some data stream negotiation
 */
public class TestNegotiationData {
	
	static byte [] data = new byte[] { 0, (byte)73, (byte)18, (byte)-96, (byte)-112, (byte)0, (byte)5, (byte)96, (byte)6, (byte)0, (byte)32, (byte)-64, (byte)0, (byte)61, (byte)0, (byte)0, (byte)-8, (byte)-7, (byte)-14, (byte)-11, (byte)-41, (byte)-28, (byte)-62, (byte)-15, (byte)64, (byte)64, (byte)64, (byte)64, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)-1, (byte)-17};

	static byte[] saveStream;
	
	public static void main(String[] args) throws IOException {
		
		System.out.println(bytesToHex(data));
		
		ByteBuffer buffer  = ByteBuffer.allocate(data.length);
		buffer.put(data);
		buffer.flip();
		
		int size = buffer.getShort();
		buffer.compact();
		buffer.limit(size);
		System.out.println(bytesToHex(buffer.array()));
		
		buffer.rewind();
		loadStream(data, 0);
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
	

	static void loadStream(byte abyte0[], int i)
			     throws IOException {

			      int j = 0;
			      int size = 0;
			      if (saveStream == null) {
			         j = (abyte0[i] & 0xff) << 8 | abyte0[i + 1] & 0xff;
			         size = abyte0.length;
			      }
			      else {
			         size = saveStream.length + abyte0.length;
			         byte[] inter = new byte[size];
			         System.arraycopy(saveStream, 0, inter, 0, saveStream.length);
			         System.arraycopy(abyte0, 0, inter, saveStream.length, abyte0.length);
			         abyte0 = new byte[size];
			         System.arraycopy(inter, 0, abyte0, 0, size);
			         saveStream = null;
			         inter = null;
			         j = (abyte0[i] & 0xff) << 8 | abyte0[i + 1] & 0xff;
			      }

			      if (j > size) {
			         saveStream = new byte[abyte0.length];
			         System.arraycopy(abyte0, 0, saveStream, 0, abyte0.length);
			      }
			      else {
			         byte abyte1[];
			         try {
			            abyte1 = new byte[j + 2];

			            System.arraycopy(abyte0, i, abyte1, 0, j + 2);
			            //TODO data to pdf
			            if(abyte0.length > abyte1.length + i)
			                loadStream(abyte0, i + j + 2);
			         }
			         catch (Exception ex) {
			           ex.printStackTrace();
			         }
			      }
			   }

	   
}
