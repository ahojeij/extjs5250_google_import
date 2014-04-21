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

import hr.ws4is.tn3812.interfaces.ITn3812DataListener;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Tn3812 printer session factory class.
 * Creates 5250 printer session ready to connect to the server
 */
public enum Tn3812DriverFactory {
	ASCII, PDF, RAW, SEGMENT
	;
         
    public static final ITn3812DataListener create(Tn3812DriverFactory type) throws InterruptedException, ExecutionException, IOException {
    	ITn3812DataListener driver = null;
    	switch(type){
    	case ASCII :
    		driver = new ASCIIPrintWriter();
    		break;
    	case PDF :
    		driver = new PdfFileWriter();
    		break;
    	case RAW : 
    		driver = new RawFileWriter();
    		break;    		
    	case SEGMENT :
    		driver = new RawSegmentFileWriter();
    		break;        	
    	}
    	return driver;
		
    }
    
}
