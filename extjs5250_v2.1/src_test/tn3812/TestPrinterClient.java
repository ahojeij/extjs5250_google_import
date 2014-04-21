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
package tn3812;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import hr.ws4is.tn3812.Tn3812ClientFactory;
import hr.ws4is.tn3812.Tn3812Config;
import hr.ws4is.tn3812.drivers.Tn3812DriverFactory;
import hr.ws4is.tn3812.interfaces.ITn3812Context;
import hr.ws4is.tn3812.interfaces.ITn3812DataListener;

/*
 * Start printer session with registered raw data writer 
 */
public class TestPrinterClient {


	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
		Tn3812Config config = new Tn3812Config();
		config.setDevName("PRT01");
		
		ITn3812Context ctx = Tn3812ClientFactory.createSession("192.168.140.20", 23, config);
		ITn3812DataListener driver = Tn3812DriverFactory.create(Tn3812DriverFactory.PDF);
		//ITn3812DataListener driver = Tn3812DriverFactory.create(Tn3812DriverFactory.SEGMENT);
		ctx.addDataListener(driver);
		ctx.addDataListener(new MyListener());
		ctx.connect();
		
		while(true){
			Thread.sleep(10000);
		}
	}

}
