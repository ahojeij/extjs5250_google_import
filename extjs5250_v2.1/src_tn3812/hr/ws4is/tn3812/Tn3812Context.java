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

import hr.ws4is.tn3812.enums.Tn3812ResponseTypeData;
import hr.ws4is.tn3812.interfaces.ITn3812Config;
import hr.ws4is.tn3812.interfaces.ITn3812Context;
import hr.ws4is.tn3812.interfaces.ITn3812DataListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.net.StandardSocketOptions.SO_KEEPALIVE;

/**
 * Main session context class, will be used in web server to be mapped to user session
 * and to enable access to virtual printer     
 */
class Tn3812Context implements ITn3812Context {
	
    private static AsynchronousChannelGroup channelGroup;
    
    static {
		try {
		    channelGroup = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
		} catch (IOException e) {
		    e.printStackTrace();
		}
    }

    private AsynchronousSocketChannel channel;
    
    final private ConcurrentLinkedQueue<Runnable> writeQueue;
    
    final private ByteBuffer inBuffer;
    final private ByteBuffer outBuffer;
    
    final private IncomingDataNegotiation incomingNegotiationDataHandler;
    final private IncomingDataProcessing incomingProcessingDataHandler;
    
    final private OutgoingData outgoingDataHandler;

    final private ITn3812Config config;
    final InetSocketAddress addr;
    
	//private List<Tn3812PrinterListener> printerListeners = null;
	private List<ITn3812DataListener> dataListeners = null;
	ReadWriteLock dataListenersLock = null;
	ReadWriteLock dataLock = null;
	
	private Map<String,Object> data;
    
	public Tn3812Context(InetSocketAddress addr, ITn3812Config config ) {
		super();
		this.addr = addr;
		this.config = config;
		inBuffer = ByteBuffer.allocate(4096);
		outBuffer = ByteBuffer.allocate(4096);
		incomingNegotiationDataHandler = new IncomingDataNegotiation(this);
		incomingProcessingDataHandler = new IncomingDataProcessing(this);
		outgoingDataHandler = new OutgoingData(this);
		writeQueue = new ConcurrentLinkedQueue<Runnable>();
		dataListenersLock =  new ReentrantReadWriteLock();
		dataLock =  new ReentrantReadWriteLock();
		data = new HashMap<>();
	}
	
	public void setData(String key, Object value){
		dataLock.writeLock().lock();
		try{
			data.put(key, value);	
		} finally{
			dataLock.writeLock().unlock();
		}		
	}
	
	public Object getData(String key){
		dataLock.readLock().lock();
		try{
			return data.get(key);	
		} finally{
			dataLock.readLock().unlock();
		}			
	}
	
	public AsynchronousSocketChannel getChannel() {
		return channel;
	}

	public IncomingDataNegotiation getIncomingDataHandler() {
		return incomingNegotiationDataHandler;
	}
	
	public IncomingDataProcessing getIncomingProcessingDataHandler() {
		return incomingProcessingDataHandler;
	}

	public ByteBuffer getInBuffer() {
		return inBuffer;
	}

	public ByteBuffer getOutBuffer() {
		return outBuffer;
	}

	public ConcurrentLinkedQueue<Runnable> getWriteQueue() {
		return writeQueue;
	}

	public OutgoingData getOutgoingDataHandler() {
		return outgoingDataHandler;
	}

	public ITn3812Config getConfig() {
		return config;
	}

	
	public final void fireData(Tn3812ResponseTypeData type, ByteBuffer buffer) {
		dataListenersLock.writeLock().lock();
		try {
			if (dataListeners != null) {
				for (ITn3812DataListener listener : dataListeners ){
					switch(type){
					case HEAD : 
						listener.onHeader(buffer);
						break;
					case FIRST : 
						listener.onFirstChain(buffer);
						break;
					case CHAIN :
						listener.onChain(buffer);
						break;
					case LAST :
						listener.onLastChain(buffer);
						break;
					case INIT :
						listener.onInit(this);
						break;
					case CLOSED :
						listener.onClosed();
						break;								
					}
				}
			}
		} finally {
			dataListenersLock.writeLock().unlock();
		}		
	}
	
	private void removAllListeners(){
		if (dataListeners != null) {
			for (ITn3812DataListener listener : dataListeners ){
				removeDataListener(listener);
			}
		}	
	}

	@Override
	public void connect()  throws IOException, InterruptedException, ExecutionException {
		channel = AsynchronousSocketChannel.open(channelGroup);
		channel.setOption(SO_KEEPALIVE, true);
		channel.connect(addr).get();
		BasicCompletionHandler.startReading(this, incomingNegotiationDataHandler);
	}
	
	@Override
	public void disconnect()  {
		try {
			channel.shutdownInput().shutdownOutput().close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			writeQueue.clear();
			removAllListeners();	
		}		
	}
	
	@Override
	public boolean isConnected(){
		return channel.isOpen();
	}
	
	
	@Override
	public final void addDataListener(ITn3812DataListener listener) {
		if(channel!=null && !channel.isOpen()) return;
		dataListenersLock.writeLock().lock();
		try {
			if (dataListeners == null) {
				dataListeners = new LinkedList<ITn3812DataListener>();
			}
			dataListeners.add(listener);
		} finally {
			dataListenersLock.writeLock().unlock();
		}
	}

	@Override
	public final void removeDataListener(ITn3812DataListener listener) {
		dataListenersLock.writeLock().lock();
		try {
			if (dataListeners != null) {
				dataListeners.remove(listener);
			}
		} finally {
			dataListenersLock.writeLock().unlock();
		}
	}

	@Override
	public ITn3812Config getconfiguration() {
		return config;
	}	
}
