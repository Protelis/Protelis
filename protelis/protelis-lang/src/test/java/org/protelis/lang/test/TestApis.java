package org.protelis.lang.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.protelis.test.InfrastructureTester;
import org.protelis.test.ProgramTester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Testing Protelis core libraries.
 */
public class TestApis {
    private static final Logger L = LoggerFactory.getLogger(TestApis.class);

    private static void test(final String file) {
        test(file, InfrastructureTester.SIMULATION_STEPS, InfrastructureTester.STABILITY_STEPS);
    }

    private static void test(final String file, final int simulationSteps, final int stabilitySteps) {
        InfrastructureTester.runTest(file, simulationSteps, stabilitySteps);
    }

    private static void testMultirun(final String file) {
        InfrastructureTester.multiRunTest(file);
    }

    private static void testProgram(final String file) {
        ProgramTester.runFileWithMultipleRuns(file);
    }

    /**
     * Print the current method name.
     */
    @Rule
    //CHECKSTYLE:OFF
    public final TestRule watcher = new TestWatcher() {
    //CHECKSTYLE:ON
        protected void starting(final Description description) {
            L.info(description.getMethodName());
        }
    };

    /**
     * Test aggregation.pt.
     */
    @Test
    public void testAggregation() {
        test("aggregation");
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
     * Test alignedMapDistanceTo.pt.
     */
    @Test
    public void testAlignedMapDistanceTo() {
        test("alignedMapDistanceTo");
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
     * Test applyOnTree.pt.
     */
    @Test
    public void testApplyOnTree() {
        test("applyOnTree");
    }

    /**
     * Test applyWhile.pt.
     */
    @Test
    public void testApplyWhile() {
        testProgram("applyWhile");
    }

    /**
     * Test ascendBranch.pt.
     */
    @Test
    public void testAscendBranch() {
        test("ascendBranch");
    }

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
     * Test broadcast.pt.
     */
    @Test
    public void testBroadcast() {
        test("broadcast");
    }

    /**
     * Test broadcast.pt with isolated nodes.
     */
    @Test
    public void testBroadcast2() {
        test("broadcast2");
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
     * Test checkChain.pt.
     */
    @Test
    public void testCheckChain() {
        test("checkChain");
    }

    /**
     * Test checkChain.pt.
     */
    @Test
    public void testCheckChain2() {
        test("checkChain2");
    }

    /**
     * Test checkChain.pt.
     */
    @Test
    public void testCheckChain3() {
        test("checkChain3");
    }

    /**
     * Test checkChain.pt.
     */
    @Test
    public void testCheckChain4() {
        test("checkChain4");
    }

    /**
     * Test checkChain.pt.
     */
    @Test
    public void testCheckChain5() {
        test("checkChain5");
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
     * Test multiRegion.pt.
     */
    @Test
    public void testMultiRegion() {
        test("multiRegion");
    }

    /**
     * Test multiRegion2.pt.
     */
    @Test
    public void testMultiRegion2() {
        test("multiRegion2");
    }

    /**
     * Test computeMultiRegion3.pt.
     */
    @Test
    public void testMultiRegion3() {
        test("multiRegion3");
    }

    /**
     * Test countDevices.pt.
     */
    @Test
    public void testCountDevices() {
        test("countDevices");
    }

    /**
     * Test countDevicesInRegion.pt.
     */
    @Test
    public void testCountDevicesInRegion() {
        test("countDevicesInRegion");
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
        test("crfGradient");
    }

    /**
     * Test CRFgradient2.pt.
     */
    @Test
    public void testCRFGradient2() {
        test("crfGradient2");
    }

    /**
     * Test bisGradient.pt.
     */
    @Test
    public void testBISGradient() {
        test("bisGradient");
    }

    /**
     * Test bisGradient2.pt.
     */
    @Test
    public void testBISGradient2() {
        test("bisGradient2");
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
     * Test descendBranch.pt.
     */
    @Test
    public void testDescendBranch() {
    }

    /**
     * Test diameter.pt.
     */
    @Test
    public void testDiameter() {
        test("diameter");
    }

    /**
     * Test diameterInArea.pt.
     */
    @Test
    public void testDiameterInArea() {
        test("diameterInArea");
    }

    /**
     * Test dilate.pt.
     */
    @Test
    public void testDilate() {
        test("dilate");
    }

    /**
     * Test directProjection.pt.
     */
    @Test
    public void testDirectProjection() {
        test("directProjection");
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
     * Test distanceTo2.pt.
     */
    @Test
    public void testDistanceTo2() {
        test("distanceTo2");
    }

    /**
     * Test distanceToWithMetric.pt.
     */
    @Test
    public void testDistanceToWithMetric() {
        test("distanceToWithMetric");
    }

    /**
     * Test boundDistanceTo.pt.
     */
    @Test
    public void testBoundDistanceTo() {
        test("boundDistanceTo");
    }

    /**
     * Test evaporation.pt.
     */
    @Test
    public void testEvaporation() {
        testProgram("evaporation");
    }

    /**
     * Test ebfFilter.pt.
     */
    @Test
    public void testExponentialBackoffFilter() {
        testProgram("exponentialBackoffFilter");
    }

    /**
     * Test falseAfterTime.pt.
     */
    @Test
    public void testFalseAfterTime() {
        testProgram("falseAfterTime");
    }

    /**
     * Test falseDuringLast.pt.
     */
    @Test
    public void testFalseDuringLast() {
        testProgram("falseDuringLast");
    }

    /**
     * Test falseFor.pt.
     */
    @Test
    public void testFalseFor() {
        testProgram("falseFor");
    }

    /**
     * Test flexGradient.pt.
     */
    @Test
    public void testFlexGradient() {
        test("flexGradient");
    }

    /**
     * Test flexGradient2.pt.
     */
    @Test
    public void testFlexGradient2() {
        test("flexGradient2");
    }

    /**
     * Test flexGradient3.pt.
     */
    @Test
    public void testFlexGradient3() {
        test("flexGradient3");
    }

    /**
     * Test flip.pt.
     */
    @Test
    public void testFlip() {
        testProgram("flip");
    }

    /**
     * Test G.pt.
     */
    @Test
    public void testG() {
        test("G");
    }

    /**
     * Test getAllChildren.pt.
     */
    @Test
    public void testGetAllChildren() {
        test("getAllChildren");
    }

    /**
     * Test getAllChildrenIds.pt.
     */
    @Test
    public void testGetAllChildrenIds() {
        test("getAllChildrenIds");
    }

    /**
     * Test getChildren.pt.
     */
    @Test
    public void testGetChildren() {
        test("getChildren");
    }

    /**
     * Test getChildrenIds.pt.
     */
    @Test
    public void testGetChildrenIds() {
        test("getChildrenIds");
    }

    /**
     * Test getParent.pt.
     */
    @Test
    public void testGetParentId() {
        test("getParentId");
    }

    /**
     * Test getParents.pt.
     */
    @Test
    public void testGetParentIds() {
        test("getParentIds");
    }

    /**
     * Test getParents.pt.
     */
    @Test
    public void testGetParents() {
        test("getParents");
    }

    /**
     * Test Gnull.pt.
     */
    @Test
    public void testGnull() {
        testProgram("Gnull");
    }

    /**
     * Test gossip.pt.
     */
    @Test
    public void testGossip() {
        test("gossip");
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
     * Test gradcast2.pt.
     */
    @Test
    public void testGradcast2() {
        test("gradcast2");
    }

    /**
     * Test gradcast3.pt.
     */
    @Test
    public void testGradcast3() {
        test("gradcast3");
    }

    /**
     * Test gradcast4.pt.
     */
    @Test
    public void testGradcast4() {
        test("gradcast4");
    }

    /**
     * Test gradcast5.pt.
     */
    @Test
    public void testGradcast5() {
        test("gradcast5");
    }

    /**
     * Test gradient.pt.
     */
    @Test
    public void testGradient() {
        test("gradient");
    }

    /**
     * Test hopBroadcast function.
     */
    @Test
    public void testHopBroadcast() {
        test("hopBroadcast");
    }

    /**
     * Test hopDistanceTo function.
     */
    @Test
    public void testHopDistanceTo() {
        test("hopDistanceTo");
    }

    /**
     * Test multiC.pt.
     */
    @Test
    public void testMultiC() {
        test("multiC");
    }

    /**
     * Test multiG.pt.
     */
    @Test
    public void testMultiG() {
        test("multiG");
    }

    /**
     * Test multiGradient.pt.
     */
    @Test
    public void testMultiGradient() {
        test("multiGradient");
    }

    /**
     * Test greatestLowerBound.pt.
     */
    @Test
    public void testGreatestLowerBound() {
        test("greatestLowerBound");
    }

    /**
     * Test greatestLowerBound.pt.
     */
    @Test
    public void testGreatestLowerBound2() {
        test("greatestLowerBound2");
    }

    /**
     * Test hasNoParent.pt.
     */
    @Test
    public void testHasNoParent() {
        testProgram("hasNoParent");
    }

    /**
     * Test isEdge.pt.
     */
    @Test
    public void testIsEdge() {
        test("isEdge");
    }

    /**
     * Test isFallingEdge.pt.
     */
    @Test
    public void testIsFallingEdge() {
        testProgram("isFallingEdge");
    }

    /**
     * Test isLeaf.pt.
     */
    @Test
    public void testIsLeaf() {
        test("isLeaf");
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
     * Test isRoot.pt.
     */
    @Test
    public void testIsRoot() {
        test("isRoot");
    }

    /**
     * Test isSignalStable.pt.
     */
    @Test
    public void testIsSignalStable() {
        testProgram("isSignalStable");
    }

    /**
     * Test isValueChanged.pt.
     */
    @Test
    public void testIsValueChanged() {
        testProgram("isValueChanged");
    }

    /**
     * Test laplacianConsensus.pt.
     */
    @Test
    public void testLaplacianConsensus() {
        test("laplacianConsensus");
    }

    /**
     * Test leastUpperBound.pt.
     */
    @Test
    public void testLeastUpperBound() {
        test("leastUpperBound");
    }

    /**
     * Test limitedMemory function.
     */
    @Test
    public void testLimitedMemory() {
        testMultirun("limitedMemory");
    }

    /**
     * Test nbrDelay.pt.
     */
    @Test
    public void testNbrDelay() {
        test("nbrDelay");
    }

    /**
     * Test nbrLag.pt.
     */
    @Test
    public void testNbrLag() {
        test("nbrLag");
    }

    /**
     * Test nbrRange.pt.
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
     * Test nbrVector.pt.
     */
    @Test
    public void testNbrVector() {
        test("nbrVector");
    }

    /**
     * Test the number of neighbors of each device.
     */
    @Test
    public void testNeighborhood() {
        test("neighborhood");
    }

    /**
     * Test once.pt.
     */
    @Test
    public void testOnce() {
        testProgram("once");
    }

    /**
     * Test postProcessAndApply.pt.
     */
    @Test
    public void testPostProcessAndApply() {
        test("postProcessAndApply");
    }

    /**
     * Test preProcessAndApply.pt.
     */
    @Test
    public void testPreProcessAndApply() {
        test("preProcessAndApply");
    }

    /**
     * Test processAndApply.pt.
     */
    @Test
    public void testProcessAndApply() {
        test("processAndApply");
    }

    /**
     * Test publishSubscribe.pt.
     */
    @Test
    public void testPublishSubscribe() {
        test("publishSubscribe");
    }

    /**
     * Test quorumSensing.pt.
     */
    @Test
    public void testQuorumSensing() {
        test("quorumSensing");
    }

    /**
     * Test quorumSensingWithCondition.pt.
     */
    @Test
    public void testQuorumSensingWithCondition() {
        test("quorumSensingWithCondition");
    }

    /**
     * Test range.pt.
     */
    @Test
    public void testRange() {
        testProgram("range");
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
        test("S");
    }

    /**
     * Test sequence.pt.
     */
    @Test
    public void testSequence() {
        testProgram("sequence");
    }

    /**
     * Test sequenceIfAll.pt.
     */
    @Test
    public void testSequenceIfAll() {
        testMultirun("sequenceIfAll");
    }

    /**
     * Test sequenceIfAny.pt.
     */
    @Test
    public void testSequenceIfAny() {
        testMultirun("sequenceIfAny");
    }

    /**
     * Test spanningTree.pt.
     */
    @Test
    public void testSpanningTree() {
        test("spanningTree");
    }

    /**
     * Test summarize.pt.
     */
    @Test
    public void testSummarize() {
        test("summarize");
    }

    /**
     * Test summarizeWithPotential.pt.
     */
    @Test
    public void testSummarizeWithPotential() {
        test("summarizeWithPotential");
    }

    /**
     * Test T function.
     */
    @Test
    public void testT() {
        test("T");
    }

    /**
     * Test timeReplication.pt.
     */
    @Test
    public void testTimeReplication() {
        test("timeReplication");
    }

    /**
     * Test timeSinceStart.pt.
     */
    @Test
    public void testTimeSinceStart() {
        testProgram("timeSinceStart");
    }

    /**
     * Test trueAfterTime.pt.
     */
    @Test
    public void testTrueAfterTime() {
        testProgram("trueAfterTime");
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
     * Test utils.pt.
     */
    @Test
    public void testUtils() {
        testProgram("utils");
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
