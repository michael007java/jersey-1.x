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

package com.sun.ws.rest.impl.entity;

import com.sun.ws.rest.api.client.ClientResponse;
import com.sun.ws.rest.api.client.WebResource;
import com.sun.ws.rest.api.client.config.ClientConfig;
import com.sun.ws.rest.api.client.config.DefaultClientConfig;
import com.sun.ws.rest.impl.json.JSONJAXBContext;
import java.io.Reader;
import java.io.StringReader;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class CharsetTest extends AbstractTypeTester {
    private static String[] CHARSETS = {
        "US-ASCII", 
        "ISO-8859-1", 
        "UTF-8", 
        "UTF-16BE", 
        "UTF-16LE", 
        "UTF-16"
    };
    
    private static final String CONTENT = 
            "\u00A9 CONTENT \u00FF \u2200 \u22FF";
    
    public CharsetTest(String testName) {
        super(testName);
    }

    @Path("/")
    public static class StringCharsetResource {
        @Path("US-ASCII")
        @POST
        @ProduceMime("text/plain;charset=US-ASCII")
        public String postUs_Ascii(String t) {
            return t;
        }
        
        @Path("ISO-8859-1")
        @POST
        @ProduceMime("text/plain;charset=ISO-8859-1")
        public String postIso_8859_1(String t) {
            return t;
        }
        
        @Path("UTF-8")
        @POST
        @ProduceMime("text/plain;charset=UTF-8")
        public String postUtf_8(String t) {
            return t;
        }
        
        @Path("UTF-16BE")
        @POST
        @ProduceMime("text/plain;charset=UTF-16BE")
        public String postUtf_16be(String t) {
            return t;
        }
        
        @Path("UTF-16LE")
        @POST
        @ProduceMime("text/plain;charset=UTF-16LE")
        public String postUtf_16le(String t) {
            return t;
        }
        
        @Path("UTF-16")
        @POST
        @ProduceMime("text/plain;charset=UTF-16")
        public String postUtf_16(String t) {
            return t;
        }        
    }
    
    public void testStringCharsetResource() {
        initiateWebApplication(StringCharsetResource.class);
        
        String in = "\u00A9 CONTENT \u00FF \u2200 \u22FF";
        
        for (String charset : CHARSETS) {
            WebResource r = resource(charset);
            ClientResponse rib = r.type("text/plain;charset=" + charset).post(ClientResponse.class, in);

            byte[] inBytes = (byte[])
                    rib.getProperties().get("request.entity");
            byte[] outBytes = (byte[])
                    rib.getProperties().get("response.entity");

            _verify(inBytes, outBytes);            
        }
    }
    
    public static abstract class CharsetResource<T> {
        @Context HttpHeaders h;
        
        @POST
        public Response post(T t) {
            return Response.ok(t, h.getMediaType()).build();
        }
    }    
    
    @Path("/")
    public static class StringResource extends CharsetResource<String> { }
    
    public void testStringRepresentation() {
        _test(CONTENT, StringResource.class);
    }

    @Path("/")
    public static class JSONObjectResource extends CharsetResource<JSONObject> {}
    
    public void testJSONObjectRepresentation() throws Exception {
        JSONObject object = new JSONObject();
        object.put("userid", 1234).
        put("username", CONTENT).
        put("email", "a@b").
        put("password", "****");
        
        _test(object, JSONObjectResource.class);
    }
    
    @Path("/")
    public static class JSONOArrayResource extends CharsetResource<JSONArray> {}
    
    public void testJSONArrayRepresentation() throws Exception {
        JSONArray array = new JSONArray();
        array.put(CONTENT).put("Two").put("Three").put(1).put(2.0);
        
        _test(array, JSONOArrayResource.class);
    }
    
    @Path("/")
    public static class JAXBBeanResource extends CharsetResource<JAXBBean> {}
    
    public void testJAXBBeanXMLRepresentation() {
        _test(new JAXBBean(CONTENT), JAXBBeanResource.class);
    }
    
    public void testJAXBBeanJSONRepresentation() {
        initiateWebApplication(JAXBBeanResource.class);
        
        JAXBBean in = new JAXBBean(CONTENT);
        WebResource r = resource("/");
        for (String charset : CHARSETS) {
            ClientResponse rib = r.type("application/json;charset=" + charset).post(ClientResponse.class, in);
            byte[] inBytes = (byte[])
                    rib.getProperties().get("request.entity");
            byte[] outBytes = (byte[])
                    rib.getProperties().get("response.entity");

            _verify(inBytes, outBytes);            
        }
    }
    
    @Provider
    public static class MyJAXBContextResolver implements ContextResolver<JAXBContext> {
        JAXBContext context;
        
        public MyJAXBContextResolver() throws Exception {
            context = new JSONJAXBContext(JAXBBean.class);
        }
        
        public JAXBContext getContext(Class<?> objectType) {
            return (objectType == JAXBBean.class) ? context : null;
        }
    }
    
    public void testJAXBBeanJSONRepresentationWithContextResolver() throws Exception {
        initiateWebApplication(JAXBBeanResource.class, MyJAXBContextResolver.class);
        
        JAXBBean in = new JAXBBean(CONTENT);
        ClientConfig cc = new DefaultClientConfig();
        cc.getProviderClasses().add(MyJAXBContextResolver.class);
        WebResource r = resource("/", cc);
        for (String charset : CHARSETS) {
            ClientResponse rib = r.type("application/json;charset=" + charset).
                    post(ClientResponse.class, in);
            byte[] inBytes = (byte[])
                    rib.getProperties().get("request.entity");
            byte[] outBytes = (byte[])
                    rib.getProperties().get("response.entity");

            _verify(inBytes, outBytes);            
        }        
    }
    
    @Path("/")
    public static class ReaderResource extends CharsetResource<Reader> {}
    
    public void testReaderRepresentation() throws Exception {
        initiateWebApplication(ReaderResource.class);
        
        WebResource r = resource("/");
        for (String charset : CHARSETS) {
            ClientResponse rib = r.type("text/plain;charset=" + charset).
                    post(ClientResponse.class, new StringReader(CONTENT));
            byte[] inBytes = (byte[])
                    rib.getProperties().get("request.entity");
            byte[] outBytes = (byte[])
                    rib.getProperties().get("response.entity");

            _verify(inBytes, outBytes);            
        }
    }
    
    @Override
    public <T> void _test(T in, Class resource) {
        initiateWebApplication(resource);
        
        WebResource r = resource("/");
        for (String charset : CHARSETS) {
            ClientResponse rib = r.type("text/plain;charset=" + charset).post(ClientResponse.class, in);
            byte[] inBytes = (byte[])
                    rib.getProperties().get("request.entity");            
            byte[] outBytes = (byte[])
                    rib.getProperties().get("response.entity");
            System.out.println(new String(inBytes));
            System.out.println(new String(outBytes));
            _verify(inBytes, outBytes);            
        }
    }
}