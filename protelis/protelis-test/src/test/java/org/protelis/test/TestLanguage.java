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
import static org.protelis.test.ProgramTester.runExpectingErrors;
import static org.protelis.test.ProgramTester.runFile;
import static org.protelis.test.ProgramTester.runFileWithExplicitResult;
import static org.protelis.test.ProgramTester.runFileWithMultipleRuns;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.protelis.lang.ProtelisLoader;
import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.Tuple;
import org.protelis.lang.interpreter.util.ProtelisRuntimeException;

import com.google.common.collect.ImmutableList;

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
        runFileWithMultipleRuns("/alignedMap.pt");
    }

    /**
     * Test import of a module as part of an anonymous expression.
     */
    @Test
    public void testAnonymousLoadModule() {
        runFileWithExplicitResult("import protelis:test:circular02\nfun3()", 1d);
    }

     /**
     * Test Boolean logic operators.
     */
    @Test
    public void testBinary01() {
        runFile("/binary01.pt");
    }

    /**
     * Test closures.
     */
    @Test
    public void testClosure01() {
        runFile("/closure01.pt");
    }

    /**
     * Test closures.
     */
     @Test
     public void testClosure02() {
         runFile("/closure02.pt");
     }

    /**
     * Test the cyclic timer.
     */
    @Test
    public void testCyclicTimer() {
        runFileWithMultipleRuns("/cyclicTimer.pt");
    }

    /**
     * Test putting and getting of environment variables.
     */
    @Test
    public void testEnvironment01() {
        runFile("/environment01.pt");
    }

    /**
     * Tests that failures in invoking static methods because of parameter type are
     * reported clearly.
     */
    @Test
    public void testErrorMessage01() {
        runExpectingErrors("/errorMessage01.pt", ProtelisRuntimeException.class, "static");
        runExpectingErrors("/errorMessage01.pt", ProtelisRuntimeException.class, true, "type");
    }

    /**
     * Tests that failures in invoking static methods because of parameter count are
     * reported clearly.
     */
    @Test
    public void testErrorMessage02() {
        runExpectingErrors("/errorMessage02.pt", ProtelisRuntimeException.class, "static", "parameters");
    }

    /**
     * Tests that failures in linking non existing modules (#156) complain about
     * the missing module.
     */
    @Test
    public void testErrorMessage03() {
        runExpectingErrors("import non:existent:protelismodule\n1\n", IllegalStateException.class, "resource", "protelismodule", "does not exist");
    }

    /**
     * Smoke test of basic dynamic program evaluation.
     */
    @Test
    public void testEval01() {
        runFile("/eval01.pt");
    }

    /**
     * Test of dynamic evaluation of a complex program, including function
     * definitions.
     */
    @Test
    public void testEval02() {
        runFile("/eval02.pt");
    }

    /**
     * Exercise method calls on fields.
     */
    @Test
    public void testFieldMethod01() {
        runFile("/fieldMethod01.pt");
    }

    /**
     * Exercise method calls on fields.
     */
    @Test
    public void testFieldMethod02() {
        runFile("/fieldMethod02.pt");
    }

    /**
     * Exercise method calls on fields.
     */
    @Test
    public void testFieldMethod03() {
        runFile("/fieldMethod03.pt");
    }

    /**
     * Test simple function call with no arguments.
     */
    @Test
    public void testFunction01() {
        runFile("/function01.pt");
    }

    /**
     * Test simple function call with one argument.
     */
    @Test
    public void testFunction02() {
        runFile("/function02.pt");
    }

    /**
     * Test simple function call with two arguments.
     */
    @Test
    public void testFunction03() {
        runFile("/function03.pt");
    }

    /**
     * Test to make sure that more than one instance of a function can be active
     * at a time.
     */
    @Test
    public void testFunction04() {
        runFile("/function04.pt");
    }

    /**
     * Test hood with a lambda function.
     */
    @Test
    public void testGenericHood01() {
        runFile("/genericHood01.pt");
    }

    /**
     * Test hoodPlusSelf with a function reference.
     */
    @Test
    public void testGenericHood02() {
        runFile("/genericHood02.pt");
    }

    /**
     * Test hoodPlusSelf with a Java method reference.
     */
    @Test
    public void testGenericHood03() {
        runFile("/genericHood03.pt");
    }

    /**
     * Test hood with a generated field.
     */
    @Test
    public void testGenericHood04() {
        runFile("/genericHood04.pt");
    }

    /**
     * Test hood with a generated field.
     */
    @Test
    public void testGenericHood05() {
        runFile("/genericHood05.pt");
    }

    /**
     * Test hood with a generated field.
     */
    @Test
    public void testGenericHood06() {
        runFile("/genericHood06.pt");
    }

    /**
     * Test simple use of apply.
     */
    @Test
    public void testHof01() {
        runFileWithMultipleRuns("/hof01.pt");
    }

    /**
     * Test apply on a more complex function.
     */
    @Test
    public void testHof02() {
        runFileWithMultipleRuns("/hof02.pt");
    }

    /**
     * Test to make sure that each apply call bounds to a different instance of
     * the function.
     */
    @Test
    public void testHof03() {
        runFileWithMultipleRuns("/hof03.pt");
    }

    /**
     * Test using apply to define a higher-order map function.
     */
    @Test
    public void testHof04() {
        runFile("/hof04.pt");
    }

    /**
     * Test multiple applications of a higher-order map function.
     */
    @Test
    public void testHof05() {
        runFile("/hof05.pt");
    }

    /**
     * Test direct usage of a Java method as higher order function.
     */
    @Test
    public void testHof06() {
        runFile("/hof06.pt");
    }

    /**
     * Test that plain hood functions don't include local value.
     */
    @Test
    public void testHood01() {
        runFile("/hood01.pt");
    }

    /**
     * Test that PlusSelf hood functions do include local value.
     */
    @Test
    public void testHood02() {
        runFile("/hood02.pt");
    }

    /**
     * Test a more complex hood function.
     */
    @Test
    public void testHood03() {
        runFile("/hood03.pt");
    }

    /**
     * Test operation of "if" restrictive branching.
     */
    @Test
    public void testIf01() {
        runFileWithMultipleRuns("/if01.pt");
    }

    /**
     * Make sure if throws exceptions instead of returning fields.
     */
    @Test
    public void testIf02() {
        runExpectingErrors("/if02.pt", ProtelisRuntimeException.class, "if", "cannot", "return", "field");
    }

    /**
     * Test operation of "if" restrictive branching.
     */
    @Test
    public void testIf03() {
        runFile("/if03.pt");
    }

    /**
     * Test unionHood with only local contribution.
     */
    @Test
    public void testImplicitSelf() {
        for (final String test: ImmutableList.of("Map", "Reduce", "Filter")) {
            runFile("/Tuple" + test + "02.pt");
        }
    }

    /**
     * Test that the issue described at https://github.com/Protelis/Protelis/issues/127 is solved.
     */
    @Test
    public void testIssue127() {
        runFile("/issue127.pt");
    }

    /**
     * Test java static field access.
     */
    @Test
    public void testJavaField01() {
        runFile("/javaField01.pt");
    }

    /**
     * Test that java elements imported in referenced modules are available within
     * such modules.
     */
    @Test
    public void testJavaImportsInExternalModules() {
        runFile("/useJavaImportedElsewhere.pt");
    }

    /**
     * Test a simple anonymous function inline definition and application.
     */
    @Test
    public void testLambda01() {
        runFile("/lambda01.pt");
    }

    /**
     * Test a complex passing and application of lambdas, including an if.
     */
    @Test
    public void testLambda02() {
        runFileWithMultipleRuns("/lambda02.pt");
    }

    /**
     * Test a complex passing and application of lambdas, including a mux.
     */
    @Test
    public void testLambda03() {
        runFileWithMultipleRuns("/lambda03.pt");
    }

    /**
     * Test loading of a file from name without explicit classpath statement.
     */
    @Test
    public void testLoadFile() {
        runFile("/sum.pt");
    }

    /**
     * Test loading of a file with explicit classpath statement.
     */
    @Test
    public void testLoadFromClasspath() {
        runFileWithExplicitResult("classpath:/sum.pt", 8d);
    }

    /**
     * Test loading from a module name with default package.
     */
    @Test
    public void testLoadFromModuleName01() {
        runFileWithExplicitResult("modules04", 1d);
    }

    /**
     * Test loading from a module name from a nested package.
     */
    @Test
    public void testLoadFromModuleName02() {
        runFileWithExplicitResult("protelis:test:circular02", 1d);
    }

    /**
     * Test loading from a module name from a nested package.
     */
    @Test
    public void testLoadFromModuleName03() {
        assertNotNull(ProtelisLoader.parse("replicatedgossip"));
    }

    /**
     * Test parsing of anonymous expression.
     */
    @Test
    public void testLoadModule() {
        runFileWithExplicitResult("5+3", 8d);
    }

    /**
     * Test the ability to load a module by name.
     */
    @Test
    public void testLoadModule01() {
        assertNotNull(ProtelisLoader.parse("loadmodule01"));
    }

    /**
     * Test localHood without self (which should be the same of localHoodPlusSelf).
     */
    @Test
    public void testLocalHood01() {
        runFile("/localHood01.pt", 2);
    }

    /**
     * Test constants: -Infinity.
     */
    @Test
    public void testMath01() {
        runFile("/math01.pt");
    }

    /**
     * Test constants: -3.
     */
    @Test
    public void testMath02() {
        runFile("/math02.pt");
    }

    /**
     * Test arithmetic: addition.
     */
    @Test
    public void testMath03() {
        runFile("/math03.pt");
    }

    /**
     * Test arithmetic: equality.
     */
    @Test
    public void testMath04() {
        runFile("/math04.pt");
    }

    /**
     * Test maxHood.
     */
    @Test
    public void testMaxHood01() {
        runFile("/maxhood01.pt");
    }

    /**
     * Test maxHood.
     */
    @Test
    public void testMaxHood02() {
        runFile("/maxhood02.pt");
    }

    /**
     * Test fully-qualified call of individually imported static Java method.
     */
    @Test
    public void testMethod01() {
        runFile("/method01.pt");
    }

    /**
     * Test unqualified call of individually imported static Java method.
     */
    @Test
    public void testMethod02() {
        runFileWithExplicitResult("/method02.pt", Collections.EMPTY_LIST);
    }

//    /**
//     * Test minHood.
//     */
//    @Test
//    public void testMinHood03() {
//        runFile("/minhood03.pt");
//    }

    /**
     * Test unqualified call of batch-imported static Java methods.
     */
    @Test
    public void testMethod03() {
        runFile("/method03.pt");
    }

    /**
     * Test "dot" call of non-static Java methods.
     */
    @Test
    public void testMethod04() {
        runFile("/method04.pt");
    }

    /**
     * Confirm that qualified and unqualified methods are equal.
     */
    @Test
    public void testMethod05() {
        runFile("/method05.pt");
    }

    /**
     * Ensure that double values are coerced to integers when required.
     */
    @Test
    public void testMethod06() {
        runFile("/method06.pt");
    }

    /**
     * Ensure that doubles are converted to int if needed.
     */
    @Test
    public void testMethod07() {
        runFile("/method07.pt");
    }

    /**
     * Confirm that call can be made to Java functions with varargs.
     */
    @Test
    public void testMethod08() {
        runFile("/method08.pt");
    }

    /**
     * Make sure that exceptions in method calls are passed up, rather than being
     * transformed into a "cannot call" exception.
     */
    @Test
    public void testMethod09() {
        runExpectingErrors("/method09.pt", ProtelisRuntimeException.class, "out", "of", "bounds");
    }

    /**
     * Test minHood.
     */
    @Test
    public void testMinHood01() {
        runFile("/minhood01.pt");
    }

    /**
     * Test minHood.
     */
    @Test
    public void testMinHood02() {
        runFile("/minhood02.pt");
    }

    /**
     * Test showing that when unqualified imported Protelis method names
     * conflict, first imported shadows later imports.
     */
    @Test
    public void testModules01() {
        runFile("/modules01.pt");
    }

    /**
     * Confirm that local definitions shadow imported Protelis definitions.
     */
    @Test
    public void testModules02() {
        runFile("/modules02.pt");
    }

    /**
     * Test that shadowed imported Protelis functions can still be called via
     * their fully qualified names.
     */
    @Test
    public void testModules03() {
        runFile("/modules03.pt");
    }

    /**
     * Test that module imports can handle circular references between modules.
     */
    @Test
    public void testModules04() {
        runFile("/modules04.pt");
    }

    /**
     * Test that imported modules can import other modules in different
     * packages.
     */
    @Test
    public void testModules05() {
        runFile("/modules05.pt");
    }

    /**
     * Test two statement sequence.
     */
    @Test
    public void testMultiStatement01() {
        runFile("/multistatement01.pt");
    }

    /**
     * Test multiple variable re-assignments.
     */
    @Test
    public void testMultiStatement02() {
        runFile("/multistatement02.pt");
    }

    /**
     * Test independence of sequential invocations of a function.
     */
    @Test
    public void testMultiStatement03() {
        runFile("/multistatement03.pt");
    }

    /**
     * Test assignment within nested lexical scope.
     */
    @Test
    public void testMultiStatement04() {
        runFile("/multistatement04.pt");
    }

    /**
     * Test operation of "mux" inclusive branching.
     */
    @Test
    public void testMux01() {
        runFileWithMultipleRuns("/mux01.pt");
    }

    /**
     * Test rep via a canonical use: creating a counter.
     */
    @Test
    public void testRep01() {
        runFileWithMultipleRuns("/rep01.pt", IntStreams.range(0, 4).map(i -> (int) Math.round(Math.pow(10, i))));
    }

    /**
     * Test nested rep statements.
     */
    @Test
    public void testRep02() {
        double prev = 1;
        for (int i = 1; i < 100; i++) {
            runFile("/rep02.pt", i, prev);
            prev = prev * (prev + 1);
        }
    }

    /**
     * Test rep as the only script instruction.
     */
    @Test
    public void testRep03() {
        runFileWithMultipleRuns("/rep03.pt");
    }

    /**
     * Test rep / yield.
     */
    @Test
    public void testRep04() {
        runFileWithMultipleRuns("/rep04.pt");
    }

    /**
     * Basic test for share.
     */
    @Test
    public void testShare01() {
        runFileWithMultipleRuns("/share01.pt");
    }

    /**
     * Basic test for share.
     */
    @Test
    public void testShare02() {
        runFileWithMultipleRuns("/share02.pt");
    }

    /**
     * Test yield.
     */
    @Test
    public void testShare03() {
        runFileWithMultipleRuns("/share03.pt");
    }

    /**
     * Test previous scope access in share/yield.
     */
    @Test
    public void testShare04() {
        runFileWithMultipleRuns("/share04.pt");
    }

    /**
     * Test infix addition.
     */
    @Test
    public void testStatement0() {
        runFile("/statement0.pt");
    }

    /**
     * Test infix addition.
     */
    @Test
    public void testSum() {
        runFile("/sum.pt");
    }

    /**
     * Test construction of tuple using '[]' syntax.
     */
    @Test
    public void testTuple01() {
        final Tuple expectedResult = DatatypeFactory.createTuple(new Object[] { 5.0, 4.0, 3.0, 2.0, 1.0, 0.0 });
        runFileWithExplicitResult("/tuple01.pt", expectedResult);
    }

    /**
     * Test construction of tuple with some elements computed rather than inline
     * constants.
     */
    @Test
    public void testTuple02() {
        runFile("/tuple02.pt");
    }

    /**
     * Test calling of tuple methods, in particular Tuple.size().
     */
    @Test
    public void testTuple03() {
        runFileWithExplicitResult("/tuple03.pt", Integer.valueOf(3));
    }

    /**
     * Test the Tuple.indexof method.
     */
    @Test
    public void testTuple04() {
        runFile("/tuple04.pt");
    }

    /**
     * Test the Tuple.fill method.
     */
    @Test
    public void testTuple05() {
        final Tuple expectedResult = DatatypeFactory.createTuple(new Object[] { 2.0, 2.0, 2.0 });
        runFileWithExplicitResult("/tuple05.pt", expectedResult);
    }

    /**
     * Test the tuple arithmetic.
     */
    @Test
    public void testTupleArithmetic() {
        runFile("/tuple06.pt");
        runFile("/tuple07.pt");
        runFile("/tuple08.pt");
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
        final String prefix = "import java.lang.Double.POSITIVE_INFINITY let Infinity = POSITIVE_INFINITY; "; // NOPMD
        Assert.assertEquals(true, ProgramTester.runProgram(prefix + "[Infinity] == [Infinity]", 1));
        Assert.assertEquals(true, ProgramTester.runProgram(prefix + "[Infinity, 1] < [Infinity, 2]", 1));
        Assert.assertEquals(true, ProgramTester.runProgram(prefix + "[1, Infinity] < [2, 0]", 1));
        Assert.assertEquals(true, ProgramTester.runProgram(prefix + "[-Infinity, 1] < [-Infinity, 2]", 1));
        Assert.assertEquals(true, ProgramTester.runProgram(prefix + "[Infinity] > [-Infinity]", 1));
        Assert.assertEquals(false, ProgramTester.runProgram(prefix + "[Infinity] > [Infinity]", 1));
        // comparison of nested tuples
        Assert.assertEquals(true, ProgramTester.runProgram("[1, [2], 1] >= [1, [2], 0]", 1));
        Assert.assertEquals(false, ProgramTester.runProgram("[1, [2], 1] >= [1, [2, 1], 0]", 1));
    }

    /**
     * Test the Tuple.filter method.
     */
    @Test
    public void testTupleFilter01() {
        runFile("/TupleFilter01.pt");
    }

    /**
     * Test the Tuple.map method.
     */
    @Test
    public void testTupleMap01() {
        runFile("/TupleMap01.pt");
    }

    /**
     * Test the Tuple.reduce method.
     */
    @Test
    public void testTupleReduce01() {
        runFile("/TupleReduce01.pt");
    }

    /**
     * Test the Tuple array conversion method.
     */
    @Test
    public void testTupleToArray() {
        runFileWithExplicitResult("/TupleArray01.pt", "[2.0, 3.0, 10.0]");
    }

    /**
     * Test the unary '!' operator.
     */
    @Test
    public void testUnary01() {
        runFile("/unary01.pt");
    }

    /**
     * Test the unary '-' operator.
     */
    @Test
    public void testUnary02() {
        runFileWithExplicitResult("/unary02.pt", -Math.PI);
    }

    /**
     * Test unionHood.
     */
    @Test
    public void testUnionHood01() {
        runFile("/unionhood01.pt");
    }

    /**
     * Test unionHood with only local contribution.
     */
    @Test
    public void testUnionHood02() {
        runFile("/unionhood02.pt");
    }

}
