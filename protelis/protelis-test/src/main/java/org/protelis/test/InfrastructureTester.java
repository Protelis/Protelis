package org.protelis.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.protelis.test.infrastructure.ProtelisNode;
import org.protelis.test.infrastructure.RunProtelisProgram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import it.unibo.alchemist.core.implementations.Engine;
import it.unibo.alchemist.core.implementations.Engine.StateCommand;
import it.unibo.alchemist.core.interfaces.Simulation;
import it.unibo.alchemist.loader.YamlLoader;
import it.unibo.alchemist.model.interfaces.Environment;
import java8.util.function.Function;

/**
 * Test protelis simulations.
 */
public final class InfrastructureTester {
    private static final String EXAMPLE = "/example.yml";

    private static final String DC = "$";
    /**
     * Default runs.
     */
    public static final int EXAMPLE_RUNS = 1000;
    private static final double DELTA = 0.01;
    private static final Logger L = LoggerFactory.getLogger(InfrastructureTester.class);

    private InfrastructureTester() {
    }

    /**
     * Run a test.
     */
    private static void generalTest(final String file, final int runs, final boolean multirun, final Object f)
                    throws IOException {
        final InputStream is = InfrastructureTester.class.getResourceAsStream("/" + file + ".yml");
        final String test = IOUtils.toString(is, StandardCharsets.UTF_8);
        final YamlLoader loader = new YamlLoader(test);

        if (!multirun) {
            assertTrue(runs + " is an invalid number of runs", runs >= 0);
            final Environment<Object> env = loader.getWith(null);
            final List<Pair<String, String>> expectedResult = TestMatcher.getResult(test);
            testSingleRun(runs, env, expectedResult, f);
        } else {
            final TIntObjectMap<List<Pair<String, String>>> expectedResult = TestMatcher.getMultiRunResult(test);
            for (final int r : expectedResult.keys()) {
                final Environment<Object> env = loader.getWith(null);
                L.debug("run {}", r);
                testSingleRun(r, env, expectedResult.get(r), f);
            }
        }
    }

    /**
     * Run a simulation until the given number of runs.
     * 
     * @param runs
     *            number of runs
     * @param env
     *            environment
     * @param expectedResult
     *            expected result
     * @param f
     *            testing function
     */
    @SuppressWarnings("unchecked")
    private static void testSingleRun(final int runs, final Environment<Object> env,
                    final List<Pair<String, String>> expectedResult, final Object f) {
        final Simulation<Object> sim = new Engine<Object>(env, runs);
        sim.addCommand(new StateCommand<>().run().build());
        sim.run();
        final Map<String, Object> simulationRes = new HashMap<>();
        sim.getEnvironment().getNodes().forEach(n -> {
            final ProtelisNode pNode = (ProtelisNode) n;
            simulationRes.put(pNode.toString(), pNode.get(RunProtelisProgram.RESULT));
            L.debug("[node{}] res:{}", pNode.toString(), pNode.get(RunProtelisProgram.RESULT));
        });
        if (f instanceof BiConsumer) {
            ((BiConsumer<Map<String, Object>, List<Pair<String, String>>>) f).accept(simulationRes, expectedResult);
        } else if (f instanceof Function) {
            final int occ = Integer.parseInt(expectedResult.get(0).getRight());
            final int found = ((Function<Map<String, Object>, Integer>) f).apply(simulationRes);
            assertEquals("Expected occurences [" + occ + "] != Occurences found [" + found + "]", occ, found);
        } else {
            fail("How can I test without a proper function?");
        }

    }

    /**
     * Test the given file.
     * 
     * @param file
     *            file to bested
     * @throws IOException
     *             IOexception
     * @throws InterruptedException
     *             InterruptedException
     */
    public static void test(final String file) throws InterruptedException, IOException {
        test(file, EXAMPLE_RUNS);
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
     * @throws InterruptedException
     *             InterruptedException
     */
    public static void test(final String file, final Object expectedValue) throws InterruptedException, IOException {
        test(file, EXAMPLE_RUNS, expectedValue);
    }

    /**
     * Test a given property.
     * 
     * @param file
     *            file to bested
     * @param exampleRuns
     *            number of runs
     * @param expectedValue
     *            value to be checked
     * @throws IOException
     *             IOexception
     * @throws InterruptedException
     *             InterruptedException
     */
    public static void test(final String file, final int exampleRuns, final Object expectedValue)
                    throws InterruptedException, IOException {
        generalTest(file, exampleRuns, false, new TestCount(expectedValue));
    }

    /**
     * Test the given file.
     * 
     * @param file
     *            file to bested
     * @param exampleRuns
     *            number of runs
     * @throws IOException
     *             IOexception
     * @throws InterruptedException
     *             InterruptedException
     */
    public static void test(final String file, final int exampleRuns) throws InterruptedException, IOException {
        generalTest(file, exampleRuns, false, new TestEqual());
    }

    /**
     * Test the given file.
     * 
     * @param file
     *            file to bested
     * @throws IOException
     *             IOexception
     * @throws InterruptedException
     *             InterruptedException
     */
    public static void testMultirun(final String file) throws InterruptedException, IOException {
        generalTest(file, -1, true, new TestEqual());
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
    public static void main(final String[] args) throws InterruptedException, IOException {
        final InputStream is = InfrastructureTester.class.getResourceAsStream(EXAMPLE);
        final String test = IOUtils.toString(is, StandardCharsets.UTF_8);
        TestMatcher.getResult(test);
        L.info("Done.");
    }

    private static class TestCount implements Function<Map<String, Object>, Integer> {
        private final Object expectedValue;

        /**
         * 
         * @param expectedValue
         *            expected value
         */
        TestCount(final Object expectedValue) {
            this.expectedValue = expectedValue;
        }

        @Override
        public Integer apply(final Map<String, Object> simulationRes) {
            final Long count = simulationRes.values().stream().filter(v -> v.equals(this.expectedValue)).count();
            return count.intValue();
        }
    }

    private static class TestEqual implements BiConsumer<Map<String, Object>, List<Pair<String, String>>> {
        @Override
        public void accept(final Map<String, Object> simulationRes, final List<Pair<String, String>> expectedResult) {
            assertEquals("expectedResult.length [" + expectedResult.size() + "] != simulationResult.length ["
                            + simulationRes.values().size() + "]", expectedResult.size(),
                            simulationRes.values().size());
            for (final Pair<String, String> pair : expectedResult) {
                if (!pair.getRight().equals(DC)) {
                    final Object singleNodeResult = simulationRes.get(pair.getLeft());
                    assertNotNull("Node" + pair.getLeft() + ": result can't be null!", singleNodeResult);
                    final String err = "[Node" + pair.getLeft() + "] " + simulationRes.values();
                    if (singleNodeResult instanceof Integer || singleNodeResult instanceof Double) {
                        final double tmp = singleNodeResult instanceof Integer
                                        ? ((Integer) singleNodeResult).doubleValue() : (double) singleNodeResult;
                        assertEquals(err, (double) Double.parseDouble(pair.getRight()), tmp, DELTA);
                    } else if (singleNodeResult instanceof Boolean) {
                        final String v = pair.getRight();
                        assertEquals(err, (boolean) Boolean.parseBoolean(v.equals("T") ? "true"
                                        : v.equals("F") ? "false" : pair.getRight()), (boolean) singleNodeResult);
                    } else {
                        assertEquals(err, pair.getRight(), singleNodeResult);
                    }
                }
            }
        }
    }

    /**
     * Matching results.
     */
    public static final class TestMatcher {
        private static final String ML_NAME = "multilineComment";
        private static final String ML_RUN = "multirun";
        private static final String EXPECTED = "result:";
        private static final String RESULT_LIST = "\\s*\\#?\\r?\\n?\\s*([\\d\\w]+)\\s+([\\-\\$\\d\\w\\.]+)\\s*,?\\s*\\r?\\n?";
        private static final Pattern EXTRACT_RESULT = Pattern.compile(
                        ".*?" + EXPECTED + "\\s*\\r?\\n?\\s*\\#?\\s*\\{(?<" + ML_NAME + ">.*?)\\s*\\}", Pattern.DOTALL);
        /*
         * \s*\#?\r?\n?\s*([\d\w]+)\s+([\d\w\.]+)\s*,?\s*\r?\n?
         */
        private static final Pattern MULTI_RESULT_PATTERN = Pattern
                        .compile("(\\d+)\\s*\\:\\s*\\[(?<" + ML_RUN + ">.*?)\\]\\s*,?\\s*\\r?\\n?", Pattern.DOTALL);
        private static final Pattern RESULT_PATTERN = Pattern.compile(RESULT_LIST);

        private TestMatcher() {
        }

        static {
            final InputStream is = InfrastructureTester.class.getResourceAsStream("/example.yml");
            try {
                final String test = IOUtils.toString(is, StandardCharsets.UTF_8);
                assertNotNull(getResult(test));
            } catch (IOException e) {
                fail(e.getMessage());
            }
        }

        /**
         * 
         * @param text
         *            text to be matched
         * @return result
         */
        public static TIntObjectMap<List<Pair<String, String>>> getMultiRunResult(final String text) {
            assertFalse("Empty result", text.isEmpty());
            final TIntObjectMap<List<Pair<String, String>>> res = new TIntObjectHashMap<>();
            final Matcher outer = EXTRACT_RESULT.matcher(text);
            if (outer.find()) {
                final String result = outer.group(ML_NAME);
                final Matcher multiRun = MULTI_RESULT_PATTERN.matcher(result);
                try {
                    while (multiRun.find()) {
                        L.debug("multiRun.group(): {}", multiRun.group());
                        L.debug("multiRun.group(1): {}", multiRun.group(1));
                        L.debug("multiRun.group(2): {}", multiRun.group(2));
                        res.put(Integer.parseInt(multiRun.group(1)), getResultList(multiRun.group(2)));
                    }
                } catch (IllegalStateException e) {
                    fail("This is not a multirun");
                }
            } else {
                fail("Your test does not include the expected result");
            }
            return res;
        }

        /**
         * 
         * @param text
         *            text to be matched
         * @return result
         */
        public static List<Pair<String, String>> getResult(final String text) {
            assertFalse("Empty result", text.isEmpty());
            List<Pair<String, String>> res = new LinkedList<>();
            final Matcher outer = EXTRACT_RESULT.matcher(text);
            if (outer.find()) {
                final String result = outer.group(ML_NAME);
                res = getResultList(result);
            } else {
                fail("Your test does not include the expected result");
            }
            return res;
        }

        /**
         * 
         * @param text
         *            text
         * @return result list
         */
        public static List<Pair<String, String>> getResultList(final String text) {
            assertFalse("Empty result", text.isEmpty());
            final List<Pair<String, String>> res = new LinkedList<>();
            final Matcher inner = RESULT_PATTERN.matcher(text);
            while (inner.find()) {
                assertNotNull("There is no result", inner.group());
                res.add(Pair.of(inner.group(1), inner.group(2)));
            }
            return res;
        }
    }

}
