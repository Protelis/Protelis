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
import org.protelis.lang.interpreter.util.Op1;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Unary (prefix) operator, such as negation.
 *
 */
public final class UnaryOp extends AbstractProtelisAST<Object> {

    private static final long serialVersionUID = 2803028109250981637L;
    private final Op1 op;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param name
     *            operator name
     * @param branch
     *            the operand
     */
    public UnaryOp(final Metadata metadata, final String name, final ProtelisAST<?> branch) {
        this(metadata, Op1.getOp(name), branch);
    }

    private UnaryOp(final Metadata metadata, final Op1 operator, final ProtelisAST<?> branch) {
        super(metadata, branch);
        Objects.requireNonNull(branch);
        op = operator;
    }

    @Override
    public Object evaluate(final ExecutionContext context) {
        return op.run(getBranch(0).eval(context));
    }

    @Override
    public String getName() {
        return op.name();
    }

    @Override
    public Bytecode getBytecode() {
        return op.getBytecode();
    }

}
