/**
 * 
 */
package it.unibo.alchemist.language.protelis.vm;

import gnu.trove.TCollections;
import gnu.trove.list.TByteList;
import gnu.trove.list.array.TByteArrayList;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.stack.TIntStack;
import gnu.trove.stack.array.TIntArrayStack;
import it.unibo.alchemist.language.protelis.FunctionDefinition;
import it.unibo.alchemist.language.protelis.datatype.Field;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Device;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.language.protelis.util.StackImpl;
import it.unibo.alchemist.utils.FasterString;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import com.google.common.collect.MapMaker;

/**
 * @author Danilo Pianini
 *
 */
public abstract class AbstractExecutionContext implements ExecutionContext {
	
	private static final MapMaker MAPMAKER = new MapMaker();
	
	private final TByteList callStack = new TByteArrayList();
	private final TIntStack callFrameSizes = new TIntArrayStack();
	private final NetworkManager nm;
	private Map<FasterString, ?> functions;
	private Stack gamma;
	private TLongObjectMap<Map<CodePath, Object>> theta;
	private Map<FasterString, Object> env;
	private Map<CodePath, Object> toSend;
	
	protected AbstractExecutionContext(final NetworkManager netmgr) {
		Objects.requireNonNull(netmgr);
		nm = netmgr;
	}
	
	public final void setAvailableFunctions(final Map<FasterString, FunctionDefinition> knownFunctions){
		functions = Collections.unmodifiableMap(knownFunctions);
	}
	
	protected abstract Map<FasterString, Object> currentEnvironment();
	
	protected abstract void setEnvironment(Map<FasterString, Object> newEnvironment);
	
	@Override
	public final void commit() {
		Objects.requireNonNull(env);
		Objects.requireNonNull(gamma);
		Objects.requireNonNull(theta);
		Objects.requireNonNull(toSend);
		Objects.requireNonNull(functions);
		setEnvironment(env);
		nm.sendMessage(toSend);
		env = null;
		gamma = null;
		theta = null;
		toSend = null;
	}
	
	@Override
	public final void setup() {
		callStack.clear();
		callStack.add((byte) 1);
		env = currentEnvironment();
		toSend = MAPMAKER.makeMap();
		gamma = new StackImpl(new HashMap<>(functions));
		theta = TCollections.unmodifiableMap(nm.takeMessages());
	}
	
	@Override
	public final void newCallStackFrame(final byte... id) {
		callFrameSizes.push(id.length);
		callStack.add(id);
		gamma.push();
	}

	@Override
	public final void returnFromCallFrame() {
		int size = callFrameSizes.pop();
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
	
	protected abstract AbstractExecutionContext instance();
	
	@Override
	public final AbstractExecutionContext restrictDomain(final Field f) {
		final TLongObjectMap<Map<CodePath, Object>> restricted = new TLongObjectHashMap<>(theta.size());
		final Device localDevice = getLocalDevice();
		for (final Device n : f.nodeIterator()) {
			if (!n.equals(localDevice)) {
				final long id = n.getId();
				restricted.put(id, theta.get(id));
			}
		}
		AbstractExecutionContext restrictedInstance = instance();
		restrictedInstance.theta = restricted;
		restrictedInstance.gamma = gamma;
		restrictedInstance.env = env;
		restrictedInstance.toSend = toSend;
		return restrictedInstance;
	}
	
	/**
	 * @param id
	 * @return
	 */
	protected abstract Device deviceFromId(long id);
	
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
			throw new IllegalStateException("This program has attempted to build a field twice with the same code path. It can either be a bug in Protelis");
		}
		final Field res = Field.create(theta.size() + 1);
		theta.forEachEntry((n, pathsMap) -> {
			final Object val = pathsMap.get(codePath);
			if (val != null) {
				/*
				 * This cast is OK by construction, if no bug is there and no
				 * wild casts are done by the caller.
				 */
				res.addSample(deviceFromId(n), computeValue.apply((T) val));
			}
			return true;
		});
		res.addSample(getLocalDevice(), computeValue.apply(localValue));
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
	public final boolean putEnvironmentVariable(final String id, final Object v) {
		return env.put(new FasterString(id), v) != null;
	}

	@Override
	public final Object removeEnvironmentVariable(final String id) {
		return env.remove(new FasterString(id));
	}
	
	protected final NetworkManager getNetworkManager() {
		return nm;
	}

	protected final Map<FasterString, ?> getFunctions() {
		return functions;
	}

}
