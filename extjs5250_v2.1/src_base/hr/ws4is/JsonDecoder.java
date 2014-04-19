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
package hr.ws4is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Generic JSON decoder
 */
public class JsonDecoder<T> {
    protected T object;
    protected MappingIterator<T> objectList;
    //protected Class<T> clazz;

    private static final ObjectMapper om ;

    static
    {
    	
    	om = new ObjectMapper();
    	
    	om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).
    	   disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES).
    	   disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT).
    	   disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    	
    	/*
    	DeserializationConfig dcfg = om.getDeserializationConfig();
    	dcfg.without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).
    	without(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES).
    	without(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

    	SerializationConfig scfg = om.getSerializationConfig();
    	scfg.without(SerializationFeature.FAIL_ON_EMPTY_BEANS);    	
    	
    	om.setSerializationConfig(scfg);
    	om.setDeserializationConfig(dcfg);
    	*/
    }
    
	
    public JsonDecoder(Class<T> type, String json) throws IOException {
    	super();
    	parse(type, json);
    }
    
    
    private void parse(Class<T> type, String json) throws IOException {
    	JsonFactory factory = new JsonFactory();

    	JsonParser jp = factory.createParser(json);    	
        JsonNode jn = om.readTree(jp);
        
        if(jn.isArray())
        {
        	TypeFactory tf= TypeFactory.defaultInstance();
        	JavaType jt = tf.constructCollectionType(ArrayList.class, type);
        	objectList  = om.readValues(jp,  jt);
        }else{
            object = om.treeToValue(jn, type);
        }
    }

    public boolean isSingle() {
    	return object != null;
    }

    public T getObject() {
        return object;
    }

    public List<T> getObjectList() throws IOException {
    	
    	if(objectList ==null) 
    	{
    		if(object != null)
    		{
    			return Arrays.asList(object);
    		}
    	}

        return objectList.readAll();
    }

    public void setObject(T object) {
        this.object = object;
    }

	public static ObjectMapper getJSONEngine() {
		return om;
	}
	
}
