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
package hr.ws4is.tn5250.implementation;

import hr.ws4is.tn5250.Tn5250ResponseScreen;
import hr.ws4is.tn5250.data.TnScreenData;
import hr.ws4is.tn5250.data.TnScreenElement;
import hr.ws4is.tn5250.data.TnScreenRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.tn5250j.Session5250;
import org.tn5250j.TN5250jConstants;
import org.tn5250j.framework.tn5250.Screen5250;
import org.tn5250j.framework.tn5250.ScreenField;
import org.tn5250j.framework.tn5250.ScreenOIA;

/**
 * Main 5250 stream processor which converts 5250 screen data into web data 
 *
 */
public class TnStreamProcessor {

	private StringBuffer valueBuffer = new StringBuffer(5);
	private boolean updateScreen = true;

	/*
	 * Refresh current screen 
	 */
	public void refresh(Session5250 session, Tn5250ResponseScreen response){
		List<TnScreenElement> list = getResponse(session);
		response.setData(list);
		response.setSize(session.getScreen().getColumns());
		response.setLocked(session.getScreen().getOIA().isKeyBoardLocked());
		response.setMsgw(session.getScreen().getOIA().isMessageWait());
	}
		

	public void process(Session5250 session,TnScreenRequest request, TnScreenElement[] fields, Tn5250ResponseScreen response){

		//send data to 5250 session, wait for response and return new screen
		//TODO test listener engine, this should only send data to host without waiting
		if (process(session,request,fields)){
			refresh(session,response);
		}else{
			response.setSuccess(false);
		}
		
		/*
		 * 	logger.error("Error processing 5250 request for session id {} ", wsctx.getSession(false).getId() );
		 * logger.error("",e);
		 */
	}
	

	/*
	 * forward request from web to host and waits for return
	 * after response, screen will be reloaded
	 * TODO - handle session hang & timeout so not to continue
	 */
	public boolean process(Session5250 session, TnScreenRequest request, TnScreenElement[] fields){

		Screen5250 screen= session.getScreen();

        boolean sendIt = false;

	    //Parameter PF set in javascript by key press
	    String aidS = request.getKeyRequest();
	    if (aidS != null && aidS.length() > 0) {
	            sendIt = true;
	    }


        if (aidS != null && aidS.length() > 0) {
            aidS = "[" + aidS.toLowerCase() + "]";
        }else {
	        aidS = TN5250jConstants.MNEMONIC_ENTER;
	    }

        if(aidS.equals(TN5250jConstants.MNEMONIC_SYSREQ)){
        	if(request.getData()!=null){
            	session.getVT().systemRequest(request.getData());
            	return true;
        	}else return false;
        }

        	int fl = fields.length;
        	TnScreenElement element = new TnScreenElement();
        	 for (int x = 0; x < fl; x++) {
        		element = fields[x];
	            String field = element.getValue();
	            if (field != null && field.length() > 0) {
	               sendIt = true;
	               ScreenField sf = screen.getScreenFields().getField(element.getFieldId()-1);
	               screen.setCursor(sf.startRow(), sf.startCol());
	               sf.setString(field);
	               //System.out.println("FLD" + (x + 1) + "-> " + field + " -> " + sf.getString());
	            }else{
	            	ScreenField sf = screen.getScreenFields().getField(element.getFieldId()-1);
	            	sf.setString("");
	            }

	         }

        	 //uzmi trenutno odabrani cursor ili field
	         boolean nofld = request.getCursorField()<1;
	         if (!nofld) {
	        	 int fld = request.getCursorField();
	        	 if(fld>1000) fld = (fld-1000)>>1;
	        	 ScreenField sf = screen.getScreenFields().getField(fld);
	        	 if (sf!=null)
	        		 nofld = sf.isBypassField();
	         }


	         if (nofld) {
	        	 //screen.setCursor( request.getCursorRow() , 5);
	         } else if (request.getCursorField() > 0) {
	        	  int i = request.getCursorField();
	        	  boolean setc = false;
	        	  if (i<1){
	        		 setc =  screen.gotoField(screen.getScreenFields().getSize());
	        	  }else setc = screen.gotoField(i);
	        	  if (!setc){
	        		  System.out.println("Field not set");
	        	  }
	               //screen.gotoField(request.getCursorField());
	         }

	         if (sendIt || screen.getScreenFields().getFieldCount() == 0) {
	            screen.sendKeys(aidS);
	            while (screen.getOIA().getInputInhibited() == ScreenOIA.INPUTINHIBITED_SYSTEM_WAIT
	                  && screen.getOIA().getLevel() != ScreenOIA.OIA_LEVEL_INPUT_ERROR) {
	               try {

	                  Thread.sleep(500);
	               }
	               catch (InterruptedException ex) {
	                  ;
	               }
	            }
	         }

	   return true;
	}

	protected void setUpdateScreen(boolean status){
		updateScreen = status;
	}

	protected boolean isUpdating(){
		return updateScreen;
	}

	/*
	 * Process response screen and fills up list of screen elements ready to be sent to the browser
	 */
	protected List<TnScreenElement> getResponse(Session5250 session){
		  List<TnScreenElement>  list =new Vector<TnScreenElement>();
		  Screen5250 screen = session.getScreen();
		  int focusfield = getFocusField(screen);
          int numRows = screen.getRows();
          int numCols = screen.getColumns();
          int lenScreen = screen.getScreenLength();
          int lastAttr = 32;
          int pos = 0;
          int row = 1;
          int col = 0;
          boolean changeAttr = false;

          TnScreenElement element= new TnScreenElement();
          list.add(element);
          //if(updateScreen) screen.updateScreen();
          //logger.info("****SCREEN CURSOR ACTIVE? ****" + Boolean.toString(screen.isCursorActive()));
          TnScreenData screenRect = new TnScreenData(1, 1, numRows, numCols, screen);

          //for debug purposes
       	  //saveDebug(screenRect);


	         while (pos < lenScreen) {

	            // check for the changing of the text color attributes.
	        	if(screenRect.isAttr[pos]==1){
	        		changeAttr = true;
	        		lastAttr = screenRect.attr[pos];
	        	}
	        	/*
	            if (screenRect.attr[pos] != lastAttr) {
	            	//if (! changeAttr) new ScreenElement ???????
	               if (pos < lenScreen - 1 && screenRect.field[pos + 1] == 0) {
	                  // close the previous
	                  changeAttr = true;
	               }
	               lastAttr = screenRect.attr[pos];
	            }
	        	 */
	            if (screenRect.field[pos] != 0) {
	               ScreenField sf = screen.getScreenFields().findByPosition(pos);
	               if (sf != null) {
	                  if (sf.startPos() == pos) {
	                	//element.update();
		                 element = new TnScreenElement();
		                 list.add(element);

	                     if ((screenRect.extended[pos] & TN5250jConstants.EXTENDED_5250_NON_DSP) != 0)
	                    	 element.setHidden(1);

	                     if (sf.isBypassField()) {
	                        if ((int) screenRect.attr[pos] == 39) {
	                        	element.setHidden(1);
	                        }
	                     }

	                     // if the field will extend past the screen column size
	                     //  we will just truncate it to be the size of the rest
	                     //  of the screen.
	                     int len = sf.getLength();
	                     if (col + len > numCols){
	                    	  len = numCols - col;
	                    	  //row++;
	                     }

	                     // get the field contents and only trim the non numeric
	                     //   fields so that the numeric fields show up with
	                     //   the correct alignment within the field.
	                     String value = "";
	                     if (sf.isNumeric() || sf.isSignedNumeric()){
	                    	 value = sf.getString();
	                     } else
	                    	 value = RTrim(sf.getString());


	                     int i=0;
	                     i = i | ((focusfield == sf.getFieldId())? 1 : 0);
	                     i = i << 1;
	                     i = i | ( sf.isAutoEnter() ? 1 : 0);
	                     i = i << 1;
	                     i = i | ( sf.isBypassField() ? 1 : 0);
	                     i = i << 1;
	                     i = i | ( sf.isContinued() ? 1 : 0);
	                     i = i << 1;
	                     i = i | ( sf.isContinuedFirst() ? 1 : 0);
	                     i = i << 1;
	                     i = i | ( sf.isContinuedLast() ? 1 : 0);
	                     i = i << 1;
	                     i = i | ( sf.isContinuedMiddle() ? 1 : 0);
	                     i = i << 1;
	                     i = i | ( sf.isDupEnabled() ? 1 : 0);
	                     i = i << 1;
	                     i = i | ( sf.isFER() ? 1 : 0);
	                     i = i << 1;
	                     i = i | ( sf.isHiglightedEntry() ? 1 : 0);
	                     i = i << 1;
	                     i = i | ( sf.isMandatoryEnter() ? 1 : 0);
	                     i = i << 1;
	                     i = i | ( sf.isNumeric()  ? 1 : 0);
	                     i = i << 1;
	                     i = i | ( sf.isSignedNumeric()  ? 1 : 0);
	                     i = i << 1;
	                     i = i | ( sf.isToUpper()   ? 1 : 0);

	                     element.setFieldType(i);
	                     element.setFieldId(sf.getFieldId());
	                     element.setLength(len);
	                     //element.setMaxLength(sf.getFieldLength());
	                     element.setMaxLength(len);
	                     element.setValue(value);
	                     element.setRow(row);
	                     //changeAttr = true;


	                     if(len < sf.getLength()){
	                    	 int r = row;
	                    	 int alen = (sf.getLength()-len);
	                    	 int al = alen /(numCols-1);
	                    	 int ai,astart,astop;

	                    	 for (ai=1;ai<=al;ai++){
	                    		 r++;

	                    		 astart=len + (ai-1)*(numCols-1);
	                    		 astop=astart+(numCols-1);
	                    		 if(astop>alen){
	                    			 astop=alen;
	                    		 }
	                    		 String aval = sf.getString().substring(astart,astop);
	                    		 if (sf.isNumeric() || sf.isSignedNumeric()){

	    	                     } else
	    	                    	 aval = RTrim(aval);

	                    		 int flen = (numCols-1) * ai;
	    	                     if(flen >sf.getLength()){
	    	                    	 flen =  sf.getLength() - flen;
	    	                     }else flen = numCols-1;

	                    		 element = new TnScreenElement();
	                    		 list.add(element);
	                    		 element.setFieldType(i);
	    	                     element.setFieldId(sf.getFieldId()*1000+ ai);
	    	                     element.setLength(flen);
	    	                     element.setMaxLength(flen);
	    	                     element.setValue(aval);
	    	                     element.setRow(r);
	                    	 }
	                     }
	                  }
	               }
	            }else {
	             if(screenRect.isAttr[pos]==0){
	               element.addToValue(screenRect.text[pos]);
	             } else{
		                element = new TnScreenElement();
		                list.add(element);
		            	element.addToValue(' ');
		            	element.setFieldId(-1); //not a field
		            	element.setAttributeId(39); //non-display
		                element.setRow(row);
		                changeAttr=true;
	             }
	            }

	            if (changeAttr) {
	            	//element.update();
	                element = new TnScreenElement();
	                list.add(element);
	            	element.setValue("");
	            	element.setFieldId(-1); //not a field
	            	element.setAttributeId(lastAttr);
	                element.setRow(row);
	                changeAttr = false;
	            }

	            if (++col == numCols) {
	               col = 0;
	               row++;
	                //element.update();
	                element = new TnScreenElement();
	                list.add(element);
	            	element.setFieldId(-1); //not a field
	            	element.setAttributeId(lastAttr);
	                element.setRow(row);
	                element.setValue("");

	            }
	            pos++;
	         }
	         //element.update();
	         Iterator<TnScreenElement> it = list.iterator();
	         while(it.hasNext()){
	        	 ((TnScreenElement)it.next()).update();
	         }
	         return list;
	}

	private int getFocusField(Screen5250 screen){

		ScreenField focusField = screen.getScreenFields().getCurrentField();

        if (focusField == null)
        	focusField = screen.getScreenFields().getFirstInputField();

        if (focusField != null){
        	return focusField.getFieldId();
        } else return -1;

	}


	private String RTrim(String text) {

	      valueBuffer.setLength(0);

	      // Here we are going to perform a trim of only the trailing
	      //   white space.
	      valueBuffer.append(text);
	      int len2 = valueBuffer.length();

	      while ((len2 > 0) && (valueBuffer.charAt(len2-1) <= ' ')) {

	         len2--;
	      }
	      valueBuffer.setLength(len2);
	      return valueBuffer.toString();
	   }

	/*
	 * For debug purposes
	 */
	private void save(char[] data,String name) {
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
	
	protected void saveDebug(TnScreenData screenRect){
  	  save(screenRect.attr,"attr.txt");
  	  save(screenRect.color,"color.txt");
  	  save(screenRect.extended,"extnded.txt");
  	  save(screenRect.field,"field.txt");

  	  save(screenRect.graphic,"gaphic.txt");
  	  save(screenRect.isAttr,"isattr.txt");
  	  save(screenRect.text,"text.txt");		
	}
}
