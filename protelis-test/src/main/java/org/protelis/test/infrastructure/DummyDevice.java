/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.test.infrastructure;

import static org.junit.Assert.assertNotNull;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.math3.random.RandomGenerator;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.Tuple;
import org.protelis.lang.datatype.impl.ArrayTupleImpl;
import org.protelis.vm.LocalizedDevice;
import org.protelis.vm.NetworkManager;
import org.protelis.vm.SpatiallyEmbeddedDevice;
import org.protelis.vm.TimeAwareDevice;
import org.protelis.vm.impl.AbstractExecutionContext;

import it.unibo.alchemist.model.interfaces.Environment;
import it.unibo.alchemist.model.interfaces.Reaction;

/**
 * A simple implementation of a Protelis-based device, encapsulating a
 * {@link org.protelis.vm.ProtelisVM} and a network interface.
 */
@SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "This is done by purpose")
public final class DummyDevice extends AbstractExecutionContext<DummyDevice>
                implements SpatiallyEmbeddedDevice<Double>, LocalizedDevice, TimeAwareDevice<Double> {
    private final RandomGenerator r;
    private final ProtelisNode node;
    private final Environment<Object> env;
    private final Reaction<Object> react;
    private final NetworkManager networkManager;

    /**
     * @param environment
     *            environment
     * @param node
     *            node
     * @param reaction
     *            reaction
     * @param random
     *            random
     * @param networkManager
     *            networkManager
     */
    public DummyDevice(final Environment<Object> environment, final ProtelisNode node, final Reaction<Object> reaction,
                    final RandomGenerator random, final NetworkManager networkManager) {
        super(node, networkManager);
        r = random;
        this.react = reaction;
        this.env = environment;
        this.networkManager = networkManager;
        this.node = node;
    }

    @Override
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public DeviceUID getDeviceUID() {
        assertNotNull("Device id can't be null", node);
        return node;
    }

    @Override
    public Number getCurrentTime() {
        return react.getTau().toDouble();
    }

    /*
     * ATTENTION: getDeltaTime has been overridden for testing purpose. If you
     * need to estimate the actual difference between two reactions, you can
     *  comment on this method as it is already implemented in
     * org.protelis.vm.impl.AbstractExecutionContext. Doing so, tests related to
     * getDeltaTime will fail.
     *
     * @see org.protelis.vm.impl.AbstractExecutionContext#getDeltaTime()
     */
    @Override
    public Number getDeltaTime() {
        return 1;
    }

    @Override
    protected DummyDevice instance() {
        return new DummyDevice(env, node, react, r, networkManager);
    }

    /**
     * Note: this should be going away in the future, to be replaced by standard
     * Java random.
     *
     * @return a double random value
     */
    @Override
    public double nextRandomDouble() {
        return r.nextDouble();
    }

    @Override
    public Field<Double> nbrRange() {
        return buildField(otherNode -> this.env.getDistanceBetweenNodes(node, otherNode), node);
    }

    @Override
    public Field<Double> nbrDelay() {
        return buildField(otherNode -> 1.0, node);
    }

    @Override
    public Field<Double> nbrLag() {
        return buildField(otherNode -> 1.0, node);
    }

    @Override
    public Tuple getCoordinates() {
        final double[] cd = env.getPosition(node).getCartesianCoordinates();
        Tuple c = new ArrayTupleImpl(0, cd.length);
        for (int i = 0; i < cd.length; i++) {
            c = c.set(i, cd[i]);
        }
        return c;
    }

    @Override
    public Field<Tuple> nbrVector() {
        return buildField(device -> this.getVectorToNeigh(getCoordinates(), device), this);
    }

    private Tuple getVectorToNeigh(final Tuple myC, final LocalizedDevice device) {
        final Tuple otherC = device.getCoordinates();
        Tuple tmp = device.getCoordinates();
        for (int i = 0; i < myC.size(); i++) {
            tmp = tmp.set(i, (Double) otherC.get(i) - (Double) myC.get(i));
        }
        return tmp;
    }

    @Override
    public String toString() {
        return "DummyDevice" + node.getId();
    }
}
