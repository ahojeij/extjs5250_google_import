/**
 * ws4is.tn5250.Element.java
 *
 * Copyright (C) 2009,  Tomislav Milkovic
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
 */

/*
 * This class is used to cast to ScreenElement as
 *  helper to map to shortened method names 
 */
package ws4is.tn5250;

public class Element  {

	ScreenElement sce;
	
	public Element(ScreenElement sce){
		this.sce = sce;
	}
	
	public ScreenElement getElement(){
	 return sce;	
	}
	
	public void setElement(ScreenElement sce){
		this.sce = sce;
	}
	
	
	public int getHidden() {
		return sce.getE1();
	}
	
	public void setHidden(int isHidden) {
		sce.setE1(isHidden);
	}

	public int getFieldType() {
		return sce.getE3();
	}
	
	public void setFieldType(int fieldType) {
		sce.setE3(fieldType);
	}
	
	public int getFieldId() {
		return sce.getE4();
	}
	
	public void setFieldId(int fieldId) {
		sce.setE4(fieldId);
	}
	
	public int getAttributeId() {
		return sce.getE5();
	}
	
	public void setAttributeId(int attributeId) {
		sce.setE5(attributeId);
	}
	
	
	public int getLength() {
		return sce.getE7();
	}
	
	public void setLength(int length) {
		sce.setE7(length);
	}
	
	public int getMaxLength() {
		return sce.getE8();
	}
	
	public void setMaxLength(int maxLength) {
		sce.setE8(maxLength);
	}

	public int getRow() {
		return sce.getE9();
	}

	public void setRow(int row) {
		sce.setE9(row);
	}

	public String getValue() {
		return sce.getEa();
	}
	
	public void setValue(final String value) {
		sce.setEa(value);
	}
	
	public void addToValue(final char value) {
		sce.addToValue(value);
	}
	
}
