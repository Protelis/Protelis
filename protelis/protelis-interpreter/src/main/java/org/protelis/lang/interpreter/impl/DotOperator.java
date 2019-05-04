/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;


import static org.protelis.lang.interpreter.util.Bytecode.DOT_OPERATOR;
import static org.protelis.lang.interpreter.util.Bytecode.DOT_OPERATOR_ARGUMENTS;
import static org.protelis.lang.interpreter.util.Bytecode.DOT_OPERATOR_TARGET;

import java.util.List;
import java.util.Objects;

import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.datatype.JVMEntity;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.interpreter.util.ReflectionUtils;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Call an external Java non-static method.
 */
public final class DotOperator extends AbstractSATree<FunctionCall, Object> {

    /**
     * Special method name, that causes a Protelis function invocation if the
     * left hand side of the {@link DotOperator} is a {@link FunctionDefinition}.
     */
    public static final String APPLY = "apply";
    private static final long serialVersionUID = -9128116355271771986L;
    private final boolean isApply;
    private final AnnotatedTree<?> left;
    private final String methodName;

    private DotOperator(final Metadata metadata, final boolean apply, final String name, final AnnotatedTree<?> target, final List<AnnotatedTree<?>> args) {
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
    public DotOperator(final Metadata metadata, final String name, final AnnotatedTree<?> target, final List<AnnotatedTree<?>> args) {
        this(metadata, name.equals(APPLY), name, target, args);
    }

    @Override
    public AnnotatedTree<Object> copy() {
        final DotOperator res = new DotOperator(getMetadata(), methodName, left.copy(), deepCopyBranches());
        res.setSuperscript(getSuperscript());
        return res;
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        /*
         * Eval left
         */
        evalInNewStackFrame(left, context, DOT_OPERATOR_TARGET);
        /*
         * If it is a function pointer, then create a new function call
         */
        final Object target = left.getAnnotation();
        context.newCallStackFrame(DOT_OPERATOR_ARGUMENTS.getCode());
        if (isApply && target instanceof FunctionDefinition) {
            final FunctionDefinition fd = (FunctionDefinition) target;
            /*
             * Currently, there is no change in the codepath when superscript is
             * executed: f.apply(...) is exactly equivalent to f(...).
             */
            final boolean hasCall = getSuperscript() instanceof FunctionCall;
            final FunctionCall prevFC = hasCall ? (FunctionCall) getSuperscript() : null;
            final FunctionCall fc = hasCall && fd.equals(prevFC.getFunctionDefinition())
                ? prevFC
                : new FunctionCall(getMetadata(), fd, deepCopyBranches());
            setSuperscript(fc);
            fc.eval(context);
            setAnnotation(fc.getAnnotation());
        } else {
            /*
             * Otherwise, evaluate branches and proceed to evaluation
             */
            projectAndEval(context);
            /*
             * Check everything for fields
             */
            final Object[] args = getBranchesAnnotations();
            if (isApply && target instanceof JVMEntity) {
                final JVMEntity jvmEntity = (JVMEntity) target;
                setAnnotation(ReflectionUtils.invokeFieldable(jvmEntity.getType(), jvmEntity.getMemberName(), null, args));
            } else {
                setAnnotation(ReflectionUtils.invokeFieldable(target.getClass(), methodName, target, args));
            }
        }
        context.returnFromCallFrame();
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

    /**
     * Builds a new {@link #APPLY}.
     * 
     * @param target the target of the invocation
     * @param args the arguments
     * @return a new {@link #APPLY} {@link DotOperator}.
     */
    public static DotOperator makeApply(final AnnotatedTree<FunctionDefinition> target, final List<AnnotatedTree<?>> args) {
        return new DotOperator(target.getMetadata(), true, null, target, args);
    }

}
