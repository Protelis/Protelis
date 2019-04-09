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
    private static final byte COND = 0, THEN = 1, ELSE = 2;
    private final AnnotatedTree<Boolean> conditionExpression;
    private final AnnotatedTree<T> thenExpression, elseExpression;

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
        super(metadata, cond, then, otherwise);
        conditionExpression = cond;
        thenExpression = then;
        elseExpression = otherwise;
    }

    @Override
    public AnnotatedTree<T> copy() {
        return new If<>(getMetadata(), conditionExpression.copy(), thenExpression.copy(), elseExpression.copy());
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        conditionExpression.evalInNewStackFrame(context, COND);
        final Object actualResult = conditionExpression.getAnnotation();
        final boolean bool = actualResult instanceof Boolean
                ? conditionExpression.getAnnotation()
                : actualResult != null;
        setAnnotation(bool
                ? choice(THEN, thenExpression, elseExpression, context)
                : choice(ELSE, elseExpression, thenExpression, context));
    }

    private static <T> T choice(
            final byte branch,
            final AnnotatedTree<T> selected,
            final AnnotatedTree<T> erased,
            final ExecutionContext context) {
        selected.evalInNewStackFrame(context, branch);
        erased.erase();
        final T result = selected.getAnnotation();
        if (result instanceof Field) {
            throw new IllegalStateException(
                    "if statements cannot return a Field. This could break alignment apart. Consider using mux.");
        }
        return result;
    }

    @Override
    protected void asString(final StringBuilder sb, final int i) {
        sb.append("if (\n");
        conditionExpression.toString(sb, i + 1);
        sb.append(") {\n");
        thenExpression.toString(sb, i + 1);
        sb.append('\n');
        indent(sb, i);
        sb.append("} else {\n");
        elseExpression.toString(sb, i + 1);
        sb.append('\n');
        indent(sb, i);
        sb.append('}');
    }

}
