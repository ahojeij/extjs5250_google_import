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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic data send callback handler with generally used methods
 */
abstract class BasicCompletionHandler implements CompletionHandler<Integer, Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicCompletionHandler.class);

    final Tn3812Context ctx;

    public BasicCompletionHandler(final Tn3812Context ctx) {
        super();
        this.ctx = ctx;
    }

    protected Tn3812Context getCtx() {
        return ctx;
    }

    public boolean handleClosed(final Integer result, final Void attachment) {
        boolean status = false;
        try {
            if (result == -1) {
                LOGGER.warn("No data received from socket!");
                // connection closed from host
                // TODO , call listeners
                ctx.getChannel().close();
                status = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static void startReading(final Tn3812Context ctx, final BasicCompletionHandler handler) {
        final AsynchronousSocketChannel channel = ctx.getChannel();
        final ByteBuffer buffer = ctx.getInBuffer();
        buffer.clear();
        channel.read(buffer, null, handler);
    }

    public static void startWriting(final Tn3812Context ctx) throws InterruptedException, ExecutionException {
        final AsynchronousSocketChannel channel = ctx.getChannel();
        final ByteBuffer buffer = ctx.getInBuffer();

        while (buffer.hasRemaining()) {
            final Integer i = channel.write(buffer).get();
            System.out.println("Sent bytes : " + i);
        }
        buffer.clear();
    }

    public static byte[] readFromBuffer(final Tn3812Context ctx, final Integer result) {
        byte[] data = null;
        if (result > 0) {
            data = new byte[result];
            final ByteBuffer buffer = ctx.getInBuffer();
            buffer.flip();
            buffer.get(data);
        } else {
            data = new byte[0];
        }
        return data;
    }

    public static void writeToBuffer(final Tn3812Context ctx, final ByteBuffer data) throws InterruptedException, ExecutionException {
        final ByteBuffer buffer = ctx.getInBuffer();
        data.flip();
        buffer.clear();
        buffer.put(data);
        buffer.flip();
    }
}
