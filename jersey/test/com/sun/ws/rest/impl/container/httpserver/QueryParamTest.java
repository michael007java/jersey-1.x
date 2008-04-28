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

package com.sun.ws.rest.impl.container.httpserver;

import com.sun.jersey.api.client.Client;
import javax.ws.rs.Path;
import com.sun.jersey.api.client.WebResource;
import java.net.URI;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.UriBuilder;
import junit.framework.*;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class QueryParamTest extends AbstractHttpServerTester {
    @Path("/test")
    public static class QueryParamResource {
        @GET
        public String get(@QueryParam("x") String x, @QueryParam("y") String y) {
            return y;
        }
    }
        
    public QueryParamTest(String testName) {
        super(testName);
    }
    
    public void testQueryParam() {
        startServer(QueryParamResource.class);
                
        WebResource r = Client.create().resource(
                getUri().path("test").build());
        
        URI u = UriBuilder.fromPath("").encode(false).
                queryParam("y", "1+%2B+2").build();
        assertEquals("1 + 2", r.uri(u).get(String.class));
        
        u = UriBuilder.fromPath("").
                queryParam("x", "1").encode(false).
                queryParam("y", "1+%2B+2").build();        
        assertEquals("1 + 2", r.uri(u).get(String.class));
        
        u = UriBuilder.fromPath("").
                queryParam("x", "1").encode(false).
                queryParam("y", "1+%26+2").build();
        assertEquals("1 & 2", r.uri(u).get(String.class));
        
        u = UriBuilder.fromPath("").
                queryParam("x", "1").encode(false).
                queryParam("y", "1+%7C%7C+2").build();
        assertEquals("1 || 2", r.uri(u).get(String.class));
    }
}
