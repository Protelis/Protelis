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

import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.vm.ExecutionEnvironment;

import java8.util.stream.IntStreams;

/**
 * A dummy Protelis VM to be used for testing.
 */
public final class TestContext extends AbstractExecutionContext implements DeviceUID {

    private static final long serialVersionUID = 1L;
    private final Random rng = new Random(0);
    private final DeviceUID nodeId;
    private final Map<String, Object> properties;

    /**
     * @param id
     *            device id
     * @param nm
     *            dummy network manager
     */
    public TestContext(final DeviceUID id, final TestNetworkManager nm) {
        this(id, nm, null);
    }

    /**
     * @param id
     *            device id
     * @param nm
     *            dummy network manager
     * @param properties
     *            set default properties for each device environment
     */
    public TestContext(final DeviceUID id, final TestNetworkManager nm, final Map<String, Object> properties) {
        super(new SimpleExecutionEnvironment(), nm);
        this.nodeId = id;
        this.properties = properties;
        if (properties != null) {
            final ExecutionEnvironment ex = getExecutionEnvironment();
            properties.keySet().forEach(k -> ex.put(k, properties.get(k)));
        }
    }

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
        return new TestContext(nodeId, (TestNetworkManager) getNetworkManager(), properties);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + hashCode();
    }

    /**
     * Test utility.
     * 
     * @return a field with populated with numbers from 0 to 99
     */
    public static Field makeTestField() {
        final Field res = DatatypeFactory.createField(100);
        IntStreams.range(0, 100).forEach(n -> res.addSample(new DeviceUID() {
            private static final long serialVersionUID = 1L;
        }, (double) n));
        return res;
    }

    @Override
    public DeviceUID getDeviceUID() {
        return nodeId;
    }

}
