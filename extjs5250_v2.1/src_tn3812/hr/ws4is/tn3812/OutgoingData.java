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

import java.nio.channels.CompletionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Callback handler used to start another write thread after previous is
 * finished
 */
class OutgoingData implements CompletionHandler<Integer, Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncomingDataNegotiation.class);

    private Tn3812Context ctx;

    public OutgoingData(final Tn3812Context ctx) {
        super();
        this.ctx = ctx;
    }

    public void completed(final Integer result, final Void attachment) {
        LOGGER.debug(String.format("[client] %d sent %d bytes of data", ctx.getConfig().getDevName(), result));
        if (!ctx.getWriteQueue().isEmpty()) {
            LOGGER.debug("Continuing down write queue");
            ctx.getWriteQueue().poll().run();
        }
    }

    public void failed(final Throwable exc, final Void attachment) {
        exc.printStackTrace();
    }

}