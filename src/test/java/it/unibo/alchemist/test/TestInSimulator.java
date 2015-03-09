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
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.StackImpl;
import it.unibo.alchemist.utils.FasterString;
import it.unibo.alchemist.utils.ParseUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.util.Pair;
import org.danilopianini.io.FileUtilities;


public class TestInSimulator {

	private static void testFile(final String file, final Object expectedResult) {
		testFile(file, 1, expectedResult);
	}
	
	private static void testFile(final String file, final int steps, final Object expectedResult) {
		final AnnotatedTree<?> program = runProgram(resourceToString(file), steps);
		assertEquals(expectedResult, program.getAnnotation());
	}
	
	private static void failExeption(final Throwable e) {
		fail(stackTraceToString(e));
	}
	
	private static String resourceToString(final String uri) {
		final String path = TestInSimulator.class.getResource(uri).getFile();
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
