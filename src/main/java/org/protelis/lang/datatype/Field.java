/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.datatype;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.math3.util.Pair;
import org.danilopianini.lang.TriFunction;

import java8.util.function.BiFunction;
import java8.util.function.BinaryOperator;
import java8.util.function.Function;
import java8.util.function.UnaryOperator;

/**
 * A field is a function associating a collection of devices to values.
 */
public interface Field extends Serializable {

    /**
     * Add a neighbor/value pair to this Field.
     * 
     * @param n
     *            Neighbor to add
     * @param v
     *            Value associated with the neighbor
     */
    void addSample(DeviceUID n, Object v);

    /**
     * Remove a neighbor/value pair from this Field.
     * 
     * @param n
     *            Neighbor to remove
     * @return Value that was associated with this neighbor, null if neighbor
     *         was not present
     */
    Object removeSample(DeviceUID n);

    /**
     * Get the value associated with a neighbor by this Field.
     * 
     * @param n
     *            Neighbor
     * @return Value that is associated with this neighbor, null if neighbor is
     *         not present
     */
    Object getSample(DeviceUID n);

    /**
     * Apply an operator to reduce over the set of all devices, selecting a
     * single device.
     * 
     * @param op
     *            Order-insensitive operator to reduce devices in pairs
     * @param exclude
     *            Device to be excluded (typically self), or null if all devices
     *            are to be considered
     * @return The single device selected by reduction over the set of all
     *         devices
     */
    DeviceUID reduceKeys(final BinaryOperator<DeviceUID> op, final DeviceUID exclude);

    /**
     * Apply an operator to reduce over the set of all values, selecting a
     * single value.
     * 
     * @param op
     *            Order-insensitive operator to reduce values, two at a time,
     *            starting with the default
     * @param exclude
     *            Device to be excluded (typically self), or null if all devices
     *            are to be considered
     * @param defaultVal
     *            The value to return if the field contains no devices or only
     *            the excluded device
     * @return The single value created by reduction over the set of all values
     */
    Object reduceVals(final BinaryOperator<Object> op, final DeviceUID exclude, final Object defaultVal);

    /**
     * Apply an operator to reduce over the set of all device/value pairs,
     * selecting a single pair.
     * 
     * @param accumulator
     *            Order-insensitive operator to reduce pairs, two at a time,
     *            starting with the default
     * @param exclude
     *            Device to be excluded (typically self), or null if all devices
     *            are to be considered
     * @return The single pair selected by reduction over the set of all pairs
     */
    Pair<DeviceUID, Object> reducePairs(final BinaryOperator<Pair<DeviceUID, Object>> accumulator,
            final DeviceUID exclude);

    /**
     * @return An iterator over the set of neighbors.
     */
    Iterable<DeviceUID> nodeIterator();

    /**
     * @return An iterator over the set of neighbor values.
     */
    Iterable<Object> valIterator();

    /**
     * @return An iterator over the set of neighbor/value pairs
     */
    Iterable<Pair<DeviceUID, Object>> coupleIterator();

    /**
     * @return Number of neighbors with values in the field
     */
    int size();

    /**
     * @return True if there are no neighbors
     */
    boolean isEmpty();

    /**
     * @param n
     *            The device being checked for
     * @return True if n is contained in this field
     */
    boolean containsNode(DeviceUID n);

    /**
     * @return The type of the values of this field
     */
    Class<?> getExpectedType();

}
