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
package hr.ws4is.tn3812.drivers.listeners;

import hr.ws4is.tn3812.drivers.processors.IControls;
import hr.ws4is.tn3812.drivers.processors.scs.SCSStyleType;
import hr.ws4is.tn3812.interfaces.ITn3812Context;

import java.nio.ByteBuffer;

public interface IProcessorListener {
	
	public void onFormFeed();
	public void onLineFeed();
	
	public void onHorizontalMove();
	public void onVerticalMove();
	public void onNewLine();
	
	public void onFontStyleChange(SCSStyleType type);

	public void onStart(IControls controls);
	public void onFinish(ITn3812Context context);
	
	public void onData(ByteBuffer buffer);
	
}
