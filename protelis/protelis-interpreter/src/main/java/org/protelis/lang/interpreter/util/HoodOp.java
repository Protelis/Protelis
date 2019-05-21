/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
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
import java.util.List;

import org.apache.commons.math3.util.Pair;
import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.Tuple;
import org.protelis.lang.datatype.Tuples;

import java8.util.J8Arrays;
import java8.util.function.BiFunction;
import java8.util.function.Function;
import java8.util.function.Supplier;

/**
 * Collection of functions and helper methods for reducing fields into local
 * values.
 */
public enum HoodOp implements WithBytecode {

    /**
     * Logical product.
     */
    ALL(HOOD_ALL, HoodOp::all,
        HoodOp::no,
        of(create(Boolean.class,
        () -> true)), of()),
    /**
     * Logical sum.
     */
    ANY(HOOD_ANY, HoodOp::any,
        HoodOp::no,
        of(create(Boolean.class,
        () -> false)), of()),
    /**
     * Pick local value.
     */
    LOCAL(HOOD_LOCAL,
        (field, id) -> field.getSample(id),
        () -> {
            throw new IllegalStateException("Local field pick operation must always work");
        },
         emptyList(),
         emptyList()),
    /**
     * Maximum.
     */
    MAX(HOOD_MAX, HoodOp::max,
        () -> NEGATIVE_INFINITY,
        of(create(Number.class, () -> NEGATIVE_INFINITY)),
        of(create(Tuple.class, t -> fillTuple(NEGATIVE_INFINITY, (Tuple) t)))),
    /**
     * Mean of values.
     */
    MEAN(HOOD_MEAN, HoodOp::mean,
         () -> NaN,
         of(create(Number.class, () -> NaN)),
         of(create(Tuple.class, t -> fillTuple(NaN, (Tuple) t)))),
    /**
     * Minimum.
     */
    MIN(HOOD_MIN, HoodOp::min,
        () -> POSITIVE_INFINITY,
        of(create(Number.class, () -> POSITIVE_INFINITY)),
        of(create(Tuple.class, t -> fillTuple(POSITIVE_INFINITY, (Tuple) t)))),
    /**
     * Sum of values.
     */
    SUM(HOOD_SUM, HoodOp::sum,
        () -> 0d,
        of(create(Number.class, () -> 0d)),
        of(create(Tuple.class, t -> fillTuple(0d, (Tuple) t)))),
    /**
     * Union of values.
     */
    UNION(HOOD_UNION, HoodOp::union,
          DatatypeFactory::createTuple,
          of(create(Object.class, DatatypeFactory::createTuple)),
          of(create(Object.class, DatatypeFactory::createTuple)));

    private final Bytecode bytecode;
    private final SerializableFunction defs;
    private final SerializableBifunction function;

    /**
     * @param fun the reduction function
     * @param empty
     *            function that generates a default in case of empty field
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
            final Supplier<Object> empty,
            final List<Pair<Class<?>, Supplier<Object>>> suppliers,
            final List<Pair<Class<?>, Function<Object, Object>>> cloners) {
        function = fun;
        defs = (field) -> {
            /*
             * Field empty: generate a default.
             */
            if (field.isEmpty()) {
                return empty.get();
            }
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
                    return cloner.getSecond().apply(field.valIterator().iterator().next());
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
     * @param o
     *            the field
     * @param n
     *            the node on which the field is sampled
     * @return the Object resulting in the hood application
     */
    public Object run(final Field o, final DeviceUID n) {
        return function.apply(o, n);
    }

    private static Object all(final Field f, final DeviceUID n) {
        return f.reduceVals(Op2.AND.getFunction(), n, ALL.defs.apply(f));
    }

    private static Object any(final Field f, final DeviceUID n) {
        return f.reduceVals(Op2.OR.getFunction(), n, ANY.defs.apply(f));
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
        return J8Arrays.stream(values()).filter(ho -> ho.name().equalsIgnoreCase(reducer))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No built-in hood operation matches " + reducer));
    }

    private static Object max(final Field f, final DeviceUID n) {
        return f.reduceVals(Op2.MAX.getFunction(), n, MAX.defs.apply(f));
    }

    private static Object mean(final Field f, final DeviceUID n) {
        if (f.isEmpty()) {
            return NaN;
        }
        return Op2.DIVIDE.getFunction().apply(sum(f, n), f.size() - (n == null ? 0 : 1));
    }

    private static Object min(final Field f, final DeviceUID n) {
        return f.reduceVals(Op2.MIN.getFunction(), n, MIN.defs.apply(f));
    }

    private static Object no() {
        throw new UnsupportedOperationException("Unsupported operation on empty fields.");
    }

    private static Object sum(final Field f, final DeviceUID n) {
        return f.reduceVals(Op2.PLUS.getFunction(), n, SUM.defs.apply(f));
    }

    private static Tuple union(final Field f, final DeviceUID n) {
        final Object reduced = f.reduceVals((a, b) -> {
                final Tuple at = a instanceof Tuple ? (Tuple) a : DatatypeFactory.createTuple(a);
                final Tuple bt = b instanceof Tuple ? (Tuple) b : DatatypeFactory.createTuple(b);
                return Tuples.union(at, bt);
            }, n, UNION.defs.apply(f));
        return reduced instanceof Tuple ? (Tuple) reduced : DatatypeFactory.createTuple(reduced);
    }

    @FunctionalInterface
    private interface SerializableBifunction extends BiFunction<Field, DeviceUID, Object>, Serializable { }

    @FunctionalInterface
    private interface SerializableFunction extends Function<Field, Object>, Serializable { }

}
