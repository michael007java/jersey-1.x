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

package com.sun.ws.rest.impl.methodparams;

import com.sun.ws.rest.impl.AbstractResourceTester;
import com.sun.ws.rest.impl.AbstractResourceTester;
import com.sun.jersey.api.client.ClientResponse;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.CookieParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Cookie;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class CookieParamAsPrimitiveTest extends AbstractResourceTester {

    public CookieParamAsPrimitiveTest(String testName) {
        super(testName);
        initiateWebApplication(
                ResourceHeaderPrimitives.class,
                ResourceHeaderPrimitivesDefaultNull.class,
                ResourceHeaderPrimitivesDefault.class,
                ResourceHeaderPrimitivesDefaultOverride.class,
                ResourceHeaderPrimitiveWrappers.class,
                ResourceHeaderPrimitiveWrappersDefaultNull.class,
                ResourceHeaderPrimitiveWrappersDefault.class,
                ResourceHeaderPrimitiveWrappersDefaultOverride.class,                
                ResourceHeaderPrimitiveList.class,
                ResourceHeaderPrimitiveListDefaultNull.class,
                ResourceHeaderPrimitiveListDefault.class,
                ResourceHeaderPrimitiveListDefaultOverride.class
                );
    }

    @Path("/")
    public static class ResourceHeaderPrimitives {
        @GET
        @ProduceMime("application/boolean")
        public String doGet(@CookieParam("boolean") boolean v) {
            assertEquals(true, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/byte")
        public String doGet(@CookieParam("byte") byte v) {
            assertEquals(127, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/short")
        public String doGet(@CookieParam("short") short v) {
            assertEquals(32767, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/int")
        public String doGet(@CookieParam("int") int v) {
            assertEquals(2147483647, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/long")
        public String doGet(@CookieParam("long") long v) {
            assertEquals(9223372036854775807L, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/float")
        public String doGet(@CookieParam("float") float v) {
            assertEquals(3.14159265f, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/double")
        public String doGet(@CookieParam("double") double v) {
            assertEquals(3.14159265358979d, v);
            return "content";
        }        
    }
    
    @Path("/default/null")
    public static class ResourceHeaderPrimitivesDefaultNull {
        @GET
        @ProduceMime("application/boolean")
        public String doGet(@CookieParam("boolean") boolean v) {
            assertEquals(false, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/byte")
        public String doGet(@CookieParam("byte") byte v) {
            assertEquals(0, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/short")
        public String doGet(@CookieParam("short") short v) {
            assertEquals(0, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/int")
        public String doGet(@CookieParam("int") int v) {
            assertEquals(0, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/long")
        public String doGet(@CookieParam("long") long v) {
            assertEquals(0l, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/float")
        public String doGet(@CookieParam("float") float v) {
            assertEquals(0.0f, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/double")
        public String doGet(@CookieParam("double") double v) {
            assertEquals(0.0d, v);
            return "content";
        }        
    }
    
    @Path("/default")
    public static class ResourceHeaderPrimitivesDefault {
        @GET
        @ProduceMime("application/boolean")
        public String doGet(@CookieParam("boolean") @DefaultValue("true") boolean v) {
            assertEquals(true, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/byte")
        public String doGet(@CookieParam("byte") @DefaultValue("127") byte v) {
            assertEquals(127, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/short")
        public String doGet(@CookieParam("short") @DefaultValue("32767") short v) {
            assertEquals(32767, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/int")
        public String doGet(@CookieParam("int") @DefaultValue("2147483647") int v) {
            assertEquals(2147483647, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/long")
        public String doGet(@CookieParam("long") @DefaultValue("9223372036854775807") long v) {
            assertEquals(9223372036854775807L, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/float")
        public String doGet(@CookieParam("float") @DefaultValue("3.14159265") float v) {
            assertEquals(3.14159265f, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/double")
        public String doGet(@CookieParam("double") @DefaultValue("3.14159265358979") double v) {
            assertEquals(3.14159265358979d, v);
            return "content";
        }        
    }
    
    @Path("/default/override")
    public static class ResourceHeaderPrimitivesDefaultOverride {
        @GET
        @ProduceMime("application/boolean")
        public String doGet(@CookieParam("boolean") @DefaultValue("false") boolean v) {
            assertEquals(true, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/byte")
        public String doGet(@CookieParam("byte") @DefaultValue("1") byte v) {
            assertEquals(127, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/short")
        public String doGet(@CookieParam("short") @DefaultValue("1") short v) {
            assertEquals(32767, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/int")
        public String doGet(@CookieParam("int") @DefaultValue("1") int v) {
            assertEquals(2147483647, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/long")
        public String doGet(@CookieParam("long") @DefaultValue("1") long v) {
            assertEquals(9223372036854775807L, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/float")
        public String doGet(@CookieParam("float") @DefaultValue("0.0") float v) {
            assertEquals(3.14159265f, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/double")
        public String doGet(@CookieParam("double") @DefaultValue("0.0") double v) {
            assertEquals(3.14159265358979d, v);
            return "content";
        }        
    }
    
    @Path("/wrappers")
    public static class ResourceHeaderPrimitiveWrappers {
        @GET
        @ProduceMime("application/boolean")
        public String doGet(@CookieParam("boolean") Boolean v) {
            assertEquals(true, v.booleanValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/byte")
        public String doGet(@CookieParam("byte") Byte v) {
            assertEquals(127, v.byteValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/short")
        public String doGet(@CookieParam("short") Short v) {
            assertEquals(32767, v.shortValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/int")
        public String doGet(@CookieParam("int") Integer v) {
            assertEquals(2147483647, v.intValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/long")
        public String doGet(@CookieParam("long") Long v) {
            assertEquals(9223372036854775807L, v.longValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/float")
        public String doGet(@CookieParam("float") Float v) {
            assertEquals(3.14159265f, v.floatValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/double")
        public String doGet(@CookieParam("double") Double v) {
            assertEquals(3.14159265358979d, v.doubleValue());
            return "content";
        }        
    }
    
    @Path("/wrappers/default/null")
    public static class ResourceHeaderPrimitiveWrappersDefaultNull {
        @GET
        @ProduceMime("application/boolean")
        public String doGet(@CookieParam("boolean") Boolean v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/byte")
        public String doGet(@CookieParam("byte") Byte v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/short")
        public String doGet(@CookieParam("short") Short v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/int")
        public String doGet(@CookieParam("int") Integer v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/long")
        public String doGet(@CookieParam("long") Long v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/float")
        public String doGet(@CookieParam("float") Float v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/double")
        public String doGet(@CookieParam("double") Double v) {
            assertEquals(null, v);
            return "content";
        }        
    }
    
    @Path("/wrappers/default")
    public static class ResourceHeaderPrimitiveWrappersDefault {
        @GET
        @ProduceMime("application/boolean")
        public String doGet(@CookieParam("boolean") @DefaultValue("true") Boolean v) {
            assertEquals(true, v.booleanValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/byte")
        public String doGet(@CookieParam("byte") @DefaultValue("127") Byte v) {
            assertEquals(127, v.byteValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/short")
        public String doGet(@CookieParam("short") @DefaultValue("32767") Short v) {
            assertEquals(32767, v.shortValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/int")
        public String doGet(@CookieParam("int") @DefaultValue("2147483647") Integer v) {
            assertEquals(2147483647, v.intValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/long")
        public String doGet(@CookieParam("long") @DefaultValue("9223372036854775807") Long v) {
            assertEquals(9223372036854775807L, v.longValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/float")
        public String doGet(@CookieParam("float") @DefaultValue("3.14159265") Float v) {
            assertEquals(3.14159265f, v.floatValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/double")
        public String doGet(@CookieParam("double") @DefaultValue("3.14159265358979") Double v) {
            assertEquals(3.14159265358979d, v.doubleValue());
            return "content";
        }        
    }
    
    @Path("/wrappers/default/override")
    public static class ResourceHeaderPrimitiveWrappersDefaultOverride {
        @GET
        @ProduceMime("application/boolean")
        public String doGet(@CookieParam("boolean") @DefaultValue("false") Boolean v) {
            assertEquals(true, v.booleanValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/byte")
        public String doGet(@CookieParam("byte") @DefaultValue("1") Byte v) {
            assertEquals(127, v.byteValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/short")
        public String doGet(@CookieParam("short") @DefaultValue("1") Short v) {
            assertEquals(32767, v.shortValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/int")
        public String doGet(@CookieParam("int") @DefaultValue("1") Integer v) {
            assertEquals(2147483647, v.intValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/long")
        public String doGet(@CookieParam("long") @DefaultValue("1") Long v) {
            assertEquals(9223372036854775807L, v.longValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/float")
        public String doGet(@CookieParam("float") @DefaultValue("0.0") Float v) {
            assertEquals(3.14159265f, v.floatValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/double")
        public String doGet(@CookieParam("double") @DefaultValue("0.0") Double v) {
            assertEquals(3.14159265358979d, v.doubleValue());
            return "content";
        }        
    }
    
    @Path("/list")
    public static class ResourceHeaderPrimitiveList {
        @GET
        @ProduceMime("application/boolean")
        public String doGetBoolean(@CookieParam("boolean") List<Boolean> v) {
            assertEquals(true, v.get(0).booleanValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/byte")
        public String doGetByte(@CookieParam("byte") List<Byte> v) {
            assertEquals(127, v.get(0).byteValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/short")
        public String doGetShort(@CookieParam("short") List<Short> v) {
            assertEquals(32767, v.get(0).shortValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/int")
        public String doGetInteger(@CookieParam("int") List<Integer> v) {
            assertEquals(2147483647, v.get(0).intValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/long")
        public String doGetLong(@CookieParam("long") List<Long> v) {
            assertEquals(9223372036854775807L, v.get(0).longValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/float")
        public String doGetFloat(@CookieParam("float") List<Float> v) {
            assertEquals(3.14159265f, v.get(0).floatValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/double")
        public String doGetDouble(@CookieParam("double") List<Double> v) {
            assertEquals(3.14159265358979d, v.get(0).doubleValue());
            return "content";
        }        
    }
    
    @Path("/list/default/null")
    public static class ResourceHeaderPrimitiveListDefaultNull {
        @GET
        @ProduceMime("application/boolean")
        public String doGetBoolean(@CookieParam("boolean") List<Boolean> v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/byte")
        public String doGetByte(@CookieParam("byte") List<Byte> v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/short")
        public String doGetShort(@CookieParam("short") List<Short> v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/int")
        public String doGetInteger(@CookieParam("int") List<Integer> v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/long")
        public String doGetLong(@CookieParam("long") List<Long> v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/float")
        public String doGetFloat(@CookieParam("float") List<Float> v) {
            assertEquals(null, v);
            return "content";
        }        
        
        @GET
        @ProduceMime("application/double")
        public String doGetDouble(@CookieParam("double") List<Double> v) {
            assertEquals(null, v);
            return "content";
        }        
    }
    
    @Path("/list/default")
    public static class ResourceHeaderPrimitiveListDefault {
        @GET
        @ProduceMime("application/boolean")
        public String doGetBoolean(@CookieParam("boolean") @DefaultValue("true") List<Boolean> v) {
            assertEquals(true, v.get(0).booleanValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/byte")
        public String doGetByte(@CookieParam("byte") @DefaultValue("127") List<Byte> v) {
            assertEquals(127, v.get(0).byteValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/short")
        public String doGetShort(@CookieParam("short") @DefaultValue("32767") List<Short> v) {
            assertEquals(32767, v.get(0).shortValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/int")
        public String doGetInteger(@CookieParam("int") @DefaultValue("2147483647") List<Integer> v) {
            assertEquals(2147483647, v.get(0).intValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/long")
        public String doGetLong(@CookieParam("long") @DefaultValue("9223372036854775807") List<Long> v) {
            assertEquals(9223372036854775807L, v.get(0).longValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/float")
        public String doGetFloat(@CookieParam("float") @DefaultValue("3.14159265") List<Float> v) {
            assertEquals(3.14159265f, v.get(0).floatValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/double")
        public String doGetDouble(@CookieParam("double") @DefaultValue("3.14159265358979") List<Double> v) {
            assertEquals(3.14159265358979d, v.get(0).doubleValue());
            return "content";
        }        
    }
    
    @Path("/list/default/override")
    public static class ResourceHeaderPrimitiveListDefaultOverride {
        @GET
        @ProduceMime("application/boolean")
        public String doGetBoolean(@CookieParam("boolean") @DefaultValue("false") List<Boolean> v) {
            assertEquals(true, v.get(0).booleanValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/byte")
        public String doGetByte(@CookieParam("byte") @DefaultValue("0") List<Byte> v) {
            assertEquals(127, v.get(0).byteValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/short")
        public String doGetShort(@CookieParam("short") @DefaultValue("0") List<Short> v) {
            assertEquals(32767, v.get(0).shortValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/int")
        public String doGetInteger(@CookieParam("int") @DefaultValue("0") List<Integer> v) {
            assertEquals(2147483647, v.get(0).intValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/long")
        public String doGetLong(@CookieParam("long") @DefaultValue("0") List<Long> v) {
            assertEquals(9223372036854775807L, v.get(0).longValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/float")
        public String doGetFloat(@CookieParam("float") @DefaultValue("0.0") List<Float> v) {
            assertEquals(3.14159265f, v.get(0).floatValue());
            return "content";
        }        
        
        @GET
        @ProduceMime("application/double")
        public String doGetDouble(@CookieParam("double") @DefaultValue("0.0") List<Double> v) {
            assertEquals(3.14159265358979d, v.get(0).doubleValue());
            return "content";
        }        
    }
    
    
    public void _test(String type, String value) {
        resource("/").accept("application/" + type).
                cookie(new Cookie(type, value)).get(String.class);
        
        resource("/wrappers").accept("application/" + type).
                cookie(new Cookie(type, value)).get(String.class);    
        
        resource("/list").accept("application/" + type).
                cookie(new Cookie(type, value)).
                get(String.class);
    }
    
    public void _testDefault(String base, String type, String value) {
        resource(base + "default/null").accept("application/" + type).
                get(String.class);
        
        resource(base + "default").accept("application/" + type).
                get(String.class);
        
        resource(base + "default/override").accept("application/" + type).
                cookie(new Cookie(type, value)).get(String.class);        
    }
    
    public void _testDefault(String type, String value) {
        _testDefault("/", type, value);
    }
    
    public void _testWrappersDefault(String type, String value) {
        _testDefault("/wrappers/", type, value);
    }

    public void _testListDefault(String type, String value) {
        _testDefault("/list/", type, value);
    }

    public void testGetBoolean() {
        _test("boolean", "true");
    }
    
    public void testGetBooleanPrimitivesDefault() {
        _testDefault("boolean", "true");
    }
    
    public void testGetBooleanPrimitiveWrapperDefault() {
        _testWrappersDefault("boolean", "true");
    }
    
    public void testGetBooleanPrimitiveListDefault() {
        _testListDefault("boolean", "true");
    }    
    
    public void testGetByte() {
        _test("byte", "127");
    }
    
    public void testGetBytePrimitivesDefault() {
        _testDefault("byte", "127");
    }
    
    public void testGetBytePrimitiveWrappersDefault() {
        _testWrappersDefault("byte", "127");
    }
    
    public void testGetBytePrimitiveListDefault() {
        _testListDefault("byte", "127");
    }    
    
    public void testGetShort() {
        _test("short", "32767");
    }
    
    public void testGetShortPrimtivesDefault() {
        _testDefault("short", "32767");
    }
    
    public void testGetShortPrimtiveWrappersDefault() {
        _testWrappersDefault("short", "32767");
    }
    
    public void testGetShortPrimtiveListDefault() {
        _testListDefault("short", "32767");
    }    
    
    public void testGetInt() {
        _test("int", "2147483647");
    }
    
    public void testGetIntPrimitivesDefault() {
        _testDefault("int", "2147483647");
    }
    
    public void testGetIntPrimitiveWrappersDefault() {
        _testWrappersDefault("int", "2147483647");
    }
    
    public void testGetIntPrimitiveListDefault() {
        _testListDefault("int", "2147483647");
    }    
    
    public void testGetLong() {
        _test("long", "9223372036854775807");
    }
    
    public void testGetLongPrimitivesDefault() {
        _testDefault("long", "9223372036854775807");
    }
    
    public void testGetLongPrimitiveWrappersDefault() {
        _testWrappersDefault("long", "9223372036854775807");
    }
    
    public void testGetLongPrimitiveListDefault() {
        _testListDefault("long", "9223372036854775807");
    }    
    
    public void testGetFloat() {
        _test("float", "3.14159265");
    }
    
    public void testGetFloatPrimitivesDefault() {
        _testDefault("float", "3.14159265");
    }
    
    public void testGetFloatPrimitiveWrappersDefault() {
        _testWrappersDefault("float", "3.14159265");
    }
    
    public void testGetFloatPrimitiveListDefault() {
        _testListDefault("float", "3.14159265");
    }    
    
    public void testGetDouble() {
        _test("double", "3.14159265358979");
    }
    
    public void testGetDoublePrimitivesDefault() {
        _testDefault("double", "3.14159265358979");
    }
    
    public void testGetDoublePrimitiveWrappersDefault() {
        _testWrappersDefault("double", "3.14159265358979");
    }
    
    public void testGetDoublePrimitiveListDefault() {
        _testListDefault("double", "3.14159265358979");
    }
    
    public void testBadPrimitiveValue() {
        ClientResponse response = resource("/", false).accept("application/int").
                cookie(new Cookie("int", "abcdef")).get(ClientResponse.class);
        
        assertEquals(400, response.getStatus());
    }
    
    public void testBadPrimitiveWrapperValue() {
        ClientResponse response = resource("/wrappers", false).accept("application/int").
                cookie(new Cookie("int", "abcdef")).get(ClientResponse.class);
        
        assertEquals(400, response.getStatus());
    }
    
    public void testBadPrimitiveListValue() {
        ClientResponse response = resource("/wrappers", false).accept("application/int").
                cookie(new Cookie("int", "abcdef")).
                get(ClientResponse.class);
        
        assertEquals(400, response.getStatus());
    }
}
