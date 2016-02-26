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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java8.util.stream.IntStream;
import java8.util.stream.IntStreams;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.danilopianini.io.FileUtilities;
import org.danilopianini.lang.LangUtils;
import org.junit.Test;
import org.protelis.lang.ProtelisLoader;
import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.Tuple;
import org.protelis.vm.ProtelisProgram;
import org.protelis.vm.ProtelisVM;
import org.protelis.vm.impl.DummyContext;

/**
 * Main collection of tests for the Protelis language and VM.
 */
public class TestLanguage {

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
     * Test the alignedMap construct.
     */
    @Test
    public void testAlignedMap() {
        testFileWithMultipleRuns("/alignedMap.pt");
    }

    /**
     * Test closures.
     */
    @Test
    public void testClosure01() {
        testFile("/closure01.pt");
    }

    /**
     * Test the cyclic timer.
     */
    @Test
    public void testCyclicTimer() {
        testFileWithMultipleRuns("/cyclicTimer.pt");
    }

    /**
     * Test Boolean logic operators.
     */
    @Test
    public void testBinary01() {
        testFile("/binary01.pt");
    }

    /**
     * Test putting and getting of environment variables.
     */
    @Test
    public void testEnvironment01() {
        testFile("/environment01.pt");
    }

    /**
     * Smoke test of basic dynamic program evaluation.
     */
    @Test
    public void testEval01() {
        testFile("/eval01.pt");
    }

    /**
     * Test of dynamic evaluation of a complex program, including function
     * definitions.
     */
    @Test
    public void testEval02() {
        testFile("/eval02.pt");
    }

    /**
     * Exercise method calls on fields.
     */
    @Test
    public void testFieldMethod01() {
        testFile("/fieldMethod01.pt");
    }

    /**
     * Exercise method calls on fields.
     */
    @Test
    public void testFieldMethod02() {
        testFile("/fieldMethod02.pt");
    }

    /**
     * Exercise method calls on fields.
     */
    @Test
    public void testFieldMethod03() {
        testFile("/fieldMethod03.pt");
    }

    /**
     * Test simple function call with no arguments.
     */
    @Test
    public void testFunction01() {
        testFile("/function01.pt");
    }

    /**
     * Test simple function call with one argument.
     */
    @Test
    public void testFunction02() {
        testFile("/function02.pt");
    }

    /**
     * Test simple function call with two arguments.
     */
    @Test
    public void testFunction03() {
        testFile("/function03.pt");
    }

    /**
     * Test to make sure that more than one instance of a function can be active
     * at a time.
     */
    @Test
    public void testFunction04() {
        testFile("/function04.pt");
    }

    /**
     * Test hood with a lambda function.
     */
    @Test
    public void testGenericHood01() {
        testFile("/genericHood01.pt");
    }

    /**
     * Test hoodPlusSelf with a function reference.
     */
    @Test
    public void testGenericHood02() {
        testFile("/genericHood02.pt");
    }

    /**
     * Test hoodPlusSelf with a Java method reference.
     */
    @Test
    public void testGenericHood03() {
        testFile("/genericHood03.pt");
    }

    /**
     * Test hood with a generated field.
     */
    @Test
    public void testGenericHood04() {
        testFile("/genericHood04.pt");
    }

    /**
     * Test hood with a generated field.
     */
    @Test
    public void testGenericHood05() {
        testFile("/genericHood05.pt");
    }

    /**
     * Test hood with a generated field.
     */
    @Test
    public void testGenericHood06() {
        testFile("/genericHood06.pt");
    }

    /**
     * Test simple use of apply.
     */
    @Test
    public void testHof01() {
        testFileWithMultipleRuns("/hof01.pt");
    }

    /**
     * Test apply on a more complex function.
     */
    @Test
    public void testHof02() {
        testFileWithMultipleRuns("/hof02.pt");
    }

    /**
     * Test to make sure that each apply call bounds to a different instance of
     * the function.
     */
    @Test
    public void testHof03() {
        testFileWithMultipleRuns("/hof03.pt");
    }

    /**
     * Test using apply to define a higher-order map function.
     */
    @Test
    public void testHof04() {
        testFile("/hof04.pt");
    }

    /**
     * Test multiple applications of a higher-order map function.
     */
    @Test
    public void testHof05() {
        testFile("/hof05.pt");
    }

    /**
     * Test that plain hood functions don't include local value.
     */
    @Test
    public void testHood01() {
        testFile("/hood01.pt");
    }

    /**
     * Test that PlusSelf hood functions do include local value.
     */
    @Test
    public void testHood02() {
        testFile("/hood02.pt");
    }

    /**
     * Test a more complex hood function.
     */
    @Test
    public void testHood03() {
        testFile("/hood03.pt");
    }

    /**
     * Test operation of "if" restrictive branching.
     */
    @Test
    public void testIf01() {
        testFileWithMultipleRuns("/if01.pt");
    }

    /**
     * Make sure if throws exceptions instead of returning fields.
     */
    @Test
    public void testIf02() {
        try {
            testFile("/if02.pt", 1, null);
            fail("If should never return fields");
        } catch (IllegalStateException e) {
            assertNotNull(e);
        }
    }

    /**
     * Test a simple anonymous function inline definition and application.
     */
    @Test
    public void testLambda01() {
        testFile("/lambda01.pt");
    }

    /**
     * Test a complex passing and application of lambdas, including an if.
     */
    @Test
    public void testLambda02() {
        testFileWithMultipleRuns("/lambda02.pt");
    }

    /**
     * Test a complex passing and application of lambdas, including a mux.
     */
    @Test
    public void testLambda03() {
        testFileWithMultipleRuns("/lambda03.pt");
    }

    /**
     * Test constants: -Infinity.
     */
    @Test
    public void testMath01() {
        testFile("/math01.pt");
    }

    /**
     * Test constants: -3.
     */
    @Test
    public void testMath02() {
        testFile("/math02.pt");
    }

    /**
     * Test arithmetic: addition.
     */
    @Test
    public void testMath03() {
        testFile("/math03.pt");
    }

    /**
     * Test arithmetic: equality.
     */
    @Test
    public void testMath04() {
        testFile("/math04.pt");
    }

    /**
     * Test fully-qualified call of individually imported static Java method.
     */
    @Test
    public void testMethod01() {
        testFile("/method01.pt");
    }

    /**
     * Test unqualified call of individually imported static Java method.
     */
    @Test
    public void testMethod02() {
        testFileWithExplicitResult("/method02.pt", Collections.EMPTY_LIST);
    }

    /**
     * Test unqualified call of batch-imported static Java methods.
     */
    @Test
    public void testMethod03() {
        testFile("/method03.pt");
    }

    /**
     * Test "dot" call of non-static Java methods.
     */
    @Test
    public void testMethod04() {
        testFile("/method04.pt");
    }

    /**
     * Confirm that qualified and unqualified methods are equal.
     */
    @Test
    public void testMethod05() {
        testFile("/method05.pt");
    }

    /**
     * Test showing that when unqualified imported Protelis method names
     * conflict, first imported shadows later imports.
     */
    @Test
    public void testModules01() {
        testFile("/modules01.pt");
    }

    /**
     * Confirm that local definitions shadow imported Protelis definitions.
     */
    @Test
    public void testModules02() {
        testFile("/modules02.pt");
    }

    /**
     * Test that shadowed imported Protelis functions can still be called via
     * their fully qualified names.
     */
    @Test
    public void testModules03() {
        testFile("/modules03.pt");
    }

    /**
     * Test that module imports can handle circular references between modules.
     */
    @Test
    public void testModules04() {
        testFile("/modules04.pt");
    }

    /**
     * Test that imported modules can import other modules in different
     * packages.
     */
    @Test
    public void testModules05() {
        testFile("/modules05.pt");
    }

    /**
     * Test operation of "mux" inclusive branching.
     */
    @Test
    public void testMux01() {
        testFileWithMultipleRuns("/mux01.pt");
    }

    /**
     * Test two statement sequence.
     */
    @Test
    public void testMultiStatement01() {
        testFile("/multistatement01.pt");
    }

    /**
     * Test multiple variable re-assignments.
     */
    @Test
    public void testMultiStatement02() {
        testFile("/multistatement02.pt");
    }

    /**
     * Test independence of sequential invocations of a function.
     */
    @Test
    public void testMultiStatement03() {
        testFile("/multistatement03.pt");
    }

    /**
     * Test assignment within nested lexical scope.
     */
    @Test
    public void testMultiStatement04() {
        testFile("/multistatement04.pt");
    }

    /**
     * Test rep via a canonical use: creating a counter.
     */
    @Test
    public void testRep01() {
        testFileWithMultipleRuns("/rep01.pt", IntStreams.range(0, 4).map(i -> (int) Math.round(Math.pow(10, i))));
    }

    /**
     * Test nested rep statements.
     */
    @Test
    public void testRep02() {
        double prev = 1;
        for (int i = 1; i < 100; i++) {
            testFile("/rep02.pt", i, prev);
            prev = prev * (prev + 1);
        }
    }

    /**
     * Test infix addition.
     */
    @Test
    public void testSum() {
        testFile("/sum.pt");
    }

    /**
     * Test loading of a file from name without explicit classpath statement.
     */
    @Test
    public void testLoadFile() {
        testFile("/sum.pt");
    }

    /**
     * Test loading of a file with explicit classpath statement.
     */
    @Test
    public void testLoadFromClasspath() {
        testFileWithExplicitResult("classpath:/sum.pt", 8d);
    }

    /**
     * Test parsing of anonymous expression.
     */
    @Test
    public void testLoadModule() {
        testFileWithExplicitResult("5+3", 8d);
    }

    /**
     * Test import of a module as part of an anonymous expression.
     */
    @Test
    public void testAnonymousLoadModule() {
        testFileWithExplicitResult("import protelis:test:circular02\nfun3()", 1d);
    }

    /**
     * Test loading from a module name with default package.
     */
    @Test
    public void testLoadFromModuleName01() {
        testFileWithExplicitResult("modules04", 1d);
    }

    /**
     * Test loading from a module name from a nested package.
     */
    @Test
    public void testLoadFromModuleName02() {
        testFileWithExplicitResult("protelis:test:circular02", 1d);
    }

    /**
     * Test construction of tuple using '[]' syntax.
     */
    @Test
    public void testTuple01() {
        final Tuple expectedResult = DatatypeFactory.createTuple(new Object[] { 5.0, 4.0, 3.0, 2.0, 1.0, 0.0 });
        testFileWithExplicitResult("/tuple01.pt", expectedResult);
    }

    /**
     * Test construction of tuple with some elements computed rather than inline
     * constants.
     */
    @Test
    public void testTuple02() {
        testFile("/tuple02.pt");
    }

    /**
     * Test calling of tuple methods, in particular Tuple.size().
     */
    @Test
    public void testTuple03() {
        testFileWithExplicitResult("/tuple03.pt", Integer.valueOf(3));
    }

    /**
     * Test the Tuple.indexof method.
     */
    @Test
    public void testTuple04() {
        testFile("/tuple04.pt");
    }

    /**
     * Test the Tuple.fill method.
     */
    @Test
    public void testTuple05() {
        final Tuple expectedResult = DatatypeFactory.createTuple(new Object[] { 2.0, 2.0, 2.0 });
        testFileWithExplicitResult("/tuple05.pt", expectedResult);
    }

    /**
     * Test the Tuple.map method.
     */
    @Test
    public void testTupleMap01() {
        testFile("/TupleMap01.pt");
    }

    /**
     * Test the Tuple.reduce method.
     */
    @Test
    public void testTupleReduce01() {
        testFile("/TupleReduce01.pt");
    }

    /**
     * Test the Tuple.filter method.
     */
    @Test
    public void testTupleFilter01() {
        testFile("/TupleFilter01.pt");
    }

    /**
     * Test the unary '!' operator.
     */
    @Test
    public void testUnary01() {
        testFile("/unary01.pt");
    }

    /**
     * Test the unary '-' operator.
     */
    @Test
    public void testUnary02() {
        testFileWithExplicitResult("/unary02.pt", -Math.PI);
    }

    /*
     * From this point the rest of the file is not tests, but utility methods
     */

    private static void testFileWithExplicitResult(final String file, final Object expectedResult) {
        testFile(file, 1, expectedResult);
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
        final Object execResult = runProgram(file, runs);
        final InputStream is = TestLanguage.class.getResourceAsStream(file);
        try {
            final String test = IOUtils.toString(is, Charsets.UTF_8);
            final Matcher extractor = EXTRACT_RESULT.matcher(test);
            if (extractor.find()) {
                String result = extractor.group(ML_NAME);
                if (result == null) {
                    result = extractor.group(SL_NAME);
                }
                final String toCheck = CYCLE.matcher(result).replaceAll(Integer.toString(runs));
                final ProtelisVM vm = new ProtelisVM(ProtelisLoader.parse(toCheck), new DummyContext());
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

    private static Object runProgram(final String s, final int runs) {
        final ProtelisProgram program = ProtelisLoader.parse(s);
        try {
            FileUtilities.serializeObject(program);
        } catch (Exception e) {
            fail();
        }
        final ProtelisVM vm = new ProtelisVM(program, new DummyContext());
        for (int i = 0; i < runs; i++) {
            vm.runCycle();
        }
        return vm.getCurrentValue();
    }
}
