
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
        test(Results.DISTANCETO);
    }

    /**
     * Test distanceTo with obstacle.
     */
    @Test
    public void testDistanceToWithObstacle() {
        test(Results.OBSTACLE);
    }

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
        test(Results.NBRRANGE);
    }

    /**
     * Test addRange.
     */
    @Test
    public void testAddRange() {
        test(Results.ADDRANGE);
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
        test(Results.DISTANCE);
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
     * Test CMultisum.pt.
     */
    @Test
    public void testCMultisum() {
        test(Results.CMULTISUM);
    }

    /**
     * Test gossip.pt.
     */
    @Test
    public void testGossip() {
        test(Results.GOSSIP);
    }

    /**
     * Test boundedSpreading.pt.
     */
    @Test
    public void testBoundedSpreading() {
        testDoubles(Results.BOUNDED_SPREADING, 0.01);
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
        testDoubles(Results.LAPLACIAN_CONSENSUS, 0.01);
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

    private Pair<Object[], Object[]> setTest(final TestConfig testConfig) {
        SimulationTest sim = new SimulationTest(testConfig.getFileName(), testConfig.getMaxRound(),
                        testConfig.getDistance());
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
        return Pair.of(res, simRes);
    }

    private void test(final TestConfig testConfig) {
        Pair<Object[], Object[]> res = setTest(testConfig);
        assertArrayEquals(res.getLeft(), res.getRight());
    }

    private void testDoubles(final TestConfig testConfig, final double delta) {
        Pair<Object[], Object[]> res = setTest(testConfig);
        Object[] left = res.getLeft(), right = res.getRight();
        double[] l = new double[left.length], r = new double[right.length];
        for (int i = 0; i < l.length; i++) {
            l[i] = (double) left[i];
            r[i] = (double) right[i];
        }
        assertArrayEquals(l, r, delta);
    }

}
