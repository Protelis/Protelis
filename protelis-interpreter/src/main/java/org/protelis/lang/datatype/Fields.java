/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.lang.datatype;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.protelis.lang.interpreter.util.TriFunction;

/**
 * Utility class for {@link Field}.
 *
 */
public final class Fields {

    private Fields() {
    }

    /**
     * @param fun    the {@link UnaryOperator} to apply
     * @param fields an array of integers containing the indexes of the arguments
     *               that are fields
     * @param a      the single parameter
     * @return the result of the execution of fun(a)
     */
    public static Field<Object> applyWithSingleParam(final UnaryOperator<Object> fun, final int[] fields,
            final Object a) {
        return apply((t, p) -> fun.apply(p[0]), false, fields, null, a);
    }

    /**
     * @param fun    the 2-ary function to apply, in form of a
     *               {@link BinaryOperator}
     * @param fields an array of integers containing the indexes of the arguments
     *               that are fields
     * @param a      the first parameter
     * @param b      the second parameter
     * @return the result of the execution of fun(a, b)
     */
    public static Field<Object> apply(final BinaryOperator<Object> fun, final int[] fields, final Object a,
            final Object b) {
        return apply((t, p) -> fun.apply(p[0], p[1]), false, fields, null, a, b);
    }

    /**
     * @param fun    the {@link TriFunction} to apply
     * @param fields an array of integers containing the indexes of the arguments
     *               that are fields
     * @param a      the first parameter
     * @param b      the second parameter
     * @param c      the third parameter
     * @param <R>    result field type
     * @return the result of the execution of fun(a, b, c)
     */
    public static <R> Field<R> apply(final TriFunction<Object, Object, Object, R> fun, final int[] fields,
            final Object a, final Object b, final Object c) {
        return apply((t, p) -> fun.apply(p[0], p[1], p[2]), false, fields, null, a, b, c);
    }

    /**
     * @param fun          the function to apply.
     * @param fieldIndexes the indexes of which among the arguments are fields
     * @param args         the arguments
     * @param <R>    result field type
     * @return a new field resulting from the application of the {@link Function} to
     *         the target and the arguments
     */
    public static <R> Field<R> apply(final Function<Object[], R> fun, final int[] fieldIndexes, final Object... args) {
        return apply(fun, false, fieldIndexes, null, args);
    }

    /**
     * @param fun          the function to apply.
     * @param fieldTarget  true if the target is a field
     * @param fieldIndexes the indexes of which among the arguments are fields
     * @param target       the object this method will be invoked on
     * @param args         the arguments
     * @param <R>    result field type
     * @return a new field resulting from the application of the {@link Function} to
     *         the target and the arguments
     */
    public static <R> Field<R> apply(final Function<Object[], R> fun, final boolean fieldTarget,
            final int[] fieldIndexes, final Object target, final Object... args) {
        return apply((t, p) -> fun.apply(p), fieldTarget, fieldIndexes, target, args);
    }

    /**
     * @param fun          the function to apply. It must accept as a first argument
     *                     the data type carried by the target, and an Object array
     *                     as second argument (namely, it is a vararg)
     * @param fieldTarget  true if the target is a field
     * @param fieldIndices the indexes of which among the arguments are fields
     * @param target       the object this method will be invoked on
     * @param args         the arguments
     * @param <T>    input field type
     * @param <R>    result field type
     * @return a new field resulting from the application of the {@link BiFunction}
     *         to the target and the arguments
     */
    public static <T, R> Field<R> apply(final BiFunction<T, Object[], R> fun, final boolean fieldTarget,
            final int[] fieldIndices, final T target, final Object... args) {
        if (!fieldTarget && fieldIndices.length == 0) {
            throw new IllegalArgumentException(
                    "To use this field application at least one of the parameters must be a field.");
        }
        /*
         * A consistency check may make sense here.
         */
        final Field<?> refField = (Field<?>) (fieldTarget ? target : args[fieldIndices[0]]);
        return refField.map(id -> fun.apply(targetFor(fieldTarget, target, id), argumentsFor(args, fieldIndices, id)));
    }

    @SuppressWarnings("unchecked")
    private static <T> T targetFor(final boolean fieldTarget, final T target, final DeviceUID id) {
        return fieldTarget ? ((Field<T>) target).get(id) : target;
    }

    private static Object[] argumentsFor(final Object[] args, final int[] fieldIndices, final DeviceUID id) {
        final Object[] actualArgs = Arrays.copyOf(args, args.length);
        for (final int i : fieldIndices) {
            final Object arg = ((Field<?>) actualArgs[i]).get(id);
            if (arg == null) {
                throw new IllegalStateException("Field " + actualArgs[i] + " is not aligned with "
                        + args[fieldIndices[0]] + " (missing " + id + ")");
            }
            actualArgs[i] = arg;
        }
        return actualArgs;
    }
}
