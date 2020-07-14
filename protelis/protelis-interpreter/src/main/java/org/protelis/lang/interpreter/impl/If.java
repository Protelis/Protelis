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
import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

import javax.annotation.Nonnull;

import static org.protelis.lang.interpreter.util.Bytecode.IF_ELSE;
import static org.protelis.lang.interpreter.util.Bytecode.IF_THEN;

/**
 * Branch, restricting domain of true and false branches into their own aligned
 * subspaces.
 *
 * @param <T>
 */
public final class If<T> extends AbstractProtelisAST<T> {

    private static final long serialVersionUID = -4830593657731078743L;
    private final ProtelisAST<Boolean> conditionExpression;
    private final ProtelisAST<T> elseExpression;
    private final ProtelisAST<T> thenExpression;

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
    public If(
            @Nonnull final Metadata metadata,
            @Nonnull final ProtelisAST<Boolean> cond,
            @Nonnull final ProtelisAST<T> then,
            @Nonnull final ProtelisAST<T> otherwise) {
        super(metadata);
        conditionExpression = cond;
        thenExpression = then;
        elseExpression = otherwise;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T evaluate(final ExecutionContext context) {
        return ensureNotAField(
            conditionExpression.eval(context)
                ? context.runInNewStackFrame(IF_THEN.getCode(), thenExpression::eval)
                : context.runInNewStackFrame(IF_ELSE.getCode(), elseExpression::eval)
        );
    }

    private static <T> T ensureNotAField(final T in) {
        if (in instanceof Field) {
            throw new IllegalStateException("if statements cannot return a Field, consider using mux: " + in);
        }
        return in;
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
