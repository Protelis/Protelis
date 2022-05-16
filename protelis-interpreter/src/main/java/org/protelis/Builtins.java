/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis;

import static org.protelis.lang.interpreter.util.JavaInteroperabilityUtils.runProtelisFunctionWithJavaArguments;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.datatype.Option;
import org.protelis.lang.datatype.Tuple;
import org.protelis.vm.ExecutionContext;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

/**
 * Collection of static methods automatically imported by Protelis.
 */
public final class Builtins {

    private static final String UNCHECKED = "unchecked";
    /**
     * This variable is used by the interpreter for providing compatibility hints in the Eclipse plugin.
     * See https://github.com/Protelis/Protelis/issues/245.
     */
    public static final ImmutableList<Integer> MINIMUM_PARSER_VERSION = ImmutableList.of(10, 0, 0);
    /**
     * This variable is used by the interpreter for providing compatibility hints in the Eclipse plugin.
     * See https://github.com/Protelis/Protelis/issues/245.
     */
    public static final ImmutableList<Integer> MAXIMUM_PARSER_VERSION = ImmutableList.of(10, 0, 1);

    private Builtins() { }

    /**
     * @param target the field
     * @return true if all the elements of the field are true
     */
    public static boolean all(final Field<Boolean> target) {
        return target.foldValuesIncludingLocal(Boolean::logicalAnd);
    }

    /**
     * @param target the field
     * @return true if all the elements of the field are true. The local element is
     *         not considered.
     */
    public static boolean allButSelf(final Field<Boolean> target) {
        return target.foldValuesExcludingLocal(true, Boolean::logicalAnd);
    }

    /**
     * @param target the field
     * @return true if any of the elements of the field is true
     */
    public static boolean any(final Field<Boolean> target) {
        return target.foldValuesIncludingLocal(Boolean::logicalOr);
    }

    /**
     * @param target the field
     * @return true if any of the elements of the field are true. The local element
     *         is not considered.
     */
    public static boolean anyButSelf(final Field<Boolean> target) {
        return target.foldValuesExcludingLocal(false, Boolean::logicalOr);
    }

    @SuppressWarnings(UNCHECKED)
    private static <X, Y, R> R byReflection(final String name, final X a, final Y b) {
        try {
            return (R) a.getClass().getMethod(name, b.getClass()).invoke(a, b);
        } catch (
            IllegalAccessException
            | IllegalArgumentException
            | InvocationTargetException
            | NoSuchMethodException
            | SecurityException e
        ) {
            throw new IllegalStateException("Unable to perform operation a." + name
                    + "(b) where a=" + a + " and b=" + b
                    + ", a of type " + a.getClass()
                    + ", b of type " + b.getClass(), e);
        }
    }

    private static <I, O> Function<I, O> conversionFunction(final Class<I> in, final Class<O> out) {
        if (in.equals(out) || out.isAssignableFrom(in)) {
            return out::cast;
        }
        throw new IllegalStateException("Impossible conversion between " + in + " and " + out);
    }

    @SuppressWarnings(UNCHECKED)
    private static <I, O> O convert(final Class<O> out, final I in) {
        return conversionFunction((Class<I>) in.getClass(), out).apply(in);
    }

    /**
     * Folds a field, <b>excluding the local value</b>. This method requires a base
     * value to begin the computation with. The base value is used as initial
     * element. If the field only contains the local value, then the base value is
     * returned.
     * 
     * @param <T>               field and result type
     * @param context           {@link ExecutionContext}
     * @param target            the field to be reduced
     * @param reductionFunction the reduction function
     * @return an {@link Option} with the result of the field reduction, or an empty
     *         option.
     * @param base the base value
     */
    public static <T> T foldHood(
            @Nonnull final ExecutionContext context,
            @Nonnull final T base,
            @Nonnull final Field<T> target,
            @Nonnull final FunctionDefinition reductionFunction) {
        @SuppressWarnings(UNCHECKED)
        final Class<? extends T> defaultType = (Class<? extends T>) Objects.requireNonNull(base).getClass();
        final Class<? extends T> fieldType = Objects.requireNonNull(target).getExpectedType();
        final Class<? extends T> expectedType;
        if (defaultType.isAssignableFrom(fieldType)) {
            expectedType = defaultType;
        } else if (fieldType.isAssignableFrom(defaultType)) {
            expectedType = fieldType;
        } else {
            throw new IllegalArgumentException("Default type " + defaultType.getName()
                + " and field expected type " + fieldType.getName()
                + " do not seem to be compatible");
        }
        return target.foldValuesExcludingLocal(base, reductionFunction(context, expectedType, target, reductionFunction));
    }

    /**
     * Folds the field, including the local value.
     * 
     * @param <T> field and result type
     * @param context {@link ExecutionContext}
     * @param target target field
     * @param reductionFunction a Protelis function (T, T) =&gt; T
     * @return the folded value
     */
    public static <T> T foldHoodPlusSelf(
            @Nonnull final ExecutionContext context,
            @Nonnull final Field<T> target,
            @Nonnull final FunctionDefinition reductionFunction) {
        return target.foldValuesIncludingLocal(reductionFunction(context, target.getExpectedType(), target, reductionFunction));
    }

    /**
     * Folds the field, including the local value, by picking the maximum value.
     * This method requires the field elements to implement {@link Comparable}.
     * 
     * @param <T>               field and result type
     * @param target            target field
     * @return the maximum value among the field values
     */
    public static <T extends Comparable<T>> T foldMax(final Field<T> target) {
        return target.foldValuesIncludingLocal(Builtins::max);
    }

    /**
     * Folds a field by picking the maximum of its values, <b>excluding the local
     * value</b>. This method requires a base value to begin the computation with.
     * The base value is used as initial element. If the field only contains the
     * local value, then the base value is returned. This method requires the field
     * elements to implement {@link Comparable}.
     * 
     * @param <T>    field and result type
     * @param target the field to be reduced
     * @return the maximum value among the field values and base.
     * @param base the base value
     */
    public static <T extends Comparable<T>> T foldMax(final T base, final Field<T> target) {
        return target.foldValuesExcludingLocal(base, Builtins::max);
    }

    /**
     * Folds a field of numbers by computing the mathematical mean. Includes the
     * local value.
     * 
     * @param target the field to be reduced
     * @return a {@link Double} with the arithmetic mean of the values
     */
    public static double foldMean(final Field<? extends Number> target) {
        return foldSum(target).doubleValue() / (target.size() + 1);
    }

    /**
     * Folds the field, including the local value, by picking the minimum value.
     * This method requires the field elements to implement {@link Comparable}.
     * 
     * @param <T>               field and result type
     * @param target            target field
     * @return the minimum value among the field values
     */
    public static <T extends Comparable<T>> T foldMin(final Field<T> target) {
        return target.foldValuesIncludingLocal(Builtins::min);
    }

    /**
     * Folds a field by picking the minimum of its values, <b>excluding the local
     * value</b>. This method requires a base value to begin the computation with.
     * The base value is used as initial element. If the field only contains the
     * local value, then the base value is returned. This method requires the field
     * elements to implement {@link Comparable}.
     * 
     * @param <T>               field and result type
     * @param target            the field to be reduced
     * @return the minimum value among the field values and base.
     * @param base the base value
     */
    public static <T extends Comparable<T>> T foldMin(final T base, final Field<T> target) {
        return target.foldValuesExcludingLocal(base, Builtins::min);
    }

    /**
     * Folds the field, including the local value, by computing the sum.
     * The sum operation must be well defined for the field type.
     * 
     * @param <T>               field and result type
     * @param target            target field
     * @return the sum of the field values
     */
    public static <T> T foldSum(final Field<T> target) {
        return target.foldValuesIncludingLocal(Builtins::sum);
    }

    /**
     * Folds a field by computing the sum of its values, <b>excluding the local
     * value</b>. This method requires a base value to begin the computation with.
     * The base value is used as initial element. If the field only contains the
     * local value, then the base value is returned. The sum operation must be well
     * defined for the field type.
     * 
     * @param <T>    field and result type
     * @param target the field to be reduced
     * @return the sum of the field values and base
     * @param base the base value
     */
    public static <T> T foldSum(final T base, final Field<T> target) {
        return target.foldValuesExcludingLocal(base, Builtins::sum);
    }

    /**
     * Folds the field, including the local value, by computing the union. The union
     * operation must be well defined for the field type, e.g., this operation can
     * work on a field of tuples.
     * 
     * @param <T>    field and result type
     * @param target target field
     * @return the union of the field values
     */
    public static <T> T foldUnion(final Field<T> target) {
        return target.foldValuesIncludingLocal(Builtins::union);
    }

    /**
     * Folds a field by computing the union, <b>excluding the local value</b>. This
     * method requires a base value to begin the computation with. The base value is
     * used as initial element. If the field only contains the local value, then the
     * base value is returned. The union operation must be well defined for the
     * field type, e.g., this operation can work on a field of tuples.
     * 
     * @param <T>    field and result type
     * @param target the field to be reduced
     * @return the sum of the field values and base
     * @param base the base value
     */
    public static <T> T foldUnion(final T base, final Field<T> target) {
        return target.foldValuesExcludingLocal(base, Builtins::union);
    }

    /**
     * Picks the local field value (same operation of the previously available
     * "localHood" and "pickHood").
     * 
     * @param <T>    field and result type
     * @param target the field to be reduced
     * @return the local field value
     */
    public static <T> T local(final Field<T> target) {
        return target.getLocalValue();
    }

    private static <T extends Comparable<T>> T max(final T a, final T b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    /**
     * Given a nullable reference, builds an {@link Option}.
     * 
     * @param <T>    the input and {@link Option} type
     * @param object the input object
     * @return an Option containing the provided object, or an empty {@link Option}
     *         if null is passed
     */
    public static <T> Option<T> maybe(@Nullable final T object) {
        return Option.of(object);
    }

    private static <T extends Comparable<T>> T min(final T a, final T b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    /**
     * @param target the field
     * @return true if all the elements of the field are false
     */
    public static boolean none(final Field<Boolean> target) {
        return !any(target);
    }

    /**
     * @param target the field
     * @return true if any of the elements of the field are false. The local value
     *         is not considered.
     */
    public static boolean noneButSelf(final Field<Boolean> target) {
        return !anyButSelf(target);
    }

    /**
     * Reduces a field, <b>excluding the local value</b>. This method wraps the
     * result in an {@link Option}. If the field only contains the local value, then
     * an empty {@link Option} is returned.
     * 
     * @param <T>               field and result type
     * @param context           {@link ExecutionContext}
     * @param target            the field to be reduced
     * @param reductionFunction the reduction function
     * @return an {@link Option} with the result of the field reduction, or an empty
     *         option.
     */
    public static <T> Option<T> reduceHood(
            @Nonnull final ExecutionContext context,
            @Nonnull final Field<T> target,
            @Nonnull final FunctionDefinition reductionFunction) {
        return target.reduceValues(reductionFunction(context, target.getExpectedType(), target, reductionFunction));
    }

    /**
     * Produces an Option value. If the passed object is null, then an empty option
     * is returned. Otherwise, an Option enclosing the value is returned.
     * Recommended way to interact with Java method that may return null.
     * 
     * @param <T>    Object type
     * @param object the nullable object
     * @return If the passed object is null, then an empty option is returned.
     *         Otherwise, an Option enclosing the value is returned.
     */
    public static <T> Option<T> optionally(@Nullable final T object) {
        return Option.fromNullable(object);
    }

    /**
     * Reduces a field, <b>excluding the local value</b>, by picking the maximum
     * value. This method wraps the result in an {@link Option}. If the field only
     * contains the local value, then an empty {@link Option} is returned.
     * 
     * @param <T>               field and result type
     * @param target            the field to be reduced
     * @return an {@link Option} with the result of the field reduction, or an empty
     *         option.
     */
    public static <T extends Comparable<T>> Option<T> reduceMax(final Field<T> target) {
        return target.reduceValues(Builtins::max);
    }

    /**
     * Reduces a field, <b>excluding the local value</b>, by computing the
     * arithmetic mean of the values. This method wraps the result in an
     * {@link Option}. If the field only contains the local value, then an empty
     * {@link Option} is returned.
     * 
     * @param target the field to be reduced
     * @return an {@link Option} with the result of the field reduction, or an empty
     *         option.
     */
    public static Option<Double> reduceMean(final Field<? extends Number> target) {
        return reduceSum(target).map(it -> it.doubleValue() / target.size());
    }

    /**
     * Reduces a field by picking the minimum of its values, <b>excluding the local
     * value</b>. This method wraps the result in an {@link Option}. If the field
     * only contains the local value, then an empty {@link Option} is returned.
     * 
     * @param <T>               field and result type
     * @param target            the field to be reduced
     * @return an {@link Option} with the result of the field reduction, or an empty
     *         option.
     */
    public static <T extends Comparable<T>> Option<T> reduceMin(final Field<T> target) {
        return target.reduceValues(Builtins::min);
    }

    /**
     * Reduces a field, <b>excluding the local value</b>, by computing the sum of
     * the values. This method wraps the result in an {@link Option}. If the field
     * only contains the local value, then an empty {@link Option} is returned.
     * 
     * @param <T>    field and result type
     * @param target the field to be reduced
     * @return an {@link Option} with the result of the field reduction, or an empty
     *         option.
     */
    public static <T> Option<T> reduceSum(final Field<T> target) {
        return target.reduceValues(Builtins::sum);
    }

    /**
     * Reduces a field, <b>excluding the local value</b>, by computing the union of
     * the values. This method wraps the result in an {@link Option}. If the field
     * only contains the local value, then an empty {@link Option} is returned.
     * 
     * @param <T>    field and result type
     * @param target the field to be reduced
     * @return an {@link Option} with the result of the field reduction, or an empty
     *         option.
     */
    public static <T> Option<T> reduceUnion(final Field<T> target) {
        return target.reduceValues(Builtins::union);
    }

    @Nonnull
    private static <T> BinaryOperator<T> reductionFunction(
        @Nonnull final ExecutionContext context,
        @Nonnull final Class<? extends T> expectedType,
        @Nonnull final Field<T> target,
        @Nonnull final FunctionDefinition reductionFunction
    ) {
        return (a, b) -> {
            final Object reductionResult = runProtelisFunctionWithJavaArguments(
                context,
                reductionFunction,
                ImmutableList.of(a, b)
            );
            if (expectedType.isAssignableFrom(reductionResult.getClass())) {
                return expectedType.cast(reductionResult);
            }
            throw new IllegalStateException(
                "Reduction operation over target field " + target
                    + " with type " + target.getExpectedType()
                    + " failed because the provided reduction function reduced "
                    + a + " of type " + a.getClass().getName()
                    + " and " + b + " of type " + b.getClass().getName()
                    + " into " + reductionResult
                    + " of type " + reductionResult.getClass().getName()
                    + " which does not conform to expected type "
                    + expectedType.getName()
            );
        };
    }

    private static <X, Y, R> R run(
        final Class<X> xClass,
        final Object x,
        final Class<Y> yClass,
        final Object y,
        final BiFunction<X, Y, R> fun
    ) {
        return fun.apply(convert(xClass, x), convert(yClass, y));
    }

    private static <X, R> R run(final Class<X> inClass, final Object x, final Object y, final BiFunction<X, X, R> fun) {
        return run(inClass, x, inClass, y, fun);
    }

    private static <X> X runBi(final Class<X> inClass, final Object x, final Object y, final BinaryOperator<X> fun) {
        return run(inClass, x, y, fun);
    }

    @SuppressWarnings(UNCHECKED)
    private static <T> T sum(final T a, final T b) {
        if (a instanceof CharSequence || b instanceof CharSequence) {
            return (T) (a.toString() + b.toString());
        }
        if (a instanceof BigDecimal || b instanceof BigDecimal) {
            return (T) runBi(BigDecimal.class, a, b, BigDecimal::add);
        }
        if (a instanceof BigInteger || b instanceof BigInteger) {
            return (T) runBi(BigInteger.class, a, b, BigInteger::add);
        }
        if (a instanceof Double || b instanceof Double) {
            return (T) runBi(Double.class, a, b, Double::sum);
        }
        if (a instanceof Float || b instanceof Float) {
            return (T) runBi(Float.class, a, b, Float::sum);
        }
        if (a instanceof Long || b instanceof Long) {
            return (T) runBi(Long.class, a, b, Long::sum);
        }
        if (a instanceof Integer || b instanceof Integer
            || a instanceof Byte || b instanceof Byte
            || a instanceof Short || b instanceof Short
        ) {
            return (T) runBi(Integer.class, a, b, Integer::sum);
        }
        if (a instanceof Boolean && b instanceof Boolean) {
            return (T) runBi(Boolean.class, a, b, Boolean::logicalAnd);
        }
        if (a instanceof Tuple && b instanceof Tuple) {
            return (T) runBi(Tuple.class, a, b, Tuple::mergeAfter);
        }
        return byReflection("plus", a, b);
    }

    @SuppressWarnings(UNCHECKED)
    private static <T> T union(final T a, final T b) {
        if (a instanceof Tuple && b instanceof Tuple) {
            return (T) runBi(Tuple.class, a, b, Tuple::union);
        }
        if (a instanceof Set && b instanceof Set) {
            return (T) runBi(Set.class, a, b, Sets::union);
        }
        if (b instanceof Tuple) {
            return (T) runBi(Tuple.class, DatatypeFactory.createTuple(a), b, Tuple::union);
        }
        if (a instanceof Tuple) {
            return (T) runBi(Tuple.class, a, DatatypeFactory.createTuple(b), Tuple::union);
        }
        return byReflection("union", a, b);
    }

}
