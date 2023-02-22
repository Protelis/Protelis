/*
 * Copyright (C) 2023, Danilo Pianini and contributors listed in the project's build.gradle.kts file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.test.infrastructure;

import java.util.Random;

import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.Tuple;
import org.protelis.lang.datatype.impl.IntegerUID;
import org.protelis.vm.ExecutionContext;
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
public final class DummyContext extends AbstractExecutionContext<DummyContext> {

    private static final DeviceUID DUMMYUID = new DeviceUID() {
        private static final long serialVersionUID = 1L;
        @Override
        public String toString() {
            return "DummyUID";
        }
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
    protected DummyContext instance() {
        return new DummyContext();
    }

    /**
     * Test utility.
     * 
     * @param entries
     *            how many entries for the field
     * @return a field with populated with numbers from 0 to 99
     */
    public Field<Double> makeTestField(final int entries) {
        final Field.Builder<Double> res = DatatypeFactory.createFieldBuilder();
        IntStreams.range(1, entries)
            .mapToDouble(it -> it)
            .forEach(n -> res.add(new DeviceUID() { }, n));
        return res.build(getDeviceUID(), 0.0);
    }

    /**
     * Test utility.
     * 
     * @param entries
     *            how many entries for the field
     * @return a field with populated with tuples of numbers from 0 to 99
     */
    public Field<Tuple> makeTupleTestField(final int entries) {
        final Field.Builder<Tuple> res = DatatypeFactory.createFieldBuilder();
        IntStreams.range(1, entries)
            .mapToDouble(it -> it)
            .forEach(n -> res.add(new DeviceUID() { }, DatatypeFactory.createTuple(n)));
        return res.build(getDeviceUID(), DatatypeFactory.createTuple(0));
    }

    @Override
    @SuppressFBWarnings(value = "DMI_RANDOM_USED_ONLY_ONCE", justification = "False positive")
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
     * @param self the current Context
     * @return a field with populated with numbers from 0 to 99
     */
    public static Field<Double> makeTestField(final ExecutionContext self) {
        final Field.Builder<Double> res = DatatypeFactory.createFieldBuilder();
        IntStreams.range(1, 100)
                .mapToDouble(it -> it)
                .forEach(n -> res.add(new DeviceUID() { private static final long serialVersionUID = 1L; }, n));
        return res.build(self.getDeviceUID(), 0.0);
    }

    /**
     * Test utility.
     *
     * @param self the current Context
     * @return a field with populated with numbers from 0 to 99
     */
    public static Field<Double> hoodFailureField(final ExecutionContext self) {
        return DatatypeFactory.<Double>createFieldBuilder()
            .add(new IntegerUID(1), 0.0)
            .add(new IntegerUID(2), 1.0)
            .add(new IntegerUID(4), 0.0)
            // CHECKSTYLE: MagicNumber OFF
            .add(new IntegerUID(5), 0.0)
            .add(new IntegerUID(6), 1.0)
            // CHECKSTYLE: MagicNumber ON
            .build(self.getDeviceUID(), 0.0);
    }

}
