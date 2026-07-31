/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.lang.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.protelis.test.InfrastructureTester;
import org.protelis.test.ProgramTester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Testing Protelis core libraries.
 */
@ExtendWith(TestApis.LoggingExtension.class)
class TestApis {

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
     * Test aggregation.pt.
     */
    @Test
    void testAggregation() {
        test("aggregation");
    }

    /**
     * Test addRange.pt.
     */
    @Test
    void testAddRange() {
        test("addRange");
    }

    /**
     * Test addRangeWithLag.pt.
     */
    @Test
    void testAddRangeWithLag() {
        test("addRangeWithLag");
    }

    /**
     * Test alignedMapDistanceTo.pt.
     */
    @Test
    void testAlignedMapDistanceTo() {
        test("alignedMapDistanceTo");
    }

    /**
     * Test alignedMapIff.pt.
     */
    @Test
    void testAlignedMapIff() {
        testProgram("alignedMapIff");
    }

    /**
     * Test alignedMapMr.pt.
     */
    @Test
    void testAlignedMapMr() {
        test("alignedMapMr");
    }

    /**
     * Test allTime.pt.
     */
    @Test
    void testAllTime() {
        testProgram("allTime");
    }

    /**
     * Test anyTime.pt.
     */
    @Test
    void testAnyTime() {
        testProgram("anyTime");
    }

    /**
     * Test applyOnTree.pt.
     */
    @Test
    void testApplyOnTree() {
        test("applyOnTree");
    }

    /**
     * Test applyWhile.pt.
     */
    @Test
    void testApplyWhile() {
        testProgram("applyWhile");
    }

    /**
     * Test ascendBranch.pt.
     */
    @Test
    void testAscendBranch() {
        test("ascendBranch");
    }

    /**
     * Test average.pt.
     */
    @Test
    void testAverage() {
        test("average");
    }

    /**
     * Test boundBroadcast.pt.
     */
    @Test
    void testBoundBroadcast() {
        test("boundBroadcast");
    }

    /**
     * Test boundG.pt.
     */
    @Test
    void testBoundedElection() {
        test("boundedElection");
    }

    /**
     * Test boundG.pt.
     */
    @Test
    void testBoundG() {
        test("boundG");
    }

    /**
     * Test boundedSpreading.pt.
     */
    @Test
    void testBoundSpreading() {
        test("boundSpreading");
    }

    /**
     * Test broadcast.pt.
     */
    @Test
    void testBroadcast() {
        test("broadcast");
    }

    /**
     * Test broadcast.pt with isolated nodes.
     */
    @Test
    void testBroadcast2() {
        test("broadcast2");
    }

    /**
     * Test C.pt.
     */
    @Test
    void testC() {
        test("C");
    }

    /**
     * Test canSee.pt.
     */
    @Test
    void testCanSee() {
        test("canSee");
    }

    /**
     * Test channel.pt.
     */
    @Test
    void testChannel() {
        test("channel");
    }

    /**
     * Test channel2.pt.
     */
    @Test
    void testChannel2() {
        test("channel2");
    }

    /**
     * Test checkChain.pt.
     */
    @Test
    void testCheckChain() {
        test("checkChain");
    }

    /**
     * Test checkChain.pt.
     */
    @Test
    void testCheckChain2() {
        test("checkChain2");
    }

    /**
     * Test checkChain.pt.
     */
    @Test
    void testCheckChain3() {
        test("checkChain3");
    }

    /**
     * Test checkChain.pt.
     */
    @Test
    void testCheckChain4() {
        test("checkChain4");
    }

    /**
     * Test checkChain.pt.
     */
    @Test
    void testCheckChain5() {
        test("checkChain5");
    }

    /**
     * Test closerThan.pt.
     */
    @Test
    void testCloserThan() {
        test("closerThan");
    }

    /**
     * Test cMultiMax.pt.
     */
    @Test
    void testCMultiMax() {
        test("cMultiMax");
    }

    /**
     * Test cMultiMin.pt.
     */
    @Test
    void testCMultiMin() {
        test("cMultiMin");
    }

    /**
     * Test cMultiSum.pt.
     */
    @Test
    void testCMultiSum() {
        test("cMultiSum");
    }

    /**
     * Test multiRegion.pt.
     */
    @Test
    void testMultiRegion() {
        test("multiRegion");
    }

    /**
     * Test multiRegion2.pt.
     */
    @Test
    void testMultiRegion2() {
        test("multiRegion2");
    }

    /**
     * Test computeMultiRegion3.pt.
     */
    @Test
    void testMultiRegion3() {
        test("multiRegion3");
    }

    /**
     * Test countDevices.pt.
     */
    @Test
    void testCountDevices() {
        test("countDevices");
    }

    /**
     * Test countDevicesInRegion.pt.
     */
    @Test
    void testCountDevicesInRegion() {
        test("countDevicesInRegion");
    }

    /**
     * Test countDown.pt.
     */
    @Test
    void testCountDown() {
        testProgram("countDown");
    }

    /**
     * Test countTrue.pt.
     */
    @Test
    void testCountTrue() {
        testProgram("countTrue");
    }

    /**
     * Test CRFgradient.pt.
     */
    @Test
    void testCRFGradient() {
        test("crfGradient");
    }

    /**
     * Test CRFgradient2.pt.
     */
    @Test
    void testCRFGradient2() {
        test("crfGradient2");
    }

    /**
     * Test bisGradient.pt.
     */
    @Test
    void testBISGradient() {
        test("bisGradient");
    }

    /**
     * Test bisGradient2.pt.
     */
    @Test
    void testBISGradient2() {
        test("bisGradient2");
    }

    /**
     * Test cyclicFunction.pt.
     */
    @Test
    void testCyclicFunction() {
        testProgram("cyclicFunction");
    }

    /**
     * Test cyclicTimer function.
     */
    @Test
    void testCyclicTimer() {
        testMultirun("cyclicTimer");
    }

    /**
     * Test cyclicTimer function.
     */
    @Test
    void testCyclicTimer2() {
        testProgram("cyclicTimer2");
    }

    /**
     * Test delta.pt.
     */
    @Test
    void testDelta() {
        testProgram("delta");
    }

    /**
     * Test descendBranch.pt.
     */
    @Test
    void testDescendBranch() {
    }

    /**
     * Test diameter.pt.
     */
    @Test
    void testDiameter() {
        test("diameter");
    }

    /**
     * Test diameterInArea.pt.
     */
    @Test
    void testDiameterInArea() {
        test("diameterInArea");
    }

    /**
     * Test dilate.pt.
     */
    @Test
    void testDilate() {
        test("dilate");
    }

    /**
     * Test directProjection.pt.
     */
    @Test
    void testDirectProjection() {
        test("directProjection");
    }

    /**
     * Test distanceBetween.pt.
     */
    @Test
    void testDistanceBetween() {
        test("distanceBetween");
    }

    /**
     * Test distanceTo function.
     */
    @Test
    void testDistanceTo() {
        test("distanceTo");
    }

    /**
     * Test distanceTo2.pt.
     */
    @Test
    void testDistanceTo2() {
        test("distanceTo2");
    }

    /**
     * Test distanceToWithMetric.pt.
     */
    @Test
    void testDistanceToWithMetric() {
        test("distanceToWithMetric");
    }

    /**
     * Test boundDistanceTo.pt.
     */
    @Test
    void testBoundDistanceTo() {
        test("boundDistanceTo");
    }

    /**
     * Test evaporation.pt.
     */
    @Test
    void testEvalAlignment() {
        test("evalAlignment82");
    }

    /**
     * Test evaporation.pt.
     */
    @Test
    void testEvaporation() {
        testProgram("evaporation");
    }

    /**
     * Test ebfFilter.pt.
     */
    @Test
    void testExponentialBackoffFilter() {
        testProgram("exponentialBackoffFilter");
    }

    /**
     * Test falseAfterTime.pt.
     */
    @Test
    void testFalseAfterTime() {
        testProgram("falseAfterTime");
    }

    /**
     * Test falseDuringLast.pt.
     */
    @Test
    void testFalseDuringLast() {
        testProgram("falseDuringLast");
    }

    /**
     * Test falseFor.pt.
     */
    @Test
    void testFalseFor() {
        testProgram("falseFor");
    }

    /**
     * Test flexGradient.pt.
     */
    @Test
    void testFlexGradient() {
        test("flexGradient");
    }

    /**
     * Test flexGradient2.pt.
     */
    @Test
    void testFlexGradient2() {
        test("flexGradient2");
    }

    /**
     * Test flexGradient3.pt.
     */
    @Test
    void testFlexGradient3() {
        test("flexGradient3");
    }

    /**
     * Test flip.pt.
     */
    @Test
    void testFlip() {
        testProgram("flip");
    }

    /**
     * Test G.pt.
     */
    @Test
    void testG() {
        test("G");
    }

    /**
     * Test getAllChildren.pt.
     */
    @Test
    void testGetAllChildren() {
        test("getAllChildren");
    }

    /**
     * Test getAllChildrenIds.pt.
     */
    @Test
    void testGetAllChildrenIds() {
        test("getAllChildrenIds");
    }

    /**
     * Test getChildren.pt.
     */
    @Test
    void testGetChildren() {
        test("getChildren");
    }

    /**
     * Test getChildrenIds.pt.
     */
    @Test
    void testGetChildrenIds() {
        test("getChildrenIds");
    }

    /**
     * Test getParent.pt.
     */
    @Test
    void testGetParentId() {
        test("getParentId");
    }

    /**
     * Test getParents.pt.
     */
    @Test
    void testGetParentIds() {
        test("getParentIds");
    }

    /**
     * Test getParents.pt.
     */
    @Test
    void testGetParents() {
        test("getParents");
    }

    /**
     * Test Gnull.pt.
     */
    @Test
    void testGnull() {
        testProgram("Gnull");
    }

    /**
     * Test gossip.pt.
     */
    @Test
    void testGossip() {
        test("gossip");
    }

    /**
     * Test gossip3.pt.
     */
    @Test
    void testGossip3() {
        test("gossip3");
    }

    /**
     * Test gossipEver.pt.
     */
    @Test
    void testGossipEver() {
        test("gossipEver");
    }

    /**
     * Test gradcast.pt.
     */
    @Test
    void testGradcast() {
        test("gradcast");
    }

    /**
     * Test gradcast2.pt.
     */
    @Test
    void testGradcast2() {
        test("gradcast2");
    }

    /**
     * Test gradcast3.pt.
     */
    @Test
    void testGradcast3() {
        test("gradcast3");
    }

    /**
     * Test gradcast4.pt.
     */
    @Test
    void testGradcast4() {
        test("gradcast4");
    }

    /**
     * Test gradcast5.pt.
     */
    @Test
    void testGradcast5() {
        test("gradcast5");
    }

    /**
     * Test gradient.pt.
     */
    @Test
    void testGradient() {
        test("gradient");
    }

    /**
     * Test hopBroadcast function.
     */
    @Test
    void testHopBroadcast() {
        test("hopBroadcast");
    }

    /**
     * Test hopDistanceTo function.
     */
    @Test
    void testHopDistanceTo() {
        test("hopDistanceTo");
    }

    /**
     * Test multiC.pt.
     */
    @Test
    void testMeanHood104() {
        test("meanHood");
    }

    /**
     * Test multiC.pt.
     */
    @Test
    void testMultiC() {
        test("multiC");
    }

    /**
     * Test multiG.pt.
     */
    @Test
    void testMultiG() {
        test("multiG");
    }

    /**
     * Test multiGradient.pt.
     */
    @Test
    void testMultiGradient() {
        test("multiGradient");
    }

    /**
     * Test greatestLowerBound.pt.
     */
    @Test
    void testGreatestLowerBound() {
        test("greatestLowerBound");
    }

    /**
     * Test greatestLowerBound.pt.
     */
    @Test
    void testGreatestLowerBound2() {
        test("greatestLowerBound2");
    }

    /**
     * Test hasNoParent.pt.
     */
    @Test
    void testHasNoParent() {
        testProgram("hasNoParent");
    }

    /**
     * Test isEdge.pt.
     */
    @Test
    void testIsEdge() {
        test("isEdge");
    }

    /**
     * Test isFallingEdge.pt.
     */
    @Test
    void testIsFallingEdge() {
        testProgram("isFallingEdge");
    }

    /**
     * Test isLeaf.pt.
     */
    @Test
    void testIsLeaf() {
        test("isLeaf");
    }

    /**
     * Test isRecentEvent.pt.
     */
    @Test
    void testIsRecentEvent() {
        testProgram("isRecentEvent");
    }

    /**
     * Test isRisingEdge.pt.
     */
    @Test
    void testIsRisingEdge() {
        testProgram("isRisingEdge");
    }

    /**
     * Test isRoot.pt.
     */
    @Test
    void testIsRoot() {
        test("isRoot");
    }

    /**
     * Test isSignalStable.pt.
     */
    @Test
    void testIsSignalStable() {
        testProgram("isSignalStable");
    }

    /**
     * Test isValueChanged.pt.
     */
    @Test
    void testIsValueChanged() {
        testProgram("isValueChanged");
    }

    /**
     * Test laplacianConsensus.pt.
     */
    @Test
    void testLaplacianConsensus() {
        test("laplacianConsensus");
    }

    /**
     * Test leastUpperBound.pt.
     */
    @Test
    void testLeastUpperBound() {
        test("leastUpperBound");
    }

    /**
     * Test limitedMemory function.
     */
    @Test
    void testLimitedMemory() {
        testMultirun("limitedMemory");
    }

    /**
     * Test nbrDelay.pt.
     */
    @Test
    void testNbrDelay() {
        test("nbrDelay");
    }

    /**
     * Test nbrLag.pt.
     */
    @Test
    void testNbrLag() {
        test("nbrLag");
    }

    /**
     * Test nbrRange.pt.
     */
    @Test
    void testNbrRange() {
        test("nbrRange");
    }

    /**
     * Test nbrRangeHop.pt.
     */
    @Test
    void testNbrRangeHop() {
        test("nbrRangeHop");
    }

    /**
     * Test nbrVector.pt.
     */
    @Test
    void testNbrVector() {
        test("nbrVector");
    }

    /**
     * Test the number of neighbors of each device.
     */
    @Test
    void testNeighborhood() {
        test("neighborhood");
    }

    /**
     * Test once.pt.
     */
    @Test
    void testOnce() {
        testProgram("once");
    }

    /**
     * Test postProcessAndApply.pt.
     */
    @Test
    void testPostProcessAndApply() {
        test("postProcessAndApply");
    }

    /**
     * Test preProcessAndApply.pt.
     */
    @Test
    void testPreProcessAndApply() {
        test("preProcessAndApply");
    }

    /**
     * Test processAndApply.pt.
     */
    @Test
    void testProcessAndApply() {
        test("processAndApply");
    }

    /**
     * Test publishSubscribe.pt.
     */
    @Test
    void testPublishSubscribe() {
        test("publishSubscribe");
    }

    /**
     * Test quorumSensing.pt.
     */
    @Test
    void testQuorumSensing() {
        test("quorumSensing");
    }

    /**
     * Test quorumSensingWithCondition.pt.
     */
    @Test
    void testQuorumSensingWithCondition() {
        test("quorumSensingWithCondition");
    }

    /**
     * Test range.pt.
     */
    @Test
    void testRange() {
        testProgram("range");
    }

    /**
     * Test rendezvous.pt.
     */
    @Test
    void testRendezvous() {
        test("rendezvous");
    }

    /**
     * Test S function.
     */
    @Test
    void testS() {
        test("S");
    }

    /**
     * Test sequence.pt.
     */
    @Test
    void testSequence() {
        testProgram("sequence");
    }

    /**
     * Test sequenceIfAll.pt.
     */
    @Test
    void testSequenceIfAll() {
        testMultirun("sequenceIfAll");
    }

    /**
     * Test sequenceIfAny.pt.
     */
    @Test
    void testSequenceIfAny() {
        testMultirun("sequenceIfAny");
    }

    /**
     * Test spanningTree.pt.
     */
    @Test
    void testSpanningTree() {
        test("spanningTree");
    }

    /**
     * Test summarize.pt.
     */
    @Test
    void testSummarize() {
        test("summarize");
    }

    /**
     * Test summarizeWithPotential.pt.
     */
    @Test
    void testSummarizeWithPotential() {
        test("summarizeWithPotential");
    }

    /**
     * Test T function.
     */
    @Test
    void testT() {
        test("T");
    }

    /**
     * Test timeReplication.pt.
     */
    @Test
    void testTimeReplication() {
        test("timeReplication");
    }

    /**
     * Test timeSinceStart.pt.
     */
    @Test
    void testTimeSinceStart() {
        testProgram("timeSinceStart");
    }

    /**
     * Test trueAfterTime.pt.
     */
    @Test
    void testTrueAfterTime() {
        testProgram("trueAfterTime");
    }

    /**
     * Test trueDuringLast.pt.
     */
    @Test
    void testTrueDuringLast() {
        testProgram("trueDuringLast");
    }

    /**
     * Test trueFor.pt.
     */
    @Test
    void testTrueFor() {
        testProgram("trueFor");
    }

    /**
     * Test utils.pt.
     */
    @Test
    void testUtils() {
        testProgram("utils");
    }

    /**
     * Test for bug #104.
     */
    @Test
    void testVariableRestriction104() {
        test("variableRestriction104");
    }

    /**
     * Test vm.pt.
     */
    @Test
    void testVm() {
        test("vm");
    }

    /**
     * Test voronoiPartitioning.pt.
     */
    @Test
    void testVoronoiPartitioning() {
        test("voronoiPartitioning");
    }

    /**
     * Test wait.pt.
     */
    @Test
    void testWait() {
        testProgram("wait");
    }

    /**
     * Test waitAndApply.pt.
     */
    @Test
    void testWaitAndApply() {
        testProgram("waitAndApply");
    }

    /**
     * Print the current method name.
     */
    static final class LoggingExtension implements BeforeTestExecutionCallback {
        @Override
        public void beforeTestExecution(final ExtensionContext context) {
            context.getTestMethod().ifPresent(method -> L.info(method.getName()));
        }
    }

}
