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
package hr.ws4is.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Load properties file with list of all available 5250 server connections and
 * websocket setup properties. Initialized from WebSocketLoader class. Self
 * register to ServletContext with Constants.HOST_5250_CONFIG.
 */
public enum TnConfigLoader {
    ;

    private static final Logger LOGGER = LoggerFactory.getLogger(TnConfigLoader.class);

    // suffix for name of properties file
    private static final String PROP_ID = "5250_5250.properties";

    // SUFFIXES FOR REMOTE 5250 HOST
    private static final String HOST_SCREEN = "5250_screen132";

    // SUFFIXES FOR REMOTE 5250 HOST
    private static final String HOST_PREFIX = "5250_prefixes";

    // address of 5250 server
    private static final String HOST_IP = "5250.ip";

    // port of 5250 server
    private static final String HOST_PORT = "5250.port";

    // virtual name used from frontend
    private static final String HOST_NAME = "5250.name";

    // supported code page
    private static final String CODE_PAGE = "5250.codepage";

    public static final Map<String, TnHost> reload() throws RuntimeException {

        final Properties props = loadConfig();

        // get list config names; if not found, do not allow web app startup
        final String prefixes = (String) props.get(HOST_PREFIX);
        if (prefixes == null) {
            throw new RuntimeException("5250 hosts not set in configuration file : " + PROP_ID);
        }

        final HashMap<String, TnHost> hosts = new HashMap<String, TnHost>();
        // load 5250 hosts
        String[] prefs = prefixes.split(",");
        for (String pref : prefs) {
            String tmp = pref + ".";
            TnHost rh = new TnHost();
            rh.setIpAddress(props.getProperty(tmp + HOST_IP));
            rh.setPort(props.getProperty(tmp + HOST_PORT));
            rh.setName(props.getProperty(tmp + HOST_NAME));
            rh.setScreen132(props.getProperty(tmp + HOST_SCREEN));
            rh.setCodePage(props.getProperty(tmp + CODE_PAGE));
            if (rh.isValid()) {
                hosts.put(rh.getName(), rh);
            } else {
                LOGGER.warn("Host config not valid for {}", rh.getName());
            }

        }
        props.clear();
        return Collections.unmodifiableMap(hosts);
    }

    private static Properties loadConfig() {

        final String prefix = System.getProperty("user.home") + System.getProperty("file.separator");
        final File file = new File(prefix + PROP_ID);
        final Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            prop.load(fis);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(fis);
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        return prop;
    }

    private static void close(InputStream closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

}
