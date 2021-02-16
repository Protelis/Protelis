/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.lang.interpreter.util;

import static com.google.common.collect.ImmutableList.of;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.util.Collections.emptyList;
import static org.apache.commons.math3.util.Pair.create;
import static org.protelis.lang.interpreter.util.Bytecode.HOOD_ALL;
import static org.protelis.lang.interpreter.util.Bytecode.HOOD_ANY;
import static org.protelis.lang.interpreter.util.Bytecode.HOOD_LOCAL;
import static org.protelis.lang.interpreter.util.Bytecode.HOOD_MAX;
import static org.protelis.lang.interpreter.util.Bytecode.HOOD_MEAN;
import static org.protelis.lang.interpreter.util.Bytecode.HOOD_MIN;
import static org.protelis.lang.interpreter.util.Bytecode.HOOD_SUM;
import static org.protelis.lang.interpreter.util.Bytecode.HOOD_UNION;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.math3.util.Pair;
import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.Tuple;
import org.protelis.lang.datatype.Tuples;

/**
 * Collection of functions and helper methods for reducing fields into local
 * values.
 */
@Deprecated
public enum HoodOp implements WithBytecode {

    /**
     * Logical product.
     */
    ALL(HOOD_ALL, HoodOp::all,
        of(create(Boolean.class, () -> true)),
        emptyList()),
    /**
     * Logical sum.
     */
    ANY(HOOD_ANY, HoodOp::any,
        of(create(Boolean.class, () -> false)),
        emptyList()),
    /**
     * Pick local value.
     */
    LOCAL(HOOD_LOCAL, HoodOp::local,
         emptyList(),
         emptyList()),
    /**
     * Maximum.
     */
    MAX(HOOD_MAX, HoodOp::max,
        of(create(Number.class, () -> NEGATIVE_INFINITY)),
        of(create(Tuple.class, t -> fillTuple(NEGATIVE_INFINITY, (Tuple) t)))),
    /**
     * Mean of values.
     */
    MEAN(HOOD_MEAN, HoodOp::mean,
         of(create(Number.class, () -> NaN)),
         of(create(Tuple.class, t -> fillTuple(NaN, (Tuple) t)))),
    /**
     * Minimum.
     */
    MIN(HOOD_MIN, HoodOp::min,
        of(create(Number.class, () -> POSITIVE_INFINITY)),
        of(create(Tuple.class, t -> fillTuple(POSITIVE_INFINITY, (Tuple) t)))),
    /**
     * Sum of values.
     */
    SUM(HOOD_SUM, HoodOp::sum,
        of(create(Number.class, () -> 0d)),
        of(create(Tuple.class, t -> fillTuple(0d, (Tuple) t)))),
    /**
     * Union of values.
     */
    UNION(HOOD_UNION, HoodOp::union,
          of(create(Object.class, DatatypeFactory::createTuple)),
          of(create(Object.class, DatatypeFactory::createTuple)));

    private final Bytecode bytecode;
    private final SerializableFunction defs; // NOPMD false positive, not a singular field
    private final SerializableBifunction function;

    /**
     * @param fun the reduction function
     * @param suppliers
     *            list of pairs mapping classes to 0-ary functions that provide
     *            a default
     * @param cloners
     *            list of pairs mapping classes to 1-ary functions that, given
     *            an element of the field as input, provide a comparison. Such
     *            functions are used in case there is no supplier that can
     *            provide a specific value-agnostic default
     */
    HoodOp(final Bytecode bytecode,
            final SerializableBifunction fun,
            final List<Pair<Class<?>, Supplier<Object>>> suppliers,
            final List<Pair<Class<?>, Function<Object, Object>>> cloners) {
        function = fun;
        defs = (field) -> {
            /*
             * Field empty: generate a default.
             */
            final Class<?> type = field.getExpectedType();
            for (final Pair<Class<?>, Supplier<Object>> sup : suppliers) {
                if (sup.getFirst().isAssignableFrom(type)) {
                    /*
                     * Field has compatible type
                     */
                    return sup.getSecond().get();
                }
            }
            for (final Pair<Class<?>, Function<Object, Object>> cloner : cloners) {
                if (cloner.getFirst().isAssignableFrom(type)) {
                    return cloner.getSecond().apply(field.values().iterator().next());
                }
            }
            return no(type);
        };
        this.bytecode = bytecode;
    }

    @Override
    public Bytecode getBytecode() {
        return bytecode;
    }

    private <T> T no(final Class<?> c) {
        throw new UnsupportedOperationException(this + " cannot compute on " + c);
    }

    /**
     * @param target
     *            the field
     * @param inclusive
     *            true if the local value should be considered
     * @return the Object resulting in the hood application
     */
    public Object run(final Field<Object> target, final boolean inclusive) {
        return function.apply(target, inclusive);
    }

    private static Object all(final Field<Object> f, final boolean inclusive) {
        return reduceFieldValues(f, inclusive, ALL.defs, Op2.AND);
    }

    private static Object any(final Field<Object> f, final boolean inclusive) {
        return reduceFieldValues(f, inclusive, ANY.defs, Op2.OR);
    }

    private static Tuple fillTuple(final Object defVal, final Tuple in) {
        final Object[] r = new Object[in.size()];
        for (int i = 0; i < r.length; i++) {
            final Object value = in.get(i);
            r[i] = value instanceof Tuple ? fillTuple(defVal, (Tuple) value) : defVal;
        }
        return DatatypeFactory.createTuple(r);
    }

    /**
     * @param reducer
     *            the desired operator
     * @return the corresponding {@link HoodOp}
     */
    public static HoodOp get(final String reducer) {
        return Arrays.stream(values()).filter(ho -> ho.name().equalsIgnoreCase(reducer))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No built-in hood operation matches " + reducer));
    }

    private static Object local(final Field<Object> f, final boolean inclusive) {
        return f.getLocalValue();
    }

    private static Object max(final Field<Object> f, final boolean inclusive) {
        return reduceFieldValues(f, inclusive, MAX.defs, Op2.MAX);
    }

    private static Object mean(final Field<Object> f, final boolean inclusive) {
        final int size = f.size() + (inclusive ? 1 : 0);
        if (size == 0) {
            return MEAN.defs.apply(f);
        }
        return Op2.DIVIDE.getFunction().apply(sum(f, inclusive), size);
    }

    private static Object min(final Field<Object> f, final boolean inclusive) {
        return reduceFieldValues(f, inclusive, MIN.defs, Op2.MIN.getFunction());
    }

    private static Object reduceFieldValues(final Field<Object> f, final boolean inclusive, final SerializableFunction defs, final Op2 reducer) {
        if (inclusive) {
            return f.foldValuesIncludingLocal(reducer.getFunction());
        } else {
            return f.reduceValues(reducer.getFunction())
                    .orElseGet(() -> defs.apply(f));
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T reduceFieldValues(final Field<T> f, final boolean inclusive, final SerializableFunction defs, final BinaryOperator<T> reducer) {
        if (inclusive) {
            return f.foldValuesIncludingLocal(reducer);
        } else {
            return f.reduceValues(reducer)
                .orElseGet(() -> (T) defs.apply(f));
        }
    }

    private static Object sum(final Field<Object> f, final boolean inclusive) {
        return reduceFieldValues(f, inclusive, SUM.defs, Op2.PLUS.getFunction());
    }

    private static Tuple union(final Field<Object> f, final boolean inclusive) {
        final Object reduced = reduceFieldValues(f, inclusive, UNION.defs, (a, b) -> {
                final Tuple at = a instanceof Tuple ? (Tuple) a : DatatypeFactory.createTuple(a);
                final Tuple bt = b instanceof Tuple ? (Tuple) b : DatatypeFactory.createTuple(b);
                return Tuples.union(at, bt);
            });
        return reduced instanceof Tuple ? (Tuple) reduced : DatatypeFactory.createTuple(reduced);
    }

    @FunctionalInterface
    private interface SerializableBifunction extends BiFunction<Field<Object>, Boolean, Object>, Serializable { }

    @FunctionalInterface
    private interface SerializableFunction extends Function<Field<? extends Object>, Object>, Serializable { }

}
