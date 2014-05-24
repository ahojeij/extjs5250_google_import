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

import static java.net.StandardSocketOptions.SO_KEEPALIVE;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.LoggerFactory;

/**
 * Main session context class, will be used in web server to be mapped to user
 * session. and to enable access to virtual printer.
 */
class Tn3812Context implements ITn3812Context {

    private static final int BUFFER_SIZE = 4096;
    private static AsynchronousChannelGroup channelGroup;

    static {
        try {
            channelGroup = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
        } catch (IOException exception) {
            LoggerFactory.getLogger(Tn3812Context.class).error(exception.getMessage(), exception);
        }
    }

    private AsynchronousSocketChannel channel;

    private final ConcurrentLinkedQueue<Runnable> writeQueue;

    private final ByteBuffer inBuffer;
    private final ByteBuffer outBuffer;

    private final IncomingDataNegotiation incomingNegotiationDataHandler;
    private final IncomingDataProcessing incomingProcessingDataHandler;

    private final OutgoingData outgoingDataHandler;

    private final ITn3812Config config;
    private final InetSocketAddress addr;

    // private List<Tn3812PrinterListener> printerListeners = null;
    private List<ITn3812DataListener> dataListeners = null;
    private ReadWriteLock dataListenersLock = null;
    private ReadWriteLock dataLock = null;

    private Map<String, Object> data;

    public Tn3812Context(final InetSocketAddress addr, final ITn3812Config config) {
        super();
        this.addr = addr;
        this.config = config;
        inBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        outBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        incomingNegotiationDataHandler = new IncomingDataNegotiation(this);
        incomingProcessingDataHandler = new IncomingDataProcessing(this);
        outgoingDataHandler = new OutgoingData(this);
        writeQueue = new ConcurrentLinkedQueue<Runnable>();
        dataListenersLock = new ReentrantReadWriteLock();
        dataLock = new ReentrantReadWriteLock();
        data = new HashMap<>();
    }

    public void setData(final String key, final Object value) {
        dataLock.writeLock().lock();
        try {
            data.put(key, value);
        } finally {
            dataLock.writeLock().unlock();
        }
    }

    public Object getData(final String key) {
        dataLock.readLock().lock();
        try {
            return data.get(key);
        } finally {
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

    public final void fireData(final Tn3812ResponseTypeData type, final ByteBuffer buffer) {
        dataListenersLock.writeLock().lock();
        try {
            if (dataListeners != null) {
                for (ITn3812DataListener listener : dataListeners) {
                    switch (type) {
                    case HEAD:
                        listener.onHeader(buffer);
                        break;
                    case FIRST:
                        listener.onFirstChain(buffer);
                        break;
                    case CHAIN:
                        listener.onChain(buffer);
                        break;
                    case LAST:
                        listener.onLastChain(buffer);
                        break;
                    case INIT:
                        listener.onInit(this);
                        break;
                    case CLOSED:
                        listener.onClosed();
                        break;
                    case ERROR:
                        listener.onError(this, buffer);
                    default:
                        break;
                    }
                }
            }
        } finally {
            dataListenersLock.writeLock().unlock();
        }
    }

    private void removAllListeners() {
        if (dataListeners != null) {
            for (ITn3812DataListener listener : dataListeners) {
                removeDataListener(listener);
            }
        }
    }

    @Override
    public void connect() throws IOException, InterruptedException, ExecutionException {
        channel = AsynchronousSocketChannel.open(channelGroup);
        channel.setOption(SO_KEEPALIVE, true);
        channel.connect(addr).get();
        BasicCompletionHandler.startReading(this, incomingNegotiationDataHandler);
    }

    @Override
    public void disconnect() {
        try {
            if (channel.isOpen()) {
                channel.close();
            }
            // channel.shutdownInput().shutdownOutput().close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writeQueue.clear();
            removAllListeners();
        }
    }

    @Override
    public boolean isConnected() {
        return channel.isOpen();
    }

    @Override
    public final void addDataListener(final ITn3812DataListener listener) {
        if (channel != null && !channel.isOpen()) {
            return;
        }
        Lock lock = dataListenersLock.writeLock();
        lock.lock();
        try {
            if (dataListeners == null) {
                dataListeners = new LinkedList<ITn3812DataListener>();
            }
            dataListeners.add(listener);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public final void removeDataListener(final ITn3812DataListener listener) {
        Lock lock = dataListenersLock.writeLock();
        lock.lock();
        try {
            if (dataListeners != null) {
                dataListeners.remove(listener);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ITn3812Config getconfiguration() {
        return config;
    }

}
