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

import java.util.List;

/**
 * Class representing ExtJS Direct request.
 * It is used for decoding received JSON data from ExtJS into Java class instance
 * @param <T>
 */
public class ExtJSDirectRequest<T> {

    // {"action":"DemoForm","method":"submit","data":[{"id":"0","username":"asfsa","password":"asdfv","email":"sadfv","rank":"345"}],"type":"rpc","tid":1}

    private String action;
    private String method;
    private String namespace;
    private String type;
    private String tid;
    private List<T> data;

    public final String getNamespace() {
        return namespace;
    }

    public final void setNamespace(final String namespace) {
        this.namespace = namespace;
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

    public final List<T> getData() {
        return data;
    }

    public final void setData(final List<T> data) {
        this.data = data;
    }

    public final T getDataByIndex(final int index) {

        T value = null;
        if (data != null && !data.isEmpty()) {
            value = data.get(index);
        }
        return value;
    }

}
