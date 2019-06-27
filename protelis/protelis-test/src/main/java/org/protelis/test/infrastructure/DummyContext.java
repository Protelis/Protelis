/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.test.infrastructure;

import java.util.Random;

import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.vm.NetworkManager;
import org.protelis.vm.impl.AbstractExecutionContext;
import org.protelis.vm.impl.SimpleExecutionEnvironment;
import org.protelis.vm.impl.SimpleNetworkManager;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java8.util.stream.IntStreams;

/**
 * A **dummy** Protelis VM to be used for testing.
 *
 */
@SuppressFBWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
public final class DummyContext extends AbstractExecutionContext {

    private static final DeviceUID DUMMYUID = new DeviceUID() {
        private static final long serialVersionUID = 2306021805006825289L;

        @Override
        public String toString() {
            return "DummyUID";
        };
    };
    private final Random rng = new Random(0);

    /**
     *
     */
    public DummyContext() {
        this(new SimpleNetworkManager());
    }

    /**
     * @param networkManager the network manager to be used
     */
    public DummyContext(final NetworkManager networkManager) {
        super(new SimpleExecutionEnvironment(), networkManager);
    }

    // ATTENTION: System.currentTimeMillis() is not reproducible
    @Override
    public Number getCurrentTime() {
        return System.currentTimeMillis() / 1000d;
    }

    /*
     * ATTENTION: getDeltaTime has been overridden for testing purpose. If you
     * need to estimate the actual difference between two reactions you can
     * comment this method as it is already implemented in
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
    public DeviceUID getDeviceUID() {
        return DUMMYUID;
    }

    @Override
    protected AbstractExecutionContext instance() {
        return new DummyContext();
    }

    /**
     * Test utility.
     * 
     * @param entries
     *            how many entries for the field
     * @return a field with populated with numbers from 0 to 99
     */
    @SuppressWarnings("serial")
    public Field makeTestField(final int entries) {
        final Field res = DatatypeFactory.createField(entries);
        IntStreams.range(0, entries).forEach(n -> res.addSample(new DeviceUID() { }, (double) n));
        return res;
    }

    @Override
    public double nextRandomDouble() {
        return rng.nextDouble();
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

}
