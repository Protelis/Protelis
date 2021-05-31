/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.test;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.protelis.lang.interpreter.util.ProtelisRuntimeException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests of stack traces.
 */
public final class TestStacktrace {
    private static final Pattern EXCEPTION_FORMAT_OPENJ9 = Pattern.compile(".*Tuple.*incompatible\\swith.*Field.*");
    /**
     * Test error in main script.
     */
    @Test
    public void testErrorInMainModule() {
        ProgramTester.runExpectingErrors("/errorTrace01.pt", ProtelisRuntimeException.class, e -> {
            assertTrue("Exception does not include main script identification", e.toString().contains("main script"));
        });
    }
    /**
     * Test stacktrace in chained call.
     */
    @Test
    public void testErrorTraceModule() {
        ProgramTester.runExpectingErrors("/errorTrace02.pt", ProtelisRuntimeException.class, e -> {
            final String fullTrace = e.toString();
            assertTrue("Exception does not identify line numbers\n" + fullTrace, fullTrace.contains("line"));
            for (final String function : ImmutableList.of("errorTrace02:rootError", "errorTrace02:aCall", "errorTrace02:anotherCall")) {
                assertTrue("Exception does not identify function name " + function + '\n' + fullTrace,
                        fullTrace.contains(function));
            }
        });
    }

    /**
     * Test issue #231.
     */
    @Test
    public void testRuntimeErrorOnClassCastFailure() {
        ProgramTester.runExpectingErrors("minHood([])", ProtelisRuntimeException.class, e -> {
            final String message = e.getMessage();
            assertNotNull(message);
            final Matcher openJ9Exception = EXCEPTION_FORMAT_OPENJ9.matcher(message);
            assertTrue("Exception does not include type cast failure indication\n" + message,
                    message.contains("cannot be cast")
                    || openJ9Exception.find());
        });
    }

    /**
     * Test issue #257.
     */
    @Test
    public void testRuntimeErrorOnNonExistingSelfMethod() {
        ProgramTester.runExpectingErrors("self.getDcopInfoProvider()", ProtelisRuntimeException.class, e -> {
            assertNotNull(e.getMessage());
            final String fullTrace = e.toString();
            assertTrue("Exception does not Protelis stacktrace\n" + fullTrace,
                    fullTrace.contains("Fully detailed interpreter trace"));
        });
    }
}

