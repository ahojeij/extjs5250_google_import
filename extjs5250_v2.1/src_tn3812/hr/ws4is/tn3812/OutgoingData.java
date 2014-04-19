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
 * Callback handler used to start another write thread after previous is finished
 */
class OutgoingData implements CompletionHandler<Integer, Void> {
	
	private static Logger logger = LoggerFactory.getLogger(IncomingDataNegotiation.class);

	Tn3812Context ctx;
	
	public OutgoingData(Tn3812Context ctx) {
		super();
		this.ctx = ctx;
	}
	
	public void completed(Integer result, Void attachment) {
	    logger.debug(String.format("[client] %d sent %d bytes of data", ctx.getConfig().getDevName(), result));
	    if (!ctx.getWriteQueue().isEmpty()) {
			logger.debug("Continuing down write queue");
			ctx.getWriteQueue().poll().run();
	    }
	}
	
	public void failed(Throwable exc, Void attachment) {
	    exc.printStackTrace();
	}

}