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
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.apache.commons.math3.util.Pair;
import org.danilopianini.lang.PrimitiveUtils;
import org.danilopianini.lang.util.FasterString;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.NetworkManager;
import org.protelis.vm.util.CodePath;
import org.protelis.vm.util.Stack;
import org.protelis.vm.util.StackImpl;

import com.google.common.collect.MapMaker;

import gnu.trove.list.TByteList;
import gnu.trove.list.array.TByteArrayList;
import gnu.trove.stack.TIntStack;
import gnu.trove.stack.array.TIntArrayStack;

/**
 *	Partial implementation of ExecutionContext, containing functionality expected to be shared
 *	between most implementations.  Instantiations of Protelis should generally extend this class.
 */
public abstract class AbstractExecutionContext implements ExecutionContext {
	
	private static final MapMaker MAPMAKER = new MapMaker();
	
	private final TByteList callStack = new TByteArrayList();
	private final TIntStack callFrameSizes = new TIntArrayStack();
	private final NetworkManager nm;
	private Map<FasterString, ?> functions;
	private Stack gamma;
	private Map<DeviceUID, Map<CodePath, Object>> theta;
	private Map<FasterString, Object> env;
	private Map<CodePath, Object> toSend;
	private Number previousRoundTime;
	
	/**
	 * Create a new AbstractExecutionContext.
	 * @param netmgr Abstract network interface to be used
	 */
	protected AbstractExecutionContext(final NetworkManager netmgr) {
		Objects.requireNonNull(netmgr);
		nm = netmgr;
	}
	
	@Override
	public final void setAvailableFunctions(final Map<FasterString, FunctionDefinition> knownFunctions) {
		functions = Collections.unmodifiableMap(knownFunctions);
	}
	
	/**
	 * @return The current set of environment variables bindings
	 */
	protected abstract Map<FasterString, Object> currentEnvironment();
	
	/**
	 * Replace the entire current set of environment variable bindings.
	 * @param newEnvironment New set of variable bindings
	 */
	protected abstract void setEnvironment(Map<FasterString, Object> newEnvironment);
	
	@Override
	public final void commit() {
		Objects.requireNonNull(env);
		Objects.requireNonNull(gamma);
		Objects.requireNonNull(theta);
		Objects.requireNonNull(toSend);
		Objects.requireNonNull(functions);
		previousRoundTime = getCurrentTime();
		setEnvironment(env);
		nm.sendMessage(toSend);
		env = null;
		gamma = null;
		theta = null;
		toSend = null;
	}
	
	@Override
	public final void setup() {
		if (previousRoundTime == null) {
			previousRoundTime = getCurrentTime();
		}
		callStack.clear();
		callStack.add((byte) 1);
		env = currentEnvironment();
		toSend = MAPMAKER.makeMap();
		gamma = new StackImpl(new HashMap<>(functions));
		theta = Collections.unmodifiableMap(nm.takeMessages());
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
	public final void putVariable(final FasterString name, final Object value, final boolean canShadow) {
		gamma.put(name, value, canShadow);
	}
	
	@Override
	public final void putMultipleVariables(final Map<FasterString, ?> map) {
		gamma.putAll(map);
	}
	
	/**
	 * Produce a child execution context, for encapsulated evaluation of sub-programs.
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
		restrictedInstance.env = env;
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
		 * If there is a request to build a field, then it means this is a nbr-like operation
		 */
		if (toSend.putIfAbsent(codePath, localValue) != null) {
			throw new IllegalStateException("This program has attempted to build a field twice with the same code path. It is probably a bug in Protelis");
		}
		final Field res = Field.create(theta.size() + 1);
		theta.entrySet().stream()
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
	public final Object getVariable(final FasterString name) {
		return gamma.get(name);
	}
	
	@Override
	public final boolean hasEnvironmentVariable(final String id) {
		return env.containsKey(new FasterString(id));
	}

	@Override
	public final Object getEnvironmentVariable(final String id) {
		return env.get(new FasterString(id));
	}

	@Override
	public final Object getEnvironmentVariable(final String id, final Object defaultValue) {
		if (hasEnvironmentVariable(id)) {
			return env.get(new FasterString(id));
		} else {
			return defaultValue;
		}
	}

	@Override
	public final boolean putEnvironmentVariable(final String id, final Object v) {
		return env.put(new FasterString(id), v) != null;
	}

	@Override
	public final Object removeEnvironmentVariable(final String id) {
		return env.remove(new FasterString(id));
	}
	
	/**
	 * Accessor for abstract network interface.
	 * @return Current abstract network interface
	 */
	protected final NetworkManager getNetworkManager() {
		return nm;
	}

	/**
	 * Support for first-class functions by returning the set of currently accessible functions.
	 * @return Map from function name to function objects
	 */
	protected final Map<FasterString, ?> getFunctions() {
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

}
