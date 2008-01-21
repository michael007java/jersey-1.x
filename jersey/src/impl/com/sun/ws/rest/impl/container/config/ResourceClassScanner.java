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

package com.sun.ws.rest.impl.container.config;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Search for root resource classes.
 *
 * @author Frank D. Martinez. fmartinez@asimovt.com
 */
public final class ResourceClassScanner {
    private static final Logger LOGGER = 
            Logger.getLogger(ResourceClassScanner.class.getName());
    
    /** Root resource classes found. */
    private Set<Class> classes;
    
    /** Annotation class names we are looking for. */
    private final Set<String> annotations;
    
    private final ClassLoader classloader;
    
    public ResourceClassScanner(Class... annotations) {
        this.classloader = Thread.currentThread().getContextClassLoader();
        this.annotations = getAnnotationSet(annotations);
    }
    
    /**
     * Scans paths for matching classes.
     * 
     * @param paths A list of absolute paths used to scan for Resource classes.
     * @return A set of classes annotated with specified annotations in the 
     *         specified paths.
     */
    public Set<Class> scan(File[] paths) {
        this.classes = new HashSet<Class>();
        
        for (File file : paths) {
            index(file);
        }

        return classes;
    }

    /**
     * Scans packages for matching classes.
     * 
     * @param packages the array of packages.
     * @return A set of classes annotated with specified annotations in the 
     *         specified paths.
     */
    public Set<Class> scan(String[] packages) {
        this.classes = new HashSet<Class>();
        
        for (String p : packages) {
            try {
                String fileP = p.replace('.', '/');
                Enumeration<URL> urls = classloader.getResources(fileP);
                while (urls.hasMoreElements()) {
                    index(urls.nextElement(), fileP);
                }
            } catch (IOException ex) {
                String s = "The resources for the package" + 
                        p + 
                        ", could not be obtained";
                LOGGER.severe(s);
                throw new RuntimeException(s, ex);
            }
        }
        
        return classes;
    }
    
    private Set<String> getAnnotationSet(Class... annotations) {
        Set<String> a = new HashSet<String>();
        for (Class cls : annotations) {
            a.add(
                "L" + cls.getName().replaceAll("\\.", "/") + ";");
        }
        return a;
    }
    
    private void index(File file) {
        if (file.isDirectory()) {
            indexDir(file, true);
        } else if (file.getName().endsWith(".jar") || 
                file.getName().endsWith(".zip")) {
            indexJar(file);
        } else {
            LOGGER.warning("File, " + 
                    file.getAbsolutePath() + 
                    ", is ignored, it not a directory, a jar file or a zip file");
        }
    }
    
    private void index(URL u, String filePackageName) {
        String protocol = u.getProtocol();
        if (protocol.equals("file")) {
            indexDir(new File(u.getPath()), false);
        } else if (protocol.equals("jar")) {
            URI jarUri = URI.create(u.getPath());
            String jarFile = jarUri.getPath();
            jarFile = jarFile.substring(0, jarFile.indexOf('!'));            
            indexJar(new File(jarFile), filePackageName);
        } else {
            LOGGER.warning("URL, " + 
                    u + 
                    ", is ignored, it not a file or a jar file");            
        }
    }
    
    private void indexDir(File root, boolean indexJars) {
        for (File child : root.listFiles()) {
            if (child.isDirectory()) {
                indexDir(child, indexJars);
            } else if (indexJars && child.getName().endsWith(".jar")) {
                indexJar(child);
            } else if (child.getName().endsWith(".class")) {
                analyzeClassFile(child.toURI());
            }
        }        
    }
    
    private void indexJar(File file) {
        indexJar(file, "");
    }

    private void indexJar(File file, String parent) {
        final JarFile jar = getJarFile(file);
        final Enumeration<JarEntry> entries = jar.entries();
        final String jarBase = "jar:" + file.toURI() + "!/";
        while (entries.hasMoreElements()) {
            JarEntry e = entries.nextElement();
            if (!e.isDirectory() && e.getName().startsWith(parent) && 
                    e.getName().endsWith(".class")) {
                analyzeClassFile(URI.create(jarBase + e.getName()));
            }
        }
    }
    
    private JarFile getJarFile(File file) {
        try {
            return new JarFile(file);
        } catch (IOException ex) {
            String s = "File, " + 
                    file.getAbsolutePath() +
                    ", is not a jar file";
            LOGGER.severe(s);
            throw new RuntimeException(s, ex);
        }
    }
    
    private void analyzeClassFile(URI classFileUri) {        
        getClassReader(classFileUri).accept(classVisitor, 0);
    }
    
    private ClassReader getClassReader(URI classFileUri) {
        try {
            return new ClassReader(classFileUri.toURL().openStream());
        } catch (IOException ex) {
            String s = "The input stream of the class file URI, " + 
                    classFileUri + 
                    ", could not be obtained";
            LOGGER.severe(s);
            throw new RuntimeException(s, ex);
        }
    }
    
    private Class getClassForName(String className) {
        try {
            return classloader.loadClass(className);
        } catch (ClassNotFoundException ex) {
            String s = "A (root resource) class file of the class name, " + 
                    className + 
                    "is identified but the class could not be loaded";
            LOGGER.severe(s);
            throw new RuntimeException(s, ex);
        }
    }
    
    private final ResourceClassVisitor classVisitor = new ResourceClassVisitor();
    
    private final class ResourceClassVisitor implements ClassVisitor {
        /**
         * The name of the visited class.
         */
        private String className;
        
        /**
         * True if the class has the correct scope
         */
        private boolean isScoped;        
        
        /**
         * True if the class has the correct declared annotations
         */
        private boolean isAnnotated;
        
        public void visit(int version, int access, String name, 
                String signature, String superName, String[] interfaces) {
            className = name;
            isScoped = (access & Opcodes.ACC_PUBLIC) != 0;
            isAnnotated = false;
        }
        
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            isAnnotated |= annotations.contains(desc);
            return null;
        }
        
        public void visitInnerClass(String name, String outerName, 
                String innerName, int access) {
            // If the name of the class that was visited is equal
            // to the name of this visited inner class then
            // this access field needs to be used for checking the scope
            // of the inner class
            if (className.equals(name)) {
                isScoped = (access & Opcodes.ACC_PUBLIC) != 0;
                
                // Inner classes need to be statically scoped
                isScoped &= (access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC;
            }
        }
        
        public void visitEnd() {
            if (isScoped && isAnnotated) {
                // Correctly scoped and annotated
                // add to list of root resource classes.
                classes.add(getClassForName(className.replaceAll("/", ".")));    
            }
        }
        
        
        public void visitOuterClass(String string, String string0, 
                String string1) {
            // Do nothing
        }
        
        public FieldVisitor visitField(int i, String string, 
                String string0, String string1, Object object) {
            // Do nothing
            return null;
        }
        
        public void visitSource(String string, String string0) {
            // Do nothing
        }
        
        public void visitAttribute(Attribute attribute) {
            // Do nothing
        }
        
        public MethodVisitor visitMethod(int i, String string, 
                String string0, String string1, String[] string2) {
            // Do nothing
            return null;
        }        
    };
}
