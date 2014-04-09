/*
 * Copyright (C) 2010,  Tomislav Milkovic
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
package hr.ws4is.tn5250.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Vector;

import org.tn5250j.TN5250jConstants;
import org.tn5250j.framework.tn5250.Screen5250;
import org.tn5250j.framework.tn5250.ScreenField;
import org.tn5250j.framework.tn5250.ScreenFields;

/**
 * Class representing 5250 screen data 
 * It is used to get 5250 screen information from host response
 */
public class TnScreenData  implements TN5250jConstants{

	final private char[] text;
    final private char[] attr;
    final private char[] isAttr;
    final private char[] color;
    final private char[] extended;
    final private char[] graphic;
    final private char[] field;
    
    final private ScreenFields screenFields;
    final private int numRows;
    final private int numCols;    
    final private int lenScreen;
    final private List<TnScreenElement> screenElements;
       
    private int row = 1;
    private int col = 0;
    private int lastAttr = 32; 
    private boolean changeAttr = false;
    private TnScreenElement element = null;
    
    
    public TnScreenData(int startRow, int startCol, Screen5250 screen) {
        this.screenElements = new Vector<TnScreenElement>();
        this.screenFields = screen.getScreenFields();
        this.numCols = screen.getColumns(); 
        this.numRows = screen.getRows();
        this.lenScreen = screen.getScreenLength();
        
        int size = numCols * numRows ;
        
        text = new char[size];
        attr = new char[size];
        isAttr = new char[size];
        color = new char[size];
        extended =new char[size];
        graphic = new char[size];
        field = new char[size];
        
    	init(startRow,startCol,screen);
    }
    	
    private void init(int startRow, int startCol, Screen5250 screen) {
           
       int size = numCols * numRows ;
       int endRow = numRows;
       int endCol = numCols;

       initScreenElement(0);
       
       if (size == screen.getScreenLength()) {
	         screen.GetScreen(text, size, PLANE_TEXT);
	         screen.GetScreen(attr, size, PLANE_ATTR);
	         screen.GetScreen(isAttr, size, PLANE_IS_ATTR_PLACE);
	         screen.GetScreen(color, size, PLANE_COLOR);
	         screen.GetScreen(extended, size, PLANE_EXTENDED);
	         screen.GetScreen(graphic, size, PLANE_EXTENDED_GRAPHIC);
	         screen.GetScreen(field, size, PLANE_FIELD);
       }
       else {
	         screen.GetScreenRect(text, size, startRow, startCol, endRow, endCol, PLANE_TEXT);
	         screen.GetScreenRect(attr, size, startRow, startCol, endRow, endCol, PLANE_ATTR);
	         screen.GetScreenRect(isAttr, size, startRow, startCol, endRow, endCol, PLANE_IS_ATTR_PLACE);
	         screen.GetScreenRect(color, size, startRow, startCol, endRow, endCol, PLANE_COLOR);
	         screen.GetScreenRect(extended, size, startRow, startCol, endRow, endCol, PLANE_EXTENDED);
	         screen.GetScreenRect(graphic, size, startRow, startCol, endRow, endCol, PLANE_EXTENDED_GRAPHIC);
	         screen.GetScreenRect(field, size, startRow, startCol, endRow, endCol, PLANE_FIELD);
       }
    }

	public TnScreenElement getElement() {
		return element;
	}

	public TnScreenElement initScreenElement(){
		return initScreenElement(row, lastAttr);
	}
	
	public TnScreenElement initScreenElement(int lastAttr){
		return initScreenElement(row, lastAttr);
	}
	
	private TnScreenElement initScreenElement(int row, int lastAttr){
		TnScreenElement element = new TnScreenElement();
    	element.setFieldId(-1); //not a field
    	element.setAttributeId(lastAttr);
        element.setRow(row);
        element.setValue("");
        screenElements.add(element);
        this.element = element;
        return this.element;
	}
	
	public void addTextToElement(int pos){
		element.addToValue(text[pos]);
	}

	public void addSpace(){
   	  initScreenElement(39);		            	 
   	  element.addToValue(' ');		            	 
   	  setChangeAttr(true);
	}
	
    public boolean isField(int pos){
    	return field[pos] != 0;
    }
    
    public boolean isText(int pos){
    	return isAttr[pos]==0;
    }
        
	public boolean isColorChanged(int pos){
		return isAttr[pos]==1;
	}
	
	
	public void updateIfColorChanged(int pos){
    	if(isColorChanged(pos)){
    		setChangeAttr(true);
    		setLastAttr(pos);
    	}
	}	
	
	public void updateIfAttributeChanged(){
        if (isChangeAttr()) {
        	initScreenElement();	            	
        	setChangeAttr(false);
        }
	}	
	
	public void updateIfNewLine(){
        if (++col == numCols) {
           col = 0;
           row++;
           initScreenElement();
        }		
	}
	
	public void updateIfHiddenField(int pos){
        if ((extended[pos] & TN5250jConstants.EXTENDED_5250_NON_DSP) != 0){
      	  element.setHidden(1);
        }
	}
	
	public void updateIfBypassField(int pos){
        if ((int) attr[pos] == 39) {
       	 element.setHidden(1);
        }		
	}
	
	public ScreenField findScreenField(int pos){
		return screenFields.findByPosition(pos);
	}
	
	
	public int getLenScreen() {
		return lenScreen;
	}

	public int getLastAttr() {
		return lastAttr;
	}

	public void setLastAttr(int pos) {
		this.lastAttr = attr[pos];
	}


	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public boolean isChangeAttr() {
		return changeAttr;
	}

	public void setChangeAttr(boolean changeAttr) {
		this.changeAttr = changeAttr;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public ScreenFields getScreenFields() {
		return screenFields;
	}

	public List<TnScreenElement> getScreenElements() {
		return screenElements;
	}
	
	 public void updateScreenElements(){
			for(TnScreenElement element : screenElements){
				element.update();
			}
	}

		/*
		 * For debug purposes
		 */
		static private void save(char[] data,String name) {
			File file = new File("d:\\" + name);
			Writer output;
			try {
				output = new FileWriter(file);
				output.write(data);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		static private void load(char[] data,String name) {
			File file = new File("d:\\" + name);
			Reader output;
			try {
				output = new FileReader(file);
				output.read(data);
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		static public void readDebug(TnScreenData screenRect){
			load(screenRect.attr,"attr.txt");
		  	load(screenRect.color,"color.txt");
		  	load(screenRect.extended,"extnded.txt");
		  	load(screenRect.field,"field.txt");
		  	load(screenRect.graphic,"graphic.txt");
		  	load(screenRect.isAttr,"isattr.txt");
		  	load(screenRect.text,"text.txt");				
		}
		
		static public void saveDebug(TnScreenData screenRect){
	  	  save(screenRect.attr,"attr.txt");
	  	  save(screenRect.color,"color.txt");
	  	  save(screenRect.extended,"extnded.txt");
	  	  save(screenRect.field,"field.txt");
	  	  save(screenRect.graphic,"graphic.txt");
	  	  save(screenRect.isAttr,"isattr.txt");
	  	  save(screenRect.text,"text.txt");		
		}
 }