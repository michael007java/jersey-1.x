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

package com.sun.ws.rest.impl;

import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.api.client.ClientHandler;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.ws.rest.impl.http.header.HttpHeaderFactory;
import com.sun.jersey.spi.container.AbstractContainerRequest;
import com.sun.jersey.spi.container.AbstractContainerResponse;
import com.sun.jersey.spi.container.MessageBodyContext;
import com.sun.jersey.spi.container.WebApplication;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.RuntimeDelegate;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class TestResourceClientHandler implements ClientHandler {
    private static final Annotation[] EMPTY_ANNOTATIONS = new Annotation[0];
    
    @Context private MessageBodyContext bodyContext;
    
    private final WebApplication w;
    
    private final URI baseUri;
    
    public TestResourceClientHandler(URI baseUri, WebApplication w) {
        this.baseUri = baseUri;
        this.w = w;
    }
    
    private final class Response extends ClientResponse {
        private final InputStream responseEntity;
        private final AbstractContainerResponse response;
        private final MultivaluedMap<String, String> metadata;
        private Map<String, Object> properties;
        
       Response(InputStream responseEntity, AbstractContainerResponse response) {
            this.responseEntity = responseEntity;
            this.response = response;
            this.metadata = new RequestHttpHeadersImpl();
            
            writeHeaders(response.getHttpHeaders(), metadata);
        }
        
        public int getStatus() {
            return response.getStatus();
        }

        public MultivaluedMap<String, String> getMetadata() {
            return metadata;
        }

        public boolean hasEntity() {
            return response.getEntity() != null;
        }
        
        public <T> T getEntity(Class<T> c) {
            if (response.getEntity() == null) return null;
            
            try {
                MediaType mediaType = getType();
                final MessageBodyReader<T> br = bodyContext.getMessageBodyReader(
                        c, c,
                        EMPTY_ANNOTATIONS, mediaType);
                if (br == null) {
                    throw new ClientHandlerException(
                            "A message body reader for Java type, " + c + 
                            ", and MIME media type, " + mediaType + ", was not found");
                }
                return br.readFrom(c, null, null, mediaType, metadata, responseEntity);
            } catch (IOException ex) {
                throw new ClientHandlerException(ex);
            }
        }
        
        public Map<String, Object> getProperties() {
            if (properties != null) return properties;

            return properties = new HashMap<String, Object>();
        }
    }

    public ClientResponse handle(ClientRequest clientRequest) {
        byte[] requestEntity = writeEntity(clientRequest.getMetadata(), 
                clientRequest.getEntity());
        final AbstractContainerRequest serverRequest = new TestHttpRequestContext(
                w.getMessageBodyContext(), 
                clientRequest.getMethod(), 
                new ByteArrayInputStream(requestEntity),
                clientRequest.getURI(),
                baseUri);
        
        writeHeaders(clientRequest.getMetadata(), serverRequest.getRequestHeaders());
        List<String> cookies = serverRequest.getRequestHeaders().get("Cookie");
        if (cookies != null) {
            for (String cookie : cookies) {
                if (cookie != null)
                    serverRequest.getCookies().putAll(
                            HttpHeaderFactory.createCookies(cookie));
            }
        }
        
        final TestHttpResponseContext serverResponse = new TestHttpResponseContext(
                w.getMessageBodyContext(), 
                serverRequest);
        
        w.handleRequest(serverRequest, serverResponse);
        try {
            serverResponse.commitAll();
        } catch (IOException e) {
            throw new ContainerException(e);
        }
        
        byte[] responseEntity = serverResponse.
                getUnderlyingByteArrayOutputStream().toByteArray();        
        Response clientResponse = new Response(
                new ByteArrayInputStream(responseEntity), serverResponse);
        
        clientResponse.getProperties().put("request.entity", requestEntity);
        clientResponse.getProperties().put("response.entity", responseEntity);
        return clientResponse;
    }

    private static void writeHeaders(MultivaluedMap<String, Object> metadata, 
            MultivaluedMap<String, String> request) {
        for (Map.Entry<String, List<Object>> e : metadata.entrySet()) {
            for (Object v : e.getValue()) {
                request.add(e.getKey(), writeHeaderValue(v));
            }
        }        
    }
    
    @SuppressWarnings("unchecked")
    private static String writeHeaderValue(Object headerValue) {
        HeaderDelegate hp = RuntimeDelegate.getInstance().createHeaderDelegate(headerValue.getClass());
        return hp.toString(headerValue);
    }
    
    private byte[] writeEntity(MultivaluedMap<String, Object> metadata, Object entity) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeEntity(metadata, entity, baos);
        return baos.toByteArray();        
    }
    
    @SuppressWarnings("unchecked")
    private void writeEntity(MultivaluedMap<String, Object> metadata, Object entity, 
            OutputStream out) {
        if (entity == null) return;
                
        try {
            MediaType mediaType = null;
            final Object mediaTypeHeader = metadata.getFirst("Content-Type");
            if (mediaTypeHeader instanceof MediaType) {
                mediaType = (MediaType)mediaType;
            } else {
                if (mediaTypeHeader != null) {
                    mediaType = MediaType.valueOf(mediaTypeHeader.toString());
                } else {
                    mediaType = new MediaType("application", "octet-stream");
                }
            }
            final MessageBodyWriter bw = bodyContext.getMessageBodyWriter(
                    entity.getClass(), entity.getClass(),
                    null, mediaType);
            if (bw == null) {
                throw new ClientHandlerException(
                        "A message body writer for Java type, " + entity.getClass() + 
                        ", and MIME media type, " + mediaType + ", was not found");
            }
            bw.writeTo(entity, entity.getClass(), entity.getClass(), 
                    EMPTY_ANNOTATIONS, (MediaType)mediaType, metadata, out);
            out.flush();
            out.close();
        } catch (IOException ex) {
            throw new ClientHandlerException(ex);
        }
    }
}
