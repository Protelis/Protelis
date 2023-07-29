/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.test;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import it.unibo.alchemist.boundary.interfaces.OutputMonitor;
import it.unibo.alchemist.core.implementations.Engine;
import it.unibo.alchemist.core.implementations.Engine.StateCommand;
import it.unibo.alchemist.loader.YamlLoader;
import it.unibo.alchemist.model.interfaces.Environment;
import it.unibo.alchemist.model.interfaces.Reaction;
import it.unibo.alchemist.model.interfaces.Time;
import java8.util.function.BiConsumer;
import java8.util.function.Function;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.protelis.test.infrastructure.ProtelisNode;
import org.protelis.test.infrastructure.RunProtelisProgram;
import org.protelis.test.matcher.TestCount;
import org.protelis.test.matcher.TestEqual;
import org.protelis.test.observer.ExceptionObserver;
import org.protelis.test.observer.SimpleExceptionObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
/**
 * Test protelis simulations.
 */
public final class InfrastructureTester {

    /**
     * Skip this value while comparing results.
     */
    public static final String DC = "$";
    /**
     * Tolerance while comparing double values.
     */
    public static final double DELTA = 0.01;
    private static final String EXAMPLE = "/example.yml";
    /**
     * Default runs.
     */
    public static final int SIMULATION_STEPS = 1000;
    private static final Logger L = LoggerFactory.getLogger(InfrastructureTester.class);
    /**
     * Check function stability for this number of steps.
     */
    public static final int STABILITY_STEPS = 100;

    /**
     * Matching results.
     */
    public static final class TestMatcher {
        private static final String EXPECTED = "result:";
        private static final String ML_NAME = "multilineComment";
        private static final String ML_RUN = "multirun";
        private static final Pattern EXTRACT_RESULT = Pattern.compile(
            ".*?" + EXPECTED + "\\s*\\r?\\n?\\s*#?\\s*\\{(?<" + ML_NAME + ">.*?)\\s*}",
            Pattern.DOTALL
        );
        private static final Pattern MULTI_RESULT_PATTERN = Pattern.compile(
            "(\\d+)\\s*:\\s*\\[(?<" + ML_RUN + ">.*?)]\\s*,?\\s*\\r?\\n?",
            Pattern.DOTALL
        );
        private static final String RESULT_LIST =
            "\\s*#?\\r?\\n?\\s*([\\d\\w]+)\\s+([\\-$\\w.]+)\\s*,?\\s*\\r?\\n?";
        private static final Pattern RESULT_PATTERN = Pattern.compile(RESULT_LIST);

        static {
            final InputStream is = Objects.requireNonNull(
                InfrastructureTester.class.getResourceAsStream("/example.yml")
            );
            try {
                final String test = IOUtils.toString(is, StandardCharsets.UTF_8);
                assertNotNull(getResult(new SimpleExceptionObserver(), test));
            } catch (IOException e) {
                fail();
            }
        }

        /**
         * @param obs
         *            exception observer
         * @param text
         *            text to be matched
         * @return result
         */
        public static TIntObjectMap<List<Pair<String, String>>> getMultiRunResult(
            final ExceptionObserver obs,
            final String text
        ) {
            final TIntObjectMap<List<Pair<String, String>>> res = new TIntObjectHashMap<>();
            try {
                assertTrue(text.isEmpty());
                final Matcher outer = EXTRACT_RESULT.matcher(text);
                if (outer.find()) {
                    final String result = outer.group(ML_NAME);
                    final Matcher multiRun = MULTI_RESULT_PATTERN.matcher(result);
                    try {
                        while (multiRun.find()) {
                            L.debug("multiRun.group(): {}", multiRun.group());
                            L.debug("multiRun.group(1): {}", multiRun.group(1));
                            L.debug("multiRun.group(2): {}", multiRun.group(2));
                            res.put(Integer.parseInt(multiRun.group(1)), getResultList(obs, multiRun.group(2)));
                        }
                    } catch (IllegalStateException e) {
                        obs.exceptionThrown(new IllegalArgumentException("This is not a multirun"));
                    }
                } else {
                    obs.exceptionThrown(new IllegalArgumentException("Your test does not include the expected result"));
                }
            } catch (AssertionError e) {
                obs.exceptionThrown(new IllegalArgumentException("Empty result"));
            }
            return res;
        }

        /**
         * @param obs
         *            exception observer
         * @param text
         *            text to be matched
         * @return result
         */
        public static List<Pair<String, String>> getResult(final ExceptionObserver obs, final String text) {
            List<Pair<String, String>> res = new LinkedList<>();
            try {
                assertFalse(text.isEmpty());
                final Matcher outer = EXTRACT_RESULT.matcher(text);
                if (outer.find()) {
                    final String result = outer.group(ML_NAME);
                    res = getResultList(obs, result);
                } else {
                    obs.exceptionThrown(new IllegalArgumentException("Your test does not include the expected result"));
                }
            } catch (AssertionError e) {
                obs.exceptionThrown(new IllegalArgumentException("Empty result"));
            }
            return res;
        }

        /**
         * @param obs
         *            exception observer
         * @param text
         *            text
         * @return result list
         */
        public static List<Pair<String, String>> getResultList(final ExceptionObserver obs, final String text) {
            final List<Pair<String, String>> res = new LinkedList<>();
            try {
                assertFalse(text.isEmpty());
                final Matcher inner = RESULT_PATTERN.matcher(text);
                while (inner.find()) {
                    assertNotNull(inner.group());
                    res.add(Pair.of(inner.group(1), inner.group(2)));
                }
            } catch (AssertionError e) {
                obs.exceptionThrown(new IllegalArgumentException("Empty result"));
            }
            return res;
        }

        private TestMatcher() {
        }
    }

    @SuppressWarnings("unchecked")
    private static void checkResult(
        final ExceptionObserver obs,
        final int totalSimulationSteps,
        final int stabilitySteps,
        final List<Pair<String,  String>> expectedResult,
        final Object f,
        final Environment<Object> env,
        final long step
    ) {
        if (step >= totalSimulationSteps - stabilitySteps) {
            L.debug("---- ROUND - {}", step);
            final Map<String, Object> simulationRes = new HashMap<>();
            env.getNodes().forEach(n -> {
                final ProtelisNode pNode = (ProtelisNode) n;
                simulationRes.put(pNode.toString(), pNode.get(RunProtelisProgram.RESULT));
                L.debug("[node{}] res:{}", pNode, pNode.get(RunProtelisProgram.RESULT));
            });
            if (f instanceof BiConsumer) {
                ((BiConsumer<Map<String, Object>, List<Pair<String, String>>>) f).accept(simulationRes, expectedResult);
            } else if (f instanceof Function) {
                final int occ = expectedResult.size();
                final int found = ((Function<Map<String, Object>, Integer>) f).apply(simulationRes);
                try {
                    assertEquals(occ, found);
                } catch (AssertionError e) {
                    obs.exceptionThrown(
                        new IllegalArgumentException("Expected occurences [" + occ + "] != Occurences found [" + found + "]")
                    );
                }
            } else {
                obs.exceptionThrown(new IllegalArgumentException("How can I test without a proper function?"));
            }
        }
    }

    private static void generalTest(
        final ExceptionObserver obs,
        final String file,
        final int simulationSteps,
        final int stabilitySteps,
        final boolean multirun,
        final Object f
    ) {
        final String resource = "/" + file + ".yml";
        final InputStream is = InfrastructureTester.class.getResourceAsStream(resource);
        if (is == null) {
            throw new IllegalArgumentException(resource + " is not an accessible resource.");
        }
        String test;
        try {
            test = IOUtils.toString(is, StandardCharsets.UTF_8);
            final YamlLoader loader = new YamlLoader(test);
            if (!multirun) {
                assertTrue(simulationSteps + " is an invalid number of runs", simulationSteps >= 0);
                final Environment<Object> env = loader.getWith(null);
                final List<Pair<String, String>> expectedResult = TestMatcher.getResult(obs, test);
                testSingleRun(obs, simulationSteps, stabilitySteps, env, expectedResult, f);
            } else {
                final TIntObjectMap<List<Pair<String, String>>> expectedResult = TestMatcher.getMultiRunResult(obs, test);
                for (final int r : expectedResult.keys()) {
                    final Environment<Object> env = loader.getWith(null);
                    L.debug("run {}", r);
                    testSingleRun(obs, r, 0, env, expectedResult.get(r), f);
                }
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Run a test.
     * 
     * @param args
     *            parameters
     * @throws InterruptedException
     *             if interrupted
     * @throws IOException
     *             file not found
     */
    public static void main(final String[] args) throws IOException {
        final InputStream is = Objects.requireNonNull(InfrastructureTester.class.getResourceAsStream(EXAMPLE));
        final String test = IOUtils.toString(is, StandardCharsets.UTF_8);
        final SimpleExceptionObserver obs = new SimpleExceptionObserver();
        TestMatcher.getResult(obs, test);
        assertTrue(obs.getExceptionList().isEmpty());
        L.info("Done.");
    }

    /**
     * Test the given file.
     * 
     * @param file
     *            file to bested
     */
    public static void multiRunTest(final String file) {
        final ExceptionObserver obs = new SimpleExceptionObserver();
        generalTest(obs, file, -1, -1, true, new TestEqual(obs));
    }

    /**
     * Test the given file.
     * 
     * @param file
     *            file to bested
     */
    public static void runTest(final String file) {
        runTest(file, SIMULATION_STEPS, STABILITY_STEPS);
    }

    /**
     * Test the given file.
     * 
     * @param file
     *            file to bested
     * @param exampleRuns
     *            number of runs
     * @param stabilitySteps
     *            check function stability for this number of steps
     */
    public static void runTest(final String file, final int exampleRuns, final int stabilitySteps) {
        final ExceptionObserver obs = new SimpleExceptionObserver();
        generalTest(obs, file, exampleRuns, stabilitySteps, false, new TestEqual(obs));
    }

    /**
     * Test a given property.
     * 
     * @param file
     *            file to bested
     * @param simulationSteps
     *            number of runs
     * @param stabilitySteps
     *            check function stability for this number of steps
     * @param expectedValue
     *            value to be checked
     */
    public static void runTest(
        final String file,
        final int simulationSteps,
        final int stabilitySteps,
        final Object expectedValue
    ) {
        generalTest(
            new SimpleExceptionObserver(),
            file,
            simulationSteps,
            stabilitySteps,
            false,
            new TestCount(expectedValue)
        );
    }

    /**
     * Test a given property.
     * 
     * @param file
     *            file to bested
     * @param expectedValue
     *            value to be checked
     * @throws IOException
     *             IOexception
     */
    public static void runTest(final String file, final Object expectedValue) throws IOException {
        runTest(file, SIMULATION_STEPS, STABILITY_STEPS, expectedValue);
    }

    @SuppressFBWarnings(value = "SE_BAD_FIELD", justification = "This is not going to get serialized")
    private static void testSingleRun(
        final ExceptionObserver obs,
        final int totalSimulationSteps,
        final int stabilitySteps,
        final Environment<Object> env,
        final List<Pair<String, String>> expectedResult,
        final Object f
    ) {
        final Engine<Object> simulation = new Engine<>(env, totalSimulationSteps + stabilitySteps);
        simulation.addOutputMonitor(new OutputMonitor<Object>() {
            private static final long serialVersionUID = 1L;
            @Override
            public void finished(final Environment<Object> env, final Time time, final long step) {
                if (simulation.getError() != null) {
                    throw new IllegalStateException("Simulation Failure", simulation.getError());
                }
                assertEquals(totalSimulationSteps + stabilitySteps, step);
            }
            @Override
            public void initialized(final Environment<Object> env) { }
            @Override
            public void stepDone(final Environment<Object> env, final Reaction<Object> r, final Time time, final long step) {
                checkResult(obs, totalSimulationSteps + stabilitySteps, stabilitySteps, expectedResult, f, env, step);
            }
        });
        simulation.addCommand(new StateCommand<>().run().build());
        simulation.run();
        assertFalse(
            obs.getFirstException().isPresent() ? obs.getFirstException().get().getMessage() : "",
            obs.getFirstException().isPresent()
        );
    }

    private InfrastructureTester() {
    }

}
