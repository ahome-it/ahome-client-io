/*
 * Copyright 2011 Moxie Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ait.toolkit.clientio.uploader.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * A common base class that any of the objects which support configuration options will
 * extend to allow the caller to set the options on them.  Provides a convenient
 * {@link #setOption(String, Object)} method that will allow for a configuration option
 * to be set at any level.
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public abstract class Configurable<T> {

    private JSONObject options;

    /**
     * Set an option on the object at any level, using "/" characters to designate which
     * level of option you'd like to set.  E.g., the following code:
     * <pre><code>
     * Uploader uploader = new Uploader();
     * uploader.setOption("/upload_url", "http://widgetcorp.com/uploads.php");
     * uploader.setOption("/post_params/post_param_name_1", "Photo Upload");
     * uploader.setOption("/post_params/post_param_name_2", "User 24");
     * </code></pre>
     * Would result in initializing the SWFUpload component like the following:
     * <pre><code>
     * new SWFUpload({
     *     upload_url: "http://widgetcorp.com/uploads.php",
     *     post_params: {
     *         post_param_name_1: "Photo Upload",
     *         post_param_name_2: "User 24"
     *     }
     * });
     * </code></pre>
     * Note that the beginning "/" is optional, so <code>uploader.setOption("/thing", "piglet")</code> is
     * equivalent to <code>uploader.setOption("thing", "piglet")</code>.
     * <p/>
     * For details on available options see the <a href="http://demo.uploader.org/Documentation/#settingsobject">SWFUpload reference</a>.
     * <p/>
     * Important note: this method is only intended to support additional options of the SWFUpload API
     * that may not have explicit wrapper methods available in the GWT Uploader API.  For all of the standard
     * configuration options it is important to use the appropriate setter methods instead in order
     * for the Ajax/DOM implementation to function properly as well.  E.g. instead of doing this:
     * <pre><code>
     * uploader.setOption("upload_url", "http://widgetcorp.com/uploads.php");
     * </code></pre>
     * Do this instead:
     * <pre><code>
     * uploader.setUploadURL("http://widgetcorp.com/uploads.php");
     * </code></pre>
     *
     * @param path  The path to the option to set (e.g. "/title/text");
     * @param value The value to set for the option (can be a String, Number, Boolean, or JSONObject)
     * @return A reference to this {@link Configurable} instance for convenient method chaining.
     */
    public T setOption( String path, Object value ) {
        if( options == null ) {
            options = new JSONObject();
        }
        setOption( options, path, value );
        @SuppressWarnings( { "unchecked", "UnnecessaryLocalVariable" } )
        final T instance = (T) this;
        return instance;
    }

    /**
     * Retrieve all of the options that have been configured for this instance
     * as a JSONObject.
     *
     * @return A JSONObject representing all of the configuration options
     *         that have been set on the instance (will be null if no options have been set)
     */
    public JSONObject getOptions() {
        return options;
    }

    // Internal...
    private void setOption( JSONObject rootObject, String path, Object value ) {
        if( path == null ) {
            return;
        }
        if( path.startsWith( "/" ) ) {
            path = path.substring( 1 );
        }
        if( path.length() <= 0 ) {
            return;
        }
        String nodeName = path;
        if( nodeName.contains( "/" ) ) {
            nodeName = nodeName.substring( 0, nodeName.indexOf( "/" ) );
            JSONValue objectAsValue = rootObject.get( nodeName );
            if( objectAsValue == null || objectAsValue.isObject() == null ) {
                rootObject.put( nodeName, new JSONObject() );
            }
            JSONObject object = (JSONObject) rootObject.get( nodeName );
            setOption( object, path.substring( path.indexOf( "/" ) + 1 ), value );
        } else {
            rootObject.put( nodeName, convertToJSONValue( value ) );
        }
    }

    private JSONValue convertToJSONValue( Object value ) {
        if( value == null ) {
            return JSONNull.getInstance();
        } else if( value instanceof JSONValue ) {
            return (JSONValue) value;
        }
        if( value instanceof Boolean ) {
            return JSONBoolean.getInstance( (Boolean) value );
        } else if( value instanceof Number ) {
            return new JSONNumber( ( (Number) value ).doubleValue() );
        } else if( value instanceof String ) {
            return new JSONString( (String) value );
        } else if( value instanceof JavaScriptObject ) {
            return new JSONObject( (JavaScriptObject) value );
        } else if( value instanceof Configurable ) {
            return ( (Configurable) value ).getOptions();
        } else if( value.getClass().isArray() ) {
            JSONArray jsonArray = new JSONArray();
            Object[] valueArray = (Object[]) value;
            for( int i = 0, valueArrayLength = valueArray.length; i < valueArrayLength; i++ ) {
                Object arrayValue = valueArray[i];
                jsonArray.set( i, convertToJSONValue( arrayValue ) );
            }
            return jsonArray;
        }
        return null;
    }

}
