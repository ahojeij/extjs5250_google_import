package hr.ws4is.tn3812.drivers.processors;

import hr.ws4is.tn3812.drivers.processors.SCSProcessor;

import java.nio.ByteBuffer;

/**
 * SCS data stream processor used for logging parsed instructions
 */
class SCSLogger extends SCSProcessor {

		protected void processDATA(ByteBuffer textBuffer, byte current){
			System.out.println("     --> CHARACTER");
			super.processDATA(textBuffer, current);
		}
		
	   // ignored null byte
	   protected void processNULL(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("NULL --> NULL");
		   super.processNULL(buffer, textBuffer);		   
	   }
	   
	   //read ASCII transparent data, ignored
	   protected void processATRN(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("ATRN --> TRANSPARENT ASCII");
		   super.processATRN(buffer, textBuffer);
	   }
	   	   
	   //superscript
	   protected void processSPS(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("SPS  --> SUPERSCRIPT");
		   super.processSPS(buffer, textBuffer);		   
	   }
	   
	   //horizontal tab
	   protected void processHT(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("HT   --> HORIZONTAL TAB");
		   super.processHT(buffer, textBuffer);		   
	   }
	   
	   //required new line 
	   protected void processRNL(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("RNL  --> REQUIRED NEW LINE");
		   super.processRNL(buffer, textBuffer);		   
	   }
	   	   
	   //repeat
	   protected void processRPT(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("RPT  --> REPEAT");
		   super.processRPT(buffer, textBuffer);		   
	   }
	   
	   //form feed - print current page
	   protected void processFF(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("FF   --> FORM FEED");
		   super.processFF(buffer, textBuffer);		   
	   }
	   
	   //new line 
	   protected void processNL(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("NL   --> NEW LINE");
		   super.processNL(buffer, textBuffer);
	   }
	   
	   //backspace
	   protected void processBS(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("BS   --> BACKSPACE");
		   super.processBS(buffer, textBuffer);
	   }
	   
	   // UNIT BACKSPACE
	   protected void processUBS(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("UBS  --> UNIT BACKSPACE");
		   super.processUBS(buffer, textBuffer);
	   }
	   
	   // INTERCHANGE RECORD SEPARATOR
	   protected void processIRS(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("IRS  --> INTERCHANGE RECORD SEPARATOR");
		   super.processIRS(buffer, textBuffer);
	   }
	   
	   // WORD UNDERSCORE
	   protected void processWUS(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("WUS  --> WORD UNDERSCORE");
		   super.processWUS(buffer, textBuffer);
	   }
	   
	   //LINE FEED
	   protected void processLF(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("LF   --> LINE FEED");
		   super.processLF(buffer, textBuffer);
	   }
	   
	   //switch
	   protected void processSW(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("SW   --> SWITCH");
		   super.processSW(buffer, textBuffer);
	   }
	   
	   //bell - ignore
	   protected void processBELL(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("BELL --> BELL");
		   super.processBELL(buffer, textBuffer);		   
	   }
	   
	   //INDEX RETURN
	   protected void processIRT(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("IRT  --> INDEX RETURN");
		   super.processIRT(buffer, textBuffer);		   
	   }
	   
	   // PRESENTATION POSITION - cursor movement
	   protected void processPP(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("PP   --> PRESENTATION POSITION");
		   super.processPP(buffer, textBuffer);
	   }
	   
	   // NUMERIC BACKSPACE
	   protected void processNBS(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("NBS  --> NUMERIC BACKSPACE");
		   super.processNBS(buffer, textBuffer);		   
	   }
	   
	   // SUBSCRIPT
	   protected void processSBS(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("SBS  --> SUBSCRIPT");
		   super.processSBS(buffer, textBuffer);		   
	   }
	   
	   // INDENT TAB 
	   protected void processIT(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("IT   --> INDENT TAB ");
		   super.processIT(buffer, textBuffer);		   
	   }
	   
	   // REQUEST FORM FEED
	   protected void processRFF(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("RFE  --> REQUEST FORM FEED");
		   super.processRFF(buffer, textBuffer);		   
	   }
	   
	   // SUBSTITUTE
	   protected void processSUB(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("SUB  --> SUBSTITUTE");
		   super.processSUB(buffer, textBuffer);		   
	   }
	   
	   // ADD SPACE
	   protected void processSP(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("SP   --> ADD SPACE");
		   super.processSP(buffer, textBuffer);
	   }
	   
	   // REQUIRED SPACE
	   protected void processRSP(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("RSP  --> REQUIRED SPACE");
		   super.processRSP(buffer, textBuffer);
	   }
	   
	   // REQUIRED HYPEN
	   protected void processHYP(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("HYP  --> REQUIRED HYPEN");
		   super.processHYP(buffer, textBuffer);		   
	   }
	   
	   // UNDERSCORE
	   protected void processUS(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("US   --> UNDERSCORE");
		   super.processUS(buffer, textBuffer);		   
	   }
	   
	   // SYLLABLE HYPEN
	   protected void processSHY(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("SHY  --> SYLLABLE HYPEN");
		   super.processSHY(buffer, textBuffer);		   
	   }
	   
	   // NUMERIC SPACE
	   protected void processNSP(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("NSP  --> NUMERIC SPACE");
		   super.processNSP(buffer, textBuffer);   
	   }
	   
	   // EIGHT ONES
	   protected void processEO(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("EQ   --> EIGHT ONES");
		   super.processEO(buffer, textBuffer);		   
	   }
	   
	   //MULTIBYTE COMMANDS
	   protected void processMBC(ByteBuffer buffer, ByteBuffer textBuffer){
		   System.out.println("     --> MULTIBYTE COMMANDS");
		   super.processMBC(buffer, textBuffer);	
	   }
	   
	   //***********************************************************************************************
	   // PROCESS MULTIBYTE SUBCOMMANDS
	   //***********************************************************************************************
	   
	   protected void processSHF(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("SHF  --> SET HORIZONTAL FORMAT");
		   super.processSHF(buffer, textBuffer, len);
	   }
	   
	   protected void processSVF(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("SVF  --> SET VERTICAL FORMAT");
		   super.processSVF(buffer, textBuffer, len);
	   }
	
	   protected void processSLD(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("     --> processMBC_C6");
		   super.processSLD(buffer, textBuffer, len);
	   }
	
	   protected void processMBC_C8(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("     --> processMBC_C8");
		   super.processMBC_C8(buffer, textBuffer, len);
	   }
	
	   protected void processMBC_CA(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("     --> processMBC_CA");
		   super.processMBC_CA(buffer, textBuffer, len);
	   }
	   
	   protected void processMBC_D1(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("     --> processMBC_D1");
		   super.processMBC_D1(buffer, textBuffer, len);
	   }
	
	   protected void processMBC_D2(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("     --> processMBC_D2");
		   super.processMBC_D2(buffer, textBuffer, len);		   
	   }
	
	   // SET TEXT ORIENTATION
	   protected void processMBC_D3(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("     --> processMBC_D3");
		   System.out.println("STO  --> SET TEXT ORIENTATION");
		   super.processMBC_D3(buffer, textBuffer, len);
	   }
	
	   
	   protected void processMBC_D4(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("     --> processMBC_D4");
		   super.processMBC_D4(buffer, textBuffer, len);	
	   }
	
	   //***********************************************************************************************
	   // PROCESS MULTIBYTE SUBCOMMANDS MBC_D1
	   //***********************************************************************************************	
	   
	   // SET GCGID THROUGH GCID
	   protected void processSCG(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("GCID --> SET GCGID THROUGH GCID");
		   super.processSCG(buffer, textBuffer, len);
	   }
	   
	   // SET FID THROUGH GFID
	   protected void processSFG(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("SFG  --> SET FID THROUGH GFID");
		   super.processSFG(buffer, textBuffer, len);
	   }
	   
	   // SET CGSC THROUGH LOCAL ID
	   protected void processSCGL(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("SCGL --> SET CGSC THROUGH LOCAL ID");
		   super.processSCGL(buffer, textBuffer, len);
	   }
	   
	   // SET FORM FEE CONTROL
	   protected void processSFFC(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("SFFC --> SET FORM FEE CONTROL");
		   super.processSFFC(buffer, textBuffer, len);
	   }
	   
	   // BEGIN EMPHASIS
	   protected void processBES(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("BES  --> BEGIN EMPHASIS");
		   super.processBES(buffer, textBuffer, len);
	   }
	   
	   // BOLDING; ON | OFF
	   protected void processBAC(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("BAC  --> BOLDING; ON | OFF");
		   super.processBAC(buffer, textBuffer, len);
	   }
	   
	   // END EMPHASIS
	   protected void processFES(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("FES  --> END EMPHASIS");
		   super.processFES(buffer, textBuffer, len);
	   }
	   
	   
	   //***********************************************************************************************
	   // PROCESS MULTIBYTE SUBCOMMANDS MBC_D2
	   //***********************************************************************************************	   
	   
	   //SET HORIZONTAL TAB STOPS 
	   protected void processSTAB(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("STAB --> SET HORIZONTAL TAB STOPS");
		   super.processSTAB(buffer, textBuffer, len);
	   }
	   
	   // JUSTIFY TEXT FIELD
	   protected void processJTF(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("JTF  --> JUSTIFY TEXT FIELD");
		   super.processJTF(buffer, textBuffer, len);
	   }
	   
	   // SET INDENT LEVEL
	   protected void processSIL(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("SIL  --> SET INDENT LEVEL");
		   super.processSIL(buffer, textBuffer, len);
	   }
	   
	   // SET LINE SPACING
	   protected void processSLS(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("SLS  --> SET LINE SPACING");
		   super.processSLS(buffer, textBuffer, len);
	   }
	   
	   // RELEASE LEFT MARGIN
	   protected void processRLM(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("RLM  --> RELEASE LEFT MARGIN");
		   super.processRLM(buffer, textBuffer, len);
	   }
	   
	   // SET JUSTIFY MODE
	   protected void processSJM(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("SJM  --> SET JUSTIFY MODE");
		   super.processSJM(buffer, textBuffer, len);
	   }
	   
	   // SET HORIZONTAL MARGINS
	   protected void processSHM(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("SHM  --> SET HORIZONTAL MARGINS");
		   super.processSHM(buffer, textBuffer, len);
	   }
	   
	   // SET LINE DISTANCE
	   protected void processSSLD(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("SSLD --> SET LINE DISTANCE");
		   super.processSSLD(buffer, textBuffer, len);
	   }
	   
	   // SET CHARACTER DISTANCE 
	   protected void processSCD(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("SCD  --> SET CHARACTER DISTANCE ");
		   super.processSCD(buffer, textBuffer, len);
	   }
	   
	   // SET PRESENTATION PAGE SIZE
	   protected void processSPPS(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("SPPS --> SET PRESENTATION PAGE SIZE");
		   super.processSPPS(buffer, textBuffer, len);
	   }
	   
	   // SET INITIAL CONDITIONS
	   protected void processSIC(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("SIC  --> SET INITIAL CONDITIONS");
		   super.processSIC(buffer, textBuffer, len);
	   }
	   
	   // SET PRESENTATION MEDIA
	   protected void processPPM(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("PPM  --> SET PRESENTATION MEDIA");
		   super.processPPM(buffer, textBuffer, len);
	   }
	   
	   // SET VERTICAL MARGINS
	   protected void processSVM(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("SVM  --> SET VERTICAL MARGINS");
		   super.processSVM(buffer, textBuffer, len);
	   }
	   
	   // SET PRINT SETUP
	   protected void processSPSU(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("SPSU --> SET PRINT SETUP");
		   super.processSPSU(buffer, textBuffer, len);
	   }
	   
	   // SET EXCEPTION ACTION
	   protected void processSEA(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("SEA  --> SET EXCEPTION ACTION");
		   super.processSEA(buffer, textBuffer, len);
	   }
	   
	   //***********************************************************************************************
	   // PROCESS MULTIBYTE SUBCOMMANDS MBC_D4
	   //***********************************************************************************************	
	   
	   // BEGIN UNDERSCORE
	   protected void processBUS(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("BUS  --> BEGIN UNDERSCORE");
		   super.processBUS(buffer, textBuffer, len);
	   }
	   
	   // END UNDERSCORE
	   protected void processEUS(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("EUS  --> END UNDERSCORE");
		   super.processEUS(buffer, textBuffer, len);
	   }
	   
	   // BEGIN OVERSTRKE
	   protected void processBOS(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("BOS  --> BEGIN OVERSTRKE");
		   super.processBOS(buffer, textBuffer, len);
	   }
	   
	   // END OVERSTRIKE
	   protected void processEOS(ByteBuffer buffer, ByteBuffer textBuffer, short len){
		   System.out.println("EOS  --> END OVERSTRIKE");
		   super.processEOS(buffer, textBuffer, len);
	   }
}
