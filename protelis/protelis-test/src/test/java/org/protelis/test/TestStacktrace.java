package org.protelis.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.protelis.lang.ProtelisRuntimeException;

import com.google.common.collect.ImmutableList;

/**
 * Tests of stack traces.
 */
public final class TestStacktrace {
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
            assertTrue("Exception does not include argument type mismatch identification\n" + fullTrace,
                    e.getMessage().contains("argument type mismatch"));
            assertTrue("Exception does not identify line numbers\n" + fullTrace, fullTrace.contains("line"));
            for (final String function : ImmutableList.of("errorTrace02:rootError", "errorTrace02:aCall", "errorTrace02:anotherCall")) {
                assertTrue("Exception does not identify function name " + function + '\n' + fullTrace,
                        fullTrace.contains(function));
            }
        });
    }

}
