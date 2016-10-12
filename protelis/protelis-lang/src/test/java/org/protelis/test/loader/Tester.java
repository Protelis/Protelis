package org.protelis.test.loader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

import it.unibo.alchemist.core.implementations.Engine;
import it.unibo.alchemist.core.implementations.Engine.StateCommand;
import it.unibo.alchemist.core.interfaces.Simulation;
import it.unibo.alchemist.loader.YamlLoader;
import it.unibo.alchemist.model.interfaces.Environment;

/**
 * Test protelis simulations.
 */
public final class Tester {
    private static final String EXAMPLE = "example";

    private static final String DC = "$";
    /**
     * Default runs.
     */
    public static final int EXAMPLE_RUNS = 1000;
    private static final double DELTA = 0.01;

    /**
     * @param file
     *            file to be tested
     * @throws InterruptedException
     *             if interrupted
     * @throws IOException
     *             file not found
     */
    private Tester(final String file) throws InterruptedException, IOException {
        this(file, EXAMPLE_RUNS);
    }

    /**
     * @param file
     *            file to be tested
     * @param runs
     *            number of runs
     * @throws InterruptedException
     *             if interrupted
     * @throws IOException
     *             file not found
     * 
     */
    private Tester(final String file, final int runs) throws InterruptedException, IOException {
        final InputStream is = Tester.class.getResourceAsStream("/" + file + ".yml");
        final String test = IOUtils.toString(is, StandardCharsets.UTF_8);
        final YamlLoader loader = new YamlLoader(test);
        final Environment<Object> env = loader.getWith(null);
        final Simulation<Object> sim = new Engine<Object>(env, runs);
        sim.addCommand(new StateCommand<>().run().build());
        sim.run();
        Map<String, Object> simulationRes = new HashMap<>();
        sim.getEnvironment().getNodes().forEach(n -> {
            ProtelisNode pNode = (ProtelisNode) n;
            simulationRes.put(pNode.toString(), pNode.get(RunProtelisProgram.RESULT));
            System.out.println("[Node" + pNode.toString() + "]: " + pNode.get(RunProtelisProgram.RESULT));
        });

        final List<Pair<String, String>> expectedResult = TestMatcher.getResult(test);
        for (Pair<String, String> pair : expectedResult) {
            if (!pair.getRight().equals(DC)) {
                Object singleNodeResult = simulationRes.get(pair.getLeft());
                assertNotNull("Node" + pair.getLeft() + ": result can't be null!", singleNodeResult);
                if (singleNodeResult instanceof Double) {
                    assertEquals("Node" + pair.getLeft(), (double) Double.parseDouble(pair.getRight()),
                                    (double) singleNodeResult, DELTA);
                } else {
                    assertEquals("Node" + pair.getLeft(), pair.getRight(), singleNodeResult);
                }
            }
        }
    }

    /**
     * Test the given file.
     * 
     * @param file
     *            file to bested
     * @throws IOException 
     * @throws InterruptedException 
     */
    public static void test(final String file) throws InterruptedException, IOException {
        test(file, EXAMPLE_RUNS);
    }

    /**
     * Test the given file.
     * 
     * @param file
     *            file to bested
     * @param exampleRuns
     *            number of runs
     * @throws IOException 
     * @throws InterruptedException 
     */
    public static void test(final String file, final int exampleRuns) throws InterruptedException, IOException {
        new Tester(file, exampleRuns);
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
        new Tester(EXAMPLE, EXAMPLE_RUNS);
    }

    /**
     * Matching results.
     */
    public static class TestMatcher {
        private static final String ML_NAME = "multilineComment";
        private static final String EXPECTED = "result:";
        private static final Pattern EXTRACT_RESULT = Pattern.compile(
                        ".*?" + EXPECTED + "\\s*\\r?\\n?\\s*\\#?\\s*\\{(?<" + ML_NAME + ">.*?)\\s*\\}", Pattern.DOTALL);
        /*
         * \s*\#?\r?\n?\s*([\d\w]+)\s+([\d\w\.]+)\s*,?\s*\r?\n?
         */
        private static final Pattern RESULT_PATTERN = Pattern
                        .compile("\\s*\\#?\\r?\\n?\\s*([\\d\\w]+)\\s+([\\$\\d\\w\\.]+)\\s*,?\\s*\\r?\\n?");

        static {
            InputStream is = Tester.class.getResourceAsStream("/example.yml");
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
        public static List<Pair<String, String>> getResult(final String text) {
            assertFalse("Empty result", text.isEmpty());
            final List<Pair<String, String>> res = new LinkedList<>();
            final Matcher outer = EXTRACT_RESULT.matcher(text);
            if (outer.find()) {
                String result = outer.group(ML_NAME);
                final Matcher inner = RESULT_PATTERN.matcher(result);
                while (inner.find()) {
                    assertNotNull("There is no result", inner.group());
                    res.add(Pair.of(inner.group(1), inner.group(2)));
                }
            } else {
                fail("Your test does not include the expected result");
            }
            return res;
        }
    }
}
