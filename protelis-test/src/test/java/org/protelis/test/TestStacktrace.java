/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.test;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.protelis.lang.interpreter.util.ProtelisRuntimeException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests of stack traces.
 */
final class TestStacktrace {
    private static final Pattern EXCEPTION_FORMAT_OPENJ9 = Pattern.compile(".*Tuple.*incompatible\\swith.*Field.*");

    /**
     * Test error in the main script.
     */
    @Test
    void testErrorInMainModule() {
        ProgramTester.runExpectingErrors(
            "/errorTrace01.pt",
            ProtelisRuntimeException.class,
            e ->
                    assertTrue(
                        e.toString().contains("main script"),
                        "Exception does not include main script identification"
                    )
        );
    }

    /**
     * Test stacktrace in a chained call.
     */
    @Test
    void testErrorTraceModule() {
        ProgramTester.runExpectingErrors("/errorTrace02.pt", ProtelisRuntimeException.class, e -> {
            final String fullTrace = e.toString();
            assertTrue(fullTrace.contains("line"), "Exception does not identify line numbers\n" + fullTrace);
            final List<String> functions = ImmutableList.of(
                "errorTrace02:rootError",
                "errorTrace02:aCall",
                "errorTrace02:anotherCall"
            );
            for (final String function : functions) {
                assertTrue(
                    fullTrace.contains(function),
                    "Exception does not identify function name " + function + '\n' + fullTrace
                );
            }
        });
    }

    /**
     * Test issue #231.
     */
    @Test
    void testRuntimeErrorOnClassCastFailure() {
        ProgramTester.runExpectingErrors("minHood([])", ProtelisRuntimeException.class, e -> {
            final String message = e.getMessage();
            assertNotNull(message);
            final Matcher openJ9Exception = EXCEPTION_FORMAT_OPENJ9.matcher(message);
            assertTrue(
                message.contains("cannot be cast") || openJ9Exception.find(),
                "Exception does not include type cast failure indication\n" + message
            );
        });
    }

    /**
     * Test issue #257.
     */
    @Test
    void testRuntimeErrorOnNonExistingSelfMethod() {
        ProgramTester.runExpectingErrors("self.getDcopInfoProvider()", ProtelisRuntimeException.class, e -> {
            assertNotNull(e.getMessage());
            final String fullTrace = e.toString();
            assertTrue(
                fullTrace.contains("Fully detailed interpreter trace"),
                "Exception does not Protelis stacktrace\n" + fullTrace
            );
        });
    }
}
