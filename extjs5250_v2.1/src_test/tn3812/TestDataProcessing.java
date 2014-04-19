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

import hr.ws4is.tn3812.drivers.listeners.IProcessorListener;
import hr.ws4is.tn3812.drivers.listeners.ListenerType;
import hr.ws4is.tn3812.drivers.listeners.ProcessorListenerFactory;
import hr.ws4is.tn3812.drivers.processors.IProcessor;
import hr.ws4is.tn3812.drivers.processors.ProcessorFactory;
import hr.ws4is.tn3812.drivers.processors.ProcessorType;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/*
 * Tests data processing from saved stream
 */
public class TestDataProcessing {

	public static void main(String[] args) throws Exception {
		IProcessor processor = ProcessorFactory.initProcessor(ProcessorType.SCS);
		IProcessorListener listener = ProcessorListenerFactory.initListener(ListenerType.ASCII);
		
		test_file1(processor, listener);
	}
	
	// play recorded data stream 
	private static void test_file1(IProcessor processor, IProcessorListener listener) {
		ByteBuffer buffer = null;
		
		//simulate printer initialization 
		buffer = load("output/file1/1989464302747");
		processor.initialize(buffer);

		//simulate printer initialization 
		buffer = load("output/file1/2041182656084");
		processor.initialize(buffer);

		//simulate first of chain - report startup header
		processor.start(listener);

		//simulate chain - report dta flow to the printer 
		buffer = load("output/file1/2052755946773");	
		processor.process(buffer);
		
		//simulate last of chain - end report printing
		processor.finnish();		
		
	}

	//load recorded data stream from file
	private static ByteBuffer load(String file) {
		ByteBuffer buffer = null;
		try {
			RandomAccessFile aFile = new RandomAccessFile(file, "r");
			FileChannel inChannel = aFile.getChannel();
			long fileSize = inChannel.size();
			buffer = ByteBuffer.allocate((int) fileSize);
			inChannel.read(buffer);
			// buffer.rewind();
			buffer.flip();
			inChannel.close();
			aFile.close();
		} catch (IOException exc) {
			System.out.println(exc);
			System.exit(1);
		}
		return buffer;
	}
}
