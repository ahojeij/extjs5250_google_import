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
package hr.ws4is.ext;

/**
 * Class to be converted to JSON data, specific format for ExtJS response.
 * For Ext.Direct to recognize response, action, method, type and tid must be set
 * as ExtJS keeps signature on send and expects the same signature in response to match queued event
 *
 * @param <T>
 */
public class ExtJSDirectResponse<T> {

    // {"action":"DemoForm","method":"submit","data":[{"id":"0","username":"asfsa","password":"asdfv","email":"sadfv","rank":"345"}],"type":"rpc","tid":1}
    private String action;
    private String method;
    private String type;
    private String tid;
    private boolean keepTransaction;
    private Object result;

    public ExtJSDirectResponse(final ExtJSDirectRequest<T> request, final Object response) {
        super();

        this.result = response;
        if (request != null) {
            this.action = request.getAction();
            this.method = request.getMethod();
            this.tid = request.getTid();
            this.type = request.getType();
        }
    }

    public final String getAction() {
        return action;
    }

    public final void setAction(final String action) {
        this.action = action;
    }

    public final String getMethod() {
        return method;
    }

    public final void setMethod(final String method) {
        this.method = method;
    }

    public final String getType() {
        return type;
    }

    public final void setType(final String type) {
        this.type = type;
    }

    public final String getTid() {
        return tid;
    }

    public final void setTid(final String tid) {
        this.tid = tid;
    }

    public final Object getResult() {
        return result;
    }

    public final void setResult(final Object result) {
        this.result = result;
    }

    public final boolean isKeepTransaction() {
        return keepTransaction;
    }

    public final void setKeepTransaction(final boolean keepTransaction) {
        this.keepTransaction = keepTransaction;
    }

}
