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
package hr.ws4is.tn3812.drivers.processors.scs;

import hr.ws4is.tn3812.drivers.listeners.IProcessorListener;
import hr.ws4is.tn3812.drivers.processors.IControls;
import hr.ws4is.tn3812.drivers.processors.IProcessor;
import hr.ws4is.tn3812.interfaces.ITn3812Context;

import java.nio.ByteBuffer;

/**
 * Abstract SCS data steam processor with generic methods 
 */
abstract class SCSProcessorA implements IProcessor {
	
	protected ITn3812Context context;
	protected SCSControls controls;
	protected SCSControls defaults;
	
	//callback listener for SCS stream processing
	protected IProcessorListener listener;	
	
	
	//device initialization; ignored ?  
	@Override
	public void initialize(ITn3812Context context, ByteBuffer data) {
		this.context = context;
		if(defaults!=null){
			controls = defaults;
		} else{
			controls = new SCSControls();
		}
		process(data);
		try {
			defaults = (SCSControls) controls.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	//resets temp data for new report 
	@Override
	public void start(IProcessorListener listener) {
		try {
			if(defaults!=null){
				controls = (SCSControls) defaults.clone();
			} else{
				controls = new SCSControls();
			}					
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}		
		this.listener = listener;
		if(listener!=null){
			listener.onStart(this.controls);
		}
	}
	
	//finalizes report, call onFinish listeners
	@Override
	public void finish() {	
		if(listener!=null){
			listener.onFinish(context);
		}
	}
	
	protected void doData(ByteBuffer buffer){
		if(listener!=null){
			listener.onData(buffer);
		}
	}

	@Override
	public IControls getControls() {
		return controls;
	}
	
	
	   
}
