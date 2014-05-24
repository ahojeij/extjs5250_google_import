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

/**
 * Request sent from web 5250 to process screen. Contains device name, current
 * cursor field & position and pressed keyboard.
 */
public class Tn5250ScreenRequest {

    private String keyRequest;
    private int cursorField;
    private int cursorRow;
    private String displayID;
    private String data;

    public final String getKeyRequest() {
        return keyRequest;
    }

    public final void setKeyRequest(final String keyRequest) {
        this.keyRequest = keyRequest;
    }

    public final int getCursorField() {
        return cursorField;
    }

    public final void setCursorField(final int cursorField) {
        this.cursorField = cursorField;
    }

    public final int getCursorRow() {
        return cursorRow;
    }

    public final void setCursorRow(final int cursorRow) {
        this.cursorRow = cursorRow;
    }

    public final String getData() {
        return data;
    }

    public final void setData(final String data) {
        this.data = data;
    }

    public final String getDisplayID() {
        return displayID;
    }

    public final void setDisplayID(final String displayID) {
        this.displayID = displayID;
    }

}
