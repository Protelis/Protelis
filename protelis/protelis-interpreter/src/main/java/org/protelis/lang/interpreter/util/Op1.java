/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.util;

import static org.protelis.lang.interpreter.util.Bytecode.UNARY_MINUS;
import static org.protelis.lang.interpreter.util.Bytecode.UNARY_NOT;
import static org.protelis.lang.interpreter.util.OpUtils.unsupported;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.Fields;

import java8.util.J8Arrays;
import java8.util.function.UnaryOperator;

/**
 * Collection of functions and helper methods for unary operators.
 */
public enum Op1 implements WithBytecode {

    /**
     * Sign inversion.
     */
    MINUS(UNARY_MINUS, "-", Op1::minus),
    /**
     * Not.
     */
    NOT(UNARY_NOT, "!", Op1::not);

    private static final int[] FIELDS = new int[] { 0 };
    private static final Map<String, Op1> MAP = new ConcurrentHashMap<>();
    private final Bytecode bytecode;
    private final UnaryOperation fun;
    private final String opName;

    Op1(final Bytecode bytecode, final String name, final UnaryOperation function) {
        fun = function;
        opName = name;
        this.bytecode = bytecode;
    }

    @Override
    public Bytecode getBytecode() {
        return bytecode;
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

    private interface UnaryOperation extends UnaryOperator<Object>, Serializable { }
}
