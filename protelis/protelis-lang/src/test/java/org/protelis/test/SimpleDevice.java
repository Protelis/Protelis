package org.protelis.test;

import org.apache.commons.math3.random.RandomGenerator;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.Tuple;
import org.protelis.vm.NetworkManager;
import org.protelis.vm.impl.AbstractExecutionContext;

import it.unibo.alchemist.model.interfaces.Environment;
import it.unibo.alchemist.model.interfaces.Reaction;

/**
 * A simple implementation of a Protelis-based device, encapsulating a
 * ProtelisVM and a network interface.
 */
public class SimpleDevice extends AbstractExecutionContext implements SpatiallyEmbeddedDevice {
    private final RandomGenerator r;
    private final ProtelisNode node;
    private final Environment<Object> env;
    private final Reaction<Object> react;
    private final NetworkManager netmgr;

    /**
     * 
     * @param environment
     *            environment
     * @param node
     *            node
     * @param reaction
     *            reaction
     * @param random
     *            random
     * @param netmgr
     *            netmgr
     */
    public SimpleDevice(final Environment<Object> environment, final ProtelisNode node, final Reaction<Object> reaction,
                    final RandomGenerator random, final NetworkManager netmgr) {
        super(node, netmgr);
        r = random;
        this.react = reaction;
        this.env = environment;
        this.netmgr = netmgr;
        this.node = node;
    }

    /**
     * Move in a direction specified by the 3-tuple vector in meters Uses a
     * kludge vector in which +X = East, +Y = North.
     * 
     * @param vector
     *            move the device of the given vector
     */
    public void move(final Tuple vector) {
        // position = position.addVector((Double) vector.get(0), (Double)
        // vector.get(1), (Double) vector.get(2));
    }

    @Override
    public DeviceUID getDeviceUID() {
        return node;
    }

    @Override
    public Number getCurrentTime() {
        return react.getTau().toDouble();
    }

    /**
     * Return delta time.
     * 
     * @return delta time
     */
    public double dt() {
        return 1;
    }

    @Override
    protected AbstractExecutionContext instance() {
        return new SimpleDevice(env, node, react, r, netmgr);
    }

    /**
     * Note: this should be going away in the future, to be replaced by standard
     * Java random.
     */
    @Override
    public double nextRandomDouble() {
        return r.nextDouble();
    }

    @Override
    public Field nbrRange() {
        return buildField(deviceUid -> this.env.getDistanceBetweenNodes(node, (ProtelisNode) deviceUid), node);
    }
}
