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

import hr.ws4is.tn3812.drivers.listeners.IProcessorListener;
import hr.ws4is.tn3812.drivers.listeners.ListenerType;
import hr.ws4is.tn3812.drivers.listeners.ProcessorListenerFactory;
import hr.ws4is.tn3812.drivers.processors.IProcessor;
import hr.ws4is.tn3812.drivers.processors.ProcessorFactory;
import hr.ws4is.tn3812.drivers.processors.ProcessorType;
import hr.ws4is.tn3812.interfaces.ITn3812Context;
import hr.ws4is.tn3812.interfaces.ITn3812DataListener;

import java.nio.ByteBuffer;

/**
 * SCS data stream listener used to convert SCS data to PDF PDF will be saved in
 * temp location
 */
class PdfFileWriter implements ITn3812DataListener {

    IProcessor scsProcessor;
    ITn3812Context config;

    @Override
    public void onInit(final ITn3812Context config) {
        scsProcessor = ProcessorFactory.initProcessor(ProcessorType.SCS);
        this.config = config;
    }

    @Override
    public void onHeader(final ByteBuffer data) {
        scsProcessor.initialize(config, data);
    }

    @Override
    public void onFirstChain(final ByteBuffer data) {
        final IProcessorListener pdfListener = ProcessorListenerFactory.initListener(ListenerType.PDF);
        scsProcessor.start(pdfListener);
        scsProcessor.process(data);
    }

    @Override
    public void onChain(final ByteBuffer data) {
        scsProcessor.process(data);
    }

    @Override
    public void onLastChain(final ByteBuffer data) {
        scsProcessor.process(data);
        scsProcessor.finish();
    }

    @Override
    public void onClosed() {

    }

    @Override
    public void onError(final ITn3812Context config, final ByteBuffer data) {

    }

    @Override
    public void onRemoved() {
    }

}
