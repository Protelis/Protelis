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
import org.protelis.lang.datatype.impl.FieldMapImpl;

import java8.util.function.BiFunction;
import java8.util.function.BinaryOperator;
import java8.util.function.Function;
import java8.util.function.UnaryOperator;

/**
 * A field is a function associating a collection of devices to values.
 */
public interface Field extends Serializable {

    /**
     * @param defaultSize
     *            creates a new and empty {@link Field}, defaulting on the
     *            specified size
     * @return an empty {@link Field}
     */
    static Field create(int defaultSize) {
        return new FieldMapImpl(defaultSize + 1, 1f);
    }

    /**
     * @param fun
     *            the {@link UnaryOperator} to apply
     * @param fields
     *            an array of integers containing the indexes of the arguments
     *            that are fields
     * @param a
     *            the single parameter
     * @return the result of the execution of fun(a)
     */
    static Field applyWithSingleParam(UnaryOperator<Object> fun, int[] fields, Object a) {
        return apply((t, p) -> fun.apply(p[0]), false, fields, null, a);
    }

    /**
     * @param fun
     *            the 2-ary function to apply, in form of a
     *            {@link BinaryOperator}
     * @param fields
     *            an array of integers containing the indexes of the arguments
     *            that are fields
     * @param a
     *            the first parameter
     * @param b
     *            the second parameter
     * @return the result of the execution of fun(a, b)
     */
    static Field apply(BinaryOperator<Object> fun, int[] fields, Object a, Object b) {
        return apply((t, p) -> fun.apply(p[0], p[1]), false, fields, null, a, b);
    }

    /**
     * @param fun
     *            the {@link TriFunction} to apply
     * @param fields
     *            an array of integers containing the indexes of the arguments
     *            that are fields
     * @param a
     *            the first parameter
     * @param b
     *            the second parameter
     * @param c
     *            the third parameter
     * @return the result of the execution of fun(a, b, c)
     */
    static Field apply(final TriFunction<Object, Object, Object, Object> fun, int[] fields, Object a, Object b,
            Object c) {
        return apply((t, p) -> fun.apply(p[0], p[1], p[2]), false, fields, null, a, b, c);
    }

    /**
     * @param fun
     *            the function to apply.
     * @param fieldIndexes
     *            the indexes of which among the arguments are fields
     * @param args
     *            the arguments
     * @return a new field resulting from the application of the
     *         {@link Function} to the target and the arguments
     */
    static Field apply(final Function<Object[], Object> fun, int[] fieldIndexes, Object... args) {
        return apply(fun, false, fieldIndexes, null, args);
    }

    /**
     * @param fun
     *            the function to apply.
     * @param fieldTarget
     *            true if the target is a field
     * @param fieldIndexes
     *            the indexes of which among the arguments are fields
     * @param target
     *            the object this method will be invoked on
     * @param args
     *            the arguments
     * @return a new field resulting from the application of the
     *         {@link Function} to the target and the arguments
     */
    static Field apply(final Function<Object[], Object> fun, final boolean fieldTarget, int[] fieldIndexes,
            final Object target, Object... args) {
        return apply((t, p) -> fun.apply(p), fieldTarget, fieldIndexes, target, args);
    }

    /**
     * @param fun
     *            the function to apply. It must accept as a first argument the
     *            data type carried by the target, and an Object array as second
     *            argument (namely, it is a vararg)
     * @param fieldTarget
     *            true if the target is a field
     * @param fieldIndexes
     *            the indexes of which among the arguments are fields
     * @param target
     *            the object this method will be invoked on
     * @param args
     *            the arguments
     * @return a new field resulting from the application of the
     *         {@link BiFunction} to the target and the arguments
     */
    static Field apply(final BiFunction<Object, Object[], Object> fun, final boolean fieldTarget,
            final int[] fieldIndexes, final Object target, final Object... args) {
        if (!fieldTarget && fieldIndexes.length == 0) {
            throw new IllegalArgumentException(
                    "To use this field application at least one of the parameters must be a field.");
        }
        /*
         * A consistency check may make sense here.
         */
        final Field refField = (Field) (fieldTarget ? target : args[fieldIndexes[0]]);
        final Field result = create(refField.size());
        for (final DeviceUID node : refField.nodeIterator()) {
            Object[] actualArgs = Arrays.copyOf(args, args.length);
            for (final int i : fieldIndexes) {
                final Object arg = ((Field) actualArgs[i]).getSample(node);
                if (arg == null) {
                    throw new IllegalStateException("Field " + actualArgs[i] + " is not aligned with "
                            + args[fieldIndexes[0]] + " (missing " + node + ")");
                }
                actualArgs[i] = arg;
            }
            final Object actualTarget = fieldTarget ? (((Field) target).getSample(node)) : target;
            result.addSample(node, fun.apply(actualTarget, actualArgs));
        }
        return result;
    }

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
