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
import org.protelis.lang.interpreter.util.Op2;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Two-argument (inline) operator, such as addition.
 */
public final class BinaryOp extends AbstractProtelisAST<Object> {

    private static final long serialVersionUID = 2803028109250981637L;
    private final Op2 op;

    private BinaryOp(final Metadata metadata, final Op2 operator, final ProtelisAST<?> branch1, final ProtelisAST<?> branch2) {
        super(metadata, branch1, branch2);
        Objects.requireNonNull(branch1);
        Objects.requireNonNull(branch2);
        op = operator;
    }

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param name
     *            operation name
     * @param branch1
     *            left operand
     * @param branch2
     *            right operand
     */
    public BinaryOp(final Metadata metadata, final String name, final ProtelisAST<?> branch1, final ProtelisAST<?> branch2) {
        this(metadata, Op2.getOp(name), branch1, branch2);
    }

    @Override
    public Object evaluate(final ExecutionContext context) {
        return op.run(
            context.runInNewStackFrame(0, getBranch(0)::eval),
            context.runInNewStackFrame(1, getBranch(1)::eval)
        );
    }

    @Override
    public Bytecode getBytecode() {
        return op.getBytecode();
    }

    @Override
    public String getName() {
        return op.toString();
    }

    @Override
    public String toString() {
        return stringFor(getBranch(0)) + ' ' + getName() + ' ' + stringFor(getBranch(1));
    }

}
