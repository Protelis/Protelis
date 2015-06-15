/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import it.unibo.alchemist.language.protelis.datatype.Tuple;
import it.unibo.alchemist.language.protelis.util.ProtelisLoader;
import it.unibo.alchemist.language.protelis.vm.DummyContext;
import it.unibo.alchemist.language.protelis.vm.ProtelisVM;
import it.unibo.alchemist.utils.L;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class TestLanguage {
	
	private static final String SL_NAME = "singleLineComment";
	private static final String ML_NAME = "multilineComment";
	private static final String EXPECTED = "EXPECTED_RESULT:";
	private static final Pattern EXTRACT_RESULT = Pattern.compile(".*?" + EXPECTED + "\\s*(?<" + ML_NAME + ">.*?)\\s*\\*\\/|\\/\\/\\s*" + EXPECTED + "\\s*(?<" + SL_NAME + ">.*?)\\s*\\n", Pattern.DOTALL);
	private static final Pattern CYCLE = Pattern.compile("\\$CYCLE");

	@Test
	public void testAlignedMap() {
		IntStream.range(1, 100).parallel().forEach(i -> {
			testFile("/alignedMap.pt", i);
		});
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
		testFileWithExplicitResult("/fieldMethod01.pt", 1.0);
	}

	@Test
	public void testFieldMethod02() {
		testFileWithExplicitResult("/fieldMethod02.pt", 2.0);
	}

	@Test
	public void testFieldMethod03() {
		testFileWithExplicitResult("/fieldMethod03.pt", 2.0);
	}

	@Test
	public void testFunction01() {
		testFileWithExplicitResult("/function01.pt", 1.0);
	}

	@Test
	public void testFunction02() {
		testFileWithExplicitResult("/function02.pt", 10d);
	}

	@Test
	public void testFunction03() {
		testFileWithExplicitResult("/function03.pt", 15.2);
	}

	@Test
	public void testFunction04() {
		testFileWithExplicitResult("/function04.pt", 6.0);
	}

	@Test
	public void testHof01() {
		for (int i = 1; i <= 100; i++) {
			testFileWithExplicitResult("/hof01.pt", 1.0);
		}
	}

	@Test
	public void testHof02() {
		for (int i = 1; i <= 100; i++) {
			testFile("/hof02.pt", i, (double) i);
		}
	}

	@Test
	public void testHof03() {
		for (int i = 1; i <= 100; i++) {
			testFile("/hof03.pt", i, (double) i);
		}
	}

	@Test
	public void testHof04() {
		testFileWithExplicitResult("/hof04.pt", Tuple.create(new Object[] { 6.0, 5.0, 4.0, 3.0, 2.0, 1.0 }));
	}

	@Test
	public void testHof05() {
		testFileWithExplicitResult("/hof05.pt", Tuple.create(new Object[] { 36.0, 25.0, 16.0, 9.0, 4.0, 1.0 }));
	}

	@Test
	public void testLambda01() {
		testFileWithExplicitResult("/lambda01.pt", 2.0);
	}

	@Test
	public void testLambda02() {
		for (int i = 1; i < 100; i++) {
			testFile("/lambda02.pt", i, ((i - 1) % 6 < 3) ? ((i - 1d) % 3 + 1) : (-((i - 1d) % 3) - 1));
		}
	}

	@Test
	public void testLambda03() {
		for (int i = 1; i < 100; i++) {
			testFile("/lambda03.pt", i, ((i - 1) % 6 < 3) ? ((i - 1d) % 3 + 1) : (-((i - 1d) % 3) - 1));
		}
	}

	@Test
	public void testIf01() {
		for (int i = 1; i < 100; i++) {
			testFile("/if01.pt", i, ((i - 1) % 6 < 3) ? ((i - 1d) % 3 + 1) : (-((i - 1d) % 3) - 1));
		}
	}

	@Test
	public void testMethod01() {
		testFileWithExplicitResult("/method01.pt", 0d);
	}

	@Test
	public void testMethod02() {
		testFileWithExplicitResult("/method02.pt", Collections.EMPTY_LIST);
	}

	@Test
	public void testMethod03() {
		testFileWithExplicitResult("/method03.pt", 7.658068177120837);
	}

	@Test
	public void testMethod04() {
		testFileWithExplicitResult("/method04.pt", Tuple.create(new Object[] { 9.0, 2.0, 7.0, 4.0 }));
	}

	@Test
	public void testMethod05() {
		testFileWithExplicitResult("/method05.pt", true);
	}

	@Test
	public void testModules01() {
		testFileWithExplicitResult("/modules01.pt", 1d);
	}

	@Test
	public void testModules02() {
		testFileWithExplicitResult("/modules02.pt", true);
	}

	@Test
	public void testModules03() {
		testFileWithExplicitResult("/modules03.pt", true);
	}

	@Test
	public void testModules04() {
		testFileWithExplicitResult("/modules04.pt", 1d);
	}

	@Test
	public void testMux01() {
		for (int i = 1; i < 100; i++) {
			testFile("/mux01.pt", i, (double) (((i - 1) % 6 < 3) ? i : -i));
		}
	}

	@Test
	public void testMultiStatement01() {
		testFileWithExplicitResult("/multistatement01.pt", 9.0);
	}

	@Test
	public void testMultiStatement02() {
		testFileWithExplicitResult("/multistatement02.pt", 256.0);
	}

	@Test
	public void testMultiStatement03() {
		testFileWithExplicitResult("/multistatement03.pt", 1.0);
	}

	@Test
	public void testMultiStatement04() {
		testFileWithExplicitResult("/multistatement04.pt", 2.0);
	}

	@Test
	public void testRandom01() {
		testFile("/random01.pt", 10, true);
	}

	@Test
	public void testRep01() {
		for (int i = 1; i < 100000; i *= 10) {
			testFile("/rep01.pt", i, (double) i);
		}
	}

	@Test
	public void testRep02() {
		testFile("/rep02.pt", 1, 1d);
		testFile("/rep02.pt", 2, 2d);
		testFile("/rep02.pt", 3, 6d);
		testFile("/rep02.pt", 4, 42d);
	}

	@Test
	public void testSum() {
		testFileWithExplicitResult("/sum.pt", 8d);
	}

	@Test
	public void testLoadFile() {
		testFileWithExplicitResult("/sum.pt", 8d);
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
		testFileWithExplicitResult("/tuple02.pt", Tuple.create(new Object[] { 3.0, 3.0, 4.0 }));
	}

	@Test
	public void testUnary01() {
		testFileWithExplicitResult("/unary01.pt", true);
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
			L.error(e);
			fail();
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
