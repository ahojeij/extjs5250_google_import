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
package hr.ws4is.tn5250.data;

/**
 * Screen element is mapped to this to minimize communication between browser and client
 */
public class TnScreenElement  {

	public int[] d = {0,0,0,0,0,0,0};
	public String t;
	private StringBuffer text = new StringBuffer ();

	
	public int getHidden() {
		return d[0];
	}
	
	public void setHidden(int isHidden) {
		d[0]=isHidden;
	}

	public int getFieldType() {
		return d[1];
	}
	
	public void setFieldType(int fieldType) {
		d[1]=fieldType;
	}
	
	public int getFieldId() {
		return d[2];
	}
	
	public void setFieldId(int fieldId) {
		d[2]=fieldId;
	}
	
	public int getAttributeId() {
		return d[3];
	}
	
	public void setAttributeId(int attributeId) {
		d[3]=attributeId;
	}
	
	
	public int getLength() {
		return d[4];
	}
	
	public void setLength(int length) {
		d[4]=length;
	}
	
	public int getMaxLength() {
		return d[5];
	}
	
	public void setMaxLength(int maxLength) {
		d[5]=maxLength;
	}

	public int getRow() {
		return d[6];
	}

	public void setRow(int row) {
		d[6]=row;
	}

	public String getValue() {
		return t;
	}
	
	public void setValue(final String value) {
		t = value;
	}
	
	public void addToValue(final char value) {
		if(value == 0){
			text.append(" ");
		}else
			text.append(value);
	}
	
	public void update(){
		if(d[2]>0) return;
		t = text.toString();
	}
}
