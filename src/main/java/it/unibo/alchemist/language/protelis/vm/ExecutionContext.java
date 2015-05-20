/**
 * 
 */
package it.unibo.alchemist.language.protelis.vm;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import it.unibo.alchemist.language.protelis.datatype.Field;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.model.interfaces.INode;
import it.unibo.alchemist.model.interfaces.IPosition;
import it.unibo.alchemist.utils.FasterString;

/**
 * @author Danilo Pianini
 *
 */
public interface ExecutionContext {

	void newCallStackFrame(byte... id);
	
	void returnFromCallFrame();
	
	void returnFromCallFrame(int frameSize);
	
	void pushOnVariablesStack();
	
	void popOnVariableStack();
	
	void putMultipleVariables(Map<FasterString, ? extends Object> map);
	
	/**
	 * @param name
	 * @param value
	 * @param canShadow if no other variable with the same name exists, this parameter is irrelevant. Otherwise, if true, the previous variable will be shadowed. If false, the variable will be overridden (with possible side effects upon return) instead.
	 */
	void putVariable(FasterString name, Object value, boolean canShadow);

	ExecutionContext restrictDomain(Field f);
	
	INode<Object> getLocalDevice();

	/**
	 * @return the current time, absolute
	 */
	double getCurrentTime();
	
	/**
	 * @param target
	 *            the device to compute the distance to
	 * @return the distance to the device
	 */
	double distanceTo(INode<Object> target);
	
	IPosition getDevicePosition();

	/**
	 * Builds a new {@link Field}, fetching data from all the aligned neighbors.
	 * A neighbor is considered to be aligned it it has reached the exact same
	 * {@link CodePath}. The field will always contain at least one value,
	 * namely the value of the local device.
	 * 
	 * @param localValue
	 *            a {@link Supplier} which will be used to compute the local
	 *            value for this field
	 * @return a new {@link Field} containing the local device value and the
	 *         values for any of the aligned neighbors
	 */
	<T> Field buildField(Function<T, ?> computeValue, T localValue);

	double nextRandomDouble();

	Object getVariable(FasterString name);
	
}
