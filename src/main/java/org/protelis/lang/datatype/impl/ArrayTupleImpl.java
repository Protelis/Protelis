/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.datatype.impl;

import java.util.Arrays;

import java8.util.J8Arrays;
import java8.util.function.BinaryOperator;
import java8.util.function.Function;
import java8.util.function.Predicate;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.danilopianini.lang.HashUtils;
import org.danilopianini.lang.LangUtils;
import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.datatype.Tuple;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.impl.Constant;
import org.protelis.lang.interpreter.impl.FunctionCall;
import org.protelis.vm.ExecutionContext;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Implementation of a Tuple using an array data structure.
 */
public class ArrayTupleImpl implements Tuple {

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

    private ArrayTupleImpl(final Object[] base, final boolean copy) {
        arrayContents = copy ? Arrays.copyOf(base, base.length) : base;
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

    @Override
    public Iterator<Object> iterator() {
        return Iterators.forArray(arrayContents);
    }

    @Override
    public Object get(final int i) {
        return arrayContents[i];
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
    public int size() {
        return arrayContents.length;
    }

    @Override
    public ArrayTupleImpl subTupleEnd(final int i) {
        return subTuple(i, arrayContents.length);
    }

    @Override
    public ArrayTupleImpl subTupleStart(final int i) {
        return subTuple(0, i);
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
                    res = ((Comparable<Object>) o1).compareTo(((Comparable<?>) o2));
                } catch (ClassCastException ex) {
                    /*
                     * Uncomparable, go lexicographically
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
    public Tuple append(final Object element) {
        final Object[] copy = Arrays.copyOf(arrayContents, arrayContents.length + 1);
        copy[arrayContents.length] = element;
        return new ArrayTupleImpl(copy, false);
    }

    @Override
    public Tuple insert(final int i, final Object element) {
        return new ArrayTupleImpl(ArrayUtils.add(arrayContents, (int) i, element), false);
    }

    @Override
    public Tuple set(final int i, final Object element) {
        final Object[] copy = Arrays.copyOf(arrayContents, arrayContents.length);
        copy[(int) i] = element;
        return new ArrayTupleImpl(copy, false);
    }

    @Override
    public ArrayTupleImpl subTuple(final int i, final int j) {
        return new ArrayTupleImpl(ArrayUtils.subarray(arrayContents, (int) i, (int) j), false);
    }

    @Override
    public Tuple mergeAfter(final Tuple tuple) {
        if (tuple instanceof ArrayTupleImpl) {
            return new ArrayTupleImpl(ArrayUtils.addAll(arrayContents, ((ArrayTupleImpl) tuple).arrayContents), false);
        }
        final Object[] copy = new Object[arrayContents.length + (int) tuple.size()];
        System.arraycopy(arrayContents, 0, copy, 0, arrayContents.length);
        for (int i = 0; i < copy.length; i++) {
            copy[i] = tuple.get(i - arrayContents.length);
        }
        return new ArrayTupleImpl(copy, false);
    }

    @Override
    public boolean isEmpty() {
        return arrayContents.length == 0;
    }

    @Override
    public boolean contains(final Object element) {
        return indexof(element) >= 0;
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
    public boolean equals(final Object o) {
        if (o instanceof ArrayTupleImpl) {
            return Arrays.equals(arrayContents, ((ArrayTupleImpl) o).arrayContents);
        }
        if (o instanceof Tuple) {
            final Tuple t = (Tuple) o;
            if ((int) t.size() == arrayContents.length) {
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
    public int hashCode() {
        if (hash == 0) {
            hash = HashUtils.hash32(arrayContents);
        }
        return hash;
    }

    @Override
    public Tuple unwrap(final int i) {
        return DatatypeFactory.createTuple(J8Arrays.stream(arrayContents).map((o) -> {
            if (o instanceof Tuple) {
                return ((Tuple) o).get(i);
            }
            return o;
        }).toArray());
    }

    @Override
    public ArrayTupleImpl union(final Tuple t) {
        return new ArrayTupleImpl(Sets.newLinkedHashSet(Iterables.concat(this, t)).toArray(), false);
    }

    @Override
    public Tuple intersection(final Tuple t) {
        final Set<Object> l1 = Sets.newLinkedHashSet(this);
        final Set<Object> l2 = Sets.newLinkedHashSet(t);
        return new ArrayTupleImpl(Sets.intersection(l1, l2).toArray(), false);
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
    public Object reduce(final ExecutionContext ctx, final Object defVal, final FunctionDefinition fun) {
        Objects.requireNonNull(fun);
        if (fun.getArgNumber() == 2) {
            return J8Arrays.stream(arrayContents).reduce((first, second) -> {
                final FunctionCall fc = new FunctionCall(fun,
                        Lists.newArrayList(new Constant<>(first), new Constant<>(second)));
                fc.eval(ctx);
                return fc.getAnnotation();
            }).orElse(defVal);
        }
        throw new IllegalArgumentException("Reducing function must take two parameters.");
    }

    @Override
    public Object reduce(final Object defVal, final BinaryOperator<Object> fun) {
        LangUtils.requireNonNull(defVal, fun);
        return J8Arrays.stream(arrayContents).reduce(fun).orElse(defVal);
    }

    @Override
    public Tuple map(final ExecutionContext ctx, final FunctionDefinition fun) {
        if (fun.getArgNumber() == 1) {
            return DatatypeFactory.createTuple(J8Arrays.stream(arrayContents)
                .map(Constant<Object>::new)
                .map(elem -> {
                    final FunctionCall fc = new FunctionCall(fun, Lists.newArrayList(elem));
                    fc.eval(ctx);
                    return fc.getAnnotation();
                })
                .toArray());
        }
        throw new IllegalArgumentException("Mapping function must take one parameter.");
    }

    @Override
    public Tuple map(final Function<Object, Object> fun) {
        Objects.requireNonNull(fun);
        return DatatypeFactory.createTuple(J8Arrays.stream(arrayContents).map(fun).toArray());
    }

    @Override
    public Tuple filter(final ExecutionContext ctx, final FunctionDefinition fun) {
        Objects.requireNonNull(fun);
        if (fun.getArgNumber() == 1) {
            return DatatypeFactory
                    .createTuple(J8Arrays.stream(arrayContents)
                        .map(Constant<Object>::new)
                        .filter(elem -> {
                            final FunctionCall fc = new FunctionCall(fun, Lists.newArrayList(elem));
                            fc.eval(ctx);
                            final Object outcome = fc.getAnnotation();
                            if (outcome instanceof Boolean) {
                                return (Boolean) outcome;
                            } else {
                                throw new IllegalArgumentException("Filtering function must return a boolean.");
                            }
                        })
                        .map(AnnotatedTree::getAnnotation).toArray());
        }
        throw new IllegalArgumentException("Filtering function must take one parameter.");
    }

    @Override
    public Tuple filter(final Predicate<Object> fun) {
        Objects.requireNonNull(fun);
        return DatatypeFactory.createTuple(J8Arrays.stream(arrayContents).filter(fun).toArray());
    }

    @Override
    public Tuple prepend(final Object element) {
        return insert(0, element);
    }

}
