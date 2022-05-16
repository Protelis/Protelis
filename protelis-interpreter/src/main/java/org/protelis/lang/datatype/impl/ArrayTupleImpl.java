/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.lang.datatype.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.ArrayUtils;
import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.datatype.Tuple;
import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.impl.Constant;
import org.protelis.lang.interpreter.impl.FunctionCall;
import org.protelis.lang.interpreter.util.JavaInteroperabilityUtils;
import org.protelis.vm.ExecutionContext;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Implementation of a Tuple using an array data structure.
 */
public final class ArrayTupleImpl implements Tuple {

    @SuppressWarnings("unchecked")
    private static final Comparator<Object> COMPARE_TO = (a, b) -> {
        if (a instanceof Comparable && b instanceof Comparable) {
            try {
                return ((Comparable<Object>) a).compareTo(b);
            } catch (RuntimeException e) { // NOPMD: this is done by purpose
                return compareLexicographically(a, b);
            }
        }
        return compareLexicographically(a, b);
    };
    private static final long serialVersionUID = 5453783531251313649L;
    private final Object[] arrayContents;
    private int hash;
    private String string;

    /**
     * @param base
     *            the elements
     */
    public ArrayTupleImpl(final Object... base) {
        this(base, true);
    }

    /**
     * Create an ArrayTupleImpl with all elements initialized to a given value.
     *
     * @param value
     *            The value to initialize to
     * @param length
     *            The length of the tuple
     */
    public ArrayTupleImpl(final Object value, final int length) {
        arrayContents = new Object[length];
        for (int i = 0; i < length; i++) {
            arrayContents[i] = value;
        }
    }

    private ArrayTupleImpl(final Object[] base, final boolean copy) {
        arrayContents = copy ? Arrays.copyOf(base, base.length) : base;
    }

    @Override
    public Tuple append(final Object element) {
        final Object[] copy = Arrays.copyOf(arrayContents, arrayContents.length + 1);
        copy[arrayContents.length] = element;
        return new ArrayTupleImpl(copy, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(final Tuple o) {
        int res = 0;
        final int otherSize = o.size();
        for (int i = 0; res == 0 && i < arrayContents.length && i < otherSize; i++) {
            final Object o1 = arrayContents[i];
            final Object o2 = o.get(i);
            if (o1 instanceof Comparable && o2 instanceof Comparable) {
                try {
                    res = ((Comparable<Object>) o1).compareTo(o2);
                } catch (ClassCastException ex) {
                    /*
                     * Incomparable, go lexicographically
                     */
                    res = o1.toString().compareTo(o2.toString());
                }
            } else {
                /*
                 * Fall back to lexicographic comparison
                 */
                return o1.toString().compareTo(o2.toString());
            }
        }
        if (res == 0 && arrayContents.length != otherSize) {
            /*
             * Same content but different size: shortest is smaller
             */
            if (arrayContents.length > otherSize) {
                return 1;
            }
            return -1;
        }
        return res;
    }

    @Override
    public boolean contains(final Object element) {
        return indexof(element) >= 0;
    }

    @Override
    public boolean containsAll(final Iterable<?> element) {
        for (final Object obj: element) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof ArrayTupleImpl) {
            return Arrays.equals(arrayContents, ((ArrayTupleImpl) o).arrayContents);
        }
        if (o instanceof Tuple) {
            final Tuple t = (Tuple) o;
            if (t.size() == arrayContents.length) {
                for (int i = 0; i < arrayContents.length; i++) {
                    if (!arrayContents[i].equals(t.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public Tuple filter(final ExecutionContext ctx, final FunctionDefinition fun) {
        Objects.requireNonNull(fun);
        if (fun.getParameterCount() == 1 || fun.invokerShouldInitializeIt()) {
            final AtomicInteger counter = new AtomicInteger();
            return new ArrayTupleImpl(
                Arrays.stream(arrayContents).filter(elem -> {
                    final List<ProtelisAST<?>> arguments = elementAsArguments(elem);
                    final FunctionCall fc = new FunctionCall(JavaInteroperabilityUtils.METADATA, fun, arguments);
                    final Object outcome = ctx.runInNewStackFrame(counter.getAndIncrement(), fc::eval);
                    if (outcome instanceof Boolean) {
                        return (Boolean) outcome;
                    } else {
                        throw new IllegalArgumentException("Filtering functions must return boolean.");
                    }
                }).toArray(),
                false
            );
        }
        throw new IllegalArgumentException("Filtering function must take one parameter.");
    }

    @Override
    public Tuple filter(final Predicate<Object> fun) {
        Objects.requireNonNull(fun);
        return new ArrayTupleImpl(Arrays.stream(arrayContents).filter(fun).toArray(), false);
    }

    @Override
    public Tuple flatMap(final Function<Object, Tuple> fun) {
        final Stream<Object> flatMapped = Arrays.stream(arrayContents).flatMap(e ->
            Arrays.stream(fun.apply(e).toArray())
        );
        final Object[] mappedArray = flatMapped.toArray();
        return new ArrayTupleImpl(mappedArray, false);
    }

    @Override
    public Object fold(final Object initial, final BinaryOperator<Object> fun) {
        return Arrays.stream(arrayContents).reduce(Objects.requireNonNull(initial), Objects.requireNonNull(fun));
    }

    /**
     * Compatibility method to speed up calls made using doubles.
     *
     * @param i
     *            the element position (will be floored to int)
     * @return the i-th element
     */
    public Object get(final double i) {
        return get((int) i);
    }

    /**
     * Compatibility method to speed up calls made using doubles.
     *
     * @param i
     *            the element position (will be floored to int)
     * @return the i-th element
     */
    public Object get(final Double i) {
        return get(i.intValue());
    }

    @Override
    public Object get(final int i) {
        return arrayContents[i];
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public int hashCode() {
        if (hash == 0) {
            hash = Hashing.murmur3_32_fixed().newHasher()
                .putObject(arrayContents, (array, dest) -> {
                    for (final Object it: array) {
                        dest.putInt(it.hashCode());
                    }
                }).hash().asInt();
        }
        return hash;
    }

    @Override
    public Object head() {
        return get(0);
    }

    @Override
    public int indexof(final Object element) {
        for (int i = 0; i < arrayContents.length; i++) {
            if (arrayContents[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Tuple insert(final int i, final Object element) {
        return new ArrayTupleImpl(ArrayUtils.insert(i, arrayContents, element), false);
    }

    @Override
    public Tuple intersection(final Tuple t) {
        final Set<Object> l1 = Sets.newLinkedHashSet(this);
        final Set<Object> l2 = Sets.newLinkedHashSet(t);
        return new ArrayTupleImpl(Sets.intersection(l1, l2).toArray(), false);
    }

    @Override
    public boolean isEmpty() {
        return arrayContents.length == 0;
    }

    @Override
    public Iterator<Object> iterator() {
        return Iterators.forArray(arrayContents);
    }

    @Override
    public Tuple map(final ExecutionContext ctx, final FunctionDefinition fun) {
        if (fun.getParameterCount() == 1 || fun.invokerShouldInitializeIt()) {
            final AtomicInteger counter = new AtomicInteger();
            return new ArrayTupleImpl(
                Arrays.stream(arrayContents).map(elem -> {
                    final FunctionCall fc = new FunctionCall(JavaInteroperabilityUtils.METADATA, fun, elementAsArguments(elem));
                    return ctx.runInNewStackFrame(counter.getAndIncrement(), fc::eval);
                }).toArray(),
                false
            );
        }
        throw new IllegalArgumentException("Mapping function must take one parameter.");
    }

    @Override
    public Tuple map(final Function<Object, Object> fun) {
        Objects.requireNonNull(fun);
        return DatatypeFactory.createTuple(Arrays.stream(arrayContents).map(fun).toArray());
    }

    @Override
    public Object max(final Object def) {
        return Arrays.stream(arrayContents).max(COMPARE_TO).orElse(def);
    }

    @Override
    public Tuple mergeAfter(final Tuple tuple) {
        if (tuple instanceof ArrayTupleImpl) {
            return new ArrayTupleImpl(ArrayUtils.addAll(arrayContents, ((ArrayTupleImpl) tuple).arrayContents), false);
        }
        final Object[] copy = new Object[arrayContents.length + tuple.size()];
        System.arraycopy(arrayContents, 0, copy, 0, arrayContents.length);
        for (int i = 0; i < copy.length; i++) {
            copy[i] = tuple.get(i - arrayContents.length);
        }
        return new ArrayTupleImpl(copy, false);
    }

    @Override
    public Object min(final Object def) {
        return Arrays.stream(arrayContents).min(COMPARE_TO).orElse(def);
    }

    @Override
    public Tuple prepend(final Object element) {
        return insert(0, element);
    }

    @Override
    public Object reduce(final Object defVal, final BinaryOperator<Object> fun) {
        return Arrays.stream(arrayContents)
            .reduce(Objects.requireNonNull(fun))
            .orElse(Objects.requireNonNull(defVal));
    }

    @Override
    public Tuple set(final int i, final Object element) {
        final Object[] copy = Arrays.copyOf(arrayContents, arrayContents.length);
        copy[i] = element;
        return new ArrayTupleImpl(copy, false);
    }

    @Override
    public int size() {
        return arrayContents.length;
    }

    @Override
    public Tuple sort() {
        final Object[] newArray = Arrays.copyOf(arrayContents, arrayContents.length);
        Arrays.sort(newArray, COMPARE_TO);
        return DatatypeFactory.createTuple(newArray);
    }

    @Override
    public Tuple subtract(final Tuple t) {
        final Set<Object> l = Sets.newLinkedHashSet(this);
        for (final Object o : t) {
            l.remove(o);
        }
        return DatatypeFactory.createTuple(l.toArray());
    }

    @Override
    public ArrayTupleImpl subTuple(final int i, final int j) {
        return new ArrayTupleImpl(ArrayUtils.subarray(arrayContents, i, j), false);
    }

    @Override
    public ArrayTupleImpl subTupleEnd(final int i) {
        return subTuple(i, arrayContents.length);
    }

    @Override
    public ArrayTupleImpl subTupleStart(final int i) {
        return subTuple(0, i);
    }

    @Override
    public Tuple tail() {
        return subTupleEnd(1);
    }

    @Override
    public Object[] toArray() {
        return arrayContents.clone();
    }

    @Override
    public String toString() {
        if (string == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append('[');
            for (final Object o : arrayContents) {
                final boolean notNumber = !(o instanceof Number || o instanceof Tuple);
                final boolean isString = o instanceof String;
                if (isString) {
                    sb.append('"');
                } else if (notNumber) {
                    sb.append('\'');
                }
                sb.append(o.toString());
                if (isString) {
                    sb.append('"');
                } else if (notNumber) {
                    sb.append('\'');
                }
                sb.append(", ");
            }
            if (arrayContents.length > 0) {
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append(']');
            string = sb.toString();
        }
        return string;
    }

    @Override
    public ArrayTupleImpl union(final Tuple t) {
        return new ArrayTupleImpl(Sets.newLinkedHashSet(Iterables.concat(this, t)).toArray(), false);
    }

    @Override
    public Tuple unwrap(final int i) {
        return DatatypeFactory.createTuple(Arrays.stream(arrayContents).map((o) -> {
            if (o instanceof Tuple) {
                return ((Tuple) o).get(i);
            }
            return o;
        }).toArray());
    }

    @Override
    public Tuple zip(final Tuple other) {
        final Object[] result = new Object[Math.min(size(), other.size())];
        for (int i = 0; i < result.length; i++) {
            result[i] = new ArrayTupleImpl(new Object[]{ get(i), other.get(i) }, false);
        }
        return new ArrayTupleImpl(result, false);
    }

    private static int compareLexicographically(final Object a, final Object b) {
        return a.toString().compareTo(b.toString());
    }

    private static List<ProtelisAST<?>> elementAsArguments(final Object element) {
        return ImmutableList.of(new Constant<>(JavaInteroperabilityUtils.METADATA, element));
    }
}
