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
        try (ScanResult scanResult = new ClassGraph().whitelistPathsNonRecursive("protelis/option").scan()) {
            final ResourceList programs = scanResult.getResourcesWithExtension("pt");
            final ResourceList exceptions = programs.filter(it -> it.getPath().contains("error"));
            final ResourceList regular = programs.filter(it -> !exceptions.contains(it));
            regular.forEach(it -> ProgramTester.runFile('/' + it.getPath()));
            exceptions.forEach(it -> ProgramTester.runExpectingErrors('/' + it.getPath(), Throwable.class));
        }
    }

    /**
     * @param o must be null
     */
    public static void expectsNull(final Object o) {
        assertNull(o);
    }

    /**
     * 
     */
    public static void returnsVoid() {
    }

    /**
     * @return null
     */
    public String returnsNull() {
        return null;
    }

    /**
     * @return with 50% probability null, with 50% probability the "notNull" String.
     */
    public static String mayReturnNull() {
        return Math.random() > 0.5 ? null : "notNull";
    }

}
