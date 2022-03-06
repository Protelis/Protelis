/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.lang.datatype;

import java.util.function.BinaryOperator;

import org.protelis.lang.datatype.impl.ArrayTupleImpl;

/**
 * Utility class for {@link Tuple}.
 */
public final class Tuples {

    private Tuples() {
    }

    /**
     * Set arithmetic: compute union of set of tuple elements in t1 and t2.
     * 
     * @param t1
     *            First tuple to union
     * @param t2
     *            Second tuple to union
     * @return a new tuple containing elements appearing in either tuple, in
     *         arbitrary order
     */
    public static Tuple union(final Tuple t1, final Tuple t2) {
        return t1.union(t2);
    }

    /**
     * Set arithmetic: compute intersection of set of tuple elements in t1 and
     * t2.
     * 
     * @param t1
     *            First tuple to intersect
     * @param t2
     *            Second tuple to intersect
     * @return a new tuple containing elements appearing in both tuples, in
     *         arbitrary order
     */
    public static Tuple intersection(final Tuple t1, final Tuple t2) {
        return t1.intersection(t2);
    }

    /**
     * Set arithmetic: compute subtraction of set of tuple elements in t2 from
     * elements in t1.
     * 
     * @param t1
     *            Base tuple
     * @param t2
     *            Tuple to subtract
     * @return a new tuple containing elements appearing in t1 but not t2, in
     *         arbitrary order
     */
    public static Tuple subtract(final Tuple t1, final Tuple t2) {
        return t1.subtract(t2);
    }

    /**
     * Apply fun to pairs of elements from t1 and t2. If one tuple is longer,
     * then its unmatched elements will be added to the output e.g.,
     * pairOperation([1,2,3],[4,5],max) -&gt; [4,5,3]
     * 
     * @param t1
     *            Tuple of first arguments
     * @param t2
     *            Tuple of second arguments
     * @param fun
     *            The function to apply to pairs of arguments
     * @return a new tuple, such that the ith element is
     *         fun(t1.get(i),t2.get(i))
     */
    public static Tuple pairOperation(final Tuple t1, final Tuple t2, final BinaryOperator<Object> fun) {
        final boolean t1Bigger = t1.size() > t2.size();
        final Tuple big = t1Bigger ? t1 : t2;
        final Tuple small = t1Bigger ? t2 : t1;
        final int max = (int) big.size();
        final int min = (int) small.size();
        final Object[] res = new Object[max];
        for (int i = 0; i < max; i++) {
            res[i] = i < min ? fun.apply(t1.get(i), t2.get(i)) : big.get(i);
        }
        return DatatypeFactory.createTuple(res);
    }

    /**
     * Create a Tuple with all elements initialized to the same value.
     * 
     * @param value
     *            Value to which all elements will be initialized
     * @param length
     *            Size of the tuple
     * @return a new tuple
     */
    public static Tuple fill(final Object value, final int length) {
        return new ArrayTupleImpl(value, length);
    }


}
