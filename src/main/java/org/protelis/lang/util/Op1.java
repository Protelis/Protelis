/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.util;

import static org.protelis.lang.util.OpUtils.unsupported;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java8.util.function.UnaryOperator;
import java8.util.J8Arrays;

import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.Fields;

/**
 * Collection of functions and helper methods for unary operators.
 */
public enum Op1 {

    /**
     * Not.
     */
    NOT("!", Op1::not),
    /**
     * Sign inversion.
     */
    MINUS("-", Op1::minus);

    private static final int[] FIELDS = new int[] { 0 };
    private static final Map<String, Op1> MAP = new ConcurrentHashMap<>();
    private final UnaryOperator<Object> fun;
    private final String opName;

    Op1(final String name, final UnaryOperator<Object> function) {
        fun = function;
        opName = name;
    }

    /**
     * @param a
     *            the object on which the {@link Op1} should be run
     * @return the result of the evaluation
     */
    public Object run(final Object a) {
        if (a instanceof Field) {
            return Fields.applyWithSingleParam(fun, FIELDS, a);
        }
        return fun.apply(a);
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
     * @return an {@link Op1}
     */
    public static Op1 getOp(final String name) {
        Op1 op = MAP.get(name);
        if (op == null) {
            op = J8Arrays.stream(values()).parallel().filter(o -> o.opName.equals(name)).findFirst().get();
            MAP.put(name, op);
        }
        return op;
    }

    private static double minus(final Object o) {
        if (o instanceof Number) {
            return -((Number) o).doubleValue();
        }
        return unsupported("-", o);
    }

    private static boolean not(final Object o) {
        if (o instanceof Boolean) {
            return !(Boolean) o;
        }
        return unsupported("!", o);
    }
}
