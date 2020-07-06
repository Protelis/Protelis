/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
/**
 * 
 */
package org.protelis.vm;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.interpreter.util.Reference;

/**
 * Interface between a ProtelisVM and environment in which it is executing. The
 * ExecutionContext is responsible for three things:
 * 
 * 1. Tracking local persistent state from evaluation to evaluation
 * 
 * 2. Tracking evaluation state shared by neighbors
 * 
 * 3. The state of the device in its external environment (time, space, sensors,
 * etc.)
 */
public interface ExecutionContext {

    /**
     * Builds a new {@link Field}, fetching data from all the aligned neighbors.
     * A neighbor is considered to be aligned it it has reached the exact same
     * {@link org.protelis.vm.impl.DefaultTimeEfficientCodePath}. The field will always contain at least one value,
     * namely the value of the local device.
     * 
     * @param computeValue
     *            a function that will be applied to localValue and the
     *            equivalents shared from neighbors in the process of
     *            constructing this field: the field consists of the values
     *            returned from applying computeValue to each of device's value
     * @param localValue
     *            the local value for this field
     * @param <T>
     *            the type of the input
     * @param <R>
     *            the type of the output
     * @return a new {@link Field} containing the local device value and the
     *         values for any of the aligned neighbors
     */
    <T, R> Field<R> buildField(Function<T, R> computeValue, T localValue);

    /**
     * Builds a new {@link Field}, fetching data from all the aligned neighbors.
     * A neighbor is considered to be aligned it it has reached the exact same
     * {@link org.protelis.vm.impl.DefaultTimeEfficientCodePath}. The field will always contain at least one value,
     * namely the value of the local device. The deferred version does not immediately schedule
     * the local value for being sent away. Rather, it schedules the provided {@link Supplier}
     * to be executed at the end of the round for obtaining the value to be shared. This
     * function is the base upon which the {@link org.protelis.lang.interpreter.impl.ShareCall}
     * is built.
     * 
     * @param computeValue
     *            a function that will be applied to localValue and the
     *            equivalents shared from neighbors in the process of
     *            constructing this field: the field consists of the values
     *            returned from applying computeValue to each of device's value
     * @param currentLocal
     *            the value to be used as local for this field
     * @param toBeSent
     *            a {@link java8.util.function.Supplier} which will be used to compute the local
     *            value for this field that will get exported at the end of the round
     * @param <T>
     *            the type of the input
     * @param <R>
     *            the type of the output
     * @return a new {@link Field} containing the local device value and the
     *         values for any of the aligned neighbors
     */
    <T, R> Field<R> buildFieldDeferred(Function<T, R> computeValue, T currentLocal, Supplier<T> toBeSent);

    /**
     * Called just after the VM is executed, to finalize information of the
     * execution for the environment.
     */
    void commit();

    /**
     * @return the current device time, absolute
     */
    Number getCurrentTime();

    /**
     * @return the current time, relative to last round
     */
    Number getDeltaTime();

    /**
     * @return The unique identifier for the device where execution is taking
     *         place.
     */
    DeviceUID getDeviceUID();

    /**
     * @return The current {@link ExecutionEnvironment}
     */
    ExecutionEnvironment getExecutionEnvironment();

    <S> S getPersistent(Supplier<S> ifAbsent);

    /**
     * Look up the value of a variable from the local environment.
     * 
     * @param reference
     *            The variable to be looked up
     * @return Value of the variable, or null if it cannot be found.
     */
    Object getVariable(Reference reference);

    /**
     * @param id
     *            stack frame type
     */
    void newCallStackFrame(byte... id);

    /**
     * @param id
     *            stack frame type
     */
    void newCallStackFrame(int... id);

    /**
     * @param id
     *            stack frame type
     * @param operation the operation to run in the new context
     * @param <T> the return type
     * @return the result of the evaluation
     */
    <T> T runInNewStackFrame(int id, Function<ExecutionContext, T> operation);

    /**
     * Obtain a system-independent (pseudo)random number.
     * 
     * @return a uniformly distributed value between 0.0 and 1.0
     */
    double nextRandomDouble();

    /**
     * Pushes multiple variables.
     * 
     * @param map
     *            the variables to push
     */
    void putMultipleVariables(Map<Reference, ?> map);

    /**
     * Puts a variable value, overwriting the previous one, if any.
     * 
     * @param name
     *            variable reference
     * @param value
     *            variable value
     */
    void putVariable(Reference name, Object value);

    /**
     * Give a field, returns a new {@link ExecutionContext} whose domain is the same of the field one.
     * 
     * @param f the field
     * @return the restricted domain
     */
    ExecutionContext restrictDomain(Field<?> f);

    /**
     * returns from the last frame.
     */
    void returnFromCallFrame();

    /**
     * Used internally to support first-class functions by make the functions of
     * a program accessible for reflection at runtime.
     * 
     * @param knownFunctions
     *            Collection of accessible functions, associating function name
     *            and value.
     */
    void setGloballyAvailableReferences(Map<Reference, ?> knownFunctions);

    void setPersistent(Object o);

    /**
     * Called just before the VM is executed, to enable and preparations needed
     * in the environment.
     */
    void setup();
}
