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

/**
 ExtJs standard response structure used by other extended response classes

 { "success": false,
   "msg": "",
   "error": "",
   "stack": ""
  }
 */
public class ExtJSResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private String msg;
    private Throwable exception;

    public ExtJSResponse(final boolean success, final String message) {
        super();
        this.success = success;
        this.msg = message;
    }

    public ExtJSResponse(final Throwable exception, final String message) {
        setError(exception, message);
    }

    public ExtJSResponse() {
        super();
        this.success = false;
    }

    public final boolean isSuccess() {
        return success;
    }

    public final void setSuccess(final boolean success) {
        this.success = success;
    }

    public final String getMsg() {
        return msg;
    }

    public final void setMsg(final String msg) {
        this.msg = msg;
    }

    public final Throwable getException() {
        return exception;
    }

    public final void setException(final Throwable exception) {
        this.exception = exception;

        if (exception == null) {
            return;
        }

        if (exception instanceof RuntimeException && exception.getCause() != null) {
            this.exception = exception.getCause();
        } else {
            this.exception = exception;
        }

    }

    public final void setError(final Throwable exception, final String message) {
        success = false;
        msg = message;
        setException(exception);
    }

}
