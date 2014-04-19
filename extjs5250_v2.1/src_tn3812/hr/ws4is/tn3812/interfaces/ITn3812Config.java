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
package hr.ws4is.tn3812.interfaces;

import hr.ws4is.tn3812.enums.Tn3812EnvelopeTypes;
import hr.ws4is.tn3812.enums.Tn3812PageTypes;

/**
 * Generic interface of session config object used outside engine 
 */
public interface ITn3812Config {

	public abstract String getDevName();

	public abstract String getIBMMSGQNAME();

	public abstract String getIBMMSGQLIB();

	public abstract String getIBMFONT();

	public abstract String getIBMFORMFEED();

	public abstract String getIBMMFRTYPMDL();

	public abstract Tn3812PageTypes getIBMPPRSRC1();

	public abstract Tn3812PageTypes getIBMPPRSRC2();

	public abstract Tn3812EnvelopeTypes getIBMENVELOPE();

	public abstract String getIBMWSCSTNAME();

	public abstract String getIBMWSCSTLIB();

	public abstract String getCodepage();

	public abstract boolean isIBMTRANSFORM();

	public abstract boolean isIBMASCII899();

}