package org.protelis.lang.datatype;

import java.util.Arrays;

import org.danilopianini.lang.TriFunction;

import java8.util.function.BiFunction;
import java8.util.function.BinaryOperator;
import java8.util.function.Function;
import java8.util.function.UnaryOperator;

/**
 * Utility class for {@link Field}.
 *
 */
public final class Fields {

    private Fields() {
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
    public static Field applyWithSingleParam(final UnaryOperator<Object> fun, final int[] fields, final Object a) {
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
    public static Field apply(final BinaryOperator<Object> fun, final int[] fields, final Object a, final Object b) {
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
    public static Field apply(final TriFunction<Object, Object, Object, Object> fun, final int[] fields, final Object a, final Object b,
            final Object c) {
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
    public static Field apply(final Function<Object[], Object> fun, final int[] fieldIndexes, final Object... args) {
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
    public static Field apply(final Function<Object[], Object> fun, final boolean fieldTarget, final int[] fieldIndexes,
            final Object target, final Object... args) {
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
    public static Field apply(final BiFunction<Object, Object[], Object> fun, final boolean fieldTarget,
            final int[] fieldIndexes, final Object target, final Object... args) {
        if (!fieldTarget && fieldIndexes.length == 0) {
            throw new IllegalArgumentException(
                    "To use this field application at least one of the parameters must be a field.");
        }
        /*
         * A consistency check may make sense here.
         */
        final Field refField = (Field) (fieldTarget ? target : args[fieldIndexes[0]]);
        final Field result = DatatypeFactory.createField(refField.size());
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
            final Object actualTarget = fieldTarget ? ((Field) target).getSample(node) : target;
            result.addSample(node, fun.apply(actualTarget, actualArgs));
        }
        return result;
    }

}
