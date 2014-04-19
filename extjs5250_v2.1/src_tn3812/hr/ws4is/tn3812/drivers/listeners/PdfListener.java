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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import hr.ws4is.tn3812.drivers.processors.IControls;
import hr.ws4is.tn3812.drivers.processors.SCSStyleType;

final class PdfListener implements IProcessorListener {

	ByteBuffer buffer;
	
	@Override
	public void onFormFeed(IControls controls) {
		System.out.println("*** onFormFeed");
	}

	@Override
	public void onLineFeed(IControls controls) {
		System.out.println("*** onLineFeed");
	}

	@Override
	public void onHorizontalMove(IControls controls) {
		System.out.println("*** onHorizontalMove");
	}

	@Override
	public void onVerticalMove(IControls controls) {
		System.out.println("*** onVerticalMove");
	}

	@Override
	public void onNewLine(IControls controls) {
		//System.out.println("*** onNewLine");
		byte[] data = new byte[buffer.limit()];
		buffer.get(data);
		try {
			System.out.println(new String(data,"Cp870"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onFontStyleChange(IControls controls, SCSStyleType type) {
		System.out.println("*** onFontStyleChange");
	}

	@Override
	public void onStart(IControls controls) {
		System.out.println("*** onStart");
	}

	@Override
	public void onFinish(IControls controls) {
		System.out.println("*** onFinish");
	}

	@Override
	public void onData(ByteBuffer buffer, IControls controls) {
		this.buffer = buffer;
	}

}
