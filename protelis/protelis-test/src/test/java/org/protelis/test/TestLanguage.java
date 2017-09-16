/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package org.protelis.test; // NOPMD by jakebeal on 8/25/15 12:41 PM

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Assert;

import java.util.Collections;

import org.junit.Test;
import org.protelis.lang.ProtelisLoader;
import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.Tuple;

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
        ProgramTester.runFileWithMultipleRuns("/alignedMap.pt");
    }

    /**
     * Test closures.
     */
    @Test
    public void testClosure01() {
        ProgramTester.runFile("/closure01.pt");
    }

    /*
     * TODO: this test has to be introduced as soon as a complete support to
     * closures is provided
     */
    // /**
    // * Test closures.
    // */
    // @Test
    // public void testClosure02() {
    // ProgramTester.testFile("/closure02.pt");
    // }

    /**
     * Test the cyclic timer.
     */
    @Test
    public void testCyclicTimer() {
        ProgramTester.runFileWithMultipleRuns("/cyclicTimer.pt");
    }

    /**
     * Test Boolean logic operators.
     */
    @Test
    public void testBinary01() {
        ProgramTester.runFile("/binary01.pt");
    }

    /**
     * Test putting and getting of environment variables.
     */
    @Test
    public void testEnvironment01() {
        ProgramTester.runFile("/environment01.pt");
    }

    /**
     * Smoke test of basic dynamic program evaluation.
     */
    @Test
    public void testEval01() {
        ProgramTester.runFile("/eval01.pt");
    }

    /**
     * Test of dynamic evaluation of a complex program, including function
     * definitions.
     */
    @Test
    public void testEval02() {
        ProgramTester.runFile("/eval02.pt");
    }

    /**
     * Exercise method calls on fields.
     */
    @Test
    public void testFieldMethod01() {
        ProgramTester.runFile("/fieldMethod01.pt");
    }

    /**
     * Exercise method calls on fields.
     */
    @Test
    public void testFieldMethod02() {
        ProgramTester.runFile("/fieldMethod02.pt");
    }

    /**
     * Exercise method calls on fields.
     */
    @Test
    public void testFieldMethod03() {
        ProgramTester.runFile("/fieldMethod03.pt");
    }

    /**
     * Test simple function call with no arguments.
     */
    @Test
    public void testFunction01() {
        ProgramTester.runFile("/function01.pt");
    }

    /**
     * Test simple function call with one argument.
     */
    @Test
    public void testFunction02() {
        ProgramTester.runFile("/function02.pt");
    }

    /**
     * Test simple function call with two arguments.
     */
    @Test
    public void testFunction03() {
        ProgramTester.runFile("/function03.pt");
    }

    /**
     * Test to make sure that more than one instance of a function can be active
     * at a time.
     */
    @Test
    public void testFunction04() {
        ProgramTester.runFile("/function04.pt");
    }

    /**
     * Test hood with a lambda function.
     */
    @Test
    public void testGenericHood01() {
        ProgramTester.runFile("/genericHood01.pt");
    }

    /**
     * Test hoodPlusSelf with a function reference.
     */
    @Test
    public void testGenericHood02() {
        ProgramTester.runFile("/genericHood02.pt");
    }

    /**
     * Test hoodPlusSelf with a Java method reference.
     */
    @Test
    public void testGenericHood03() {
        ProgramTester.runFile("/genericHood03.pt");
    }

    /**
     * Test hood with a generated field.
     */
    @Test
    public void testGenericHood04() {
        ProgramTester.runFile("/genericHood04.pt");
    }

    /**
     * Test hood with a generated field.
     */
    @Test
    public void testGenericHood05() {
        ProgramTester.runFile("/genericHood05.pt");
    }

    /**
     * Test hood with a generated field.
     */
    @Test
    public void testGenericHood06() {
        ProgramTester.runFile("/genericHood06.pt");
    }

    /**
     * Test hood with a generated field.
     */
    // @Test
    // public void testGenericHood07() {
    // ProgramTester.testFile("/genericHood07.pt");
    // }

    /**
     * Test simple use of apply.
     */
    @Test
    public void testHof01() {
        ProgramTester.runFileWithMultipleRuns("/hof01.pt");
    }

    /**
     * Test apply on a more complex function.
     */
    @Test
    public void testHof02() {
        ProgramTester.runFileWithMultipleRuns("/hof02.pt");
    }

    /**
     * Test to make sure that each apply call bounds to a different instance of
     * the function.
     */
    @Test
    public void testHof03() {
        ProgramTester.runFileWithMultipleRuns("/hof03.pt");
    }

    /**
     * Test using apply to define a higher-order map function.
     */
    @Test
    public void testHof04() {
        ProgramTester.runFile("/hof04.pt");
    }

    /**
     * Test multiple applications of a higher-order map function.
     */
    @Test
    public void testHof05() {
        ProgramTester.runFile("/hof05.pt");
    }

    /**
     * Test that plain hood functions don't include local value.
     */
    @Test
    public void testHood01() {
        ProgramTester.runFile("/hood01.pt");
    }

    /**
     * Test that PlusSelf hood functions do include local value.
     */
    @Test
    public void testHood02() {
        ProgramTester.runFile("/hood02.pt");
    }

    /**
     * Test a more complex hood function.
     */
    @Test
    public void testHood03() {
        ProgramTester.runFile("/hood03.pt");
    }

    /**
     * Test operation of "if" restrictive branching.
     */
    @Test
    public void testIf01() {
        ProgramTester.runFileWithMultipleRuns("/if01.pt");
    }

    /**
     * Make sure if throws exceptions instead of returning fields.
     */
    @Test
    public void testIf02() {
        try {
            ProgramTester.runFile("/if02.pt", 1, null);
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
        ProgramTester.runFile("/lambda01.pt");
    }

    /**
     * Test a complex passing and application of lambdas, including an if.
     */
    @Test
    public void testLambda02() {
        ProgramTester.runFileWithMultipleRuns("/lambda02.pt");
    }

    /**
     * Test a complex passing and application of lambdas, including a mux.
     */
    @Test
    public void testLambda03() {
        ProgramTester.runFileWithMultipleRuns("/lambda03.pt");
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
        ProgramTester.runFile("/math01.pt");
    }

    /**
     * Test constants: -3.
     */
    @Test
    public void testMath02() {
        ProgramTester.runFile("/math02.pt");
    }

    /**
     * Test arithmetic: addition.
     */
    @Test
    public void testMath03() {
        ProgramTester.runFile("/math03.pt");
    }

    /**
     * Test arithmetic: equality.
     */
    @Test
    public void testMath04() {
        ProgramTester.runFile("/math04.pt");
    }

    /**
     * Test maxHood.
     */
    @Test
    public void testMaxHood01() {
        ProgramTester.runFile("/maxhood01.pt");
    }

    /**
     * Test maxHood.
     */
    @Test
    public void testMaxHood02() {
        ProgramTester.runFile("/maxhood02.pt");
    }

    /**
     * Test fully-qualified call of individually imported static Java method.
     */
    @Test
    public void testMethod01() {
        ProgramTester.runFile("/method01.pt");
    }

    /**
     * Test unqualified call of individually imported static Java method.
     */
    @Test
    public void testMethod02() {
        ProgramTester.runFileWithExplicitResult("/method02.pt", Collections.EMPTY_LIST);
    }

    /**
     * Test unqualified call of batch-imported static Java methods.
     */
    @Test
    public void testMethod03() {
        ProgramTester.runFile("/method03.pt");
    }

    /**
     * Test "dot" call of non-static Java methods.
     */
    @Test
    public void testMethod04() {
        ProgramTester.runFile("/method04.pt");
    }

    /**
     * Confirm that qualified and unqualified methods are equal.
     */
    @Test
    public void testMethod05() {
        ProgramTester.runFile("/method05.pt");
    }

    /**
     * Ensure that double values are coerced to integers when required.
     */
    @Test
    public void testMethod06() {
        ProgramTester.runFile("/method06.pt");
    }

    /**
     * Ensure that doubles are converted to int if needed.
     */
    @Test
    public void testMethod07() {
        ProgramTester.runFile("/method07.pt");
    }

    /**
     * Confirm that call can be made to Java functions with varargs.
     */
    @Test
    public void testMethod08() {
        ProgramTester.runFile("/method08.pt");
    }

    /**
     * Make sure that exceptions in method calls are passed up, rather than being
     * transformed into a "cannot call" exception.
     */
    @Test
    public void testMethod09() {
        boolean rightCause = false;
        try {
            ProgramTester.runFile("/method09.pt");
        } catch (Exception e) {
            // Should be an illegal state, caused by an invocation target, caused by a bad index
            if (e.getCause().getCause() instanceof ArrayIndexOutOfBoundsException) {
                rightCause = true;
            }
        }
        if (!rightCause) {
            fail("Didn't find an OutOfBounds exception");
        }
    }

    /**
     * Test minHood.
     */
    @Test
    public void testMinHood01() {
        ProgramTester.runFile("/minhood01.pt");
    }

    /**
     * Test minHood.
     */
    @Test
    public void testMinHood02() {
        ProgramTester.runFile("/minhood02.pt");
    }

//    /**
//     * Test minHood.
//     */
//    @Test
//    public void testMinHood03() {
//        ProgramTester.runFile("/minhood03.pt");
//    }

    /**
     * Test showing that when unqualified imported Protelis method names
     * conflict, first imported shadows later imports.
     */
    @Test
    public void testModules01() {
        ProgramTester.runFile("/modules01.pt");
    }

    /**
     * Confirm that local definitions shadow imported Protelis definitions.
     */
    @Test
    public void testModules02() {
        ProgramTester.runFile("/modules02.pt");
    }

    /**
     * Test that shadowed imported Protelis functions can still be called via
     * their fully qualified names.
     */
    @Test
    public void testModules03() {
        ProgramTester.runFile("/modules03.pt");
    }

    /**
     * Test that module imports can handle circular references between modules.
     */
    @Test
    public void testModules04() {
        ProgramTester.runFile("/modules04.pt");
    }

    /**
     * Test that imported modules can import other modules in different
     * packages.
     */
    @Test
    public void testModules05() {
        ProgramTester.runFile("/modules05.pt");
    }

    /**
     * Test operation of "mux" inclusive branching.
     */
    @Test
    public void testMux01() {
        ProgramTester.runFileWithMultipleRuns("/mux01.pt");
    }

    /**
     * Test two statement sequence.
     */
    @Test
    public void testMultiStatement01() {
        ProgramTester.runFile("/multistatement01.pt");
    }

    /**
     * Test multiple variable re-assignments.
     */
    @Test
    public void testMultiStatement02() {
        ProgramTester.runFile("/multistatement02.pt");
    }

    /**
     * Test independence of sequential invocations of a function.
     */
    @Test
    public void testMultiStatement03() {
        ProgramTester.runFile("/multistatement03.pt");
    }

    /**
     * Test assignment within nested lexical scope.
     */
    @Test
    public void testMultiStatement04() {
        ProgramTester.runFile("/multistatement04.pt");
    }

    /**
     * Test rep via a canonical use: creating a counter.
     */
    @Test
    public void testRep01() {
        ProgramTester.runFileWithMultipleRuns("/rep01.pt", IntStreams.range(0, 4).map(i -> (int) Math.round(Math.pow(10, i))));
    }

    /**
     * Test nested rep statements.
     */
    @Test
    public void testRep02() {
        double prev = 1;
        for (int i = 1; i < 100; i++) {
            ProgramTester.runFile("/rep02.pt", i, prev);
            prev = prev * (prev + 1);
        }
    }

    /**
     * Test rep as the only script instruction.
     */
    @Test
    public void testRep03() {
        ProgramTester.runFileWithMultipleRuns("/rep03.pt");
    }

    /**
     * Test infix addition.
     */
    @Test
    public void testStatement0() {
        ProgramTester.runFile("/statement0.pt");
    }

    /**
     * Test infix addition.
     */
    @Test
    public void testSum() {
        ProgramTester.runFile("/sum.pt");
    }

    /**
     * Test loading of a file from name without explicit classpath statement.
     */
    @Test
    public void testLoadFile() {
        ProgramTester.runFile("/sum.pt");
    }

    /**
     * Test loading of a file with explicit classpath statement.
     */
    @Test
    public void testLoadFromClasspath() {
        ProgramTester.runFileWithExplicitResult("classpath:/sum.pt", 8d);
    }

    /**
     * Test parsing of anonymous expression.
     */
    @Test
    public void testLoadModule() {
        ProgramTester.runFileWithExplicitResult("5+3", 8d);
    }

    /**
     * Test import of a module as part of an anonymous expression.
     */
    @Test
    public void testAnonymousLoadModule() {
        ProgramTester.runFileWithExplicitResult("import protelis:test:circular02\nfun3()", 1d);
    }

    /**
     * Test loading from a module name with default package.
     */
    @Test
    public void testLoadFromModuleName01() {
        ProgramTester.runFileWithExplicitResult("modules04", 1d);
    }

    /**
     * Test loading from a module name from a nested package.
     */
    @Test
    public void testLoadFromModuleName02() {
        ProgramTester.runFileWithExplicitResult("protelis:test:circular02", 1d);
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
        ProgramTester.runFileWithExplicitResult("/tuple01.pt", expectedResult);
    }

    /**
     * Test construction of tuple with some elements computed rather than inline
     * constants.
     */
    @Test
    public void testTuple02() {
        ProgramTester.runFile("/tuple02.pt");
    }

    /**
     * Test calling of tuple methods, in particular Tuple.size().
     */
    @Test
    public void testTuple03() {
        ProgramTester.runFileWithExplicitResult("/tuple03.pt", Integer.valueOf(3));
    }

    /**
     * Test the Tuple.indexof method.
     */
    @Test
    public void testTuple04() {
        ProgramTester.runFile("/tuple04.pt");
    }

    /**
     * Test the Tuple.fill method.
     */
    @Test
    public void testTuple05() {
        final Tuple expectedResult = DatatypeFactory.createTuple(new Object[] { 2.0, 2.0, 2.0 });
        ProgramTester.runFileWithExplicitResult("/tuple05.pt", expectedResult);
    }

    /**
     * Test the tuple arithmetic.
     */
    @Test
    public void testTupleArithmetic() {
        ProgramTester.runFile("/tuple06.pt");
        ProgramTester.runFile("/tuple07.pt");
        ProgramTester.runFile("/tuple08.pt");
    }

    /**
     * Test tuple comparisons.
     */
    @Test
    public void testTupleComparisons() {
        // Single element comparison
        Assert.assertEquals(false, ProgramTester.runProgram("[1] > [2]", 1));
        // Multi-element lexicographic comparison
        Assert.assertEquals(false, ProgramTester.runProgram("[1, 2] > [1, 3]", 1));
        Assert.assertEquals(true, ProgramTester.runProgram("[1, 2] < [1, 13]", 1));
        Assert.assertEquals(true, ProgramTester.runProgram("[1, 2, 1] > [1, 2]", 1));
        Assert.assertEquals(true, ProgramTester.runProgram("[1, 2, -1] > [1, 2]", 1));
        Assert.assertEquals(false, ProgramTester.runProgram("[1, 2, -1] > [1, 2, 0]", 1));
        Assert.assertEquals(true, ProgramTester.runProgram("[1, 2] == [1, 2]", 1));
        Assert.assertEquals(false, ProgramTester.runProgram("[1, 2] == [1, 2, 0]", 1));
        Assert.assertEquals(true, ProgramTester.runProgram("[1, 2, 0] == [1, 2, 0]", 1));
        Assert.assertEquals(false, ProgramTester.runProgram("[1, 2] > [1, 2, -1]", 1));
        Assert.assertEquals(true, ProgramTester.runProgram("[1, 2, 0] > [1, 2, -1]", 1));
        Assert.assertEquals(false, ProgramTester.runProgram("[1, 2, -1, 0,  0] < [1, 2, -1]", 1));
        Assert.assertEquals(true, ProgramTester.runProgram("[1, 2] >= [1, 2]", 1));
        Assert.assertEquals(true, ProgramTester.runProgram("[1, 2, 1] >= [1, 2, 0]", 1));
        Assert.assertEquals(false, ProgramTester.runProgram("[1, 2, -1] <= [1, 2]", 1));
        Assert.assertEquals(false, ProgramTester.runProgram("[1, 2, -1, 0, 0] <= [1, 2, -1]", 1));
        // comparison involving infinity
        Assert.assertEquals(true, ProgramTester.runProgram("[Infinity] == [Infinity]", 1));
        Assert.assertEquals(true, ProgramTester.runProgram("[Infinity, 1] < [Infinity, 2]", 1));
        Assert.assertEquals(true, ProgramTester.runProgram("[1, Infinity] < [2, 0]", 1));
        Assert.assertEquals(true, ProgramTester.runProgram("[-Infinity, 1] < [-Infinity, 2]", 1));
        Assert.assertEquals(true, ProgramTester.runProgram("[Infinity] > [-Infinity]", 1));
        Assert.assertEquals(false, ProgramTester.runProgram("[Infinity] > [Infinity]", 1));
        // comparison of nested tuples
        Assert.assertEquals(true, ProgramTester.runProgram("[1, [2], 1] >= [1, [2], 0]", 1));
        Assert.assertEquals(false, ProgramTester.runProgram("[1, [2], 1] >= [1, [2, 1], 0]", 1));
    }

    /**
     * Test the Tuple.map method.
     */
    @Test
    public void testTupleMap01() {
        ProgramTester.runFile("/TupleMap01.pt");
    }

    /**
     * Test the Tuple.reduce method.
     */
    @Test
    public void testTupleReduce01() {
        ProgramTester.runFile("/TupleReduce01.pt");
    }

    /**
     * Test the Tuple.filter method.
     */
    @Test
    public void testTupleFilter01() {
        ProgramTester.runFile("/TupleFilter01.pt");
    }

    /**
     * Test the Tuple array conversion method.
     */
    @Test
    public void testTupleToArray() {
        ProgramTester.runFileWithExplicitResult("/TupleArray01.pt", "[2.0, 3.0, 10.0]");
    }

    /**
     * Test the unary '!' operator.
     */
    @Test
    public void testUnary01() {
        ProgramTester.runFile("/unary01.pt");
    }

    /**
     * Test the unary '-' operator.
     */
    @Test
    public void testUnary02() {
        ProgramTester.runFileWithExplicitResult("/unary02.pt", -Math.PI);
    }

    /**
     * Test unionHood.
     */
    @Test
    public void testUnionHood01() {
        ProgramTester.runFile("/unionhood01.pt");
    }

    /**
     * Test unionHood with only local contribution.
     */
    @Test
    public void testUnionHood02() {
        ProgramTester.runFile("/unionhood02.pt");
    }

}
