/**
 * 
 */
package it.unibo.alchemist.language.protelis.vm;

import gnu.trove.TCollections;
import gnu.trove.list.TByteList;
import gnu.trove.list.array.TByteArrayList;
import gnu.trove.list.linked.TIntLinkedList;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.stack.TByteStack;
import gnu.trove.stack.TIntStack;
import gnu.trove.stack.array.TIntArrayStack;
import it.unibo.alchemist.language.protelis.datatype.Field;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Device;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.language.protelis.util.StackImpl;
import it.unibo.alchemist.model.interfaces.INode;
import it.unibo.alchemist.utils.FasterString;

import java.util.HashMap;
import java.util.Map;
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
	private final Stack gamma;
	private final TLongObjectMap<Map<CodePath, Object>> theta;
	private final Map<CodePath, Object> toSend = MAPMAKER.makeMap();
	private final Map<FasterString, Object> env;
	
	protected AbstractExecutionContext(final Map<FasterString, ?> environmentVariables, final TLongObjectMap<Map<CodePath, Object>> receivedMessages) {
		callStack.add((byte) 1);
		env = new HashMap<>(environmentVariables);
		gamma = new StackImpl(env);
		theta = TCollections.unmodifiableMap(receivedMessages);
	}

	@Override
	public void newCallStackFrame(final byte... id) {
		callFrameSizes.push(id.length);
		callStack.add(id);
		gamma.push();
	}

	@Override
	public void returnFromCallFrame() {
		int size = callFrameSizes.pop();
		callStack.remove(callStack.size() - size, size);
		gamma.pop();
	}

	@Override
	public void putVariable(final FasterString name, final Object value, final boolean canShadow) {
		gamma.put(name, value, canShadow);
	}
	
	@Override
	public void putMultipleVariables(final Map<FasterString, ?> map) {
		gamma.putAll(map);
	}
	
	protected abstract AbstractExecutionContext restrictedInstance(TLongObjectMap<Map<CodePath, Object>> restrictedTheta);
	
	@Override
	public AbstractExecutionContext restrictDomain(final Field f) {
		final TLongObjectMap<Map<CodePath, Object>> restricted = new TLongObjectHashMap<>(theta.size());
		final Device localDevice = getLocalDevice();
		for (final Device n : f.nodeIterator()) {
			if (!n.equals(localDevice)) {
				final long id = n.getId();
				restricted.put(id, theta.get(id));
			}
		}
		return restrictedInstance(restricted);
	}
	
	/**
	 * @param id
	 * @return
	 */
	protected abstract Device deviceFromId(long id);
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Field buildField(final Function<T, ?> computeValue, final T localValue) {
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
	public Object getVariable(final FasterString name) {
		return gamma.get(name);
	}
	
	@Override
	public boolean hasEnvironmentVariable(final String id) {
		return env.containsKey(new FasterString(id));
	}

	@Override
	public Object getEnvironmentVariable(final String id) {
		return env.get(new FasterString(id));
	}

	@Override
	public boolean putEnvironmentVariable(final String id, final Object v) {
		return env.put(new FasterString(id), v) != null;
	}

	@Override
	public Object removeEnvironmentVariable(final String id) {
		return env.remove(new FasterString(id));
	}

	@Override
	public Map<FasterString, Object> getCurrentEnvironment() {
		return new HashMap<>(env);
	}

	
}
