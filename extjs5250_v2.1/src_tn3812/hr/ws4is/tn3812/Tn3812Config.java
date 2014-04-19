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
package hr.ws4is.tn3812;

import hr.ws4is.tn3812.enums.Tn3812EnvelopeTypes;
import hr.ws4is.tn3812.enums.Tn3812PageTypes;
import hr.ws4is.tn3812.interfaces.ITn3812Config;

/**
 * Main Tn3812 configuration object representing standard printer setup parameters for 
 * virtual printer emulation  
 */
public class Tn3812Config implements ITn3812Config {
	
	 private String IBMMSGQNAME = "QSYSOPR";
	 private String IBMMSGQLIB = "*LIBL";
	 
	 private String IBMFONT = "12";
	 private String IBMFORMFEED = "C";
	 private boolean IBMTRANSFORM = false;
	 //private String IBMMFRTYPMDL = "*WSCSTLETTER"; 
	 private String IBMMFRTYPMDL = "*IBM3812"; 
	 private Tn3812PageTypes IBMPPRSRC1 = Tn3812PageTypes.A4;
	 private Tn3812PageTypes IBMPPRSRC2 = Tn3812PageTypes.A3; 
	 private Tn3812EnvelopeTypes IBMENVELOPE = Tn3812EnvelopeTypes.NONE;
	 private boolean IBMASCII899 = false;
	 private String IBMWSCSTNAME = "*NONE";
	 private String IBMWSCSTLIB = "*NONE";
	 private String Codepage = "Cp870";
	 
	 private String devName;
	 	 	 
	@Override
	public String getDevName() {
		return devName;
	}
	
	public void setDevName(String devName) {
		this.devName = devName;
	}

	@Override
	public String getIBMMSGQNAME() {
		return IBMMSGQNAME;
	}
	
	public void setIBMMSGQNAME(String iBMMSGQNAME) {
		IBMMSGQNAME = iBMMSGQNAME;
	}

	@Override
	public String getIBMMSGQLIB() {
		return IBMMSGQLIB;
	}
	
	public void setIBMMSGQLIB(String iBMMSGQLIB) {
		IBMMSGQLIB = iBMMSGQLIB;
	}

	@Override
	public String getIBMFONT() {
		return IBMFONT;
	}
	
	public void setIBMFONT(String iBMFONT) {
		IBMFONT = iBMFONT;
	}

	@Override
	public String getIBMFORMFEED() {
		return IBMFORMFEED;
	}
	
	public void setIBMFORMFEED(String iBMFORMFEED) {
		IBMFORMFEED = iBMFORMFEED;
	}

	@Override
	public String getIBMMFRTYPMDL() {
		return IBMMFRTYPMDL;
	}
	
	public void setIBMMFRTYPMDL(String iBMMFRTYPMDL) {
		IBMMFRTYPMDL = iBMMFRTYPMDL;
	}

	@Override
	public Tn3812PageTypes getIBMPPRSRC1() {
		return IBMPPRSRC1;
	}
	
	public void setIBMPPRSRC1(Tn3812PageTypes iBMPPRSRC1) {
		IBMPPRSRC1 = iBMPPRSRC1;
	}

	@Override
	public Tn3812PageTypes getIBMPPRSRC2() {
		return IBMPPRSRC2;
	}
	public void setIBMPPRSRC2(Tn3812PageTypes iBMPPRSRC2) {
		IBMPPRSRC2 = iBMPPRSRC2;
	}

	@Override
	public Tn3812EnvelopeTypes getIBMENVELOPE() {
		return IBMENVELOPE;
	}
	public void setIBMENVELOPE(Tn3812EnvelopeTypes iBMENVELOPE) {
		IBMENVELOPE = iBMENVELOPE;
	}

	@Override
	public String getIBMWSCSTNAME() {
		return IBMWSCSTNAME;
	}
	
	public void setIBMWSCSTNAME(String iBMWSCSTNAME) {
		IBMWSCSTNAME = iBMWSCSTNAME;
	}

	@Override
	public String getIBMWSCSTLIB() {
		return IBMWSCSTLIB;
	}
	
	public void setIBMWSCSTLIB(String iBMWSCSTLIB) {
		IBMWSCSTLIB = iBMWSCSTLIB;
	}

	@Override
	public String getCodepage() {
		return Codepage;
	}
	
	public void setCodepage(String codepage) {
		Codepage = codepage;
	}

	@Override
	public boolean isIBMTRANSFORM() {
		return IBMTRANSFORM;
	}
	public void setIBMTRANSFORM(boolean iBMTRANSFORM) {
		IBMTRANSFORM = iBMTRANSFORM;
	}

	@Override
	public boolean isIBMASCII899() {
		return IBMASCII899;
	}
	public void setIBMASCII899(boolean iBMASCII899) {
		IBMASCII899 = iBMASCII899;
	}
	
	
}
