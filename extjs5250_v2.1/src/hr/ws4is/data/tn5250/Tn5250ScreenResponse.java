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
package hr.ws4is.data.tn5250;

import java.util.List;

import hr.ws4is.ext.ExtJSResponse;

/**
 * Response structure with screen information.
 */
public class Tn5250ScreenResponse extends ExtJSResponse {

    private static final long serialVersionUID = 1L;

    public String devName;
    public String displayID;
    public boolean locked;          // sends lock screen request
    public boolean clearScr = true; // sends clear screen request
    public int size = 80;           // sends screen size 80/132
    public boolean msgw;            // sends message wait signal
    public String conerr;           // host error code

    public List<Tn5250ScreenElement> data;

    public Tn5250ScreenResponse() {
        super();
    }

    public Tn5250ScreenResponse(final boolean success, final String message) {
        super(success, message);
    }

    public Tn5250ScreenResponse(final Throwable exception, final String message) {
        super(exception, message);
    }

    public final String getDevName() {
        return devName;
    }

    public final void setDevName(final String devName) {
        this.devName = devName;
    }

    public final boolean isLocked() {
        return locked;
    }

    public final void setLocked(final boolean locked) {
        this.locked = locked;
    }

    public final boolean isClearScr() {
        return clearScr;
    }

    public final void setClearScr(final boolean clearScr) {
        this.clearScr = clearScr;
    }

    public final int getSize() {
        return size;
    }

    public final void setSize(final int size) {
        this.size = size;
    }

    public final boolean isMsgw() {
        return msgw;
    }

    public final void setMsgw(final boolean msgw) {
        this.msgw = msgw;
    }

    public final String getConerr() {
        return conerr;
    }

    public final void setConerr(final String conerr) {
        this.conerr = conerr;
    }

    public final List<Tn5250ScreenElement> getData() {
        return data;
    }

    public final void setData(final List<Tn5250ScreenElement> data) {
        this.data = data;
    }

    public final String getDisplayID() {
        return displayID;
    }

    public final void setDisplayID(final String displayID) {
        this.displayID = displayID;
    }
}
