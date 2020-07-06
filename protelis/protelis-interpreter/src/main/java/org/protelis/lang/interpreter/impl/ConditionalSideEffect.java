/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.datatype.Unit;
import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

import javax.annotation.Nonnull;

import static org.protelis.lang.interpreter.util.Bytecode.IF_THEN;

/**
 * Branch with side effects, returns {@link Unit}.
 *
 */
public final class ConditionalSideEffect extends AbstractProtelisAST<Unit> {

    private static final long serialVersionUID = 1L;
    private final ProtelisAST<Boolean> conditionExpression;
    private final ProtelisAST<?> thenExpression;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param cond
     *            condition
     * @param then
     *            branch to execute if condition is true (erase otherwise)
     */
    public ConditionalSideEffect(
            @Nonnull final Metadata metadata,
            @Nonnull final ProtelisAST<Boolean> cond,
            @Nonnull final ProtelisAST<?> then) {
        super(metadata);
        conditionExpression = cond;
        thenExpression = then;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Unit evaluate(final ExecutionContext context) {
        if (conditionExpression.eval(context)) {
            context.runInNewStackFrame(IF_THEN.getCode(), thenExpression::eval);
        }
        return Unit.UNIT;
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
        return "if (" + stringFor(conditionExpression) + ") { " + stringFor(thenExpression) + '}';
    }
}
