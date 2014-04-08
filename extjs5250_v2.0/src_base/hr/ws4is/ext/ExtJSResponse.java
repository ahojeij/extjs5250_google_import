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

import java.io.Serializable;

/*
 ExtJs standard response structure

{ 	  "success": false,
	  "msg": "",
	  "error": "",
	  "stack": ""
	}
*/
public class ExtJSResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	public boolean success = false;
	public String msg ;
	public Throwable exception;

	public ExtJSResponse(boolean success, String message) {
		super();
		this.success = success;
		this.msg = message;
	}

	public ExtJSResponse( Throwable exception,  String message){
		setError(exception, message);
	}

	public ExtJSResponse() {
		super();
		this.success = false;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess( boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg( String msg)	{
		this.msg = msg;
	}

	public Throwable getException()	{
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
		
		if(exception == null) 
		{
			return;		
		}
		
		if(exception instanceof RuntimeException && exception.getCause()!=null)
		{
			this.exception = exception.getCause();
		} else {
			this.exception = exception;	
		}		
		
	}

	public  void setError( Throwable exception,  String message) {
		success = false;
		msg = message;
		setException(exception);		
	}
		
}
