/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import org.eclipse.xtext.common.types.JvmOperation;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.loading.Metadata;
import org.protelis.lang.util.JavaInteroperabilityUtils;
import org.protelis.lang.util.ReflectionUtils;
import org.protelis.vm.ExecutionContext;

/**
 * Reduce a field into a local value by reduction using a {@link org.protelis.lang.util.HoodOp}.
 */
public final class GenericHoodCall extends AbstractAnnotatedTree<Object> {

    private static final long serialVersionUID = -4925767634715581329L;
    private final AnnotatedTree<FunctionDefinition> function;
    private final AnnotatedTree<Field> body;
    private final AnnotatedTree<?> empty;
    private final String methodName;
    private final Class<?> clazz;
    private final boolean inclusive;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param includeSelf
     *            if true, sigma won't be excluded
     * @param fun
     *            the {@link HoodOp} to apply
     * @param nullResult
     *            the expression that will be evaluated if the field is empty
     * @param arg
     *            the argument to evaluate (must return a {@link Field}).
     */
    public GenericHoodCall(
            final Metadata metadata,
            final boolean includeSelf,
            final AnnotatedTree<FunctionDefinition> fun,
            final AnnotatedTree<?> nullResult,
            final AnnotatedTree<Field> arg) {
        super(metadata, fun, nullResult, arg);
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
     *            the {@link HoodOp} to apply
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
            final AnnotatedTree<?> nullResult,
            final AnnotatedTree<Field> arg) {
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
    public AnnotatedTree<Object> copy() {
        return new GenericHoodCall(getMetadata(), inclusive, function.copy(), empty.copy(), body.copy());
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        /*
         * Evaluate the function, the nullResult, and the argument
         */
        projectAndEval(context);
        final Object result = body.getAnnotation().reduceVals(
                (a, b) -> function == null
//                    ? JavaInteroperabilityUtils.runStaticMethod(context, clazz, methodName, a, b)
                    ? ReflectionUtils.invokeBestStatic(clazz, methodName, a, b)
                    : JavaInteroperabilityUtils.runProtelisFunctionWithJavaArguments(context, function, a, b),
                inclusive ? null : context.getDeviceUID(),
                empty.getAnnotation());
        setAnnotation(result);
    }

    @Override
    protected void asString(final StringBuilder sb, final int i) {
        sb.append("hood");
        if (inclusive) {
            sb.append("PlusSelf");
        }
        sb.append('(');
        fillBranches(sb, i, ',');
        sb.append(')');
    }

}
