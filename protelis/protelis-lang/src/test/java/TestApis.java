
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
        test(Results.T);
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
     * Test summarize.pt.
     */
    @Test
    public void testSummarize() {
        test(Results.SUMMARIZE);
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

    private void test(final TestConfig testConfig) {
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
        assertArrayEquals(res, simRes);
    }

}
