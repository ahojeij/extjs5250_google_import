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
package hr.ws4is.tn3812.drivers;

import hr.ws4is.tn3812.interfaces.ITn3812Context;
import hr.ws4is.tn3812.interfaces.ITn3812DataListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/** 
 * Generic SCS data stream listener used to save report content in the file.
 * Mostly used for debugging purposes
 */
public class RawFileWriter implements ITn3812DataListener {

	FileChannel channel = null; 

	@Override
	public void onInit(ITn3812Context config) {
		
	}	
	
	@Override
	public void onHeader(ByteBuffer data) {
		//TODO setup printer data
	}

	@Override
	public void onFirstChain(ByteBuffer data) {
		close(channel);
		channel = open();
		write(channel, data);	
	}

	@Override
	public void onChain(ByteBuffer data) {
		write(channel, data);			
	}

	@Override
	public void onLastChain(ByteBuffer data) {
		write(channel, data);	
		close(channel);
	}
	
	private void write(FileChannel channel, ByteBuffer data){
		try {
			channel.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private FileChannel  open(){
		FileChannel channel = null;		
		try {
			channel = new FileOutputStream("output/" + System.nanoTime()).getChannel();						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 		
		return channel;
	}
	
	private void close(FileChannel channel){
		if(channel == null) return;
		try {
			channel.close();
			channel = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
