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
package hr.ws4is.tn3812.interfaces;

import java.nio.ByteBuffer;

/**
 * Main callback interface used to catch data received from the host
 */
public interface ITn3812DataListener {

    /**
     * When printer session is initialized
     * 
     * @param config
     */
    void onInit(ITn3812Context config);

    /**
     * When session initialization error received, for example, printer name
     * already used
     * 
     * @param config
     * @param data
     */
    void onError(ITn3812Context config, ByteBuffer data);

    /**
     * On printer initialization. Executed only once when connected
     * 
     * @param data
     */
    void onHeader(ByteBuffer data);

    /**
     * Whenever new report is started, this is called first
     * 
     * @param data
     */
    void onFirstChain(ByteBuffer data);

    /**
     * Continuous data flow for current report printing
     * 
     * @param data
     */
    void onChain(ByteBuffer data);

    /**
     * Last page printed. report finished
     * 
     * @param data
     */
    void onLastChain(ByteBuffer data);

    /**
     * Signal when connection is closed by peer
     */
    void onClosed();

    /**
     * Signal when listener is removed
     */
    void onRemoved();
}
