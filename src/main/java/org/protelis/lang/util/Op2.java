/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.util;

import static org.protelis.lang.util.OpUtils.unsupported;

import java8.util.J8Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java8.util.function.BiFunction;
import java8.util.function.BinaryOperator;

import org.apache.commons.math3.util.FastMath;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Infix operator that takes two inputs, such as addition, division, or
 * "greater than" comparison.
 */
public enum Op2 {

    /** Logical AND operation. */
    AND("&&", Op2::and),
    /** Arithmetic division operation. */
    DIVIDE("/", Op2::divide),
    /** Equality comparison operation. */
    EQUALS("==", Op2::equals),
    /** Inequality comparison operation. */
    NOT_EQUALS("!=", (a, b) -> !Op2.equals(a, b)),
    /** Greater-than comparison operation. */
    GREATER(">", Op2::greater),
    /** Greater-than-or-equal comparison operation. */
    GREATER_EQUALS(">=", Op2::greaterEquals),
    /** Maximum of two numbers or other Comparable objects. */
    MAX("min", Op2::max),
    /** Minimum of two numbers or other Comparable objects. */
    MIN("min", Op2::min),
    /** Arithmetic subtraction operation. */
    MINUS("-", Op2::minus),
    /** Modulus operation. */
    MODULUS("%", Op2::modulus),
    /** Logical OR operation. */
    OR("||", Op2::or),
    /** Arithmetic addition operation. */
    PLUS("+", Op2::plus),
    /** Exponent operation. */
    POWER("^", Op2::pow),
    /** Less-than comparison operation. */
    SMALLER("<", Op2::smaller),
    /** Less-than-or-equal comparison operation. */
    SMALLER_EQUALS("<=", Op2::smallerEquals),
    /** Arithmetic multiplication operation. */
    TIMES("*", Op2::times);

    private static final Logger L = LoggerFactory.getLogger(Op2.class);
    private static final int[] BOTH = new int[] { 0, 1 };
    private static final int[] LEFT = new int[] { 0 };
    private static final int[] RIGHT = new int[] { 1 };
    private static final int[] NONE = new int[] {};
    private static final Map<String, Op2> MAP = new ConcurrentHashMap<>();
    private final BinaryOperator<Object> fun;
    private final String opName;

    Op2(final String name, final BinaryOperator<Object> function) {
        fun = function;
        opName = name;
    }

    /**
     * @return The function implementing this operator
     */
    public BinaryOperator<Object> getFunction() {
        return fun;
    }

    /**
     * Compute the value of applying the two-argument operator to inputs a and
     * b.
     * 
     * @param a
     *            First input
     * @param b
     *            Second input
     * @return result of applying the operator to a and b
     */
    public Object run(final Object a, final Object b) {
        final boolean afield = a instanceof Field;
        final boolean bfield = b instanceof Field;
        final int[] fields = afield && bfield ? BOTH : afield ? LEFT : bfield ? RIGHT : NONE;
        if (fields.length > 0) {
            return Fields.apply(fun, fields, a, b);
        }
        return fun.apply(a, b);
    }

    @Override
    public String toString() {
        return opName;
    }

    /**
     * Translates a name into an operator.
     * 
     * @param name
     *            operator name
     * @return an {@link Op2}
     */
    public static Op2 getOp(final String name) {
        Op2 op = MAP.get(name);
        if (op == null) {
            op = J8Arrays.stream(values()).parallel().filter(o -> o.opName.equals(name)).findFirst().get();
            MAP.put(name, op);
        }
        return op;
    }

    private static Object and(final Object a, final Object b) {
        return logical("&&", a, b, (v1, v2) -> v1 && v2);
    }

    private static Object divide(final Object a, final Object b) {
        return arithmetic("/", a, b, (v1, v2) -> v1 / v2);
    }

    @SuppressFBWarnings("FE_FLOATING_POINT_EQUALITY")
    private static boolean equals(final Object a, final Object b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a instanceof Number && b instanceof Number) {
            return ((Number) a).doubleValue() == ((Number) b).doubleValue();
        }
        return a.equals(b);
    }

    private static boolean greater(final Object a, final Object b) {
        return comparison(">", a, b, (v1, v2) -> v1 > v2);
    }

    private static boolean greaterEquals(final Object a, final Object b) {
        return comparison(">=", a, b, (v1, v2) -> v1 >= v2);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static <T> boolean comparison(final String op, final T a, final T b,
            final BiFunction<Double, Double, Boolean> f) {
        if (a instanceof Number && b instanceof Number) {
            return f.apply(((Number) a).doubleValue(), ((Number) b).doubleValue());
        }
        try {
            if (a instanceof Comparable && b instanceof Comparable) {
                return f.apply((double) ((Comparable) a).compareTo(b), 0d);
            }
        } catch (RuntimeException e) { // NOPMD: comparison of different types
        }
        /*
         * Fall back to lexicographic comparison
         */
        return f.apply((double) a.toString().compareTo(b.toString()), 0d);
    }

    private static <T> boolean logical(final String op, final T a, final T b,
            final BiFunction<Boolean, Boolean, Boolean> f) {
        if (a instanceof Boolean && b instanceof Boolean) {
            return f.apply((Boolean) a, (Boolean) b);
        }
        return unsupported(op, a, b);
    }

    private static <T> T selection(final String op, final T a, final T b, final BinaryOperator<T> selector) {
        final boolean aNum = a instanceof Number;
        final boolean bNum = b instanceof Number;
        if (aNum && bNum) {
            final double ad = ((Number) a).doubleValue();
            final double bd = ((Number) b).doubleValue();
            if (ad > bd) {
                return selector.apply(a, b);
            }
            return selector.apply(b, a);
        }
        if (a instanceof Comparable && b instanceof Comparable) {
            try {
                @SuppressWarnings({ "rawtypes", "unchecked" })
                final int v = ((Comparable) a).compareTo(b);
                if (v > 0) {
                    return selector.apply(a, b);
                }
                return selector.apply(b, a);
            } catch (RuntimeException e) {
                /*
                 * Comparison of different types, fallback to lexicographic
                 * comparison
                 */
                L.debug("Comparison of different types.");
            }
        }
        /*
         * Fall back to lexicographic comparison
         */
        final int v = a.toString().compareTo(b.toString());
        if (v > 0) {
            return selector.apply(a, b);
        }
        return selector.apply(b, a);
    }

    private static Object min(final Object a, final Object b) {
        return selection("min", a, b, (v1, v2) -> v2);
    }

    private static Object max(final Object a, final Object b) {
        return selection("max", a, b, (v1, v2) -> v1);
    }

    private static Object minus(final Object a, final Object b) {
        return arithmetic("-", a, b, (v1, v2) -> v1 - v2);
    }

    private static Object modulus(final Object a, final Object b) {
        return arithmetic("%", a, b, (v1, v2) -> v1 % v2);
    }

    private static Object or(final Object a, final Object b) {
        return logical("||", a, b, (v1, v2) -> v1 || v2);
    }

    private static Object plus(final Object a, final Object b) {
        try {
            return arithmetic("+", a, b, (v1, v2) -> v1 + v2);
        } catch (UnsupportedOperationException e) {
            /*
             * Sum falls back to string sum.
             */
            return a.toString() + b.toString();
        }
    }

    private static Object pow(final Object a, final Object b) {
        return arithmetic("^", a, b, (v1, v2) -> FastMath.pow(v1, v2));
    }

    private static <I, O> O arithmetic(final String op, final I a, final I b, final BiFunction<Double, Double, O> f) {
        if (a instanceof Double && b instanceof Double) {
            return f.apply(((Double) a), ((Double) b));
        }
        if (a instanceof Number && b instanceof Number) {
            return f.apply(((Number) a).doubleValue(), ((Number) b).doubleValue());
        }
        return unsupported(op, a, b);
    }

    private static boolean smaller(final Object a, final Object b) {
        return comparison("<", a, b, (v1, v2) -> v1 < v2);
    }

    private static boolean smallerEquals(final Object a, final Object b) {
        return comparison("<=", a, b, (v1, v2) -> v1 <= v2);
    }

    private static Object times(final Object a, final Object b) {
        return arithmetic("*", a, b, (v1, v2) -> v1 * v2);
    }

}
