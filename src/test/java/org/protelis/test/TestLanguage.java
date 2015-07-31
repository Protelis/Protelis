/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package org.protelis.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.danilopianini.lang.LangUtils;
import org.junit.Test;
import org.protelis.lang.ProtelisLoader;
import org.protelis.lang.datatype.Tuple;
import org.protelis.vm.ProtelisVM;
import org.protelis.vm.impl.DummyContext;

public class TestLanguage {
	
	private static final String SL_NAME = "singleLineComment";
	private static final String ML_NAME = "multilineComment";
	private static final String EXPECTED = "EXPECTED_RESULT:";
	private static final Pattern EXTRACT_RESULT = Pattern.compile(".*?" + EXPECTED + "\\s*(?<" + ML_NAME + ">.*?)\\s*\\*\\/|\\/\\/\\s*" + EXPECTED + "\\s*(?<" + SL_NAME + ">.*?)\\s*\\n", Pattern.DOTALL);
	private static final Pattern CYCLE = Pattern.compile("\\$CYCLE");
	private static final int MIN_CYCLE_NUM = 1;
	private static final int MAX_CYCLE_NUM = 100;

	@Test
	public void testAlignedMap() {
		testFileWithMultipleRuns("/alignedMap.pt");
	}

	@Test
	public void testBinary01() {
		testFile("/binary01.pt");
	}

	@Test
	public void testEnvironment01() {
		testFile("/environment01.pt");
	}

	@Test
	public void testEval01() {
		testFile("/eval01.pt");
	}

	@Test
	public void testEval02() {
		testFile("/eval02.pt");
	}

	@Test
	public void testFieldMethod01() {
		testFile("/fieldMethod01.pt");
	}

	@Test
	public void testFieldMethod02() {
		testFile("/fieldMethod02.pt");
	}

	@Test
	public void testFieldMethod03() {
		testFile("/fieldMethod03.pt");
	}

	@Test
	public void testFunction01() {
		testFile("/function01.pt");
	}

	@Test
	public void testFunction02() {
		testFile("/function02.pt");
	}

	@Test
	public void testFunction03() {
		testFile("/function03.pt");
	}

	@Test
	public void testFunction04() {
		testFile("/function04.pt");
	}

	@Test
	public void testHof01() {
		testFileWithMultipleRuns("/hof01.pt");
	}

	@Test
	public void testHof02() {
		testFileWithMultipleRuns("/hof02.pt");
	}

	@Test
	public void testHof03() {
		testFileWithMultipleRuns("/hof03.pt");
	}

	@Test
	public void testHof04() {
		testFile("/hof04.pt");
	}

	@Test
	public void testHof05() {
		testFile("/hof05.pt");
	}

	@Test
	public void testHood01() {
		testFile("/hood01.pt");
	}

	@Test
	public void testHood02() {
		testFile("/hood02.pt");
	}

	@Test
	public void testHood03() {
		testFile("/hood03.pt");
	}

	@Test
	public void testIf01() {
		testFileWithMultipleRuns("/if01.pt");
	}

	@Test
	public void testLambda01() {
		testFile("/lambda01.pt");
	}

	@Test
	public void testLambda02() {
		testFileWithMultipleRuns("/lambda02.pt");
	}

	@Test
	public void testLambda03() {
		testFileWithMultipleRuns("/lambda03.pt");
	}

	@Test
	public void testMethod01() {
		testFile("/method01.pt");
	}

	@Test
	public void testMethod02() {
		testFileWithExplicitResult("/method02.pt", Collections.EMPTY_LIST);
	}

	@Test
	public void testMethod03() {
		testFile("/method03.pt");
	}

	@Test
	public void testMethod04() {
		testFile("/method04.pt");
	}

	@Test
	public void testMethod05() {
		testFile("/method05.pt");
	}

	@Test
	public void testModules01() {
		testFile("/modules01.pt");
	}

	@Test
	public void testModules02() {
		testFile("/modules02.pt");
	}

	@Test
	public void testModules03() {
		testFile("/modules03.pt");
	}

	@Test
	public void testModules04() {
		testFile("/modules04.pt");
	}

	@Test
	public void testModules05() {
		testFile("/modules05.pt");
	}

	@Test
	public void testMux01() {
		testFileWithMultipleRuns("/mux01.pt");
	}

	@Test
	public void testMultiStatement01() {
		testFile("/multistatement01.pt");
	}

	@Test
	public void testMultiStatement02() {
		testFile("/multistatement02.pt");
	}

	@Test
	public void testMultiStatement03() {
		testFile("/multistatement03.pt");
	}

	@Test
	public void testMultiStatement04() {
		testFile("/multistatement04.pt");
	}

	@Test
	public void testRandom01() {
		testFileWithMultipleRuns("/random01.pt", 100, 100);
	}

	@Test
	public void testRep01() {
		testFileWithMultipleRuns("/rep01.pt", IntStream.range(0, 4).map(i -> (int) Math.round(Math.pow(10, i))));
	}

	@Test
	public void testRep02() {
		double prev = 1;
		for (int i = 1; i < 100; i++) {
			testFile("/rep02.pt", i, prev);
			prev = prev * (prev + 1);
		}
	}

	@Test
	public void testSum() {
		testFile("/sum.pt");
	}

	@Test
	public void testLoadFile() {
		testFile("/sum.pt");
	}

	@Test
	public void testLoadFromClasspath() {
		testFileWithExplicitResult("classpath:/sum.pt", 8d);
	}

	@Test
	public void testLoadModule() {
		testFileWithExplicitResult("5+3", 8d);
	}

	@Test
	public void testLoadFromModuleName01() {
		testFileWithExplicitResult("modules04", 1d);
	}

	@Test
	public void testLoadFromModuleName02() {
		testFileWithExplicitResult("protelis:test:circular02", 1d);
	}

	@Test
	public void testTuple01() {
		testFileWithExplicitResult("/tuple01.pt", Tuple.create(new Object[] { 5.0, 4.0, 3.0, 2.0, 1.0, 0.0 }));
	}

	@Test
	public void testTuple02() {
		testFile("/tuple02.pt");
	}

	@Test
	public void testTupleMap01() {
		testFile("/TupleMap01.pt");
	}

	@Test
	public void testTupleReduce01() {
		testFile("/TupleReduce01.pt");
	}

	@Test
	public void testTupleFilter01() {
		testFile("/TupleFilter01.pt");
	}

	@Test
	public void testUnary01() {
		testFile("/unary01.pt");
	}

	@Test
	public void testUnary02() {
		testFileWithExplicitResult("/unary02.pt", -Math.PI);
	}

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
		testFileWithMultipleRuns(file, IntStream.rangeClosed(min, max));
	}
	
	private static void testFileWithMultipleRuns(final String file, final IntStream stream) {
		stream.parallel().forEach(i -> {
			testFile(file, i);
		});
	}
	
	private static void testFile(final String file, final int runs) {
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
				assertEquals(vm.getCurrentValue(), runProgram(file, runs));
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
		final ProtelisVM vm = new ProtelisVM(ProtelisLoader.parse(s), new DummyContext());
		for (int i = 0; i < runs; i++) {
			vm.runCycle();
		}
		return vm.getCurrentValue();
	}
}
