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
package hr.ws4is.tn5250.implementation;

import hr.ws4is.tn5250.Tn5250ResponseScreen;
import hr.ws4is.tn5250.data.TnScreenData;
import hr.ws4is.tn5250.data.TnScreenElement;
import hr.ws4is.tn5250.data.TnScreenRequest;

import java.util.List;
import java.util.regex.Pattern;

import org.tn5250j.Session5250;
import org.tn5250j.TN5250jConstants;
import org.tn5250j.framework.tn5250.Screen5250;
import org.tn5250j.framework.tn5250.ScreenField;
import org.tn5250j.framework.tn5250.ScreenFields;
import org.tn5250j.framework.tn5250.ScreenOIA;

/**
 * Main 5250 stream processor which converts 5250 screen data into web data 
 */
public enum TnStreamProcessor {
	;

	private final static Pattern LTRIM = Pattern.compile("^\\s+");
	private final static Pattern RTRIM = Pattern.compile("\\s+$");
	
	public static String ltrim(String s) {
	    return LTRIM.matcher(s).replaceAll("");
	}
	
	public static String rtrim(String s) {
	    return RTRIM.matcher(s).replaceAll("");
	}
	
	/*
	 * Refresh current screen 
	 */
	static public void refresh(Session5250 session, Tn5250ResponseScreen response){
		List<TnScreenElement> list = getResponse(session);
		response.setData(list);
		response.setSize(session.getScreen().getColumns());
		response.setLocked(session.getScreen().getOIA().isKeyBoardLocked());
		response.setMsgw(session.getScreen().getOIA().isMessageWait());
	}

	/*
	 * forward request from web to host and waits for return
	 * after response, screen will be reloaded
	 * TODO - handle session hang & timeout so not to continue
	 */
	static public boolean process(Session5250 session, TnScreenRequest request, TnScreenElement[] fields){

		Screen5250 screen = session.getScreen();

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
        	if(request.getData()==null){
        		return false;
        	}
        	session.getVT().systemRequest(request.getData());
        	return true;
        }

        //fill screen fields from web data 
         if(updateFieldValues(screen, fields)){
        	 sendIt = true;
         };

        //get currently cursor positioned field  
        processCursor(screen, request.getCursorField());

        sendKeyRequest(screen, sendIt, aidS);

	   return true;
	}
		
	//sends request from web to host
	static private void sendKeyRequest(Screen5250 screen, boolean sendIt, String aidS){
        if (sendIt || screen.getScreenFields().getFieldCount() == 0) {
            screen.sendKeys(aidS);           
            while (screen.getOIA().getInputInhibited() == ScreenOIA.INPUTINHIBITED_SYSTEM_WAIT
                   && 
                   screen.getOIA().getLevel() != ScreenOIA.OIA_LEVEL_INPUT_ERROR) {
               try {
                  Thread.sleep(500);
               }
               catch (InterruptedException ex) {
                  ;
               }
            }
         }
	}
	
	//detect cursor position and update position to 5250 screen
	static private void processCursor(Screen5250 screen, int fld) {
	    boolean nofld = fld<1;
	    if (!nofld) {

	        if(fld>1000) fld = (fld-1000)>>1;
	        ScreenField sf = screen.getScreenFields().getField(fld);
	        if (sf!=null) {
	        	nofld = sf.isBypassField();
	        }
	    }

	    if (nofld) {
	    	//screen.setCursor( request.getCursorRow() , 5);
	    } else if (fld > 0) {

	    	boolean setc = false;
	    	if (fld<1){
	    		setc =  screen.gotoField(screen.getScreenFields().getSize());
	    	} else {
	    		setc = screen.gotoField(fld);
	    	}
	    	if (!setc){
	    		System.out.println("Field not set");
	    	}
	        //screen.gotoField(request.getCursorField());
	    }		
	}

	//update values from web field to 5250 screen fields
	static private boolean  updateFieldValues(Screen5250 screen, TnScreenElement[] webFields){
		boolean sendIt = false;
		ScreenFields fields = screen.getScreenFields();
        for(TnScreenElement webField :  webFields){
        	String fieldValue = webField.getValue();
        	if (fieldValue != null && fieldValue.length() > 0) {
        		sendIt = true;
 	            ScreenField sf = fields.getField(webField.getFieldId()-1);
 	            screen.setCursor(sf.startRow(), sf.startCol());
 	            sf.setString(fieldValue);
 	            //System.out.println("FLD" + (x + 1) + "-> " + field + " -> " + sf.getString());        			
 	        } else {
 	        	ScreenField sf = fields.getField(webField.getFieldId()-1);
	            sf.setString("");        			
        	}
        }
        return sendIt ;
	}


	// Process response screen and fills up list of screen elements ready to be sent to the browser
	static private List<TnScreenElement> getResponse(Session5250 session){

		  Screen5250 screen = session.getScreen();
          TnScreenData screenRect = new TnScreenData(1, 1, screen);
          
          //for debug purposes
          //TnScreenData.saveDebug(screenRect);
          
          int pos = 0;
	      while (pos < screenRect.getLenScreen()) {

	        	screenRect.updateIfColorChanged(pos);
	        	
	            if (screenRect.isField(pos)) {
	               processFields(screenRect, pos);
	            }else {            	
	            	
		             if(screenRect.isText(pos)){
		            	 screenRect.addText(pos);
		             } else {
		            	 screenRect.addSpace();
	                 }
	            }

	            screenRect.updateIfAttributeChanged();
	            screenRect.updateIfNewLine();
	            
	            pos++;
	         }
	         
	         screenRect.updateScreenElements();
	         return screenRect.getScreenElements();
	}
	
	static private void processFields(TnScreenData screenRect, int pos){
        ScreenField screenField = screenRect.findScreenField(pos);
        if (screenField == null) return;
        if (screenField.startPos() != pos) return;
        
        screenRect.initScreenElement(0);
        screenRect.updateIfHiddenField(pos);
        
        if (screenField.isBypassField()) {
        	screenRect.updateIfHiddenField(pos);
        }
        
        // if the field will extend past the screen column size
        //  we will just truncate it to be the size of the rest
        //  of the screen.
        int len = screenField.getLength();
        if (screenRect.getCol() + len > screenRect.getNumCols()){
        	len = screenRect.getNumCols() - screenRect.getCol();
        }
        
        // get the field contents and only trim the non numeric
        //   fields so that the numeric fields show up with
        //   the correct alignment within the field.
        String value = getFieldValue(screenField);
      		
    	int focusfield = getFocusField(screenRect.getScreenFields());
        int fieldMask=getFieldMask(screenField, focusfield);

        screenRect.getElement().setFieldType(fieldMask);
        screenRect.getElement().setFieldId(screenField.getFieldId());
        screenRect.getElement().setLength(len);
        screenRect.getElement().setMaxLength(len);
        screenRect.getElement().setValue(value);

        if(len < screenField.getLength()){
        	processMultiLineField(screenRect, screenField, len, fieldMask);
        }
	}
	
	//process 5250 multiline screen fields to web fields
	static private void processMultiLineField(TnScreenData screenRect, ScreenField screenField, int len, int fieldMask){		 
	   	 int _row = screenRect.getRow();
	   	 int numCols =screenRect.getNumCols();
	   	 
	   	 int alen = (screenField.getLength()-len);
	   	 int al = alen /(screenRect.getNumCols()-1);
	   	 int ai,astart,astop;
	
	   	 for (ai=1;ai<=al;ai++){
	   		_row++;
	
	   		 astart=len + (ai-1)*(numCols-1);
	   		 astop=astart+(numCols-1);
	   		 if(astop>alen){
	   			 astop=alen;
	   		 }
	   		 String aval = screenField.getString().substring(astart,astop);
	   		 if (screenField.isNumeric() || screenField.isSignedNumeric()){
	
	         } else {
	           	aval = rtrim(aval);
	         }
	
	   		 int flen = (numCols-1) * ai;
	         if(flen >screenField.getLength()){
	           	 flen =  screenField.getLength() - flen;
	         } else {
	        	 flen = numCols-1;
	         }
	
	   		 screenRect.initScreenElement(0);
	   		 screenRect.getElement().setFieldType(fieldMask);
	   		 screenRect.getElement().setFieldId(screenField.getFieldId()*1000 + ai);
	   		 screenRect.getElement().setLength(flen);
	   		 screenRect.getElement().setMaxLength(flen);
	   		 screenRect.getElement().setValue(aval);
	   		 screenRect.getElement().setRow(_row);
	   	 }		
	}
		
	//get 5250 field value
	static private String getFieldValue(ScreenField screenField){
        String value = "";
        if (screenField.isNumeric() || screenField.isSignedNumeric()){
        	value = screenField.getString();
        } else {
        	value = rtrim(screenField.getString());
        }
        return value;
	}
	
	//convert 5250 field statuses to web field mask
	static private int getFieldMask(ScreenField screenField, int focusfield){
        int i=0;
        i = i | ((focusfield == screenField.getFieldId())? 1 : 0);
        i = i << 1;
        i = i | ( screenField.isAutoEnter() ? 1 : 0);
        i = i << 1;
        i = i | ( screenField.isBypassField() ? 1 : 0);
        i = i << 1;
        i = i | ( screenField.isContinued() ? 1 : 0);
        i = i << 1;
        i = i | ( screenField.isContinuedFirst() ? 1 : 0);
        i = i << 1;
        i = i | ( screenField.isContinuedLast() ? 1 : 0);
        i = i << 1;
        i = i | ( screenField.isContinuedMiddle() ? 1 : 0);
        i = i << 1;
        i = i | ( screenField.isDupEnabled() ? 1 : 0);
        i = i << 1;
        i = i | ( screenField.isFER() ? 1 : 0);
        i = i << 1;
        i = i | ( screenField.isHiglightedEntry() ? 1 : 0);
        i = i << 1;
        i = i | ( screenField.isMandatoryEnter() ? 1 : 0);
        i = i << 1;
        i = i | ( screenField.isNumeric()  ? 1 : 0);
        i = i << 1;
        i = i | ( screenField.isSignedNumeric()  ? 1 : 0);
        i = i << 1;
        i = i | ( screenField.isToUpper()   ? 1 : 0);
        return i;
	}

	//find focused field on 5250 screen
	static private int getFocusField(ScreenFields ScreenFields){

		ScreenField focusField = ScreenFields.getCurrentField();

        if (focusField == null)
        	focusField = ScreenFields.getFirstInputField();

        if (focusField != null){
        	return focusField.getFieldId();
        } else return -1;

	}

}
