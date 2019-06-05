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
import org.protelis.lang.datatype.Option;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.util.Bytecode;
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
        elseExpression = otherwise;
    }

    @Override
    public AnnotatedTree<T> copy() {
        return new If<>(getMetadata(),
                conditionExpression.copy(),
                thenExpression.copy(),
                elseExpression == null ? null : elseExpression.copy());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void evaluate(final ExecutionContext context) {
        projectAndEval(context);
        final boolean isTrue = conditionExpression.getAnnotation();
        if (elseExpression == null) {
            if (isTrue) {
                evalInNewStackFrame(thenExpression, context, Bytecode.IF_THEN);
                setAnnotation(thenExpression.getAnnotation());
            } else {
                thenExpression.erase();
                setAnnotation((T) Option.empty());
            }
        } else {
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

    @Override
    protected boolean isNullable() {
        return elseExpression == null;
    }

}
