/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package org.protelis.test; // NOPMD by jakebeal on 8/25/15 12:41 PM

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.danilopianini.io.FileUtilities;
import org.danilopianini.lang.LangUtils;
import org.junit.Test;
import org.protelis.lang.ProtelisLoader;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ProtelisProgram;
import org.protelis.vm.ProtelisVM;
import org.protelis.vm.impl.AbstractExecutionContext;
import org.protelis.vm.impl.DummyContext;
import org.protelis.vm.impl.MyDummyEnvironment;

import java8.util.stream.IntStream;
import java8.util.stream.IntStreams;

/**
 * Main collection of tests for the Protelis language and VM.
 */
public class TestApis {

    private static final String SL_NAME = "singleLineComment";
    private static final String ML_NAME = "multilineComment";
    private static final String EXPECTED = "EXPECTED_RESULT:";
    private static final Pattern EXTRACT_RESULT = Pattern.compile(
            ".*?" + EXPECTED + "\\s*(?<" + ML_NAME
            + ">.*?)\\s*\\*\\/|\\/\\/\\s*" + EXPECTED + "\\s*(?<"
            + SL_NAME + ">.*?)\\s*\\n", Pattern.DOTALL);
    private static final Pattern CYCLE = Pattern.compile("\\$CYCLE");
    private static final int MIN_CYCLE_NUM = 1;
    private static final int MAX_CYCLE_NUM = 100;


    /**
     * Test distance to.
     */
    /*@Test
    public void testDistanceTo() {
        final MyDummyEnvironment env = MyDummyEnvironment.build();
        env.getNodeEnvironment(1).put("source", true);
        testFileWithExplicitResult("distanceTo", 1, 0, MAX_CYCLE_NUM, env.getExecutionContexts());
    }*/


    /*
     * From this point the rest of the file is not tests, but utility methods
     */

    private Object testFileWithExplicitResult(String s, int nodeId, Object result, int runs, ExecutionContext[] executionContexts) {
        final ProtelisProgram program = ProtelisLoader.parse(s);
        try {
            FileUtilities.serializeObject(program);
        } catch (Exception e) {
            fail();
        }
        final int length = executionContexts.length;
        final ProtelisVM[] vms = new ProtelisVM[length];
        for (Integer i = 0; i < length; i++) {
            vms[i] = new ProtelisVM(program, executionContexts[i]);
        }
        
        for (int j = 0; j < runs; j++) {
            for (int i = 0; i < length; i++) {
                vms[i].runCycle();
            }
        }
        return vms[nodeId].getCurrentValue();
    }


    private static void testFileWithExplicitResult(final String file, final Object expectedResult) {
        testFile(file, 1, expectedResult, null);
    }
    
    private static void testFileWithExplicitResult(final String file, final Object expectedResult, final ExecutionContext ctx) {
        testFile(file, 1, expectedResult, ctx);
    }

    private static void testFile(final String file) {
        testFile(file, 1);
    }

    private static void testFileWithMultipleRuns(final String file) {
        testFileWithMultipleRuns(file, MIN_CYCLE_NUM, MAX_CYCLE_NUM);
    }

    private static void testFileWithMultipleRuns(final String file, final int min, final int max) {
        testFileWithMultipleRuns(file, IntStreams.rangeClosed(min, max));
    }

    private static void testFileWithMultipleRuns(final String file, final IntStream stream) {
        stream.forEach(i -> {
            testFile(file, i);
        });
    }

    private static void testFile(final String file, final int runs) {
        testFile(file, runs, null);
    }

    private static void testFile(final String file, final int runs, final AbstractExecutionContext ctx) {
        final AbstractExecutionContext c = ctx == null ? new DummyContext() : ctx;
        final Object execResult = runProgram(file, runs);
        final InputStream is = TestApis.class.getResourceAsStream(file);
        try {
            final String test = IOUtils.toString(is, StandardCharsets.UTF_8);
            final Matcher extractor = EXTRACT_RESULT.matcher(test);
            if (extractor.find()) {
                String result = extractor.group(ML_NAME);
                if (result == null) {
                    result = extractor.group(SL_NAME);
                }
                final String toCheck = CYCLE.matcher(result).replaceAll(Integer.toString(runs));
                final ProtelisVM vm = new ProtelisVM(ProtelisLoader.parse(toCheck), c);
                vm.runCycle();
                assertEquals(vm.getCurrentValue(), execResult instanceof Number
                        ? ((Number) execResult).doubleValue()
                        : execResult);
            } else {
                fail("Your test does not include the expected result");
            }
        } catch (IOException e) {
            fail(LangUtils.stackTraceToString(e));
        }
    }
    
    private static void testFile(final String file, final int runs, final Object expectedResult) {
        assertEquals(expectedResult, runProgram(file, runs));
    }

    private static void testFile(final String file, final int runs, final Object expectedResult, final ExecutionContext ctx) {
        assertEquals(expectedResult, runProgram(file, runs, ctx));
    }
    
    private static Object runProgram(final String s, final int runs) {
        return runProgram(s, runs, null);
    }
    
    private static Object runProgram(final String s, final int runs, final ExecutionContext ctx) {
        final ExecutionContext c = ctx == null ? new DummyContext() : ctx;
        final ProtelisProgram program = ProtelisLoader.parse(s);
        try {
            FileUtilities.serializeObject(program);
        } catch (Exception e) {
            fail();
        }
        final ProtelisVM vm = new ProtelisVM(program, c);
        for (int i = 0; i < runs; i++) {
            vm.runCycle();
        }
        return vm.getCurrentValue();
    }
}
