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
package hr.ws4is.tn5250.implementation;

import hr.ws4is.tn5250.data.TnHost;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Load properties file with list of all available 5250 server connections
 * and websocket setup properties
 * Initialized from WebSocketLoader class
 * Self register to ServletContext with Constants.HOST_5250_CONFIG
 */
public enum TnConfigLoader {
	;

	private final static Logger logger = LoggerFactory.getLogger(TnConfigLoader.class);

	//suffix for name of properties file
	private static final String PROP_ID ="5250_5250.properties";

	//SUFFIXES FOR REMOTE 5250 HOST
	private static final String HOST_SCREEN = "5250_screen132";

	//SUFFIXES FOR REMOTE 5250 HOST
	private static final String HOST_PREFIX = "5250_prefixes";
	
	//address of 5250 server
	private static final String HOST_IP = "5250.ip";
	
	//port of 5250 server
	private static final String HOST_PORT = "5250.port";
	
	//virtual name used from frontend
	private static final String HOST_NAME = "5250.name";

	public static final Map<String,TnHost> reload() throws RuntimeException  {

		Properties props = loadConfig();
		HashMap<String,TnHost> hosts = new HashMap<String, TnHost>();
		
		//get list config names; if not found, do not allow web app startup
		String prefixes = (String) props.get(HOST_PREFIX);
		if(prefixes == null){
			throw new RuntimeException("5250 hosts not set in configuration file : " + PROP_ID);
		}


		//load 5250 hosts
		String prefs[] = prefixes.split(",");
		for(String pref : prefs){
			String tmp = pref + ".";
			TnHost rh = new TnHost();
			rh.setIpAddress( props.getProperty(tmp +HOST_IP));
			rh.setPort(props.getProperty(tmp +HOST_PORT));
			rh.setName(props.getProperty(tmp +HOST_NAME));
			rh.setScreen132(props.getProperty(tmp +HOST_SCREEN));
			if(rh.isValid()){
				hosts.put(rh.getName(), rh);
			} else
				logger.warn("Host config not valid for {}" , rh.getName());

		}
		props.clear();
		props = null;
		return Collections.unmodifiableMap(hosts);
	}

	private static Properties loadConfig(){
		String prefix = System.getProperty("user.home") + System.getProperty("file.separator");
		File f = new File(prefix + PROP_ID);
		FileInputStream fis = null;
		Properties prop = new Properties();
		try{
			fis = new FileInputStream(f);
			prop.load(fis);
		}catch(Exception e){
			throw new RuntimeException(e);
		} finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

}
