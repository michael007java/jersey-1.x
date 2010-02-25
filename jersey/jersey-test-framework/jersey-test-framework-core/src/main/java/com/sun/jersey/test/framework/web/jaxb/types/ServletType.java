/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://jersey.dev.java.net/CDDL+GPL.html
 * or jersey/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at jersey/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.jersey.test.framework.web.jaxb.types;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 * Schema representation of servlet information in web.xml
 * @author Naresh (Srinivas.Bhimisetty@Sun.Com)
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType (name="servletType", propOrder={
    "servletName",
    "servletClass",
    "initParams",
    "loadOnStartup"
})
public class ServletType {

    @XmlElement(name="servlet-name", required=true, defaultValue="Jersey Web Application")
    private String servletName;

    @XmlElement(name="servlet-class", required=true)
    private String servletClass;

    @XmlElement(name="init-param", required=true)
    private List<ServletInitParamType> initParams;

    @XmlElement(name="load-on-startup", defaultValue="1", required=true)
    private String loadOnStartup;

    public ServletType() {

    }

    public List<ServletInitParamType> getInitParams() {
        return initParams;
    }

    public void setInitParam(List<ServletInitParamType> initParams) {
        this.initParams = initParams;
    }

    public String getLoadOnStartup() {
        return loadOnStartup;
    }

    public void setLoadOnStartup(String loadOnStartup) {
        this.loadOnStartup = loadOnStartup;
    }

    public String getServletClass() {
        return servletClass;
    }

    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }

    public String getServletName() {
        return servletName;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

}