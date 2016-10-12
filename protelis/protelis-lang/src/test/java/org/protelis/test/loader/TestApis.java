package org.protelis.test.loader;

import org.junit.Test;

/**
 * Testing Protelis core libraries.
 */
public class TestApis {

    /**
     * Test the number of neighbors of each device.
     */
    @Test
    public void testNeighborhood() {
        test("neighborhood");
    }

    /**
     * Test distanceTo function.
     */
    @Test
    public void testDistanceTo() {
        test("distanceTo");
    }

    /**
     * Test distanceTo with obstacle.
     */
    @Test
    public void testDistanceToWithObstacle() {
        test("obstacle");
    }

    // /**
    // * Test forecastObstacle.
    // */
    // @Test
    // public void testForecastObstacle() {
    // test(Results.FORECAST_OBSTACLE, DELTA_2);
    // }

    /**
     * Test broadcast function.
     */
    @Test
    public void testBroadcast() {
        test("broadcast");
    }

    /**
     * Test self.nbrRange.
     */
    @Test
    public void testNbrRange() {
        test("nbrRange");
    }

    /**
     * Test addRange.
     */
    @Test
    public void testAddRange() {
        test("addRange");
    }

    /**
     * Test G.pt.
     */
    @Test
    public void testG() {
        test("G");
    }

    /**
     * Test distance.pt.
     */
    @Test
    public void testDistance() {
        test("distance");
    }

    /**
     * Test T function.
     */
    @Test
    public void testT() {
        test("T");
    }

    /**
     * Test cyclicTimer function.
     */
    @Test
    public void testCyclicTimer1() {
        test("cyclickTimer");
    }

    /**
     * Test cyclicTimer function.
     */
    @Test
    public void testCyclicTimer2() {
        test("cyclickTimer");
    }

    /**
     * Test cyclicTimer function.
     */
    @Test
    public void testCyclicTimer3() {
        test("cyclickTimer");
    }

    /**
     * Test limitedMemory function.
     */
    @Test
    public void testLimitedMemory1() {
        test("limitedMemory");
    }

    /**
     * Test limitedMemory function.
     */
    @Test
    public void testLimitedMemory2() {
        test("limitedMemory");
    }

    /**
     * Test TFilter function.
     */
    @Test
    public void testTFilter() {
        test("tfilter");
    }

    /**
     * Test summarize.pt.
     */
    @Test
    public void testSummarize() {
        test("summarize");
    }

    /**
     * Test C.pt.
     */
    @Test
    public void testC() {
        test("C");
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
        test("gossip");
    }

    /**
     * Test gossip2.pt.
     */
    @Test
    public void testGossip2() {
        test("gossip2");
    }

    /**
     * Test gossip3.pt.
     */
    @Test
    public void testGossip3() {
        test("gossip3");
    }

    /**
     * Test boundedSpreading.pt.
     */
    @Test
    public void testBoundSpreading() {
        test("boundSpreading");
    }

    /**
     * Test constrainSpreading.pt.
     */
    @Test
    public void testConstrainSpreading() {
        test("constrainSpreading");
    }

    /**
     * Test gossipEver.pt.
     */
    @Test
    public void testGossipEver() {
        test("gossipEver");
    }

    /**
     * Test opinionFeedback.pt.
     */
    @Test
    public void testOpinionFeedback() {
        test("opinionFeedback");
    }

    /**
     * Test laplacianConsensus.pt.
     */
    @Test
    public void testLaplacianConsensus() {
        test("laplacianConsensus");
    }

    /**
     * Test voronoiPartitioning.pt.
     */
    @Test
    public void testVoronoiPartitioning() {
        test("voronoiPartitioning");
    }

    /**
     * Test channel.pt.
     */
    @Test
    public void testChannel() {
        test("channel");
    }

    /**
     * Test channel2.pt.
     */
    @Test
    public void testChannel2() {
        test("channel2");
    }

    /*
     * From this point the rest of the file is not tests, but utility methods
     */

    private static void test(final String file) {
        test(file, Tester.EXAMPLE_RUNS);
    }

    private static void test(final String file, final int runs) {
        Tester.test(file, runs);
    }
}
