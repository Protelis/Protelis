/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import java.util.Objects;

import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.interpreter.util.Op3;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Three-argument syntactic operator such as multiplexing (mux).
 */
public final class TernaryOp extends AbstractProtelisAST<Object> {

    private static final long serialVersionUID = 2803028109250981637L;
    private final Op3 op;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param name
     *            Operator name
     * @param branch1
     *            first argument
     * @param branch2
     *            second argument
     * @param branch3
     *            third argument
     */
    public TernaryOp(
            final Metadata metadata,
            final String name,
            final ProtelisAST<?> branch1,
            final ProtelisAST<?> branch2,
            final ProtelisAST<?> branch3) {
        this(metadata, Op3.getOp(name), branch1, branch2, branch3);
    }

    private TernaryOp(
            final Metadata metadata, 
            final Op3 operator,
            final ProtelisAST<?> branch1,
            final ProtelisAST<?> branch2,
            final ProtelisAST<?> branch3) {
        super(metadata, branch1, branch2, branch3);
        Objects.requireNonNull(branch1);
        Objects.requireNonNull(branch2);
        Objects.requireNonNull(branch3);
        op = operator;
    }

    @Override
    public Object evaluate(final ExecutionContext context) {
        return op.run(
            evalBranch(context, 0),
            evalBranch(context, 1),
            evalBranch(context, 2)
        );
    }

    private Object evalBranch(final ExecutionContext context, final int i) {
        return context.runInNewStackFrame(i, getBranch(i)::eval);
    }

    @Override
    public String getName() {
        return op.toString();
    }

    @Override
    public Bytecode getBytecode() {
        return op.getBytecode();
    }
}
