/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.vm.impl;

import static com.google.common.collect.Maps.newLinkedHashMapWithExpectedSize;
import static org.protelis.lang.interpreter.util.Bytecode.INIT;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.vm.CodePath;
import org.protelis.vm.CodePathFactory;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ExecutionEnvironment;
import org.protelis.vm.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.stack.TIntStack;
import gnu.trove.stack.array.TIntArrayStack;

/**
 * Partial implementation of ExecutionContext, containing functionality expected
 * to be shared between most implementations. Instantiations of Protelis should
 * generally extend this class.
 *
 * @param <S> self-type. Subclasses must parameterize AbstractExecutionContext
 *            with themselves, and return themselves in instance(). This forces
 *            a compiler check on the type of instanced contexts, ensuring (if
 *            no foolish cast is used) that restricted contexts have all the
 *            methods available in the main {@link ExecutionContext}. For
 *            instance, if your class is MyContext, it should be written as
 *            MyContext extends AbstractExecutionContext<MyContext>.
 */
public abstract class AbstractExecutionContext<S extends AbstractExecutionContext<S>> implements ExecutionContext {

    private static final int MASK = 0xFF;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionContext.class);
    private final TIntStack callFrameSizes = new TIntArrayStack();
    private final TIntList callStack = new TIntArrayList(10, -1);
    private final CodePathFactory codePathFactory;
    private int deferredExportSize;
    private final ExecutionEnvironment environment;
    private int exportsSize;
    private Optional<Map<Reference, ?>> functions = Optional.empty();
    private Map<Reference, Object> gamma;
    private final NetworkManager networkManager;
    private Number previousRoundTime;
    private final List<AbstractExecutionContext<S>> restrictedContexts = Lists.newArrayList();
    private Map<DeviceUID, Map<CodePath, Object>> theta;
    private Map<CodePath, Supplier<?>> tobeComputedBeforeSending;
    private Map<CodePath, Object> toSend;
    private Map<CodePath, Object> toStore;
    private Map<CodePath, Object> lastStored = Collections.emptyMap();
    private int variablesSize;

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
     * Create a new AbstractExecutionContext with the specified
     * {@link CodePathFactory}. Subclasses which want to use hashing or other means
     * to encode {@link CodePath}s can call this constructor, e.g.:
     *
     * <pre>
     * super(executionEnvironment, networkManager, new HashingCodePathFactory(Hashing.sha256()));
     * </pre>
     *
     * @param executionEnvironment The execution environment
     * @param networkManager Abstract network interface to be used
     * @param codePathFactory The code path factory to use
     */
    protected AbstractExecutionContext(
        final ExecutionEnvironment executionEnvironment,
        final NetworkManager networkManager,
        final CodePathFactory codePathFactory
    ) {
        this.networkManager = Objects.requireNonNull(networkManager);
        environment = Objects.requireNonNull(executionEnvironment);
        this.codePathFactory = codePathFactory;
    }

    @Override
    public final <T, R> Field<R> buildField(final Function<T, R> computeValue, final T localValue) {
        return buildField(computeValue, localValue, toSend, localValue);
    }

    @SuppressWarnings("unchecked")
    private <T, D, R> Field<R> buildField(
            final Function<T, R> computeValue,
            final T localValue,
            final Map<CodePath, D> destination,
            final D toBeSent) {
        /*
         * Compute where we stand
         */
        final CodePath codePath = codePathFactory.createCodePath(callStack, callFrameSizes);
        /*
         * If there is a request to build a field, then it means this is a
         * nbr-like operation
         */
        final Field.Builder<R> builder = DatatypeFactory.createFieldBuilder();
        for (final Entry<DeviceUID, Map<CodePath, Object>> e: theta.entrySet()) {
            final Object received = e.getValue().get(codePath);
            if (received != null) {
                builder.add(e.getKey(), computeValue.apply((T) received));
            }
        }
        /*
         * If a field is computed locally, the local value should get echoed to
         * neighbors. However, such operation must be performed *after* the build
         * construction has been proved successful, in order to prevent errors due to a
         * bugged NetworkManager to propagate within the intepreter, giving rise to very
         * unclear exception messages.
         */
        if (destination.putIfAbsent(codePath, toBeSent) != null) {
            throw new IllegalStateException(
                    "This program has attempted to build a field twice with the same code path. "
                        + "This is probably a bug in Protelis. Debug information: tried to insert " + codePath
                        + " into " + toSend + ". Value to insert: " + localValue + ", existing one: " + toSend.get(codePath)
            );
        }
        return builder.build(getDeviceUID(), computeValue.apply(Objects.requireNonNull(localValue)));
    }

    @Override
    public final <T, R> Field<R> buildFieldDeferred(
            final Function<T, R> computeValue,
            final T currentLocal,
            final Supplier<T> toBeSent) {
        return buildField(computeValue, currentLocal, tobeComputedBeforeSending, toBeSent);
    }

    @Override
    public final void commit() {
        // send precisely once
        tobeComputedBeforeSending.forEach((codepath, supplier) -> {
            final Object computed = supplier.get();
            final Object previous = toSend.putIfAbsent(codepath, computed);
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
        networkManager.shareState(toSend);
        exportsSize = toSend.size();
        variablesSize = gamma.size();
        deferredExportSize = tobeComputedBeforeSending.size();
        // commit and clear including recursion into restricted contexts
        commitRecursively();
    }

    /**
     * recursively commits on restricted contexts.
     */
    protected final void commitRecursively() {
        Objects.requireNonNull(environment);
        Objects.requireNonNull(gamma);
        Objects.requireNonNull(theta);
        Objects.requireNonNull(toSend);
        Objects.requireNonNull(toStore);
        Objects.requireNonNull(tobeComputedBeforeSending);
        Objects.requireNonNull(functions);
        previousRoundTime = getCurrentTime();
        environment.commit();
        gamma = null;
        theta = null;
        toSend = null;
        lastStored = Collections.unmodifiableMap(toStore);
        toStore = null;
        tobeComputedBeforeSending = null;
        for (final AbstractExecutionContext<S> rctx: restrictedContexts) {
            rctx.commitRecursively();
        }
        restrictedContexts.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Number getDeltaTime() {
        /*
         * try not to lose precision:
         */
        final Class<? extends Number> tClass = Optional.of(previousRoundTime).orElse(0.0d).getClass();
        if (tClass == Integer.class || tClass == Long.class || tClass == Short.class || tClass == Byte.class) {
            return getCurrentTime().longValue() - previousRoundTime.longValue();
        }
        return getCurrentTime().doubleValue() - previousRoundTime.doubleValue();
    }

    @Override
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "This is intentional")
    public final ExecutionEnvironment getExecutionEnvironment() {
        return environment;
    }

    /**
     * Support for first-class functions by returning the set of currently
     * accessible functions.
     *
     * @return Map from function name to function objects
     */
    protected final Map<Reference, ?> getFunctions() {
        return functions.orElseGet(Collections::emptyMap);
    }

    /**
     * Accessor for abstract network interface.
     *
     * @return Current abstract network interface
     */
    protected final NetworkManager getNetworkManager() {
        return networkManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <P> P getPersistent(final Supplier<P> ifAbsent) {
        final CodePath path = codePathFactory.createCodePath(callStack, callFrameSizes);
        final P last = (P) lastStored.get(path);
        return last == null ? ifAbsent.get() : last;
    }

    @Override
    public final Object getVariable(final Reference name) {
        return gamma.get(name);
    }

    /**
     * @return a view on the state stored in this context.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "The field is unmodifiable")
    public final Map<CodePath, Object> getStoredState() {
        return lastStored;
    }

    /**
     * Produce a child execution context, for encapsulated evaluation of
     * sub-programs.
     *
     * @return Child execution context
     */
    protected abstract S instance();

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
    public final void newCallStackFrame(final int... id) {
        if (id.length < 1) {
            throw new IllegalArgumentException("Unable to push unidentified stack frame: frame id cannot be empty");
        }
        callFrameSizes.push(id.length);
        callStack.add(id);
    }

    @Override
    public final void putMultipleVariables(final Map<Reference, ?> map) {
        gamma.putAll(map);
    }

    @Override
    public final void putVariable(final Reference name, final Object value) {
        gamma.put(name, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final S restrictDomain(@Nonnull final Field<?> f) {
        if (f.size() > theta.size()) {
            throw new IllegalArgumentException("Cannot expand domains. Current: " + theta.keySet() + ", desired: " + f.keys());
        }
        if (f.size() == theta.size()) {
            /*
             * No restriction to perform, the field has the same alignment
             */
            return (S) this;
        }
        final ImmutableMap<DeviceUID, Map<CodePath, Object>> restricted = theta.entrySet().stream()
            .filter(it -> f.containsKey(it.getKey()))
            .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue));
        final S correctlyTypedInstance = instance();
        final AbstractExecutionContext<S> restrictedInstance = correctlyTypedInstance;
        restrictedInstance.theta = restricted;
        restrictedInstance.gamma = gamma;
        restrictedInstance.toSend = toSend;
        restrictedInstance.tobeComputedBeforeSending = tobeComputedBeforeSending;
        restrictedInstance.callStack.addAll(callStack);
        restrictedInstance.functions = functions;
        restrictedInstance.exportsSize = exportsSize;
        restrictedInstance.variablesSize = variablesSize;
        restrictedInstance.previousRoundTime = previousRoundTime;
        restrictedInstance.toStore = toStore;
        restrictedInstance.lastStored = lastStored;
        restrictedContexts.add(restrictedInstance);
        return correctlyTypedInstance;
    }

    @Override
    public final void returnFromCallFrame() {
        final int size = callFrameSizes.pop();
        callStack.remove(callStack.size() - size, size);
    }

    @Override
    public final <T> T runInNewStackFrame(final int id, final Function<ExecutionContext, T> operation) {
        newCallStackFrame(id);
        final T result = operation.apply(this);
        returnFromCallFrame();
        return result;
    }

    @Override
    public final void setGloballyAvailableReferences(final Map<Reference, ?> knownFunctions) {
        if (functions.isPresent()) {
            throw new IllegalStateException("Globally available references cannot be set twice");
        } else {
            functions = Optional.of(knownFunctions);
        }
    }

    @Override
    public final void setPersistent(final Object o) {
        final CodePath path = codePathFactory.createCodePath(callStack, callFrameSizes);
        if (o == null) {
            toStore.remove(path);
        } else {
            final Object previous = toStore.put(path, o);
            if (previous != null) {
                throw new IllegalStateException("This program tried to persist two objects "
                    + "within the same code path.\n Previously inserted "
                    + previous + ": " + previous.getClass().getName() + "\n and then "
                    + o + ": " + o.getClass().getName()
                );
            }
        }
    }

    @Override
    public final void setup() {
        if (previousRoundTime == null) {
            previousRoundTime = getCurrentTime();
        }
        assert previousRoundTime != null : "Round time is null.";
        callStack.clear();
        environment.setup();
        toSend = newLinkedHashMapWithExpectedSize(exportsSize);
        tobeComputedBeforeSending = newLinkedHashMapWithExpectedSize(deferredExportSize);
        toStore = newLinkedHashMapWithExpectedSize(lastStored.size());
        gamma = newLinkedHashMapWithExpectedSize(variablesSize);
        gamma.putAll(functions.orElseGet(Collections::emptyMap));
        theta = Collections.unmodifiableMap(networkManager.getNeighborState());
        if (theta.containsKey(getDeviceUID())) {
            LOGGER.warn("Local device UID {} was included in the set of received messages, "
                    + "indicating that an auto-arc was present in your network configuration. "
                    + "This is being worked around by not considering such information, "
                    + "however, you should fix your logical netowrk.", getDeviceUID());
            final ImmutableMap.Builder<DeviceUID, Map<CodePath, Object>> immutableMap = ImmutableMap
                    .builderWithExpectedSize(theta.size() - 1);
            theta.forEach((id, object) -> {
                if (!id.equals(getDeviceUID())) {
                    immutableMap.put(id, object);
                }
            });
            theta = immutableMap.build();
        }
        newCallStackFrame(INIT.getCode());
    }
}
