/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.vm.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java8.util.Maps;
import java.util.Objects;
import java8.util.function.Function;

import org.apache.commons.math3.util.Pair;
import org.danilopianini.lang.LangUtils;
import org.danilopianini.lang.PrimitiveUtils;
import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.util.Reference;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ExecutionEnvironment;
import org.protelis.vm.NetworkManager;
import org.protelis.vm.util.CodePath;
import org.protelis.vm.util.Stack;
import org.protelis.vm.util.StackImpl;

import com.google.common.collect.MapMaker;

import gnu.trove.list.TByteList;
import gnu.trove.list.array.TByteArrayList;
import gnu.trove.stack.TIntStack;
import gnu.trove.stack.array.TIntArrayStack;

import static java8.util.stream.StreamSupport.stream;

/**
 * Partial implementation of ExecutionContext, containing functionality expected
 * to be shared between most implementations. Instantiations of Protelis should
 * generally extend this class.
 */
public abstract class AbstractExecutionContext implements ExecutionContext {

    private static final MapMaker MAPMAKER = new MapMaker();

    private final TByteList callStack = new TByteArrayList();
    private final TIntStack callFrameSizes = new TIntArrayStack();
    private final NetworkManager nm;
    private Map<Reference, ?> functions;
    private Stack gamma;
    private Map<DeviceUID, Map<CodePath, Object>> theta;
    private Map<CodePath, Object> toSend;
    private Number previousRoundTime;
    private final ExecutionEnvironment env;

    /**
     * Create a new AbstractExecutionContext.
     * 
     * @param execenv
     *            The execution environment
     * @param netmgr
     *            Abstract network interface to be used
     */
    protected AbstractExecutionContext(final ExecutionEnvironment execenv, final NetworkManager netmgr) {
        LangUtils.requireNonNull(execenv, netmgr);
        nm = netmgr;
        env = execenv;
    }

    @Override
    public final void setAvailableFunctions(final Map<Reference, FunctionDefinition> knownFunctions) {
        functions = Collections.unmodifiableMap(knownFunctions);
    }

    @Override
    public final void commit() {
        Objects.requireNonNull(env);
        Objects.requireNonNull(gamma);
        Objects.requireNonNull(theta);
        Objects.requireNonNull(toSend);
        Objects.requireNonNull(functions);
        previousRoundTime = getCurrentTime();
        env.commit();
        nm.shareState(toSend);
        gamma = null;
        theta = null;
        toSend = null;
    }

    @Override
    public final void setup() {
        if (previousRoundTime == null) {
            previousRoundTime = getCurrentTime();
        }
        assert previousRoundTime != null : "Round time is null.";
        callStack.clear();
        callStack.add((byte) 1);
        env.setup();
        toSend = MAPMAKER.makeMap();
        gamma = new StackImpl(new LinkedHashMap<>(functions));
        theta = Collections.unmodifiableMap(nm.getNeighborState());
    }

    @Override
    public final void newCallStackFrame(final byte... id) {
        callFrameSizes.push(id.length);
        callStack.add(id);
        gamma.push();
    }

    @Override
    public final void returnFromCallFrame() {
        final int size = callFrameSizes.pop();
        callStack.remove(callStack.size() - size, size);
        gamma.pop();
    }

    @Override
    public final void putVariable(final Reference name, final Object value, final boolean canShadow) {
        gamma.put(name, value, canShadow);
    }

    @Override
    public final void putMultipleVariables(final Map<Reference, ?> map) {
        gamma.putAll(map);
    }

    /**
     * Produce a child execution context, for encapsulated evaluation of
     * sub-programs.
     * 
     * @return Child execution context
     */
    protected abstract AbstractExecutionContext instance();

    @Override
    public final AbstractExecutionContext restrictDomain(final Field f) {
        final Map<DeviceUID, Map<CodePath, Object>> restricted = new HashMap<>(theta.size());
        final DeviceUID localDevice = getDeviceUID();
        for (final DeviceUID n : f.nodeIterator()) {
            if (!n.equals(localDevice)) {
                restricted.put(n, theta.get(n));
            }
        }
        final AbstractExecutionContext restrictedInstance = instance();
        restrictedInstance.theta = restricted;
        restrictedInstance.gamma = gamma;
        restrictedInstance.toSend = toSend;
        return restrictedInstance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> Field buildField(final Function<T, ?> computeValue, final T localValue) {
        /*
         * Compute where we stand
         */
        final CodePath codePath = new CodePath(callStack);
        /*
         * If there is a request to build a field, then it means this is a
         * nbr-like operation
         */
        if (Maps.putIfAbsent(toSend,codePath, localValue) != null) {
            throw new IllegalStateException(
                    "This program has attempted to build a field twice with the same code path."
                    + "This is probably a bug in Protelis");
        }
        final Field res = DatatypeFactory.createField(theta.size() + 1);
        stream(theta.entrySet())
                .map(e -> new Pair<>(e.getKey(), e.getValue().get(codePath)))
                .filter(e -> e.getValue() != null)
                /*
                 * This cast is OK by construction, if no bug is there and no
                 * wild casts are done by the caller.
                 */
                .forEachOrdered(e -> res.addSample(e.getKey(), computeValue.apply((T) e.getValue())));
        res.addSample(getDeviceUID(), computeValue.apply(localValue));
        return res;
    }

    @Override
    public final Object getVariable(final Reference name) {
        return gamma.get(name);
    }

    /**
     * Accessor for abstract network interface.
     * 
     * @return Current abstract network interface
     */
    protected final NetworkManager getNetworkManager() {
        return nm;
    }

    /**
     * Support for first-class functions by returning the set of currently
     * accessible functions.
     * 
     * @return Map from function name to function objects
     */
    protected final Map<Reference, ?> getFunctions() {
        return functions;
    }

    @Override
    public Number getDeltaTime() {
        /*
         * try not to lose precision:
         */
        final Class<? extends Number> tClass = PrimitiveUtils.toPrimitiveWrapper(previousRoundTime);
        if (Double.class.equals(tClass) || Float.class.equals(tClass)) {
            return getCurrentTime().doubleValue() - previousRoundTime.doubleValue();
        }
        return getCurrentTime().longValue() - previousRoundTime.longValue();
    }

    @Override
    public ExecutionEnvironment getExecutionEnvironment() {
        return env;
    }

}
