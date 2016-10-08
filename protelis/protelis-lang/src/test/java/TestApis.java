import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.protelis.test.SimulationTest;

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
    public void testDistance() {
        SimulationTest sim = new SimulationTest("distance", MAX_CYCLE_NUM, 3, 4, 1);
        sim.setProperty(0, "source", true);
        sim.setProperty(11, "destination", true);
        testFileWithExplicitResult(sim.getResults(), Results.DISTANCE);
    }

    /*
     * From this point the rest of the file is not tests, but utility methods
     */

    private void testFileWithExplicitResult(final Object[] simulationResult, final Object[] result) {
        assertArrayEquals(result, simulationResult);
    }

}
