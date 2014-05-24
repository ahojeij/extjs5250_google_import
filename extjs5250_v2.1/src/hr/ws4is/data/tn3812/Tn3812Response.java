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

public class Tn3812Response extends ExtJSResponse {

    private static final long serialVersionUID = 1L;

    private String printerName;
    private String reportName;
    private String tnType;

    public Tn3812Response() {
        super();
    }

    public Tn3812Response(final boolean success, final String message) {
        super(success, message);
    }

    public Tn3812Response(final Throwable exception, final String message) {
        super(exception, message);
    }

    public final String getPrinterName() {
        return printerName;
    }

    public final void setPrinterName(final String printerName) {
        this.printerName = printerName;
    }

    public final String getReportName() {
        return reportName;
    }

    public final void setReportName(final String reportName) {
        this.reportName = reportName;
    }

    public final String getTnType() {
        return tnType;
    }

    public final void setTnType(final String tnType) {
        this.tnType = tnType;
    }

}
