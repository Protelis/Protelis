/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.datatype.Field;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.interpreter.util.JavaInteroperabilityUtils;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Branch, restricting domain of true and false branches into their own aligned
 * subspaces.
 *
 * @param <T>
 */
public final class If<T> extends AbstractAnnotatedTree<T> {

    private static final long serialVersionUID = -4830593657731078743L;
    private final AnnotatedTree<Boolean> conditionExpression;
    private final AnnotatedTree<T> elseExpression;
    private final AnnotatedTree<T> thenExpression;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param cond
     *            condition
     * @param then
     *            branch to execute if condition is true (erase otherwise)
     * @param otherwise
     *            branch to execute if condition is false (erase otherwise)
     */
    public If(final Metadata metadata, final AnnotatedTree<Boolean> cond, final AnnotatedTree<T> then, final AnnotatedTree<T> otherwise) {
        super(metadata, cond);
        conditionExpression = cond;
        thenExpression = then;
        elseExpression = otherwise == null ? Nop.nop() : otherwise;
    }

    @Override
    public AnnotatedTree<T> copy() {
        return new If<>(getMetadata(), conditionExpression.copy(), thenExpression.copy(), elseExpression.copy());
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        projectAndEval(context);
        final boolean isTrue = conditionExpression.getAnnotation();
        final AnnotatedTree<T> selected = isTrue ? thenExpression : elseExpression;
        final AnnotatedTree<T> erased = isTrue ? elseExpression : thenExpression;
        final Bytecode opCode = isTrue ? Bytecode.IF_THEN : Bytecode.IF_ELSE;
        if (!erased.isErased()) {
            erased.erase();
        }
        evalInNewStackFrame(selected, context, opCode);
        final T result = selected.getAnnotation();
        if (result instanceof Field) {
            throw new IllegalStateException("if statements cannot return a Field, consider using mux: " + result);
        }
        setAnnotation(result);
    }

    @Override
    public Bytecode getBytecode() {
        return Bytecode.IF;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName() + " (" + stringFor(conditionExpression) + ") { "
                + stringFor(thenExpression) + " } else { " + stringFor(thenExpression) + '}';
    }

    private static class Nop extends AbstractAnnotatedTree<Void> {

        private static final Nop INSTANCE = new Nop();
        private static final long serialVersionUID = 1L;

        protected Nop() {
            super(JavaInteroperabilityUtils.METADATA);
        }

        @Override
        public AnnotatedTree<Void> copy() {
            return INSTANCE;
        }
        @Override
        protected void evaluate(final ExecutionContext context) { }

        @Override
        public Bytecode getBytecode() {
            throw new IllegalStateException("Nop operation has no bytecode representation.");
        }

        @SuppressWarnings("unchecked")
        public static <T> AbstractAnnotatedTree<T> nop() {
            return (AbstractAnnotatedTree<T>) INSTANCE;
        }

    }

}
