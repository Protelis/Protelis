import static org.junit.Assert.assertArrayEquals;

import java.util.stream.IntStream;

import org.apache.commons.lang3.tuple.Triple;
import org.junit.Test;
import org.protelis.test.Results;
import org.protelis.test.SimulationTest;
import org.protelis.test.TestConfig;

/**
 * Testing Protelis core libraries.
 */
public class TestApis {

    private static final int MAX_CYCLE_NUM = 100;

    /**
     * Test the number of neighbors of each device.
     */
    @Test
    public void testNeighborhood() {
        testFileWithExplicitResult(new SimulationTest("neighborhood", MAX_CYCLE_NUM, 3, 3, 1).getResults(),
                        Results.NEIGHBORHOOD);
    }

    /**
     * Test distanceTo function.
     */
    @Test
    public void testDistanceTo() {
        SimulationTest sim = new SimulationTest("distanceTo", MAX_CYCLE_NUM, 3, 1, 1);
        sim.setProperty(0, "source", true);
        testFileWithExplicitResult(sim.getResults(), new Double[] { 0.0, 1.0, 2.0 });
    }

    /**
     * Test broadcast function.
     */
    @Test
    public void testBroadcast() {
        SimulationTest sim = new SimulationTest("broadcast", MAX_CYCLE_NUM, 3, 4, 1);
        sim.setProperty(0, "source", true);
        testFileWithExplicitResult(sim.getResults(), Results.BROADCAST);
    }

    /**
     * Test self.nbrRange.
     */
    @Test
    public void testNbrRange() {
        testFileWithExplicitResult(new SimulationTest("nbrRange", MAX_CYCLE_NUM, 2, 1, 1).getResults(),
                        Results.NBRRANGE);
    }

    /**
     * Test G.pt.
     */
    @Test
    public void testG() {
        SimulationTest sim = new SimulationTest("G", MAX_CYCLE_NUM, 4, 4, 1);
        sim.setProperty(0, "source", true);
        sim.setProperty(3, "source", true);
        sim.setProperty(12, "source", true);
        sim.setProperty(15, "source", true);
        testFileWithExplicitResult(sim.getResults(), Results.G);
    }

    /**
     * Test G.pt.
     */
    @Test
    public void testG1() {
        test(Results.GTC);
    }

    /**
     * Test distance.pt.
     */
    @Test
    public void testDistance() {
        SimulationTest sim = new SimulationTest("distance", MAX_CYCLE_NUM, 3, 4, 1);
        sim.setProperty(0, "source", true);
        sim.setProperty(11, "destination", true);
        testFileWithExplicitResult(sim.getResults(), Results.DISTANCE);
    }

    /**
     * Test T function.
     */
    @Test
    public void testT() {
        testFileWithExplicitResult(new SimulationTest("T", MAX_CYCLE_NUM, 1, 1, 1).getResults(), Results.T);
    }

    /**
     * Test cyclicTimer function.
     */
    @Test
    public void testCyclicTimer1() {
        testFileWithExplicitResult(new SimulationTest("cyclictimer", 5, 1, 1, 1).getResults(), Results.CYCLICTIMER2);
    }

    /**
     * Test cyclicTimer function.
     */
    @Test
    public void testCyclicTimer2() {
        testFileWithExplicitResult(new SimulationTest("cyclictimer", 6, 1, 1, 1).getResults(), Results.CYCLICTIMER1);
    }

    /**
     * Test cyclicTimer function.
     */
    @Test
    public void testCyclicTimer3() {
        testFileWithExplicitResult(new SimulationTest("cyclictimer", 7, 1, 1, 1).getResults(), Results.CYCLICTIMER2);
    }

    /**
     * Test C.pt.
     */
    @Test
    public void testC() {
        SimulationTest sim = new SimulationTest("C", MAX_CYCLE_NUM, 4, 4, 1);
        IntStream.range(0, 16).forEach(i -> sim.setProperty(i, "n", i));
        sim.setProperty(5, "source", true);
        testFileWithExplicitResult(sim.getResults(), Results.C);
    }

    /**
     * Test gossip_ever.pt.
     */
    @Test
    public void testGossipEver() {
        test(Results.GOSSIP_EVER);
    }

    /*
     * From this point the rest of the file is not tests, but utility methods
     */

    private void testFileWithExplicitResult(final Object[] simulationResult, final Object[] result) {
        assertArrayEquals(result, simulationResult);
    }

    private void test(final TestConfig testConfig) {
        SimulationTest sim = new SimulationTest(testConfig.getFileName(), testConfig.getMaxRound(),
                        testConfig.getExpectedResult()[0].length, testConfig.getExpectedResult().length,
                        testConfig.getDistance());
        for (Triple<Integer, String, Object> t : testConfig.getProperties()) {
            if (t.getLeft() == TestConfig.ALL) {
                sim.setPropertyToAll(t.getMiddle(), t.getRight());
            } else {
                sim.setProperty(t.getLeft(), t.getMiddle(), t.getRight());
            }
        }
        assertArrayEquals(testConfig.getExpectedResult(), sim.getResults());
    }

}
