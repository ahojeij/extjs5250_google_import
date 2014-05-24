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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic SCS data stream listener used to save report content in the file as
 * received segments. Mostly used for debugging purposes
 */
class RawSegmentFileWriter implements ITn3812DataListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RawSegmentFileWriter.class);

    FileChannel channel = null;

    @Override
    public void onInit(final ITn3812Context config) {

    }

    @Override
    public void onHeader(final ByteBuffer data) {
        channel = open();
        write(channel, data);
        close(channel);
    }

    @Override
    public void onFirstChain(final ByteBuffer data) {
        channel = open();
        write(channel, data);
        close(channel);
    }

    @Override
    public void onChain(final ByteBuffer data) {
        channel = open();
        write(channel, data);
        close(channel);
    }

    @Override
    public void onLastChain(final ByteBuffer data) {
        channel = open();
        write(channel, data);
        close(channel);
    }

    @Override
    public void onClosed() {

    }

    private void write(final FileChannel channel, final ByteBuffer data) {
        try {
            channel.write(data);
        } catch (IOException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
    }

    private FileChannel open() {
        FileChannel channel = null;
        try {
            channel = new FileOutputStream("output/segments/" + System.nanoTime()).getChannel();
        } catch (FileNotFoundException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
        return channel;
    }

    private void close(final FileChannel channel) {
        if (channel == null) {
            return;
        }
        try {
            channel.close();
        } catch (IOException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
    }

    @Override
    public void onError(final ITn3812Context config, final ByteBuffer data) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onRemoved() {
        close(channel);
    }

}
