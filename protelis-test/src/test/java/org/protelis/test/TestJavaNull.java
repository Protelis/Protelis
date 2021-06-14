/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.test;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ResourceList;
import io.github.classgraph.ScanResult;

/**
 * Tests for Option and null.
 */
public final class TestJavaNull {

    /**
     * Runs all tests in protelis/option.
     */
    @Test
    public void testProtelisOption() {
        try (ScanResult scanResult = new ClassGraph().acceptPathsNonRecursive("protelis/option").scan()) {
            final ResourceList programs = scanResult.getResourcesWithExtension("pt"); // NOPMD
            final ResourceList exceptions = programs.filter(it -> it.getPath().contains("error")); // NOPMD
            final ResourceList regular = programs.filter(it -> !exceptions.contains(it)); // NOPMD
            regular.forEach(it -> ProgramTester.runFile('/' + it.getPath()));
            exceptions.forEach(it -> ProgramTester.runExpectingErrors('/' + it.getPath(), Throwable.class));
        }
    }

    /**
     * @param o must be null
     * @return null
     */
    public static Object expectsNull(final Object o) {
        assertNull(o);
        return o;
    }

    /**
     * 
     */
    public static void returnsVoid() {
    }

    /**
     * @return null
     */
    public static String returnsNull() {
        return null;
    }

    /**
     * @return with 50% probability null, with 50% probability the "notNull" String.
     */
    public static String maybeNull() {
        return Math.random() > 0.5 ? null : "notNull";
    }

}
