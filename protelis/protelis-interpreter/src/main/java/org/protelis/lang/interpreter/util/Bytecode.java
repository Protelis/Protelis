package org.protelis.lang.interpreter.util;

import static com.google.common.collect.ImmutableList.of;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.util.Collections.emptyList;
import static org.apache.commons.math3.util.Pair.create;

import org.protelis.lang.datatype.Tuple;

public enum Bytecode {

    ALIGNED_MAP,
    ALIGNED_MAP_FILTER,
    ALIGNED_MAP_EXECUTE,
    ALL,
    BINARY_AND,
    BINARY_DIFFERS,
    BINARY_DIVIDE,
    BINARY_EQUALS,
    BINARY_GREATER,
    BINARY_GREATER_EQUAL,
    BINARY_MAX,
    BINARY_MIN,
    BINARY_MINUS,
    BINARY_MODULUS,
    BINARY_OR,
    BINARY_PLUS,
    BINARY_POWER,
    BINARY_SMALLER,
    BINARY_SMALLER_EQUAL,
    BINARY_TIMES,
    CONSTANT,
    CREATE_TUPLE,
    CREATE_VARIABLE,
    DOT_OPERATOR,
    DOT_OPERATOR_TARGET,
    DOT_OPERATOR_ARGUMENTS,
    ENV,
    EVAL,
    FUNCTION_CALL,
    GENERIC_HOOD_CALL,
    HOOD_CALL,
    HOOD_ALL,
    HOOD_ANY,
    HOOD_LOCAL,
    HOOD_MAX,
    HOOD_MEAN,
    HOOD_MIN,
    HOOD_SUM,
    HOOD_UNION,
    IF,
    IF_ELSE,
    IF_THEN,
    METHOD_CALL,
    NBR,
    REP,
    SELF,
    SHARE,
    SHARE_BODY,
    SHARE_INIT,
    SHARE_YIELD,
    TERNARY_MUX,
    UNARY_MINUS,
    UNARY_NOT,
    VARIABLE_ACCESS;

//    private final byte code;

//    private Bytecode(byte code) {
//        this.code = code;
//    }
//
//    private Bytecode(int code) {
//        if (code < Byte.MIN_VALUE || code > Byte.MAX_VALUE) {
//            throw new IllegalStateException("Byte code " + code + " is out of range");
//        }
//        this.code = (byte) code;
//    }

    public int getCode() {
        return Integer.MIN_VALUE + ordinal();
    }

}
