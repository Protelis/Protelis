/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.test;

import java8.util.Objects;
import java8.util.function.Consumer;
import java8.util.stream.IntStream;
import java8.util.stream.IntStreams;
import org.apache.commons.io.IOUtils;
import org.danilopianini.io.FileUtilities;
import org.danilopianini.lang.LangUtils;
import org.protelis.lang.ProtelisLoader;
import org.protelis.test.infrastructure.DummyContext;
import org.protelis.vm.ProtelisProgram;
import org.protelis.vm.ProtelisVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test a protelis program.
 */
public final class ProgramTester {
    private static final Logger LOGGER = LoggerFactory.getLogger("Protelis Test");
    private static final String SL_NAME = "singleLineComment";
    private static final String ML_NAME = "multilineComment";
    private static final String EXPECTED = "EXPECTED_RESULT:";
    /*
     * .*?\/\*.*?EXPECTED\s*(?<ML_NAME>.*?)\s*\/\/\s*EXPECTED\s*(?<SL_NAME>.*?)\s*\n
     */
    private static final Pattern EXTRACT_RESULT = Pattern.compile(".*?\\/\\*.*?" + EXPECTED + "\\s*(?<" + ML_NAME
                    + ">.*?)\\s*\\*\\/|\\/\\/\\s*" + EXPECTED + "\\s*(?<" + SL_NAME + ">.*?)\\s*\\n", Pattern.DOTALL);
    private static final Pattern CYCLE = Pattern.compile("\\$CYCLE");
    private static final int MIN_CYCLE_NUM = 1;
    private static final int MAX_CYCLE_NUM = 100;

    private ProgramTester() {
    }

    /**
     * 
     * @param file
     *            file to be tested
     * @param expectedResult
     *            result
     */
    public static void runFileWithExplicitResult(final String file, final Object expectedResult) {
        runFile(file, 1, expectedResult);
    }

    /**
     * 
     * @param file
     *            file to be tested
     */
    public static void runFile(final String file) {
        runFile(file, MAX_CYCLE_NUM);
    }

    /**
     * Tests a program expecting an error, and checks its message contents.
     * 
     * @param program the program to execute. It it ends in ".pt", it will be loaded as Protelis script from classpath
     * @param expectedExceptionType the type of exception to be thrown
     * @param searchCause if true, the message contents are searched for in the cause exception message
     * @param messageContents the strings that the exception message must include
     */
    public static void runExpectingErrors(
            final String program,
            final Class<? extends Throwable> expectedExceptionType,
            final boolean searchCause,
            final String... messageContents) {
        runExpectingErrors(program, expectedExceptionType, result -> {
            if (searchCause) {
                assertNotNull(result.getCause());
            }
            final String message = (searchCause ? result.getCause() : result)
                    .getMessage().toLowerCase(Locale.ENGLISH);
            assertNotNull(message);
            for (final String messagePart : messageContents) {
                assertTrue("Message does not contain the expected string: " + messagePart + " (original: " + message + ")",
                        message.contains(messagePart.toLowerCase(Locale.ENGLISH)));
            }
        });
    }

    /**
     * Tests a program expecting an error, and checks its message contents.
     * 
     * @param <E> exception type (static)
     * @param program the program to execute. It it ends in ".pt", it will be loaded as Protelis script from classpath
     * @param expectedExceptionType the type of exception to be thrown
     * @param analyzer the actions to perform on the exception
     */
    public static <E extends Throwable> void runExpectingErrors(
            final String program,
            final Class<E> expectedExceptionType,
            final Consumer<E> analyzer) {
        final E result = assertThrows("The test does not fail as expected.", expectedExceptionType, () -> {
            if (program.endsWith("pt")) {
                runFile(program);
            } else {
                runProgram(program, 1);
            }
        });
        analyzer.accept(result);
    }

    /**
     * Tests a program expecting an error, and checks its message contents.
     * 
     * @param program the program to execute. It it ends in ".pt", it will be loaded as Protelis script from classpath
     * @param expectedExceptionType the type of exception to be thrown
     * @param messageContents the strings that the exception message must include
     */
    public static void runExpectingErrors(
            final String program,
            final Class<? extends Throwable> expectedExceptionType,
            final String... messageContents) {
        runExpectingErrors(program, expectedExceptionType, false, messageContents);
    }

    /**
     * 
     * @param file
     *            file to be tested
     */
    public static void runFileWithMultipleRuns(final String file) {
        runFileWithMultipleRuns(file, MIN_CYCLE_NUM, MAX_CYCLE_NUM);
    }

    /**
     * 
     * @param file
     *            file to be tested
     * @param min
     *            min runs
     * @param max
     *            max runs
     */
    public static void runFileWithMultipleRuns(final String file, final int min, final int max) {
        runFileWithMultipleRuns(file, IntStreams.rangeClosed(min, max));
    }

    /**
     * 
     * @param file
     *            file to be tested
     * @param stream
     *            stream of run
     */
    public static void runFileWithMultipleRuns(final String file, final IntStream stream) {
        stream.forEach(i -> {
            runFile(file, i);
        });
    }

    /**
     * 
     * @param file
     *            file to be tested
     * @param runs
     *            number of runs
     */
    public static void runFile(final String file, final int runs) {
        final Object execResult = runProgram(Objects.requireNonNull(file, "File in test cannot be null"), runs);
        final String fileWithExt = file.endsWith(".pt") ? file : "/" + file + ".pt";
        try (InputStream is = Objects.requireNonNull(ProgramTester.class.getResourceAsStream(fileWithExt), 
                "Unable to load resource: " + file + " (transformed in: " + fileWithExt + ')')) {
            final String test = IOUtils.toString(is, StandardCharsets.UTF_8);
            final Matcher extractor = EXTRACT_RESULT.matcher(test);
            if (extractor.find()) {
                String result = extractor.group(ML_NAME);
                if (result == null) {
                    result = extractor.group(SL_NAME);
                }
                final String toCheck = CYCLE.matcher(result).replaceAll(Integer.toString(runs));
                final ProtelisVM vm = new ProtelisVM(ProtelisLoader.parse(toCheck), new DummyContext());
                vm.runCycle();
                assertEquals(
                    vm.getCurrentValue(),
                    execResult instanceof Number ? ((Number) execResult).doubleValue() : execResult
                );
            } else {
                fail("Your test does not include the expected result");
            }
        } catch (IOException e) {
            fail(LangUtils.stackTraceToString(e));
        }
    }

    /**
     * 
     * @param file
     *            file to be tested
     * @param runs
     *            number of runs
     * @param expectedResult
     *            expected result
     */
    public static void runFile(final String file, final int runs, final Object expectedResult) {
        assertEquals(expectedResult, runProgram(file, runs));
    }

    /**
     * 
     * @param s
     *            program to run
     * @param runs
     *            number of runs
     * @return program result
     */
    public static Object runProgram(final String s, final int runs) {
        final ProtelisProgram program = ProtelisLoader.parse(s);
        try {
            FileUtilities.serializeObject(program);
        } catch (Exception e) { // NOPMD: Done by purpose
            LOGGER.error(e.getMessage(), e);
            fail(e.getMessage());
        }
        final ProtelisVM vm = new ProtelisVM(program, new DummyContext());
        for (int i = 0; i < runs; i++) {
            vm.runCycle();
            LOGGER.debug("[rnd {}] res: {}", i, vm.getCurrentValue());
        }
        return vm.getCurrentValue();
    }
}
