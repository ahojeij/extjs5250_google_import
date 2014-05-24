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
 * Generic JSON decoder used internally
 */
public class JsonDecoder<T> {

    private T object;

    private MappingIterator<T> objectList;
    // protected Class<T> clazz;

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();

        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).
                      disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES).
                      disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT).
                      disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        /*
         * DeserializationConfig dcfg = om.getDeserializationConfig();
         * dcfg.without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).
         * without(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES).
         * without(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
         *
         * SerializationConfig scfg = om.getSerializationConfig();
         * scfg.without(SerializationFeature.FAIL_ON_EMPTY_BEANS);
         * om.setSerializationConfig(scfg); om.setDeserializationConfig(dcfg);
         */
    }

    /**
     * New Decoder instance for JSON data
     * @param type - class to which to convert
     * @param json - json data to convert to Java class instance
     * @throws IOException
     */
    public JsonDecoder(final Class<T> type, final String json) throws IOException {
        super();
        parse(type, json);
    }

    /**
     * Does actual conversion from JSON string to Java class instance
     * @param type
     * @param json
     * @throws IOException
     */
    private void parse(final Class<T> type, final String json) throws IOException {
        final JsonFactory factory = new JsonFactory();
        final JsonParser jp = factory.createParser(json);
        final JsonNode jn = OBJECT_MAPPER.readTree(jp);

        if (jn.isArray()) {
            final TypeFactory tf = TypeFactory.defaultInstance();
            final JavaType jt = tf.constructCollectionType(ArrayList.class, type);
            objectList = OBJECT_MAPPER.readValues(jp, jt);
        } else {
            object = OBJECT_MAPPER.treeToValue(jn, type);
        }
    }

    /**
     * Checks is converted JSON array or single object
     * @return true if it is not array
     */
    public final boolean isSingle() {
        return object != null;
    }

    /**
     * Returns JSON data converted Java class instance.
     * If JSON data is array, this method will return null
     * @return class instance from defined class in constructor
     */
    public final T getObject() {
        return object;
    }

    /**
     * Returns JSON data converted Java class instance.
     * If JSON data is object, this method will return null
     * @return class instance from defined class in constructor
     */
    public final List<T> getObjectList() throws IOException {

        List<T> list = null;
        if (objectList != null) {
            list = objectList.readAll();
        } else if (object != null) {
            list = Arrays.asList(object);
        }

        return list;
    }

    /**
     * Retrieves internal JSON parser engine
     * @return
     */
    public static ObjectMapper getJSONEngine() {
        return OBJECT_MAPPER;
    }

}
