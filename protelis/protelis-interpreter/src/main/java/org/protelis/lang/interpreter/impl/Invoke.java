/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.lang.interpreter.impl;


import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.datatype.JVMEntity;
import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.interpreter.util.ReflectionUtils;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

import java.util.List;
import java.util.Objects;

import static org.protelis.lang.interpreter.util.Bytecode.DOT_OPERATOR;
import static org.protelis.lang.interpreter.util.Bytecode.DOT_OPERATOR_ARGUMENTS;
import static org.protelis.lang.interpreter.util.Bytecode.DOT_OPERATOR_TARGET;

/**
 * Call an external Java non-static method.
 */
public final class Invoke extends AbstractProtelisAST<Object> {

    /**
     * Special method name, that causes a Protelis function invocation if the
     * left hand side of the {@link Invoke} is a {@link FunctionDefinition}.
     */
    public static final String APPLY = "apply";
    private static final long serialVersionUID = 1L;
    private final boolean isApply;
    private final ProtelisAST<?> left;
    private final String methodName;

    private Invoke(final Metadata metadata, final boolean apply, final String name, final ProtelisAST<?> target, final List<ProtelisAST<?>> args) {
        super(metadata, args);
        Objects.requireNonNull(target);
        isApply = apply;
        methodName = apply ? APPLY : name;
        left = target;
    }

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param name
     *            function (or method) name
     * @param target
     *            Protelis sub-program that annotates itself with the target of
     *            this call
     * @param args
     *            arguments of the function
     */
    public Invoke(
            final Metadata metadata,
            final String name,
            final ProtelisAST<?> target,
            final List<ProtelisAST<?>> args
    ) {
        this(metadata, APPLY.equals(name), name, target, args);
    }

    /**
     * Builds a new {@link #APPLY}.
     *
     * @param target the target of the invocation, must evaluate to either a {@link FunctionDefinition}
     *               or a @{@link JVMEntity} of type Method.
     * @param args the arguments
     */
    public Invoke(final ProtelisAST<?> target, final List<ProtelisAST<?>> args) {
        this(target.getMetadata(), true, null, target, args);
    }

    @Override
    public Object evaluate(final ExecutionContext context) {
        /*
         * If it is a function pointer, then create a new function call
         */
        final Object target = context.runInNewStackFrame(DOT_OPERATOR_TARGET.getCode(), left::eval);
        if (isApply && target instanceof FunctionDefinition) {
            final FunctionDefinition fd = (FunctionDefinition) target;
            /*
             * Currently, there is no change in the codepath when superscript is
             * executed: f.apply(...) is exactly equivalent to f(...).
             */
            return makeFunctionCall(fd).eval(context);
        } else {
            /*
             * Otherwise, evaluate branches and proceed to call Java
             */
            /*
             * Check everything for fields
             */
            final Object[] args = new Object[getBranchesNumber()];
            context.newCallStackFrame(DOT_OPERATOR_ARGUMENTS.getCode());
            for (int i = 0; i < getBranchesNumber(); i++) {
                args[i] = context.runInNewStackFrame(i, getBranch(i)::eval);
            }
            context.returnFromCallFrame();
            if (isApply && target instanceof JVMEntity) {
                final JVMEntity jvmEntity = (JVMEntity) target;
                return ReflectionUtils.invokeFieldable(context, jvmEntity.getType(), jvmEntity.getMemberName(), null, args);
            } else {
                return ReflectionUtils.invokeFieldable(context, target.getClass(), methodName, target, args);
            }
        }
    }

    private FunctionCall makeFunctionCall(final FunctionDefinition functionDefinition) {
        return new FunctionCall(getMetadata(), functionDefinition, getBranches());
    }

    @Override
    public Bytecode getBytecode() {
        return DOT_OPERATOR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return methodName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return stringFor(left) + '.' + methodName + branchesToString();
    }

    @Override
    protected boolean isNullable() {
        return true;
    }
}
