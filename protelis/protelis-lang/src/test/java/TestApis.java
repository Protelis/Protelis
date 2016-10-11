import static org.junit.Assert.assertArrayEquals;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Test;
import org.protelis.test.Position;
import org.protelis.test.Results;
import org.protelis.test.SimulationTest;
import org.protelis.test.TestConfig;

/**
 * Testing Protelis core libraries.
 */
public class TestApis {

    private static final double DELTA_2 = 0.01;

    /**
     * Test the number of neighbors of each device.
     */
    @Test
    public void testNeighborhood() {
        test(Results.NEIGHBORHOOD);
    }

    /**
     * Test distanceTo function.
     */
    @Test
    public void testDistanceTo() {
        test(Results.DISTANCETO, DELTA_2);
    }

    /**
     * Test distanceTo with obstacle.
     */
    @Test
    public void testDistanceToWithObstacle() {
        test(Results.OBSTACLE, DELTA_2);
    }

//    /**
//     * Test forecastObstacle.
//     */
//    @Test
//    public void testForecastObstacle() {
//        test(Results.FORECAST_OBSTACLE, DELTA_2);
//    }

    /**
     * Test broadcast function.
     */
    @Test
    public void testBroadcast() {
        test(Results.BROADCAST);
    }

    /**
     * Test self.nbrRange.
     */
    @Test
    public void testNbrRange() {
        test(Results.NBRRANGE, DELTA_2);
    }

    /**
     * Test addRange.
     */
    @Test
    public void testAddRange() {
        test(Results.ADDRANGE, DELTA_2);
    }

    /**
     * Test G.pt.
     */
    @Test
    public void testG() {
        test(Results.G);
    }

    /**
     * Test distance.pt.
     */
    @Test
    public void testDistance() {
        test(Results.DISTANCE, DELTA_2);
    }

    /**
     * Test T function.
     */
    @Test
    public void testT() {
        test(Results.TFUN);
    }

    /**
     * Test cyclicTimer function.
     */
    @Test
    public void testCyclicTimer1() {
        test(Results.CYCLICTIMER1);
    }

    /**
     * Test cyclicTimer function.
     */
    @Test
    public void testCyclicTimer2() {
        test(Results.CYCLICTIMER2);
    }

    /**
     * Test cyclicTimer function.
     */
    @Test
    public void testCyclicTimer3() {
        test(Results.CYCLICTIMER3);
    }

    /**
     * Test limitedMemory function.
     */
    @Test
    public void testLimitedMemory1() {
        test(Results.LIMITED_MEMORY1);
    }

    /**
     * Test limitedMemory function.
     */
    @Test
    public void testLimitedMemory2() {
        test(Results.LIMITED_MEMORY2);
    }

    /**
     * Test TFilter function.
     */
    @Test
    public void testTFilter() {
        test(Results.TFILTER);
    }

    /**
     * Test summarize.pt.
     */
    @Test
    public void testSummarize() {
        test(Results.SUMMARIZE);
    }

    /**
     * Test C.pt.
     */
    @Test
    public void testC() {
        test(Results.C, DELTA_2);
    }

    // /**
    // * Test CMultisum.pt.
    // */
    // @Test
    // public void testCMultisum() {
    // testDoubles(Results.CMULTISUM, DELTA_2);
    // }

    /**
     * Test gossip.pt.
     */
    @Test
    public void testGossip() {
        test(Results.GOSSIP);
    }

    /**
     * Test gossip2.pt.
     */
    @Test
    public void testGossip2() {
        test(Results.GOSSIP2);
    }

    /**
     * Test gossip3.pt.
     */
    @Test
    public void testGossip3() {
        test(Results.GOSSIP3);
    }

    /**
     * Test boundedSpreading.pt.
     */
    @Test
    public void testBoundedSpreading() {
        test(Results.BOUNDED_SPREADING, DELTA_2);
    }

    /**
     * Test constrainSpreading.pt.
     */
    @Test
    public void testConstrainSpreading() {
        test(Results.CONSTRAIN_SPREADING);
    }

    /**
     * Test gossipEver.pt.
     */
    @Test
    public void testGossipEver() {
        test(Results.GOSSIP_EVER);
    }

    /**
     * Test opinionFeedback.pt.
     */
    @Test
    public void testOpinionFeedback() {
        test(Results.OPINION_FEEDBACK);
    }

    /**
     * Test laplacianConsensus.pt.
     */
    @Test
    public void testLaplacianConsensus() {
        test(Results.LAPLACIAN_CONSENSUS, DELTA_2);
    }

    /**
     * Test voronoiPartitioning.pt.
     */
    @Test
    public void testVoronoiPartitioning() {
        test(Results.VORONOI_PARTITIONING);
    }

    /**
     * Test channel.pt.
     */
    @Test
    public void testChannel() {
        test(Results.CHANNEL);
    }

    /**
     * Test channel2.pt.
     */
    @Test
    public void testChannel2() {
        test(Results.CHANNEL2);
    }

    /*
     * From this point the rest of the file is not tests, but utility methods
     */

    private static Triple<SimulationTest, Object[], Object[]> setTest(final TestConfig testConfig, final int round) {
        SimulationTest sim = new SimulationTest(testConfig.getFileName(), round, testConfig.getDistance());
        for (Pair<Position, Object[][]> group : testConfig.getExpectedResultGroups()) {
            Object[][] g = group.getRight();
            sim.addGroup(Pair.of(group.getLeft(), Pair.of(g[0].length, g.length)));
        }
        sim.createNetwork();
        for (Triple<Integer, String, Object> t : testConfig.getProperties()) {
            if (t.getLeft() == TestConfig.ALL) {
                sim.setPropertyToAll(t.getMiddle(), t.getRight());
            } else {
                sim.setProperty(t.getLeft(), t.getMiddle(), t.getRight());
            }
        }
        Object[] res = testConfig.getExpectedResult(), simRes = sim.getResults();
        for (int i = 0; i < res.length; i++) {
            if (res[i].equals(TestConfig.DC)) {
                simRes[i] = TestConfig.DC;
            }
        }
        return Triple.of(sim, res, simRes);
    }

    private static Pair<double[], double[]> setTestDouble(final TestConfig testConfig, final int round) {
        Triple<SimulationTest, Object[], Object[]> res = setTest(testConfig, testConfig.getMaxRound());
        return convert(res.getMiddle(), res.getRight());
    }

    private static Pair<double[], double[]> convert(final Object[] left, final Object[] right) {
        double[] l = new double[left.length], r = new double[right.length];
        for (int i = 0; i < l.length; i++) {
            l[i] = (double) left[i];
            r[i] = (double) right[i];
        }
        return Pair.of(l, r);
    }

    private static void test(final TestConfig testConfig) {
        Triple<SimulationTest, Object[], Object[]> res = setTest(testConfig, testConfig.getMaxRound());
        assertArrayEquals(res.getMiddle(), res.getRight());
    }

    private static void test(final TestConfig testConfig, final double delta) {
        Pair<double[], double[]> res = setTestDouble(testConfig, testConfig.getMaxRound());
        assertArrayEquals(res.getLeft(), res.getRight(), delta);
    }

    // private static final String SL_NAME = "singleLineComment";
    // private static final String ML_NAME = "multilineComment";
    // private static final String EXPECTED = "EXPECTED_RESULT:";
    // private static final Pattern EXTRACT_RESULT = Pattern.compile(//
    // ".*?" + EXPECTED + "\\s*(?<" + ML_NAME + ">.*?)\\s*\\*\\/" + "|" //
    // + "\\/\\/\\s*" + EXPECTED + "\\s*(?<" + SL_NAME + ">.*?)\\s*\\n", //
    // Pattern.DOTALL);
    // private static final Pattern CYCLE = Pattern.compile("\\$CYCLE");
    //
    // private static void testFileWithMultipleRuns(final TestConfig tc, final
    // int min, final int max) {
    // IntStreams.rangeClosed(min, max).forEach(i -> {
    // testFile(tc, i);
    // });
    // }
    //
    // private static void testFile(final TestConfig tc, final int runs) {
    // final Triple<SimulationTest, Object[], Object[]> res = setTest(tc, runs);
    // final Object[] execResult = res.getRight();
    // final InputStream is = TestApis.class.getResourceAsStream("/" +
    // tc.getFileName() + ".pt");
    // try {
    // final String test = IOUtils.toString(is, StandardCharsets.UTF_8);
    // final Matcher extractor = EXTRACT_RESULT.matcher(test);
    // if (extractor.find()) {
    // String result = extractor.group(ML_NAME);
    // if (result == null) {
    // result = extractor.group(SL_NAME);
    // }
    // final String toCheck =
    // CYCLE.matcher(result).replaceAll(Integer.toString(runs));
    // final ProtelisVM vm = new ProtelisVM(ProtelisLoader.parse(toCheck), new
    // DummyContext());
    // vm.runCycle();
    // assertEquals(vm.getCurrentValue(),
    // execResult instanceof Number ? ((Number) execResult).doubleValue() :
    // execResult);
    // } else {
    // fail("Your test does not include the expected result");
    // }
    // } catch (IOException e) {
    // fail(LangUtils.stackTraceToString(e));
    // }
    // }
}
