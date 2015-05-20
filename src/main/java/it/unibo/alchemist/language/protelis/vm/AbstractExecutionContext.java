/**
 * 
 */
package it.unibo.alchemist.language.protelis.vm;

import gnu.trove.TCollections;
import gnu.trove.list.TByteList;
import gnu.trove.list.array.TByteArrayList;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import it.unibo.alchemist.language.protelis.datatype.Field;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.language.protelis.util.StackImpl;
import it.unibo.alchemist.model.interfaces.INode;
import it.unibo.alchemist.utils.FasterString;

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
	private final Stack gamma;
	private final TIntObjectMap<Map<CodePath, Object>> theta;
	private final Map<CodePath, Object> toSend = MAPMAKER.makeMap();
	
	protected AbstractExecutionContext(final Map<FasterString, Object> environmentVariables, final TIntObjectMap<Map<CodePath, Object>> receivedMessages) {
		callStack.add((byte) 1);
		gamma = new StackImpl(environmentVariables);
		theta = TCollections.unmodifiableMap(receivedMessages);
	}

//	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
	
	@Override
	public void newCallStackFrame(final byte... id) {
		callStack.add(id);
	}

	@Override
	public void returnFromCallFrame(final int frameSize) {
		callStack.remove(callStack.size() - frameSize, frameSize);
	}

	@Override
	public void returnFromCallFrame() {
		returnFromCallFrame(1);
	}

	@Override
	public void pushOnVariablesStack() {
		gamma.push();
	}
	
	@Override
	public void popOnVariableStack() {
		gamma.pop();
	}
	
	@Override
	public void putVariable(final FasterString name, final Object value, final boolean canShadow) {
		gamma.put(name, value, canShadow);
	}
	
	@Override
	public void putMultipleVariables(final Map<FasterString, ? extends Object> map) {
		gamma.putAll(map);
	}
	
	protected abstract AbstractExecutionContext restrictedInstance(TIntObjectMap<Map<CodePath, Object>> restrictedTheta);
	
	@Override
	public AbstractExecutionContext restrictDomain(final Field f) {
		final TIntObjectMap<Map<CodePath, Object>> restricted = new TIntObjectHashMap<>(theta.size());
		final INode<Object> localDevice = getLocalDevice();
		for (final INode<Object> n : f.nodeIterator()) {
			if (!n.equals(localDevice)) {
				final int id = n.getId();
				restricted.put(id, theta.get(id));
			}
		}
		return restrictedInstance(restricted);
	}
	
	/**
	 * @param id
	 * @return
	 */
	protected abstract INode<Object> deviceFromId(int id);
	
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
	
}
