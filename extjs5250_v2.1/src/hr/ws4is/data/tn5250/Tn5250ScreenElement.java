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

/*
 * This class is used to cast to Screenelement as
 *  helper to map to shortened method names
 */
package hr.ws4is.data.tn5250;

/**
 * Screen element is mapped to this to minimize communication 
 * between browser and client.
 */
public class Tn5250ScreenElement {

    public int[] d = { 0, 0, 0, 0, 0, 0, 0 };
    public String t;
    private StringBuffer text = new StringBuffer();

    public final int getHidden() {
        return d[0];
    }

    public final void setHidden(final int isHidden) {
        d[0] = isHidden;
    }

    public final int getFieldType() {
        return d[1];
    }

    public final void setFieldType(final int fieldType) {
        d[1] = fieldType;
    }

    public final int getFieldId() {
        return d[2];
    }

    public final void setFieldId(final int fieldId) {
        d[2] = fieldId;
    }

    public final int getAttributeId() {
        return d[3];
    }

    public final void setAttributeId(final int attributeId) {
        d[3] = attributeId;
    }

    public final int getLength() {
        return d[4];
    }

    public final void setLength(final int length) {
        d[4] = length;
    }

    public final int getMaxLength() {
        return d[5];
    }

    public final void setMaxLength(final int maxLength) {
        d[5] = maxLength;
    }

    public final int getRow() {
        return d[6];
    }

    public final void setRow(final int row) {
        d[6] = row;
    }

    public final String getValue() {
        return t;
    }

    public final void setValue(final String value) {
        t = value;
    }

    public final void addToValue(final char value) {
        if (value == 0) {
            text.append(" ");
        } else {
            text.append(value);
        }
    }

    public final void update() {
        if (d[2] > 0) {
            return;
        }
        t = text.toString();
    }
    
}
