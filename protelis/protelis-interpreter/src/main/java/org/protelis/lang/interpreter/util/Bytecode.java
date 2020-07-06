package org.protelis.lang.interpreter.util;

/**
 * Associates a unique identifier to each Protelis interpreter entity.
 */
public enum Bytecode {

    // CHECKSTYLE: JavadocVariable OFF
    ALIGNED_MAP,
    ALIGNED_MAP_DEFAULT,
    ALIGNED_MAP_EXECUTE,
    ALIGNED_MAP_FILTER,
    ALIGNED_MAP_GENERATOR,
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
    EVAL_DYNAMIC_CODE,
    FUNCTION_CALL,
    GENERIC_HOOD_CALL,
    GENERIC_HOOD_CALL_DEFAULT,
    GENERIC_HOOD_CALL_FIELD,
    GENERIC_HOOD_CALL_FUNCTION,
    GENERIC_HOOD_CALL_REDUCE,
    HOOD_ALL,
    HOOD_ANY,
    HOOD_CALL,
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
    //CHECKSTYLE: JavadocVariable ON

    /**
     * @return a unique code
     */
    public int getCode() {
        return Integer.MIN_VALUE + ordinal();
    }

}
