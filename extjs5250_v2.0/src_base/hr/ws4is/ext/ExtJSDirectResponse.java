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

public class ExtJSDirectResponse<T> {
	//{"action":"DemoForm","method":"submit","data":[{"id":"0","username":"asfsa","password":"asdfv","email":"sadfv","rank":"345"}],"type":"rpc","tid":1}
	private String action;
	private String method;
	private String type;
	private String tid;
	private boolean keepTransaction = false;
	private Object result;		
	
	public ExtJSDirectResponse(ExtJSDirectRequest<T> request, Object response) {
		super();
		this.result = response;
		if(request!=null){
			this.action = request.action;
			this.method = request.method;
			this.tid = request.tid;
			this.type = request.type;			
		}
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public boolean isKeepTransaction() {
		return keepTransaction;
	}

	public void setKeepTransaction(boolean keepTransaction) {
		this.keepTransaction = keepTransaction;
	}
	
}
