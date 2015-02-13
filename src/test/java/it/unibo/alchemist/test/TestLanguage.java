/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.test;
import static org.danilopianini.lang.LangUtils.stackTraceToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import gnu.trove.list.array.TByteArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import it.unibo.alchemist.language.protelis.FunctionDefinition;
import it.unibo.alchemist.language.protelis.datatype.Tuple;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.StackImpl;
import it.unibo.alchemist.utils.FasterString;
import it.unibo.alchemist.utils.ParseUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.danilopianini.io.FileUtilities;
import org.danilopianini.lang.Pair;
import org.junit.Test;


public class TestLanguage {

	@Test
	public void testDouble() {
		final double val = 1;
		final AnnotatedTree<?> program = runProgram(Double.toString(val));
		assertEquals(val, program.getAnnotation());
	}

	@Test
	public void testInteger() {
		final double val = 1;
		final AnnotatedTree<?> program = runProgram(Integer.toString((int)val));
		assertEquals(val, program.getAnnotation());
	}

	@Test
	public void testString() {
		final String val = "test";
		final AnnotatedTree<?> program = runProgram("\""+val+"\"");
		assertEquals(val, program.getAnnotation());
	}
	
	@Test
	public void testEval01() {
		testFile("/eval01.pt", 1d);
	}
	
	@Test
	public void testEval02() {
		testFile("/eval02.pt", Tuple.create(new Object[]{36.0, 25.0, 16.0, 9.0, 4.0, 1.0}));
	}
	
	@Test
	public void testMethod01() {
		testFile("/method01.pt", 0d);
	}
	
	@Test
	public void testSum() {
		testFile("/sum.pt", 8d);
	}
	
	@Test
	public void testUnary01() {
		testFile("/unary01.pt", true);
	}
	
	@Test
	public void testUnary02() {
		testFile("/unary02.pt", -Math.PI);
	}
	
	@Test
	public void testMethod02() {
		testFile("/method02.pt", Collections.EMPTY_LIST);
	}
	
	@Test
	public void testMethod03() {
		testFile("/method03.pt", 7.658068177120837);
	}

	@Test
	public void testMethod04() {
		testFile("/method04.pt", Tuple.create(new Object[]{9.0, 2.0, 7.0, 4.0}));
	}

	@Test
	public void testFunction01() {
		testFile("/function01.pt", 1.0);
	}
	
	@Test
	public void testFunction02() {
		testFile("/function02.pt", 10d);
	}
	
	@Test
	public void testFunction03() {
		testFile("/function03.pt", 15.2);
	}
	
	@Test
	public void testHof01() {
		for(int i=1; i<=100; i++) {
			testFile("/hof01.pt", 1.0);
		}
	}
	
	@Test
	public void testHof02() {
		for(int i=1; i<=100; i++) {
			testFile("/hof02.pt", i, (double) i);
		}
	}
	
	@Test
	public void testHof03() {
		for(int i=1; i<=100; i++) {
			testFile("/hof03.pt", i, (double) i);
		}
	}
	
	@Test
	public void testHof04() {
		testFile("/hof04.pt", Tuple.create(new Object[]{6.0, 5.0, 4.0, 3.0, 2.0, 1.0}));
	}
	
	@Test
	public void testHof05() {
		testFile("/hof05.pt", Tuple.create(new Object[]{36.0, 25.0, 16.0, 9.0, 4.0, 1.0}));
	}
	
	@Test
	public void testLambda01() {
		testFile("/lambda01.pt", 2.0);
	}
	
	@Test
	public void testLambda02() {
		for(int i=1; i<100; i++) {
			testFile("/lambda02.pt", i, ( (i-1) % 6 < 3 ) ? ( (i-1d) % 3 + 1) : ( -( (i - 1d) % 3 ) -1 ));
		}
	}
	
	@Test
	public void testLambda03() {
		for(int i=1; i<100; i++) {
			testFile("/lambda03.pt", i, ( (i-1) % 6 < 3 ) ? ( (i-1d) % 3 + 1) : ( -( (i - 1d) % 3 ) -1 ));
		}
	}
	
	@Test
	public void testMultiStatement01() {
		testFile("/multistatement01.pt", 9.0);
	}
	
	@Test
	public void testMultiStatement02() {
		testFile("/multistatement02.pt", 256.0);
	}
	
	@Test
	public void testMultiStatement03() {
		testFile("/multistatement03.pt", 1.0);
	}
	
	@Test
	public void testMultiStatement04() {
		testFile("/multistatement04.pt", 2.0);
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
	public void testTuple01() {
		testFile("/tuple01.pt", Tuple.create(new Object[] { 5.0, 4.0, 3.0, 2.0, 1.0, 0.0 }));
	}
	
	@Test
	public void testTuple02() {
		testFile("/tuple02.pt", Tuple.create(new Object[]{3.0, 3.0, 4.0}));
	}
	
	@Test
	public void testIf01() {
		for (int i = 1; i < 100; i++) {
			testFile("/if01.pt", i, ((i - 1) % 6 < 3) ? ((i - 1d) % 3 + 1) : (-((i - 1d) % 3) - 1));
		}
	}
	
	@Test
	public void testMux01() {
		for (int i = 1; i < 100; i++) {
			testFile("/mux01.pt", i, (double) (((i - 1) % 6 < 3) ? i : -i));
		}
	}
	
	private static void testFile(final String file, final Object expectedResult) {
		testFile(file, 1, expectedResult);
	}
	
	private static void testFile(final String file, final int runs, final Object expectedResult) {
		final AnnotatedTree<?> program = runProgram(resourceToString(file), runs);
		assertEquals(expectedResult, program.getAnnotation());
	}
	
	private static void failExeption(final Throwable e) {
		fail(stackTraceToString(e));
	}
	
	private static String resourceToString(final String uri) {
		final String path = TestLanguage.class.getResource(uri).getFile();
		try {
			return FileUtilities.fileToString(path);
		} catch (IOException e) {
			failExeption(e);
		}
		return null;
	}
	
	private static AnnotatedTree<?> runProgram(final String s) {
		return runProgram(s, 1);
	}
	
	private static AnnotatedTree<?> runProgram(final String s, final int runs) {
		final Pair<AnnotatedTree<?>, Map<FasterString, FunctionDefinition>> prog = ParseUtils.parse(null, null, null, s);
		AnnotatedTree<?> program = prog.getFirst();
		Map<CodePath, Object> lastExec = new HashMap<>();
		for (int i = 0; i < runs; i++) {
			program = program.copy();
			final Map<CodePath, Object> newExec = new HashMap<>();
			program.eval(null, new TIntObjectHashMap<>(), new StackImpl(new HashMap<>(prog.getSecond())), lastExec, newExec, new TByteArrayList());
			lastExec = newExec;
		}
		return program;
	}
}
