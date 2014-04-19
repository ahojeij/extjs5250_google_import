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

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

/**
 * Basic data send callback handler with generally used methods 
 */
abstract class BasicCompletionHandler implements CompletionHandler<Integer, Void> {

	Tn3812Context ctx;
	
	public BasicCompletionHandler(Tn3812Context ctx) {
		super();
		this.ctx = ctx;
	}
	
	public static void startReading(Tn3812Context ctx, BasicCompletionHandler handler){
    	AsynchronousSocketChannel channel = ctx.getChannel();
    	ByteBuffer buffer = ctx.getInBuffer();		
		buffer.clear();
	    channel.read(buffer, null, handler);
	}
	
	public static void startWriting(Tn3812Context ctx) throws InterruptedException, ExecutionException{
		AsynchronousSocketChannel channel = ctx.getChannel();
		ByteBuffer buffer = ctx.getInBuffer();  
		
		while(buffer.hasRemaining()){
			Integer i = channel.write(buffer).get();
			System.out.println("Sent bytes : " + i);
		}
		buffer.clear();
	}
	
	public static byte[] readFromBuffer(Tn3812Context ctx, Integer result){
		byte[] data = null;
	    if (result > 0) {
	    	data = new byte[result];
	    	ByteBuffer buffer = ctx.getInBuffer();	
			buffer.flip();
			buffer.get(data);
	    } else {
	    	data = new byte[0];
	    }
	    return data;
	}
	
	public static void writeToBuffer(Tn3812Context ctx, ByteBuffer data) throws InterruptedException, ExecutionException{
    	ByteBuffer buffer = ctx.getInBuffer();    	
    	data.flip();
    	buffer.clear();
    	buffer.put(data);
    	buffer.flip();
    }	
}
