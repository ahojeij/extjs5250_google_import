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
package hr.ws4is.tn3812.drivers.processors.scs;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Main SCS data stream processor
 */
class SCSProcessor extends SCSProcessorA {
		
	
	//holds unfinished command for next chain
	ByteBuffer chainBuffer = ByteBuffer.allocate(1024);
			
	//starts data stream parsing
	public void process(ByteBuffer buffer){
		
		//flip(buffer);
		//flip(chainBuffer);
		
		ByteBuffer data = prepareData(buffer, chainBuffer);
		
		//current text; 
		//whenever new cursor movement changes
		//listener onData is called and buffer is reset		
		ByteBuffer textBuffer = ByteBuffer.allocate(1024);
		
		byte current = 0x00;
		while(data.hasRemaining()){
			current = data.get();
			
			switch (current){
				case SCSCodes.NULL :
					processNULL(data, textBuffer);
					break;
				case SCSCodes.ATRN :
					processATRN(data, textBuffer);
					break;					
				case SCSCodes.HT   :
					processHT(data, textBuffer);
					break;
				case SCSCodes.RNL  :
					processRNL(data, textBuffer);
					break;
				case SCSCodes.SPS  :
					processSPS(data, textBuffer);
					break;
				case SCSCodes.RPT  :
					processRPT(data, textBuffer);
					break;
				case SCSCodes.FF   :
					processFF(data, textBuffer);
					break;
				case SCSCodes.NL   :
					processNL(data, textBuffer);
					break;
				case SCSCodes.BS   :
					processBS(data, textBuffer);
					break;
				case SCSCodes.UBS  :
					processUBS(data, textBuffer);
					break;
				case SCSCodes.IRS  :
					processIRS(data, textBuffer);
					break;
				case SCSCodes.WUS  :
					processWUS(data, textBuffer);
					break;
				case SCSCodes.LF   :
					processLF(data, textBuffer);
					break;
				case SCSCodes.SW   :
					processSW(data, textBuffer);
					break;
				case SCSCodes.BELL :
					processBELL(data, textBuffer);
					break;
				case SCSCodes.IRT  :
					processIRT(data, textBuffer);
					break;
				case SCSCodes.PP   :
					processPP(data, textBuffer);
					break;
				case SCSCodes.NBS  : 
					processNBS(data, textBuffer);
					break;
				case SCSCodes.SBS  :
					processSBS(data, textBuffer);
					break;
				case SCSCodes.IT   :
					processIT(data, textBuffer);
					break;
				case SCSCodes.RFF  :
					processRFF(data, textBuffer);
					break;
				case SCSCodes.SUB  :
					processSUB(data, textBuffer);
					break;
				case SCSCodes.SP   :
					processSP(data, textBuffer);
					break;
				case SCSCodes.RSP  :
					processRSP(data, textBuffer);
					break;
				case SCSCodes.HYP  :
					processHYP(data, textBuffer);
					break;
				case SCSCodes.US   :
					processUS(data, textBuffer);
					break;
				case SCSCodes.SHY  :
					processSHY(data, textBuffer);
					break;
				case SCSCodes.NSP  :
					processNSP(data, textBuffer);
					break;
				case SCSCodes.EO   :
					processEO(data, textBuffer);
					break;
				case SCSCodes.MBC  :
					processMBC(data, textBuffer);
					break;
				default: 
					processDATA(textBuffer, current);
			}
			
		}
		if(textBuffer.position()>0){
			textBuffer.flip();
			chainBuffer.put(textBuffer);
		}
	}

	   // ignored null byte
	   protected void processDATA(ByteBuffer textBuffer, byte current){
		   increaseCursor();
		   textBuffer.put(current);
		   if(listener!=null){
			   listener.onData(textBuffer);
		   }
	   }
	   
	   // null byte
	   protected void processNULL(ByteBuffer buffer, ByteBuffer textBuffer){
		   //ignored
	   }
	   
	   //read ASCII transparent data, ignored
	   protected void processATRN(ByteBuffer buffer, ByteBuffer textBuffer){
		   buffer.mark();
		   if(!hasWholeCommand(buffer,chainBuffer, SCSCodes.ATRN, (short)2)){
			   return;
		   };
		   
		   short len = buffer.get();
		   len--;
		   byte[] data = new byte[len];
		   buffer.get(data);
	   }
	   	   
	   //superscript
	   protected void processSPS(ByteBuffer buffer, ByteBuffer textBuffer){
		 //TODO hmm on any style change, print buffer, clear buffer, set style, call listener
	   }
	   
	   //horizontal tab
	   protected void processHT(ByteBuffer buffer, ByteBuffer textBuffer){
		   //TODO
	   }
	   
	   //required new line 
	   protected void processRNL(ByteBuffer buffer, ByteBuffer textBuffer){
		   controls.currentIndent=0;
		   processNL(buffer, textBuffer);
	   }
	   	   
	   //repeat
	   protected void processRPT(ByteBuffer buffer, ByteBuffer textBuffer){
		   
	   }
	   
	   //form feed - print current page
	   protected void processFF(ByteBuffer buffer, ByteBuffer textBuffer){
		   this.controls.currentFeedCounter ++;
		   if(this.controls.currentFeedCounter == this.controls.SFFC){
			   this.controls.currentFeedCounter = 0;
			   if(this.listener!=null){
				   textBuffer.flip();
				   listener.onNewLine();
				   textBuffer.clear();
				   listener.onFormFeed();
				   controls.currentPosition = 1;
				   controls.currentLine = 1;	
			   }
		   }
	   }
	   
	   //new line 
	   protected void processNL(ByteBuffer buffer, ByteBuffer textBuffer){
		   controls.currentLine ++;
		   controls.currentPosition = 1;
		   //controls.currentTabStop = 0;  //TODO should reset here ? line feed keeps position
		   
		   if(controls.currentLine > controls.SVF){
			   if(listener!=null){
				   listener.onFormFeed();
			   }
			   controls.currentLine = 1;			   
		   }
		   
		   //flip(textBuffer);
		   textBuffer.flip();
		   if(listener!=null){
			   listener.onNewLine();
		   }
		   textBuffer.clear();

	   }
	   
	   //backspace
	   protected void processBS(ByteBuffer buffer, ByteBuffer textBuffer){
		 //TODO ? reprint for bolding; means, skip next n chars ? 
	   }
	   
	   // UNIT BACKSPACE
	   protected void processUBS(ByteBuffer buffer, ByteBuffer textBuffer){
		   //TODO ? reprint for bolding; means, skip next n chars ?  
	   }
	   
	   // INTERCHANGE RECORD SEPARATOR
	   protected void processIRS(ByteBuffer buffer, ByteBuffer textBuffer){
		   processNL(buffer, textBuffer);
	   }
	   
	   // WORD UNDERSCORE
	   protected void processWUS(ByteBuffer buffer, ByteBuffer textBuffer){
		 //TODO hmm on any style change, print buffer, clear buffer, set style, call listener
	   }
	   
	   //LINE FEED
	   protected void processLF(ByteBuffer buffer, ByteBuffer textBuffer){
		   processNL(buffer, textBuffer);
		   //TODO add spaces to textbuffer to keep current position?!
	   }
	   
	   //switch
	   protected void processSW(ByteBuffer buffer, ByteBuffer textBuffer){
		   // printer ignore
	   }
	   
	   //bell 
	   protected void processBELL(ByteBuffer buffer, ByteBuffer textBuffer){
		 // printer ignore
	   }
	   
	   //INDEX RETURN
	   protected void processIRT(ByteBuffer buffer, ByteBuffer textBuffer){
		   processRNL(buffer, textBuffer);
	   }
	   
	   // PRESENTATION POSITION - cursor movement
	   protected void processPP(ByteBuffer buffer, ByteBuffer textBuffer){
		   buffer.mark();
		   if(!hasWholeCommand(buffer,chainBuffer, SCSCodes.PP, (short)2)){
			   return;
		   };
		   
		   byte func = buffer.get();
		   int value = buffer.get();
		   switch(func){
		   case SCSCodes.RMR :
			   //add space
			   while(value>0){
				   processDATA(textBuffer, SCSCodes.SP);
				   value--;
				 }			   
			   break;
		   case SCSCodes.RMD :
			   //TODO check
			   int value2 = Math.abs(value - controls.currentLine);
			   while(value2>0){
				   processNL(buffer, textBuffer);
				   value2--;
			   } 			   
			   break;
		   case SCSCodes.AMR : 
			   //add space
			   int value1 = Math.abs(value - controls.currentPosition);
			   //int value1 = value;
			   while(value1>0){
				   processDATA(textBuffer, SCSCodes.SP);
				   value1--;
				 }				   
			   break;
		   case SCSCodes.AMD :
			   //TODO check
			   while(value>0){
				   processNL(buffer, textBuffer);
				   value--;
			   } 
			   break;
		   }
	   }
	   
	   // NUMERIC BACKSPACE
	   protected void processNBS(ByteBuffer buffer, ByteBuffer textBuffer){
		   processBS(buffer, textBuffer);
	   }
	   
	   // SUBSCRIPT
	   protected void processSBS(ByteBuffer buffer, ByteBuffer textBuffer){
		 //TODO hmm on any style change, print buffer, clear buffer, set style, call listener
	   }
	   
	   // INDENT TAB 
	   protected void processIT(ByteBuffer buffer, ByteBuffer textBuffer){
		   processHT(buffer, textBuffer);
		   controls.currentIndent++;
	   }
	   
	   // REQUEST FORM FEED
	   protected void processRFF(ByteBuffer buffer, ByteBuffer textBuffer){
		   processNL(buffer, textBuffer);
		   //TODO restore to left margin
	   }
	   
	   // SUBSTITUTE
	   protected void processSUB(ByteBuffer buffer, ByteBuffer textBuffer){
		   processSP(buffer, textBuffer);
	   }
	   
	   //add space to buffer
	   protected void processSP(ByteBuffer buffer, ByteBuffer textBuffer){
		   processDATA(textBuffer, SCSCodes.SP);
	   }
	   
	   // REQUIRED SPACE
	   protected void processRSP(ByteBuffer buffer, ByteBuffer textBuffer){
		   processSP(buffer, textBuffer);
	   }
	   
	   // REQUIRED HYPEN
	   protected void processHYP(ByteBuffer buffer, ByteBuffer textBuffer){
		   
	   }
	   
	   // UNDERSCORE
	   protected void processUS(ByteBuffer buffer, ByteBuffer textBuffer){
		 //TODO hmm on any style change, print buffer, clear buffer, set style, call listener
	   }
	   
	   // SYLLABLE HYPEN
	   protected void processSHY(ByteBuffer buffer, ByteBuffer textBuffer){
		   
	   }
	   
	   // NUMERIC SPACE
	   protected void processNSP(ByteBuffer buffer, ByteBuffer textBuffer){
		   processSP(buffer, textBuffer);  
	   }
	   
	   // EIGHT ONES - by spec every FF must be doubled
	   protected void processEO(ByteBuffer buffer, ByteBuffer textBuffer){
		   // processSP(buffer, textBuffer);
	   }
	   
	   //MULTIBYTE COMMANDS
	   protected void processMBC(ByteBuffer buffer, ByteBuffer textBuffer){
		   
		   buffer.mark();
		   if(!hasWholeCommand(buffer, chainBuffer, SCSCodes.MBC, (short)3)){
			   return;
		   };
		   
		   byte mcb = buffer.get();
		   short len = buffer.get();
		   len--;
	
		   if(!hasWholeCommand(buffer, chainBuffer, SCSCodes.MBC, len)){
			   return;
		   };
		   
		   
		   switch(mcb){
			   case SCSCodes.MBC_C1 :
				   processSHF(buffer, textBuffer, len);
				   break;
			   case SCSCodes.MBC_C2 :
				   processSVF(buffer, textBuffer, len);
				   break;
			   case SCSCodes.MBC_C6 :
				   processSLD(buffer, textBuffer, len);
				   break;
			   case SCSCodes.MBC_C8 : 
				   processMBC_C8(buffer, textBuffer, len);
				   break;
			   case SCSCodes.MBC_CA : 
				   processMBC_CA(buffer, textBuffer, len);
				   break;			   
			   case SCSCodes.MBC_D1 :
				   processMBC_D1(buffer, textBuffer, len);
				   break;
			   case SCSCodes.MBC_D2 :
				   processMBC_D2(buffer, textBuffer, len);
				   break;
			   case SCSCodes.MBC_D3 : 
				   processMBC_D3(buffer, textBuffer, len);
				   break;
			   case SCSCodes.MBC_D4 : 
				   processMBC_D4(buffer, textBuffer, len);
				   break;
			   default : 
				   System.out.println("Invalid data!");
		   }
	
	   }
	   
	   //***********************************************************************************************
	   // PROCESS MULTIBYTE SUBCOMMANDS
	   //***********************************************************************************************
	   
	   //SHF - set horizontal format
	   protected void processSHF(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   short value = 0;
		   if(len>1){
			   value = buffer.getShort();
		   } 
		   
		   //reset right margin to default 13.2 in 1440'inch (13.2 * 1440)
		   if(value == 0){
			   this.controls.SHM_RIGHT = 19008;
		   } else{
			   this.controls.SHF = value;
		   }
	   }
	   
	   //SVF - set vertical format; page automatically ejected if new line is bigger than SVF
	   protected void processSVF(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   short value = 0;
		   if(len>1){
			   value = buffer.getShort();
		   } 
		   if(value == 0){
			   this.controls.SVF = 66;
		   }
	   }
	
	   //SLD - single line denity
	   protected void processSLD(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte value = buffer.get();
		   if(value==0){
			   value = 12;
		   } 
		   controls.SLD = value;		   
		   //TODO recalculate current line distance
	   }
	
	   //IGNORED - graphic charater, default
	   protected void processMBC_C8(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte mcb = buffer.get();
		   len--;
		   byte[] bytes = readData(buffer, len);
	   }
	
	   //IGNORED PMP commands
	   protected void processMBC_CA(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte mcb = buffer.get();
		   len--;
		   byte[] bytes = readData(buffer, len);
	   }
	   
	   protected void processMBC_D1(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte mcb = buffer.get();
		   len--;
		   switch(mcb){
			   case SCSCodes.SCG : 
				   processSCG(buffer, textBuffer, len);
				   break;
			   case SCSCodes.SFG :
				   processSFG(buffer, textBuffer, len);
				   break;
			   case SCSCodes.SCGL : 
				   processSCGL(buffer, textBuffer, len);
				   break;
			   case SCSCodes.SFFC : 
				   processSFFC(buffer, textBuffer, len);
				   break;
			   case SCSCodes.BES : 
				   processBES(buffer, textBuffer, len);
				   break;
			   case SCSCodes.BAC :
				   processBAC(buffer, textBuffer, len);
				   break;
			   case SCSCodes.FES : 
				   processFES(buffer, textBuffer, len);
				   break;			   
			   default :
				   System.out.println("Invalid multibyte code");				   
		   }
	   }
	
	   protected void processMBC_D2(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte mcb = buffer.get();
		   len--;
		   switch(mcb){
			   case SCSCodes.STAB :
				   processSTAB(buffer, textBuffer, len);
				   break;
			   case SCSCodes.JTF :
				   processJTF(buffer, textBuffer, len);
				   break;
			   case SCSCodes.SIL :
				   processSIL(buffer, textBuffer, len);
				   break;
			   case SCSCodes.SLS :
				   processSLS(buffer, textBuffer, len);
				   break;
			   case SCSCodes.RLM :
				   processRLM(buffer, textBuffer, len);
				   break;		
			   case SCSCodes.SJM :
				   processSJM(buffer, textBuffer, len);
				   break;
			   case SCSCodes.SHM :
				   processSHM(buffer, textBuffer, len);
				   break;			   
			   case SCSCodes.SSLD :
				   processSSLD(buffer, textBuffer, len);
				   break;
			   case SCSCodes.SCD :
				   processSCD(buffer, textBuffer, len);
				   break;
			   case SCSCodes.SPPS :
				   processSPPS(buffer, textBuffer, len);
				   break;
			   case SCSCodes.SIC :
				   processSIC(buffer, textBuffer, len);
				   break;
			   case SCSCodes.PPM :
				   processPPM(buffer, textBuffer, len);
				   break;
			   case SCSCodes.SVM :
				   processSVM(buffer, textBuffer, len);
				   break;
			   case SCSCodes.SPSU :
				   processSPSU(buffer, textBuffer, len);
				   break;
			   case SCSCodes.SEA :
				   processSEA(buffer, textBuffer, len);
				   break;
			   default :
				   System.out.println("Invalid multibyte code");
		   }
		   
	   }
	
	   // SET TEXT ORIENTATION
	   protected void processMBC_D3(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte mcb = buffer.get();
		   len--;
		   if(mcb != SCSCodes.STO){
			   System.out.println("Invalid multibyte code");
			   return;
		   }
		   buffer.getShort();
		   this.controls.STO = buffer.getShort();
	   }
	
	   
	   protected void processMBC_D4(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte mcb = buffer.get();
		   len--;
		   switch(mcb){
			   case SCSCodes.BUS : 
				   processBUS(buffer, textBuffer, len);
				   break;
			   case SCSCodes.EUS :
				   processEUS(buffer, textBuffer, len);
				   break;
			   case SCSCodes.BOS : 
				   processBOS(buffer, textBuffer, len);
				   break;
			   case SCSCodes.EOS : 
				   processEOS(buffer, textBuffer, len);
				   break;
			   default :
				   System.out.println("Invalid multibyte code");				   
		   }
	
	   }
	
	   //***********************************************************************************************
	   // PROCESS MULTIBYTE SUBCOMMANDS MBC_D1
	   //***********************************************************************************************	
	   
	   // SET GCGID THROUGH GCID
	   protected void processSCG(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   //ignore charset 
		   byte[] data = new byte[len];
		   buffer.get(data);
	   }
	   	   
	   // SET CGSC THROUGH LOCAL ID
	   protected void processSCGL(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   //ignore charset
		   byte[] data = new byte[len];
		   buffer.get(data);
	   }
	   
	   // SET FID THROUGH GFID
	   protected void processSFG(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   short fid = buffer.getShort();
		   this.controls.FWD = buffer.getShort();
		   byte  fa  = buffer.get();
	   }	   
	   
	   // SET FORM FEED CONTROL
	   protected void processSFFC(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte value = buffer.get();
		   if(value != 0){
			   controls.SFFC = value;
		   }
	   }
	   	   
	   // BOLDING; ON | OFF
	   protected void processBAC(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte value = buffer.get();
		   controls.bolding = (value == 0x00) ? true : false;
		   if(listener!=null){
			   listener.onFontStyleChange(SCSStyleType.BOLDING);
		   }
	   }
	   
	   // BEGIN EMPHASIS
	   protected void processBES(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte[] data = new byte[len];
		   buffer.get(data);
		   controls.emphasis = false;
		   if(listener!=null){
			   listener.onFontStyleChange(SCSStyleType.EMPHASIS);
		   }
	   }
	   
	   // END EMPHASIS
	   protected void processFES(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte[] data = new byte[len];
		   buffer.get(data);
		   controls.emphasis = false;
		   if(listener!=null){
			   listener.onFontStyleChange(SCSStyleType.EMPHASIS);
		   }		   
	   }
	   
	   
	   //***********************************************************************************************
	   // PROCESS MULTIBYTE SUBCOMMANDS MBC_D2
	   //***********************************************************************************************	   
	   
	   //SET HORIZONTAL TAB STOPS 
	   protected void processSTAB(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   if(len<3) return;
		   
		   int cnt = 0 ;
		   int tabs = (len/3)-1 ;		   
		   short [] ts = new short[tabs];
		   while(tabs>0){
			   tabs--;
			   byte al = buffer.get(); // ignored
			   short value = buffer.getShort();
			   if(value>0){
				   ts[cnt] = value;
				   cnt ++;
			   }			   
		   }
		   if(cnt>0){
			   controls.STAB = Arrays.copyOf(ts, cnt);
		   } else {
			   controls.STAB = new short[0];
		   }
	   }
	   
	   // JUSTIFY TEXT FIELD
	   protected void processJTF(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte[] data = new byte[len];
		   buffer.get(data);
	   }
	   
	   // SET INDENT LEVEL
	   protected void processSIL(ByteBuffer buffer, ByteBuffer textBuffer, short len){		   
		   controls.currentIndent = buffer.get();
	   }
	   
	   // SET LINE SPACING
	   protected void processSLS(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   controls.SLS = buffer.get();
	   }
	   
	   // RELEASE LEFT MARGIN
	   protected void processRLM(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   //ignored, no data to read
		   byte[] data = new byte[len];
		   buffer.get(data);
	   }
	   
	   // SET JUTIFY MODE
	   protected void processSJM(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte[] data = new byte[len];
		   buffer.get(data);
	   }
	   
	   // SET HORISONTAL MARGINS
	   protected void processSHM(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   this.controls.SHM_LEFT = buffer.getShort();
		   this.controls.SHM_RIGHT = buffer.getShort();
	   }
	   
	   // SET LINE DISTANCE
	   protected void processSSLD(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   controls.SSLD = buffer.getShort();
		   //TODO recalculate current line distance 
	   }
	   
	   // SET CHARACTER DISTANCE 
	   protected void processSCD(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   this.controls.SCD = buffer.getShort();
	   }
	   
	   // SET PRESENTATION PAGE SIZE
	   protected void processSPPS(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   short value = buffer.getShort();
		   if(value > 0 ) {
			   this.controls.SPPS_WIDTH = value;
		   }
		   value = buffer.getShort();
		   if(value > 0 ) {
			   this.controls.SPPS_HEIGHT = value;
		   }
	   }
	   
	   // SET INITIAL CONDITIONS
	   protected void processSIC(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte[] data = new byte[len];
		   buffer.get(data);
	   }
	   
	   // SET PRESENTATION MEDIA
	   protected void processPPM(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte[] data = new byte[len];
		   buffer.get(data);
	   }
	   
	   // SET VERTICAL MARGINS
	   protected void processSVM(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   short value = buffer.getShort();
		   if(value > 0) {
			   this.controls.SVM_TOP = value;
		   }
		   
		   if(len > 4){
			   value = buffer.getShort();
			   if(value > 0) {
				   this.controls.SVM_BOTTOM = value;
			   }			   
			      
		   }
		   
	   }
	   
	   // SET PRINT SETUP
	   protected void processSPSU(ByteBuffer buffer, ByteBuffer textBuffer, short len){
           //ignored - tray selector  ?
		   byte[] data = new byte[len];
		   buffer.get(data);
	   }
	   
	   // SET EXCEPTION ACTION
	   protected void processSEA(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   //ignored
		   byte[] data = new byte[len];
		   buffer.get(data);
	   }
	   
	   //***********************************************************************************************
	   // PROCESS MULTIBYTE SUBCOMMANDS MBC_D4
	   //***********************************************************************************************	
	   
	   // BEGIN UNDERSCORE
	   protected void processBUS(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte[] data = new byte[len];
		   buffer.get(data);
		   controls.underscore = true;
		   if(listener!=null){
			   listener.onFontStyleChange(SCSStyleType.UNDERSOCRE);
		   }
	   }
	   
	   // END UNDERSCORE
	   protected void processEUS(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte[] data = new byte[len];
		   buffer.get(data);
		   controls.underscore = false;
		   if(listener!=null){
			   listener.onFontStyleChange(SCSStyleType.UNDERSOCRE);
		   }
		   
	   }
	   
	   // BEGIN OVERSTRKE
	   protected void processBOS(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte[] data = new byte[len];
		   buffer.get(data);
		   controls.overstrike = true;
		   if(listener!=null){
			   listener.onFontStyleChange(SCSStyleType.OVERSTRIKE);
		   }

	   }
	   
	   // END OVERSTRIKE
	   protected void processEOS(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   byte[] data = new byte[len];
		   buffer.get(data);
		   controls.overstrike = false;
		   if(listener!=null){
			   listener.onFontStyleChange(SCSStyleType.OVERSTRIKE);
		   }
		   
	   }
	   
	   //***********************************************************************************************	   
	   
	   //TODO; check position to margin, if yes, new line by specs
	   private void increaseCursor(){
		   this.controls.currentPosition++;
	   }
	   
	   private boolean hasWholeCommand(ByteBuffer from, ByteBuffer to, byte leading, short len){
		   boolean success = true;
		   if(from.remaining()<len){
			   copyData(from, to, leading);
			   success = false;
		   }		   
		   return success;
	   }
	   
	   private byte[] readData(ByteBuffer buffer, short len){
		   byte[] data = new byte[len];
		   buffer.get(data);
		   return data;
	   }
	
	   private void copyData(ByteBuffer from, ByteBuffer to, byte leading){
		   from.reset();
		   byte[] bs = new byte[from.remaining()];
		   from.get(bs);
		   to.clear();	
		   to.put(leading);
		   to.put(bs);		   
	   }
	   
	   private ByteBuffer prepareData(ByteBuffer current, ByteBuffer previous){
		    ByteBuffer buffer = null;
			if(previous!=null && previous.limit()>0 && previous.position()>0){				
				buffer = ByteBuffer.allocate(previous.position() + current.remaining());
				previous.flip();
				buffer.put(previous);
				buffer.put(current);
				buffer.flip();
				previous.clear();
			} else {
				buffer = current;
			}
			return buffer;
	   }
	   
	   private void flip(ByteBuffer buffer){
		   if(buffer == null) return;
		   if(buffer.position()>0){
			   buffer.flip();
		   }
	   }
}
