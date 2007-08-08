/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved. 
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License").  You may not use this file
 * except in compliance with the License. 
 * 
 * You can obtain a copy of the License at:
 *     https://jersey.dev.java.net/license.txt
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * When distributing the Covered Code, include this CDDL Header Notice in each
 * file and include the License file at:
 *     https://jersey.dev.java.net/license.txt
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 *     "Portions Copyrighted [year] [name of copyright owner]"
 */

package com.sun.ws.rest.impl.provider.entity;


import com.sun.ws.rest.impl.ImplMessages;
import com.sun.ws.rest.impl.util.ThrowHelper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.ws.rs.core.MultivaluedMap;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author japod
 */
public class JSONObjectProvider  extends AbstractTypeEntityProvider<JSONObject>{
    
    
    public void writeTo(JSONObject jsonObject, MultivaluedMap httpHeaders, OutputStream entityStream) throws IOException {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(entityStream);
            jsonObject.write(writer);
            writer.write("\n");
            writer.flush();
        } catch (JSONException je) {
            throw ThrowHelper.withInitCause(je, new IOException(ImplMessages.ERROR_WRITING_JSON_OBJECT()));
        }
    }
    
    public boolean supports(Class<?> type) {
        return type == JSONObject.class;
    }
    
    public JSONObject readFrom(Class<JSONObject> o, String mediaType, MultivaluedMap<String, String> headers, InputStream is) throws IOException {
        try {
            return new JSONObject(readFromAsString(is));
        } catch (JSONException je) {
            throw ThrowHelper.withInitCause(je, new IOException(ImplMessages.ERROR_PARSING_JSON_OBJECT()));
        }
    }
}
