/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.vm.impl;

import static com.google.common.collect.Maps.newLinkedHashMapWithExpectedSize;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.danilopianini.lang.LangUtils;
import org.danilopianini.lang.PrimitiveUtils;
import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.vm.CodePath;
import org.protelis.vm.CodePathFactory;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ExecutionEnvironment;
import org.protelis.vm.NetworkManager;
import org.protelis.vm.util.Stack;
import org.protelis.vm.util.StackImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.stack.TIntStack;
import gnu.trove.stack.array.TIntArrayStack;
import java8.util.Maps;
import java8.util.function.Function;
import java8.util.function.Supplier;

/**
 * Partial implementation of ExecutionContext, containing functionality expected
 * to be shared between most implementations. Instantiations of Protelis should
 * generally extend this class.
 */
public abstract class AbstractExecutionContext implements ExecutionContext {

    private static final int MASK = 0xFF;
    private static final Logger L = LoggerFactory.getLogger(AbstractExecutionContext.class);
    private final TIntList callStack = new TIntArrayList(10, -1);
    private final TIntStack callFrameSizes = new TIntArrayStack();
    private final NetworkManager nm;
    private final CodePathFactory codePathFactory;
    private Map<Reference, ?> functions = Collections.emptyMap();
    private Stack gamma;
    private Map<DeviceUID, Map<CodePath, Object>> theta;
    private Map<CodePath, Supplier<?>> tobeComputedBeforeSending;
    private Map<CodePath, Object> toSend;
    private final List<AbstractExecutionContext> restrictedContexts = Lists.newArrayList(); 
    private Number previousRoundTime;
    private final ExecutionEnvironment env;
    private int exportsSize;
    private int deferredExportSize;

    /**
     * Create a new AbstractExecutionContext with a default, time-efficient code path factory.
     * 
     * @param execenv
     *            The execution environment
     * @param netmgr
     *            Abstract network interface to be used
     */
    protected AbstractExecutionContext(final ExecutionEnvironment execenv, final NetworkManager netmgr) {
        this(execenv, netmgr, (stack, sizes) -> new DefaultTimeEfficientCodePath(stack));
    }

    /**
     * Create a new AbstractExecutionContext.
     * 
     * @param execenv
     *            The execution environment
     * @param netmgr
     *            Abstract network interface to be used
     * @param codePathFactory The code path factory to use
     */
    protected AbstractExecutionContext(final ExecutionEnvironment execenv, final NetworkManager netmgr, final CodePathFactory codePathFactory) {
        LangUtils.requireNonNull(execenv, netmgr);
        nm = netmgr;
        env = execenv;
        this.codePathFactory = codePathFactory;
    }

    @Override
    public final void setGloballyAvailableReferences(final Map<Reference, ?> knownFunctions) {
        functions = Collections.unmodifiableMap(knownFunctions);
    }

    @Override
    public final void commit() {
        // send precisely once
        tobeComputedBeforeSending.forEach((codepath, supplier) -> {
            final Object computed = supplier.get();
            final Object previous = Maps.putIfAbsent(toSend, codepath, computed);
            if (previous != null) {
                throw new IllegalStateException("Duplicated field entry with the same codepath "
                    + "caused by the computation of a deferred build field: this is likely a bug in Protelis.\n"
                    + "codepath: " + codepath + '\n'
                    + "previously: " + previous + '\n'
                    + "computed: " + computed + '\n'
                    + "overall exports: " + toSend + '\n'
                    + "overall deferred exports: " + tobeComputedBeforeSending
                );
            }
        });
        nm.shareState(toSend);
        exportsSize = toSend.size();
        deferredExportSize = tobeComputedBeforeSending.size();
        // commit and clear including recursion into restricted contexts
        commitRecursively();
    }

    /**
     * recursively commits on restricted contexts.
     */
    public final void commitRecursively() {
        Objects.requireNonNull(env);
        Objects.requireNonNull(gamma);
        Objects.requireNonNull(theta);
        Objects.requireNonNull(toSend);
        Objects.requireNonNull(tobeComputedBeforeSending);
        Objects.requireNonNull(functions);
        previousRoundTime = getCurrentTime();
        env.commit();
        gamma = null;
        theta = null;
        toSend = null;
        tobeComputedBeforeSending = null;
        for (final AbstractExecutionContext rctx: restrictedContexts) {
            rctx.commitRecursively();
        }
        restrictedContexts.clear();
    }

    @Override
    public final void setup() {
        if (previousRoundTime == null) {
            previousRoundTime = getCurrentTime();
        }
        assert previousRoundTime != null : "Round time is null.";
        callStack.clear();
        env.setup();
        toSend = newLinkedHashMapWithExpectedSize(exportsSize);
        tobeComputedBeforeSending = newLinkedHashMapWithExpectedSize(deferredExportSize);
        gamma = new StackImpl(functions);
        theta = Collections.unmodifiableMap(nm.getNeighborState());
        newCallStackFrame(-1);
    }

    @Override
    public final void newCallStackFrame(final int... id) {
        if (id.length < 1) {
            throw new IllegalArgumentException("Unable to push unidentified stack frame: frame id cannot be empty");
        }
        callFrameSizes.push(id.length);
        callStack.add(id);
        gamma.push();
    }

    @Override
    public final void newCallStackFrame(final byte... id) {
        final int expectedSize = id.length / 4 + Math.min(id.length % 4, 1);
        final int[] compact = new int[expectedSize];
        final IntBuffer buffer = ByteBuffer.wrap(id).asIntBuffer();
        final int bufferSize = buffer.remaining();
        buffer.get(compact, 0, bufferSize);
        if (bufferSize != expectedSize) {
            for (int i = 0; i < id.length % 4; i++) {
                compact[expectedSize - 1] |= (id[id.length - 1 - i] & MASK) << i * 8;
            }
        }
        newCallStackFrame(compact);
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
        final Map<DeviceUID, Map<CodePath, Object>> restricted = newLinkedHashMapWithExpectedSize(theta.size());
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
        restrictedInstance.tobeComputedBeforeSending = tobeComputedBeforeSending;
        restrictedInstance.callStack.addAll(callStack);
        restrictedInstance.functions = functions;
        restrictedInstance.exportsSize = exportsSize;
        restrictedInstance.previousRoundTime = previousRoundTime;
        restrictedContexts.add(restrictedInstance);
        return restrictedInstance;
    }

    @Override
    public final <T> Field buildField(final Function<T, ?> computeValue, final T localValue) {
        return buildField(computeValue, localValue, toSend, localValue);
    }

    @Override
    public final <T> Field buildFieldDeferred(
            final Function<T, ?> computeValue,
            final T currentLocal,
            final Supplier<T> toBeSent) {
        return buildField(computeValue, currentLocal, tobeComputedBeforeSending, toBeSent);
    }

    @SuppressWarnings("unchecked")
    private <T, D> Field buildField(
            final Function<T, ?> computeValue,
            final T localValue,
            final Map<CodePath, D> destination,
            final D toBeSent) {
        /*
         * Compute where we stand
         */
        final CodePath codePath = codePathFactory.createCodePath(callStack, callFrameSizes);
        final Field res = DatatypeFactory.createField(theta.size() + 1);
        for (final Entry<DeviceUID, Map<CodePath, Object>> e: theta.entrySet()) {
            final Object received = e.getValue().get(codePath);
            if (received != null) {
                res.addSample(e.getKey(), computeValue.apply((T) received));
            }
        }
        res.addSample(getDeviceUID(), computeValue.apply(Objects.requireNonNull(localValue)));
        /*
         * If there is a request to build a field, then it means this is a
         * nbr-like operation
         */
        if (Maps.putIfAbsent(destination, codePath, toBeSent) != null) {
            L.error("The map is {}", toSend);
            L.error("The codePath you are trying to insert is {}", codePath);
            L.error("The value you are trying to insert is {}, while the current one is {}", localValue, toSend.get(codePath)); 
            throw new IllegalStateException(
                    "This program has attempted to build a field twice with the same code path. "
                    + "This is probably a bug in Protelis");
        }
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

    /**
     * {@inheritDoc}
     */
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
    public final ExecutionEnvironment getExecutionEnvironment() {
        return env;
    }

}
