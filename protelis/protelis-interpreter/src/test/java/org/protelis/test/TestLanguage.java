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
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import java8.util.stream.IntStream;
import java8.util.stream.IntStreams;

/**
 * Main collection of tests for the Protelis language and VM.
 */
public class TestLanguage {


    /**
     * Test the alignedMap construct.
     */
    @Test
    public void testAlignedMap() {
        ProgramTester.testFileWithMultipleRuns("/alignedMap.pt");
    }

    /**
     * Test closures.
     */
    @Test
    public void testClosure01() {
        ProgramTester.testFile("/closure01.pt");
    }

    /**
     * Test the cyclic timer.
     */
    @Test
    public void testCyclicTimer() {
        ProgramTester.testFileWithMultipleRuns("/cyclicTimer.pt");
    }

    /**
     * Test Boolean logic operators.
     */
    @Test
    public void testBinary01() {
        ProgramTester.testFile("/binary01.pt");
    }

    /**
     * Test putting and getting of environment variables.
     */
    @Test
    public void testEnvironment01() {
        ProgramTester.testFile("/environment01.pt");
    }

    /**
     * Smoke test of basic dynamic program evaluation.
     */
    @Test
    public void testEval01() {
        ProgramTester.testFile("/eval01.pt");
    }

    /**
     * Test of dynamic evaluation of a complex program, including function
     * definitions.
     */
    @Test
    public void testEval02() {
        ProgramTester.testFile("/eval02.pt");
    }

    /**
     * Exercise method calls on fields.
     */
    @Test
    public void testFieldMethod01() {
        ProgramTester.testFile("/fieldMethod01.pt");
    }

    /**
     * Exercise method calls on fields.
     */
    @Test
    public void testFieldMethod02() {
        ProgramTester.testFile("/fieldMethod02.pt");
    }

    /**
     * Exercise method calls on fields.
     */
    @Test
    public void testFieldMethod03() {
        ProgramTester.testFile("/fieldMethod03.pt");
    }

    /**
     * Test simple function call with no arguments.
     */
    @Test
    public void testFunction01() {
        ProgramTester.testFile("/function01.pt");
    }

    /**
     * Test simple function call with one argument.
     */
    @Test
    public void testFunction02() {
        ProgramTester.testFile("/function02.pt");
    }

    /**
     * Test simple function call with two arguments.
     */
    @Test
    public void testFunction03() {
        ProgramTester.testFile("/function03.pt");
    }

    /**
     * Test to make sure that more than one instance of a function can be active
     * at a time.
     */
    @Test
    public void testFunction04() {
        ProgramTester.testFile("/function04.pt");
    }

    /**
     * Test hood with a lambda function.
     */
    @Test
    public void testGenericHood01() {
        ProgramTester.testFile("/genericHood01.pt");
    }

    /**
     * Test hoodPlusSelf with a function reference.
     */
    @Test
    public void testGenericHood02() {
        ProgramTester.testFile("/genericHood02.pt");
    }

    /**
     * Test hoodPlusSelf with a Java method reference.
     */
    @Test
    public void testGenericHood03() {
        ProgramTester.testFile("/genericHood03.pt");
    }

    /**
     * Test hood with a generated field.
     */
    @Test
    public void testGenericHood04() {
        ProgramTester.testFile("/genericHood04.pt");
    }

    /**
     * Test hood with a generated field.
     */
    @Test
    public void testGenericHood05() {
        ProgramTester.testFile("/genericHood05.pt");
    }

    /**
     * Test hood with a generated field.
     */
    @Test
    public void testGenericHood06() {
        ProgramTester.testFile("/genericHood06.pt");
    }

    /**
     * Test hood with a generated field.
     */
//    @Test
//    public void testGenericHood07() {
//        ProgramTester.testFile("/genericHood07.pt");
//    }

    /**
     * Test simple use of apply.
     */
    @Test
    public void testHof01() {
        ProgramTester.testFileWithMultipleRuns("/hof01.pt");
    }

    /**
     * Test apply on a more complex function.
     */
    @Test
    public void testHof02() {
        ProgramTester.testFileWithMultipleRuns("/hof02.pt");
    }

    /**
     * Test to make sure that each apply call bounds to a different instance of
     * the function.
     */
    @Test
    public void testHof03() {
        ProgramTester.testFileWithMultipleRuns("/hof03.pt");
    }

    /**
     * Test using apply to define a higher-order map function.
     */
    @Test
    public void testHof04() {
        ProgramTester.testFile("/hof04.pt");
    }

    /**
     * Test multiple applications of a higher-order map function.
     */
    @Test
    public void testHof05() {
        ProgramTester.testFile("/hof05.pt");
    }

    /**
     * Test that plain hood functions don't include local value.
     */
    @Test
    public void testHood01() {
        ProgramTester.testFile("/hood01.pt");
    }

    /**
     * Test that PlusSelf hood functions do include local value.
     */
    @Test
    public void testHood02() {
        ProgramTester.testFile("/hood02.pt");
    }

    /**
     * Test a more complex hood function.
     */
    @Test
    public void testHood03() {
        ProgramTester.testFile("/hood03.pt");
    }

    /**
     * Test operation of "if" restrictive branching.
     */
    @Test
    public void testIf01() {
        ProgramTester.testFileWithMultipleRuns("/if01.pt");
    }

    /**
     * Make sure if throws exceptions instead of returning fields.
     */
    @Test
    public void testIf02() {
        try {
            ProgramTester.testFile("/if02.pt", 1, null);
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
        ProgramTester.testFile("/lambda01.pt");
    }

    /**
     * Test a complex passing and application of lambdas, including an if.
     */
    @Test
    public void testLambda02() {
        ProgramTester.testFileWithMultipleRuns("/lambda02.pt");
    }

    /**
     * Test a complex passing and application of lambdas, including a mux.
     */
    @Test
    public void testLambda03() {
        ProgramTester.testFileWithMultipleRuns("/lambda03.pt");
    }

    /**
     * Test the ability to load a module by name.
     */
    @Test
    public void testLoadModule01() {
        assertNotNull(ProtelisLoader.parse("loadmodule01"));
    }

    /**
     * Test constants: -Infinity.
     */
    @Test
    public void testMath01() {
        ProgramTester.testFile("/math01.pt");
    }

    /**
     * Test constants: -3.
     */
    @Test
    public void testMath02() {
        ProgramTester.testFile("/math02.pt");
    }

    /**
     * Test arithmetic: addition.
     */
    @Test
    public void testMath03() {
        ProgramTester.testFile("/math03.pt");
    }

    /**
     * Test arithmetic: equality.
     */
    @Test
    public void testMath04() {
        ProgramTester.testFile("/math04.pt");
    }

    /**
     * Test fully-qualified call of individually imported static Java method.
     */
    @Test
    public void testMethod01() {
        ProgramTester.testFile("/method01.pt");
    }

    /**
     * Test unqualified call of individually imported static Java method.
     */
    @Test
    public void testMethod02() {
        ProgramTester.testFileWithExplicitResult("/method02.pt", Collections.EMPTY_LIST);
    }

    /**
     * Test unqualified call of batch-imported static Java methods.
     */
    @Test
    public void testMethod03() {
        ProgramTester.testFile("/method03.pt");
    }

    /**
     * Test "dot" call of non-static Java methods.
     */
    @Test
    public void testMethod04() {
        ProgramTester.testFile("/method04.pt");
    }

    /**
     * Confirm that qualified and unqualified methods are equal.
     */
    @Test
    public void testMethod05() {
        ProgramTester.testFile("/method05.pt");
    }

    /**
     * Ensure that double values are coerced to integers when required.
     */
    @Test
    public void testMethod06() {
        ProgramTester.testFile("/method06.pt");
    }

    /**
     * Ensure that doubles are converted to int if needed.
     */
    @Test
    public void testMethod07() {
        ProgramTester.testFile("/method07.pt");
    }

    /**
     * Test showing that when unqualified imported Protelis method names
     * conflict, first imported shadows later imports.
     */
    @Test
    public void testModules01() {
        ProgramTester.testFile("/modules01.pt");
    }

    /**
     * Confirm that local definitions shadow imported Protelis definitions.
     */
    @Test
    public void testModules02() {
        ProgramTester.testFile("/modules02.pt");
    }

    /**
     * Test that shadowed imported Protelis functions can still be called via
     * their fully qualified names.
     */
    @Test
    public void testModules03() {
        ProgramTester.testFile("/modules03.pt");
    }

    /**
     * Test that module imports can handle circular references between modules.
     */
    @Test
    public void testModules04() {
        ProgramTester.testFile("/modules04.pt");
    }

    /**
     * Test that imported modules can import other modules in different
     * packages.
     */
    @Test
    public void testModules05() {
        ProgramTester.testFile("/modules05.pt");
    }

    /**
     * Test operation of "mux" inclusive branching.
     */
    @Test
    public void testMux01() {
        ProgramTester.testFileWithMultipleRuns("/mux01.pt");
    }

    /**
     * Test two statement sequence.
     */
    @Test
    public void testMultiStatement01() {
        ProgramTester.testFile("/multistatement01.pt");
    }

    /**
     * Test multiple variable re-assignments.
     */
    @Test
    public void testMultiStatement02() {
        ProgramTester.testFile("/multistatement02.pt");
    }

    /**
     * Test independence of sequential invocations of a function.
     */
    @Test
    public void testMultiStatement03() {
        ProgramTester.testFile("/multistatement03.pt");
    }

    /**
     * Test assignment within nested lexical scope.
     */
    @Test
    public void testMultiStatement04() {
        ProgramTester.testFile("/multistatement04.pt");
    }

    /**
     * Test rep via a canonical use: creating a counter.
     */
    @Test
    public void testRep01() {
        ProgramTester.testFileWithMultipleRuns("/rep01.pt", IntStreams.range(0, 4).map(i -> (int) Math.round(Math.pow(10, i))));
    }

    /**
     * Test nested rep statements.
     */
    @Test
    public void testRep02() {
        double prev = 1;
        for (int i = 1; i < 100; i++) {
            ProgramTester.testFile("/rep02.pt", i, prev);
            prev = prev * (prev + 1);
        }
    }

    /**
     * Test infix addition.
     */
    @Test
    public void testSum() {
        ProgramTester.testFile("/sum.pt");
    }

    /**
     * Test loading of a file from name without explicit classpath statement.
     */
    @Test
    public void testLoadFile() {
        ProgramTester.testFile("/sum.pt");
    }

    /**
     * Test loading of a file with explicit classpath statement.
     */
    @Test
    public void testLoadFromClasspath() {
        ProgramTester.testFileWithExplicitResult("classpath:/sum.pt", 8d);
    }

    /**
     * Test parsing of anonymous expression.
     */
    @Test
    public void testLoadModule() {
        ProgramTester.testFileWithExplicitResult("5+3", 8d);
    }

    /**
     * Test import of a module as part of an anonymous expression.
     */
    @Test
    public void testAnonymousLoadModule() {
        ProgramTester.testFileWithExplicitResult("import protelis:test:circular02\nfun3()", 1d);
    }

    /**
     * Test loading from a module name with default package.
     */
    @Test
    public void testLoadFromModuleName01() {
        ProgramTester.testFileWithExplicitResult("modules04", 1d);
    }

    /**
     * Test loading from a module name from a nested package.
     */
    @Test
    public void testLoadFromModuleName02() {
        ProgramTester.testFileWithExplicitResult("protelis:test:circular02", 1d);
    }

    /**
     * Test loading from a module name from a nested package.
     */
    @Test
    public void testLoadFromModuleName03() {
        assertNotNull(ProtelisLoader.parse("replicatedgossip"));
    }

    /**
     * Test construction of tuple using '[]' syntax.
     */
    @Test
    public void testTuple01() {
        final Tuple expectedResult = DatatypeFactory.createTuple(new Object[] { 5.0, 4.0, 3.0, 2.0, 1.0, 0.0 });
        ProgramTester.testFileWithExplicitResult("/tuple01.pt", expectedResult);
    }

    /**
     * Test construction of tuple with some elements computed rather than inline
     * constants.
     */
    @Test
    public void testTuple02() {
        ProgramTester.testFile("/tuple02.pt");
    }

    /**
     * Test calling of tuple methods, in particular Tuple.size().
     */
    @Test
    public void testTuple03() {
        ProgramTester.testFileWithExplicitResult("/tuple03.pt", Integer.valueOf(3));
    }

    /**
     * Test the Tuple.indexof method.
     */
    @Test
    public void testTuple04() {
        ProgramTester.testFile("/tuple04.pt");
    }

    /**
     * Test the Tuple.fill method.
     */
    @Test
    public void testTuple05() {
        final Tuple expectedResult = DatatypeFactory.createTuple(new Object[] { 2.0, 2.0, 2.0 });
        ProgramTester.testFileWithExplicitResult("/tuple05.pt", expectedResult);
    }

    /**
     * Test the tuple arithmetic.
     */
    @Test
    public void testTupleArithmetic() {
        ProgramTester.testFile("/tuple06.pt");
        ProgramTester.testFile("/tuple07.pt");
        ProgramTester.testFile("/tuple08.pt");
    }

    /**
     * Test the Tuple.map method.
     */
    @Test
    public void testTupleMap01() {
        ProgramTester.testFile("/TupleMap01.pt");
    }

    /**
     * Test the Tuple.reduce method.
     */
    @Test
    public void testTupleReduce01() {
        ProgramTester.testFile("/TupleReduce01.pt");
    }

    /**
     * Test the Tuple.filter method.
     */
    @Test
    public void testTupleFilter01() {
        ProgramTester.testFile("/TupleFilter01.pt");
    }

    /**
     * Test the unary '!' operator.
     */
    @Test
    public void testUnary01() {
        ProgramTester.testFile("/unary01.pt");
    }

    /**
     * Test the unary '-' operator.
     */
    @Test
    public void testUnary02() {
        ProgramTester.testFileWithExplicitResult("/unary02.pt", -Math.PI);
    }

}
