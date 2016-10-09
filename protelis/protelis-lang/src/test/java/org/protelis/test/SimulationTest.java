package org.protelis.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.protelis.lang.ProtelisLoader;
import org.protelis.vm.ProtelisProgram;
import org.protelis.vm.util.CodePath;

/**
 * Minimal demonstration of an application using Protelis. This demonstration
 * does the following: - Uses ProtelisLoader to obtain a program from the
 * Protelis classes - Create a collection of Protelis-based devices, each
 * encapsulating a ProtelisVM, execution environment, and network interface -
 * Run several rounds of synchronous execution
 */
public class SimulationTest {
    /** Collection of devices */
    private final List<SimpleDevice> devices = new ArrayList<>();
    /** Network for moving messages between devices */
    private final Map<SimpleDevice, Set<SimpleDevice>> network = new HashMap<>();
    // private final Globe EARTH = new Earth();
    private final double communicationRange;
    private final int executionRound;
    private final List<Pair<Position, Pair<Integer, Integer>>> deviceGroups;
    private final String programName;

    /**
     * Test.
     * 
     * @param programName
     *            program name
     * @param maxRun
     *            number of rounds to be executed
     * @param communicationRange
     *            range under which two devices are considered neighbors
     */
    public SimulationTest(final String programName, final int maxRun, final double communicationRange) {
        this.programName = programName;
        this.communicationRange = communicationRange;
        this.executionRound = maxRun;
        this.deviceGroups = new LinkedList<>();
    }

    /**
     * Add a new group of devices to this simulation.
     * 
     * @param group
     *            group to be added
     * @return this simulation
     */
    public SimulationTest addGroup(final Pair<Position, Pair<Integer, Integer>> group) {
        deviceGroups.add(group);
        return this;
    }

    /**
     * Create an N x N grid of devices, each running the inducated program.
     */
    public void createNetwork() {
        // final int leaderId = 5; // should have neighbors 1, 4, 6, 9
        // SimpleDevice[][] cache = new SimpleDevice[width][height];
        // Create devices
        for (Pair<Position, Pair<Integer, Integer>> g : deviceGroups) {
            int width = g.getRight().getLeft(), height = g.getRight().getRight();
            double x = g.getLeft().getX(), y = g.getLeft().getY();
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    Position pos = Position.fromVector(x + (i * 1), y + (j * 1), 0.0);

                    // Parse a new copy of the program for each device:
                    // it will be marked up with values as the interpreter runs
                    ProtelisProgram program = ProtelisLoader.parse(programName);

                    // Create the device
                    SimpleDevice executionContext = new SimpleDevice(program, devices.size(), pos);
                    devices.add(executionContext);

                    // Mark the leader
                    // if (id == leaderId) {
                    // executionContext.getExecutionEnvironment().put("leader",
                    // true);
                    // }
                    // Remember the devices in a grid, for later setting up the
                    // network
                    // cache[i][j] = executionContext;

                    // Create holders for network information
                    network.put(executionContext, new HashSet<>());
                }
            }
        }

        // Link up the network
        updateNetwork();
    }

    /**
     * Execute every device once, then deliver updates to all neighbors.
     */
    private void synchronousUpdate() {
        // Execute one cycle at each device
        for (SimpleDevice d : devices) {
            d.getVM().runCycle();
            // System.out.println(d.getVM().getCurrentValue());
        }
        // Update network connectivity
        updateNetwork();

        // Deliver shared-state updates over the network
        for (SimpleDevice src : network.keySet()) {
            Map<CodePath, Object> message = src.accessNetworkManager().getSendCache();
            for (SimpleDevice dst : network.get(src)) {
                dst.accessNetworkManager().receiveFromNeighbor(src.getDeviceUID(), message);
            }
        }
    }

    /**
     * Simple unit-disc network model, implemented naively. A real application
     * will want to run its updates using a more efficient data structure like a
     * quad tree
     */
    private void updateNetwork() {
        for (SimpleDevice self : devices) {
            Set<SimpleDevice> nbrs = network.get(self);
            for (SimpleDevice other : devices) {
                double distance = self.getPosition().distanceTo(other.getPosition());
                if (distance <= communicationRange) {
                    if (!nbrs.contains(other)) {
                        nbrs.add(other);
                    }
                } else {
                    if (nbrs.contains(other)) {
                        nbrs.remove(other);
                    }
                }
            }
        }
    }

    /**
     * 
     * @return results of every device
     */
    public Object[] getResults() {
        int round = 0;
        while (round < executionRound) {
            round++;
            // System.out.println(round + " ----");
            synchronousUpdate();
        }

        // if (height > 1) {
        // Object[][] res = new Object[height][width];
        // for (SimpleDevice d : devices) {
        // int id = ((IntegerUID) d.getDeviceUID()).getUID();
        // res[id / width][id % width] = d.getVM().getCurrentValue();
        // }
        // return res;
        // } else {

        List<Object> res = new LinkedList<>();
        for (SimpleDevice d : devices) {
            res.add(d.getVM().getCurrentValue());
        }
        return res.toArray();
        // return devices.stream().map(d ->
        // d.getVM().getCurrentValue()).toArray();
        // }
    }

    /**
     * Set a property for the given device.
     * 
     * @param id
     *            device id
     * @param key
     *            property key
     * @param value
     *            property value
     */
    public void setProperty(final int id, final String key, final Object value) {
        for (SimpleDevice d : devices) {
            if (d.getDeviceUID().toString().equals(id + "")) {
                d.getExecutionEnvironment().put(key, value);
                return;
            }
        }
        throw new IllegalArgumentException("Device " + id + " not found");
    }

    /**
     * Set a property for the given device.
     * 
     * @param key
     *            property key
     * @param value
     *            property value
     */
    public void setPropertyToAll(final String key, final Object value) {
        for (SimpleDevice d : devices) {
            d.getExecutionEnvironment().put(key, value);
        }
    }
}
