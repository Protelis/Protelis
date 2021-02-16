/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Declare a variable from a "let" expression.
 */
public final class AssignmentOp extends AbstractProtelisAST<Object> {

    private static final long serialVersionUID = -7298208661255971616L;
    private final Reference var;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param name
     *            variable name
     * @param value
     *            program to evaluate to compute the value
     */
    public AssignmentOp(final Metadata metadata, final Reference name, final ProtelisAST<?> value) {
        super(metadata, value);
        var = name;
    }

    @Override
    public Object evaluate(final ExecutionContext context) {
        final Object res = getValue().eval(context);
        context.putVariable(var, res);
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "let";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName() + ' ' + var + " = " + stringFor(getValue());
    }

    @Override
    public Bytecode getBytecode() {
        return Bytecode.CREATE_VARIABLE;
    }

    private ProtelisAST<?> getValue() {
        return getBranch(0);
    }

}
