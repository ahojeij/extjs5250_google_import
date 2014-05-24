package hr.ws4is.tn3812.drivers.processors.scs;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SCS data stream processor used for logging parsed instructions
 */
class SCSLogger extends SCSProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SCSLogger.class);

    protected void processDATA(final ByteBuffer textBuffer, final byte current) {
        LOGGER.info("     --> CHARACTER");
        super.processDATA(textBuffer, current);
    }

    // ignored null byte
    protected void processNULL(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("NULL --> NULL");
        super.processNULL(buffer, textBuffer);
    }

    // read ASCII transparent data, ignored
    protected void processATRN(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("ATRN --> TRANSPARENT ASCII");
        super.processATRN(buffer, textBuffer);
    }

    // superscript
    protected void processSPS(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("SPS  --> SUPERSCRIPT");
        super.processSPS(buffer, textBuffer);
    }

    // horizontal tab
    protected void processHT(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("HT   --> HORIZONTAL TAB");
        super.processHT(buffer, textBuffer);
    }

    // required new line
    protected void processRNL(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("RNL  --> REQUIRED NEW LINE");
        super.processRNL(buffer, textBuffer);
    }

    // repeat
    protected void processRPT(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("RPT  --> REPEAT");
        super.processRPT(buffer, textBuffer);
    }

    // form feed - print current page
    protected void processFF(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("FF   --> FORM FEED");
        super.processFF(buffer, textBuffer);
    }

    // new line
    protected void processNL(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("NL   --> NEW LINE");
        super.processNL(buffer, textBuffer);
    }

    // backspace
    protected void processBS(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("BS   --> BACKSPACE");
        super.processBS(buffer, textBuffer);
    }

    // UNIT BACKSPACE
    protected void processUBS(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("UBS  --> UNIT BACKSPACE");
        super.processUBS(buffer, textBuffer);
    }

    // INTERCHANGE RECORD SEPARATOR
    protected void processIRS(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("IRS  --> INTERCHANGE RECORD SEPARATOR");
        super.processIRS(buffer, textBuffer);
    }

    // WORD UNDERSCORE
    protected void processWUS(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("WUS  --> WORD UNDERSCORE");
        super.processWUS(buffer, textBuffer);
    }

    // LINE FEED
    protected void processLF(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("LF   --> LINE FEED");
        super.processLF(buffer, textBuffer);
    }

    // switch
    protected void processSW(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("SW   --> SWITCH");
        super.processSW(buffer, textBuffer);
    }

    // bell - ignore
    protected void processBELL(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("BELL --> BELL");
        super.processBELL(buffer, textBuffer);
    }

    // INDEX RETURN
    protected void processIRT(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("IRT  --> INDEX RETURN");
        super.processIRT(buffer, textBuffer);
    }

    // PRESENTATION POSITION - cursor movement
    protected void processPP(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("PP   --> PRESENTATION POSITION");
        super.processPP(buffer, textBuffer);
    }

    // NUMERIC BACKSPACE
    protected void processNBS(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("NBS  --> NUMERIC BACKSPACE");
        super.processNBS(buffer, textBuffer);
    }

    // SUBSCRIPT
    protected void processSBS(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("SBS  --> SUBSCRIPT");
        super.processSBS(buffer, textBuffer);
    }

    // INDENT TAB
    protected void processIT(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("IT   --> INDENT TAB ");
        super.processIT(buffer, textBuffer);
    }

    // REQUEST FORM FEED
    protected void processRFF(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("RFE  --> REQUEST FORM FEED");
        super.processRFF(buffer, textBuffer);
    }

    // SUBSTITUTE
    protected void processSUB(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("SUB  --> SUBSTITUTE");
        super.processSUB(buffer, textBuffer);
    }

    // ADD SPACE
    protected void processSP(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("SP   --> ADD SPACE");
        super.processSP(buffer, textBuffer);
    }

    // REQUIRED SPACE
    protected void processRSP(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("RSP  --> REQUIRED SPACE");
        super.processRSP(buffer, textBuffer);
    }

    // REQUIRED HYPEN
    protected void processHYP(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("HYP  --> REQUIRED HYPEN");
        super.processHYP(buffer, textBuffer);
    }

    // UNDERSCORE
    protected void processUS(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("US   --> UNDERSCORE");
        super.processUS(buffer, textBuffer);
    }

    // SYLLABLE HYPEN
    protected void processSHY(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("SHY  --> SYLLABLE HYPEN");
        super.processSHY(buffer, textBuffer);
    }

    // NUMERIC SPACE
    protected void processNSP(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("NSP  --> NUMERIC SPACE");
        super.processNSP(buffer, textBuffer);
    }

    // EIGHT ONES
    protected void processEO(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("EQ   --> EIGHT ONES");
        super.processEO(buffer, textBuffer);
    }

    // MULTIBYTE COMMANDS
    protected void processMBC(final ByteBuffer buffer, final ByteBuffer textBuffer) {
        LOGGER.info("     --> MULTIBYTE COMMANDS");
        super.processMBC(buffer, textBuffer);
    }

    // ***********************************************************************************************
    // PROCESS MULTIBYTE SUBCOMMANDS
    // ***********************************************************************************************

    protected void processSHF(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("SHF  --> SET HORIZONTAL FORMAT");
        super.processSHF(buffer, textBuffer, len);
    }

    protected void processSVF(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("SVF  --> SET VERTICAL FORMAT");
        super.processSVF(buffer, textBuffer, len);
    }

    protected void processSLD(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("     --> processMBC_C6");
        super.processSLD(buffer, textBuffer, len);
    }

    protected void processMBC_C8(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("     --> processMBC_C8");
        super.processMBC_C8(buffer, textBuffer, len);
    }

    protected void processMBC_CA(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("     --> processMBC_CA");
        super.processMBC_CA(buffer, textBuffer, len);
    }

    protected void processMBC_D1(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("     --> processMBC_D1");
        super.processMBC_D1(buffer, textBuffer, len);
    }

    protected void processMBC_D2(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("     --> processMBC_D2");
        super.processMBC_D2(buffer, textBuffer, len);
    }

    // SET TEXT ORIENTATION
    protected void processMBC_D3(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("     --> processMBC_D3");
        LOGGER.info("STO  --> SET TEXT ORIENTATION");
        super.processMBC_D3(buffer, textBuffer, len);
    }

    protected void processMBC_D4(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("     --> processMBC_D4");
        super.processMBC_D4(buffer, textBuffer, len);
    }

    // ***********************************************************************************************
    // PROCESS MULTIBYTE SUBCOMMANDS MBC_D1
    // ***********************************************************************************************

    // SET GCGID THROUGH GCID
    protected void processSCG(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("GCID --> SET GCGID THROUGH GCID");
        super.processSCG(buffer, textBuffer, len);
    }

    // SET FID THROUGH GFID
    protected void processSFG(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("SFG  --> SET FID THROUGH GFID");
        super.processSFG(buffer, textBuffer, len);
    }

    // SET CGSC THROUGH LOCAL ID
    protected void processSCGL(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("SCGL --> SET CGSC THROUGH LOCAL ID");
        super.processSCGL(buffer, textBuffer, len);
    }

    // SET FORM FEE CONTROL
    protected void processSFFC(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("SFFC --> SET FORM FEE CONTROL");
        super.processSFFC(buffer, textBuffer, len);
    }

    // BEGIN EMPHASIS
    protected void processBES(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("BES  --> BEGIN EMPHASIS");
        super.processBES(buffer, textBuffer, len);
    }

    // BOLDING; ON | OFF
    protected void processBAC(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("BAC  --> BOLDING; ON | OFF");
        super.processBAC(buffer, textBuffer, len);
    }

    // END EMPHASIS
    protected void processFES(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("FES  --> END EMPHASIS");
        super.processFES(buffer, textBuffer, len);
    }

    // ***********************************************************************************************
    // PROCESS MULTIBYTE SUBCOMMANDS MBC_D2
    // ***********************************************************************************************

    // SET HORIZONTAL TAB STOPS
    protected void processSTAB(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("STAB --> SET HORIZONTAL TAB STOPS");
        super.processSTAB(buffer, textBuffer, len);
    }

    // JUSTIFY TEXT FIELD
    protected void processJTF(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("JTF  --> JUSTIFY TEXT FIELD");
        super.processJTF(buffer, textBuffer, len);
    }

    // SET INDENT LEVEL
    protected void processSIL(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("SIL  --> SET INDENT LEVEL");
        super.processSIL(buffer, textBuffer, len);
    }

    // SET LINE SPACING
    protected void processSLS(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("SLS  --> SET LINE SPACING");
        super.processSLS(buffer, textBuffer, len);
    }

    // RELEASE LEFT MARGIN
    protected void processRLM(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("RLM  --> RELEASE LEFT MARGIN");
        super.processRLM(buffer, textBuffer, len);
    }

    // SET JUSTIFY MODE
    protected void processSJM(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("SJM  --> SET JUSTIFY MODE");
        super.processSJM(buffer, textBuffer, len);
    }

    // SET HORIZONTAL MARGINS
    protected void processSHM(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("SHM  --> SET HORIZONTAL MARGINS");
        super.processSHM(buffer, textBuffer, len);
    }

    // SET LINE DISTANCE
    protected void processSSLD(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("SSLD --> SET LINE DISTANCE");
        super.processSSLD(buffer, textBuffer, len);
    }

    // SET CHARACTER DISTANCE
    protected void processSCD(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("SCD  --> SET CHARACTER DISTANCE ");
        super.processSCD(buffer, textBuffer, len);
    }

    // SET PRESENTATION PAGE SIZE
    protected void processSPPS(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("SPPS --> SET PRESENTATION PAGE SIZE");
        super.processSPPS(buffer, textBuffer, len);
    }

    // SET INITIAL CONDITIONS
    protected void processSIC(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("SIC  --> SET INITIAL CONDITIONS");
        super.processSIC(buffer, textBuffer, len);
    }

    // SET PRESENTATION MEDIA
    protected void processPPM(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("PPM  --> SET PRESENTATION MEDIA");
        super.processPPM(buffer, textBuffer, len);
    }

    // SET VERTICAL MARGINS
    protected void processSVM(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("SVM  --> SET VERTICAL MARGINS");
        super.processSVM(buffer, textBuffer, len);
    }

    // SET PRINT SETUP
    protected void processSPSU(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("SPSU --> SET PRINT SETUP");
        super.processSPSU(buffer, textBuffer, len);
    }

    // SET EXCEPTION ACTION
    protected void processSEA(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("SEA  --> SET EXCEPTION ACTION");
        super.processSEA(buffer, textBuffer, len);
    }

    // ***********************************************************************************************
    // PROCESS MULTIBYTE SUBCOMMANDS MBC_D4
    // ***********************************************************************************************

    // BEGIN UNDERSCORE
    protected void processBUS(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("BUS  --> BEGIN UNDERSCORE");
        super.processBUS(buffer, textBuffer, len);
    }

    // END UNDERSCORE
    protected void processEUS(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("EUS  --> END UNDERSCORE");
        super.processEUS(buffer, textBuffer, len);
    }

    // BEGIN OVERSTRKE
    protected void processBOS(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("BOS  --> BEGIN OVERSTRKE");
        super.processBOS(buffer, textBuffer, len);
    }

    // END OVERSTRIKE
    protected void processEOS(final ByteBuffer buffer, final ByteBuffer textBuffer, final short len) {
        LOGGER.info("EOS  --> END OVERSTRIKE");
        super.processEOS(buffer, textBuffer, len);
    }

}
