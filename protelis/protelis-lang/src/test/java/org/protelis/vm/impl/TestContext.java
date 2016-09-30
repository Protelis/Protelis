/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.vm.impl;

import java.util.Map;
import java.util.Random;

import org.protelis.lang.ProtelisLoader;
import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.vm.ExecutionEnvironment;
import org.protelis.vm.ProtelisProgram;
import org.protelis.vm.ProtelisVM;

/**
 * A dummy Protelis VM to be used for testing.
 */
public final class TestContext extends AbstractExecutionContext implements DeviceUID, SpatiallyEmbeddedContext {

    private static final long serialVersionUID = 1L;
    private final Random rng = new Random(0);
    private final DeviceUID deviceId;
    private final Double[] position;
    private final TestEnvironment env;
    private final Map<String, Object> properties;

    /**
     * @param id
     *            device id
     * @param env
     *            environment
     * @param nm
     *            dummy network manager
     */
    public TestContext(final DeviceUID id, final TestEnvironment env, final TestNetworkManager nm) {
        this(id, env, nm, null, null);
    }

    /**
     * @param id
     *            device id
     * @param env
     *            environment
     * @param nm
     *            dummy network manager
     * @param position
     *            device position
     */
    public TestContext(final DeviceUID id, final TestEnvironment env, final TestNetworkManager nm,
            final Double[] position) {
        this(id, env, nm, null, position);
    }

    /**
     * @param id
     *            device id
     * @param env
     *            environment
     * @param nm
     *            dummy network manager
     * @param properties
     *            set default properties for each device environment
     * @param position
     *            virtual position of the device
     */
    public TestContext(final DeviceUID id, final TestEnvironment env, final TestNetworkManager nm,
            final Map<String, Object> properties, final Double[] position) {
        super(new SimpleExecutionEnvironment(), nm);
        this.deviceId = id;
        this.properties = properties;
        this.position = position;
        this.env = env;
        if (properties != null) {
            final ExecutionEnvironment ex = getExecutionEnvironment();
            properties.keySet().forEach(k -> ex.put(k, properties.get(k)));
        }
    }

    // TODO: System.currentTimeMillis() / 1000d is not reproducible
    @Override
    public Number getCurrentTime() {
        return System.currentTimeMillis() / 1000d;
    }

    @Override
    public double nextRandomDouble() {
        return rng.nextDouble();
    }

    @Override
    protected AbstractExecutionContext instance() {
        return new TestContext(deviceId, env, (TestNetworkManager) getNetworkManager(), properties, position);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + hashCode();
    }

    @Override
    public DeviceUID getDeviceUID() {
        return deviceId;
    }

    @Override
    public Field nbrRange() {
        // SpatiallyEmbeddedContext[] neighs =
        // env.getLinkingStrategy().getNeighborsContext(deviceId,
        // env.getActiveExecutionContexts());
        // SpatiallyEmbeddedContext[] neighs =
        // env.getLinkingStrategy().getNeighborsContext(deviceId,
        // env.getExecutionContexts());
        // final Field r = DatatypeFactory.createField(neighs.length);
        // IntStreams.range(0, neighs.length).forEach(n -> {
        // if (neighs[n] != null) {
        // r.addSample(neighs[n].getDeviceUID(), distanceTo(((TestContext)
        // neighs[n]).getDevicePosition()));
        // }
        // });
        // r.addSample(deviceId, 0.0);
        // return r;

        // final ProtelisProgram program =
        // ProtelisLoader.parseAnonymousModule("nbr(self.getDevicePosition())");
        // final ProtelisVM vm = new ProtelisVM(program, instance());
        // vm.runCycle();
        // final Field nbrRes = (Field) vm.getCurrentValue();
        // final Field res = DatatypeFactory.createField(nbrRes.size());
        // nbrRes.coupleIterator().forEach(p -> {
        // res.addSample(p.getFirst(), distanceTo((Double[]) p.getValue()));
        // });
        // return res;
        return buildField(position -> distanceTo(position), position);
    }

    /**
     * Get the virtual position of the device.
     * 
     * @return device position
     */
    public Double[] getDevicePosition() {
        return position;
    }

    /**
     * Return the Euclidean distance between two positions.
     * 
     * @param position
     *            other position
     * @return Euclidean distance between two positions
     */
    public double distanceTo(final Double[] position) {
        double res = 0;
        for (int i = 0; i < position.length; i++) {
            res += Math.pow(position[i] - this.position[i], 2);
        }
        return Math.sqrt(res);
    }

}
