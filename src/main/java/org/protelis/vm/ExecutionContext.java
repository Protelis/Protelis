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
import java8.util.function.Function;
import java8.util.function.Supplier;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.util.Reference;
import org.protelis.vm.util.CodePath;

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
     * @param id
     *            stack frame type
     */
    void newCallStackFrame(byte... id);

    /**
     * returns from the last frame.
     */
    void returnFromCallFrame();

    /**
     * Pushes multiple variables.
     * 
     * @param map
     *            the variables to push
     */
    void putMultipleVariables(Map<Reference, ?> map);

    /**
     * @param name
     *            variable reference
     * @param value
     *            variable value
     * @param canShadow
     *            if no other variable with the same name exists, this parameter
     *            is irrelevant. Otherwise, if true, the previous variable will
     *            be shadowed. If false, the variable will be overridden (with
     *            possible side effects upon return) instead.
     */
    void putVariable(Reference name, Object value, boolean canShadow);

    /**
     * Give a field, returns a new {@link ExecutionContext} whose domain is the same of the field one.
     * 
     * @param f the field
     * @return the restricted domain
     */
    ExecutionContext restrictDomain(Field f);

    /**
     * @return The unique identifier for the device where execution is taking
     *         place.
     */
    DeviceUID getDeviceUID();

    /**
     * @return the current device time, absolute
     */
    Number getCurrentTime();

    /**
     * @return the current time, relative to last round
     */
    Number getDeltaTime();

    /**
     * Builds a new {@link Field}, fetching data from all the aligned neighbors.
     * A neighbor is considered to be aligned it it has reached the exact same
     * {@link CodePath}. The field will always contain at least one value,
     * namely the value of the local device.
     * 
     * @param computeValue
     *            a function that will be applied to localValue and the
     *            equivalents shared from neighbors in the process of
     *            constructing this field: the field consists of the values
     *            returned from applying computeValue to each of device's value
     * @param localValue
     *            a {@link Supplier} which will be used to compute the local
     *            value for this field
     * @param <T>
     *            the type of the input
     * @return a new {@link Field} containing the local device value and the
     *         values for any of the aligned neighbors
     */
    <T> Field buildField(Function<T, ?> computeValue, T localValue);

    /**
     * Obtain a system-independent (pseudo)random number.
     * 
     * @return a uniformly distributed value between 0.0 and 1.0
     */
    double nextRandomDouble();

    /**
     * Look up the value of a variable from the local environment.
     * 
     * @param reference
     *            The variable to be looked up
     * @return Value of the variable, or null if it cannot be found.
     */
    Object getVariable(Reference reference);

    /**
     * @return The current {@link ExecutionEnvironment}
     */
    ExecutionEnvironment getExecutionEnvironment();

    /**
     * Called just before the VM is executed, to enable and preparations needed
     * in the environment.
     */
    void setup();

    /**
     * Called just after the VM is executed, to finalize information of the
     * execution for the environment.
     */
    void commit();

    /**
     * Used internally to support first-class functions by make the functions of
     * a program accessible for reflection at runtime.
     * 
     * @param knownFunctions
     *            Collection of accessible functions, associating function name
     *            and value.
     */
    void setAvailableFunctions(Map<Reference, FunctionDefinition> knownFunctions);

}
