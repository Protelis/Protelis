
import java.io.IOException;

import org.junit.Test;
import org.protelis.test.InfrastructureTester;
import org.protelis.test.ProgramTester;

/**
 * Testing Protelis core libraries.
 */
public class TestApis {

    private static void test(final String file) {
        test(file, InfrastructureTester.EXAMPLE_RUNS);
    }

    private static void test(final String file, final int runs) {
        try {
            InfrastructureTester.test(file, runs);
        } catch (InterruptedException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void test(final String file, final Object value) {
        try {
            InfrastructureTester.test(file, value);
        } catch (InterruptedException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void testMultirun(final String file) {
        try {
            InfrastructureTester.testMultirun(file);
        } catch (InterruptedException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void testProgram(final String file) {
        ProgramTester.testFile(file);
    }

    /**
     * Test addRange.pt.
     */
    @Test
    public void testAddRange() {
        test("addRange");
    }

    /**
     * Test addRangeWithLag.pt.
     */
    @Test
    public void testAddRangeWithLag() {
        test("addRangeWithLag");
    }

    /**
     * Test dilate.pt.
     */
    @Test
    public void testDilate() {
        test("dilate");
    }

    /**
     * Test alignedMapIff.pt.
     */
    @Test
    public void testAlignedMapIff() {
        testProgram("alignedMapIff");
    }

    /**
     * Test alignedMapMr.pt.
     */
    @Test
    public void testAlignedMapMr() {
        test("alignedMapMr");
    }

    /**
     * Test allTime.pt.
     */
    @Test
    public void testAllTime() {
        testProgram("allTime");
    }

    /**
     * Test anyTime.pt.
     */
    @Test
    public void testAnyTime() {
        testProgram("anyTime");
    }

    /**
     * Test applyWhile.pt.
     */
    @Test
    public void testApplyWhile() {
        testProgram("applyWhile");
    }

    // /**
    // * Test alignedMapSummarize.pt.
    // */
    // @Test
    // public void testAlignedMapSummarize() {
    // test("alignedMapSummarize");
    // }

    /**
     * Test average.pt.
     */
    @Test
    public void testAverage() {
        test("average");
    }

    /**
     * Test boundBroadcast.pt.
     */
    @Test
    public void testBoundBroadcast() {
        test("boundBroadcast");
    }

    /**
     * Test boundG.pt.
     */
    @Test
    public void testBoundG() {
        test("boundG");
    }

    /**
     * Test boundedSpreading.pt.
     */
    @Test
    public void testBoundSpreading() {
        test("boundSpreading");
    }

    /**
     * Test boundSpreadingWithRange.pt.
     */
    @Test
    public void testBoundSpreadingWithRange() {
        test("boundSpreadingWithRange");
    }

    /**
     * Test broadcast.pt.
     */
    @Test
    public void testBroadcast() {
        test("broadcast");
    }

    /**
     * Test C.pt.
     */
    @Test
    public void testC() {
        test("C");
    }

    /**
     * Test canSee.pt.
     */
    @Test
    public void testCanSee() {
        test("canSee");
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

    /**
     * Test closerThan.pt.
     */
    @Test
    public void testCloserThan() {
        test("closerThan");
    }

    /**
     * Test cMultiMax.pt.
     */
    @Test
    public void testCMultiMax() {
        test("cMultiMax");
    }

    /**
     * Test cMultiMin.pt.
     */
    @Test
    public void testCMultiMin() {
        test("cMultiMin");
    }

    /**
     * Test cMultiSum.pt.
     */
    @Test
    public void testCMultiSum() {
        test("cMultiSum");
    }

    /**
     * Test computeMultiRegion.pt.
     */
    @Test
    public void testComputeMultiRegion() {
        test("computeMultiRegion");
    }

    /**
     * Test computeMultiRegion2.pt.
     */
    @Test
    public void testComputeMultiRegion2() {
        test("computeMultiRegion2");
    }

    /**
     * Test computeMultiRegion3.pt.
     */
    @Test
    public void testComputeMultiRegion3() {
        test("computeMultiRegion3");
    }

    /**
     * Test countDown.pt.
     */
    @Test
    public void testCountDown() {
        testProgram("countDown");
    }

    /**
     * Test countTrue.pt.
     */
    @Test
    public void testCountTrue() {
        testProgram("countTrue");
    }

    /**
     * Test CRFgradient.pt.
     */
    @Test
    public void testCRFGradient() {
        test("CRFgradient");
    }

    /**
     * Test CRFgradient2.pt.
     */
    @Test
    public void testCRFGradient2() {
        test("CRFgradient2");
    }

    /**
     * Test cyclicFunction.pt.
     */
    @Test
    public void testCyclicFunction() {
        testProgram("cyclicFunction");
    }

    /**
     * Test cyclicTimer function.
     */
    @Test
    public void testCyclicTimer() {
        testMultirun("cyclicTimer");
    }

    /**
     * Test cyclicTimer function.
     */
    @Test
    public void testCyclicTimer2() {
        testProgram("cyclicTimer2");
    }

    /**
     * Test delta.pt.
     */
    @Test
    public void testDelta() {
        testProgram("delta");
    }

    /**
     * Test diameter.pt.
     */
    @Test
    public void testDiameter() {
        test("diameter");
    }

    /**
     * Test distanceBetween.pt.
     */
    @Test
    public void testDistanceBetween() {
        test("distanceBetween");
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
        test("boundDistanceTo");
    }

    /**
     * Test ebfFilter.pt.
     */
    @Test
    public void testEbfFilter() {
        testProgram("ebfFilter");
    }

    /**
     * Test findParent.pt.
     */
    @Test
    public void testFindParent() {
        test("findParentId");
    }

    /**
     * Test flexGradient.pt.
     */
    @Test
    public void testFlexGradient() {
        test("flexGradient");
    }

    /**
     * Test flip.pt.
     */
    @Test
    public void testFlip() {
        testProgram("flip");
    }

    /**
     * Test forecastObstacle.pt.
     */
    @Test
    public void testForecastObstacle() {
        test("forecastObstacle");
    }

    /**
     * Test G.pt.
     */
    @Test
    public void testG() {
        test("G");
    }

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
     * Test gossipEver.pt.
     */
    @Test
    public void testGossipEver() {
        test("gossipEver");
    }

    /**
     * Test gradcast.pt.
     */
    @Test
    public void testGradcast() {
        test("gradcast");
    }

    /**
     * Test gradient.pt.
     */
    @Test
    public void testGradient() {
        test("gradient");
    }

    /**
     * Test isEdge.pt.
     */
    @Test
    public void testIsEdge() {
        test("isEdge");
    }

    /**
     * Test isRecentEvent.pt.
     */
    @Test
    public void testIsRecentEvent() {
        testProgram("isRecentEvent");
    }

    /**
     * Test isRisingEdge.pt.
     */
    @Test
    public void testIsRisingEdge() {
        testProgram("isRisingEdge");
    }

    /**
     * Test isSignalStable.pt.
     */
    @Test
    public void testIsSignalStable() {
        testProgram("isSignalStable");
    }

    /**
     * Test iterate.pt.
     */
    @Test
    public void testIterate() {
        testProgram("iterate");
    }

    /**
     * Test laplacianConsensus.pt.
     */
    @Test
    public void testLaplacianConsensus() {
        test("laplacianConsensus");
    }

    /**
     * Test limitedMemory function.
     */
    @Test
    public void testLimitedMemory() {
        testMultirun("limitedMemory");
    }

    /**
     * Test logic.pt.
     */
    @Test
    public void testLogic() {
        testProgram("logic");
    }

    /**
     * Test nbrLag.pt.
     */
    @Test
    public void testNbrLag() {
        test("nbrLag");
    }

    /**
     * Test self.nbrRange().
     */
    @Test
    public void testNbrRange() {
        test("nbrRange");
    }

    /**
     * Test nbrRangeHop.pt.
     */
    @Test
    public void testNbrRangeHop() {
        test("nbrRangeHop");
    }

    /**
     * Test the number of neighbors of each device.
     */
    @Test
    public void testNeighborhood() {
        test("neighborhood");
    }

    /**
     * Test opinionFeedback.pt.
     */
    @Test
    public void testOpinionFeedback() {
        test("opinionFeedback");
    }

    /**
     * Test range.pt.
     */
    @Test
    public void testRange() {
        testProgram("range");
    }

    /**
     * Test reduce.pt.
     */
    @Test
    public void testReduce() {
        testProgram("reduce");
    }

    /**
     * Test rendezvous.pt.
     */
    @Test
    public void testRendezvous() {
        test("rendezvous");
    }

    /**
     * Test S function.
     */
    @Test
    public void testS() {
        test("S", true);
    }

    /**
     * Test summarize.pt.
     */
    @Test
    public void testSummarize() {
        test("summarize");
    }

    /**
     * Test T function.
     */
    @Test
    public void testT() {
        test("T");
    }

    /**
     * Test TFilter function.
     */
    @Test
    public void testTFilter() {
        test("tfilter");
    }

    /**
     * Test tick.pt.
     */
    @Test
    public void testTick() {
        testProgram("tick");
    }

    /**
     * Test timeReplicated.pt.
     */
    @Test
    public void testTimeReplication() {
        test("timeReplicated");
    }

    /**
     * Test trueDuringLast.pt.
     */
    @Test
    public void testTrueDuringLast() {
        testProgram("trueDuringLast");
    }

    /**
     * Test trueFor.pt.
     */
    @Test
    public void testTrueFor() {
        testProgram("trueFor");
    }

    /**
     * Test trueOnceAfter.pt.
     */
    @Test
    public void testTrueOnceAfter() {
        testProgram("trueOnceAfter");
    }

    /**
     * Test trueOnceEvery.pt.
     */
    @Test
    public void testTrueOnceEvery() {
        testProgram("trueOnceEvery");
    }

    /**
     * Test valueChanged.pt.
     */
    @Test
    public void testValueChanged() {
        testProgram("valueChanged");
    }

    /**
     * Test vm.pt.
     */
    @Test
    public void testVm() {
        test("vm");
    }

    /**
     * Test voronoiPartitioning.pt.
     */
    @Test
    public void testVoronoiPartitioning() {
        test("voronoiPartitioning");
    }

    /**
     * Test wait.pt.
     */
    @Test
    public void testWait() {
        testProgram("wait");
    }

    /**
     * Test waitAndApply.pt.
     */
    @Test
    public void testWaitAndApply() {
        testProgram("waitAndApply");
    }

}
