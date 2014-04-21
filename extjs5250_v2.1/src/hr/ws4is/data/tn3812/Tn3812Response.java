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
package hr.ws4is.data.tn3812;

import hr.ws4is.ext.ExtJSResponse;

public class Tn3812Response extends ExtJSResponse{

	private static final long serialVersionUID = 1L;

	String printerName;
	String reportName;
	
	public Tn3812Response() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Tn3812Response(boolean success, String message) {
		super(success, message);
		// TODO Auto-generated constructor stub
	}

	public Tn3812Response(Throwable exception, String message) {
		super(exception, message);
		// TODO Auto-generated constructor stub
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

}
