/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import com.google.common.collect.ImmutableList;
import org.eclipse.xtext.common.types.JvmOperation;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.interpreter.util.ReflectionUtils;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

import java.util.function.BinaryOperator;

import static org.protelis.lang.interpreter.util.Bytecode.GENERIC_HOOD_CALL_DEFAULT;
import static org.protelis.lang.interpreter.util.Bytecode.GENERIC_HOOD_CALL_FIELD;
import static org.protelis.lang.interpreter.util.Bytecode.GENERIC_HOOD_CALL_FUNCTION;

/**
 * Reduce a field into a local value by reduction using a {@link org.protelis.lang.interpreter.util.HoodOp}.
 */
public final class GenericHoodCall extends AbstractProtelisAST<Object> {

    private static final long serialVersionUID = 1L;
    private final ProtelisAST<Field<Object>> body;
    private final Class<?> clazz;
    private final ProtelisAST<?> empty;
    private final ProtelisAST<FunctionDefinition> function;
    private final boolean inclusive;
    private final String methodName;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param includeSelf
     *            if true, sigma won't be excluded
     * @param fun
     *            the {@link FunctionDefinition} to apply
     * @param nullResult
     *            the expression that will be evaluated if the field is empty
     * @param arg
     *            the argument to evaluate (must return a {@link Field}).
     */
    public GenericHoodCall(
            final Metadata metadata,
            final boolean includeSelf,
            final ProtelisAST<FunctionDefinition> fun,
            final ProtelisAST<?> nullResult,
            final ProtelisAST<Field<Object>> arg) {
        super(metadata);
        body = arg;
        function = fun;
        empty = nullResult;
        inclusive = includeSelf;
        methodName = null;
        clazz = null;
    }

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param includeSelf
     *            if true, sigma won't be excluded
     * @param fun
     *            the {@link FunctionDefinition} to apply
     * @param nullResult
     *            the expression that will be evaluated if the field is empty
     * @param arg
     *            the argument to evaluate (must return a {@link Field}).
     * @throws ClassNotFoundException 
     */
    public GenericHoodCall(
            final Metadata metadata, 
            final boolean includeSelf,
            final JvmOperation fun,
            final ProtelisAST<?> nullResult,
            final ProtelisAST<Field<Object>> arg) {
        super(metadata, nullResult, arg);
        body = arg;
        empty = nullResult;
        inclusive = includeSelf;
        methodName = fun.getSimpleName();
        try {
            clazz = Class.forName(fun.getDeclaringType().getQualifiedName());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        function = null;
    }

    @Override
    public Object evaluate(final ExecutionContext context) {
        /*
         * Evaluate the function, the nullResult, and the argument
         */
        final Field<Object> targetField = context.runInNewStackFrame(GENERIC_HOOD_CALL_FIELD.getCode(), body::eval);
        final Object emptyResult = context.runInNewStackFrame(GENERIC_HOOD_CALL_DEFAULT.getCode(), empty::eval);
        final BinaryOperator<Object> merger;
        if (function == null) {
            merger = (a, b) -> ReflectionUtils
                    .invokeFieldable(context, clazz, methodName, null, new Object[] { a, b });
        } else {
            merger = (a, b) -> makeCall(context, a, b).eval(context);
        }
        return inclusive
            ? targetField.foldValuesIncludingLocal(merger)
            : targetField.reduceValues(merger).orElse(emptyResult);
    }

    @Override
    public Bytecode getBytecode() {
        return Bytecode.GENERIC_HOOD_CALL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "hood" + (inclusive ? "PlusSelf" : "");
    }

    private FunctionCall makeCall(final ExecutionContext context, final Object a, final Object b) {
        final FunctionDefinition reducer = context.runInNewStackFrame(GENERIC_HOOD_CALL_FUNCTION.getCode(), function::eval);
        return new FunctionCall(
                    function.getMetadata(),
                    reducer,
                    ImmutableList.of(
                        new Constant<>(function.getMetadata(), a),
                        new Constant<>(function.getMetadata(), b)));
    }

}
