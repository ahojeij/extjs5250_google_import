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

import org.tn5250j.TN5250jConstants;
import org.tn5250j.framework.tn5250.Screen5250;

/**
 * Class representing 5250 screen data 
 * It is used to get 5250 screen information from host response
 */
public class TnScreenData  implements TN5250jConstants{

    public char[] text;
    public char[] attr;
    public char[] isAttr;
    public char[] color;
    public char[] extended;
    public final char[] graphic;
    public final char[] field;
    
    public TnScreenData(int startRow, int startCol, int endRow, int endCol, Screen5250 screen) {

       int size = endCol * endRow ;

       text = new char[size];
       attr = new char[size];
       isAttr = new char[size];
       color = new char[size];
       extended =new char[size];
       graphic = new char[size];
       field = new char[size];

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

 }