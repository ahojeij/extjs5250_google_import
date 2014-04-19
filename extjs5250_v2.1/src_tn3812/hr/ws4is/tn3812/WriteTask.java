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

/**
 * Thread responsible for sending data to AS/400
 * After data is send, call next available from queue through handler
 */
class WriteTask implements Runnable {
	
	private ByteBuffer data;

	Tn3812Context ctx;
	
	public WriteTask(Tn3812Context ctx, ByteBuffer data) {
	    this.data = data;
	    this.ctx = ctx;
	}

	public void run() {
	    ctx.getChannel().write(data, null, ctx.getOutgoingDataHandler());
	}
	
}


