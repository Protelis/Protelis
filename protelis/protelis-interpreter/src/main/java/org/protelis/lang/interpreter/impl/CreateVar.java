/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Declare a variable from a "let" expression.
 */
public final class CreateVar extends AbstractAnnotatedTree<Object> {

    private static final long serialVersionUID = -7298208661255971616L;
    private final Reference var;
    private final boolean definition;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param name
     *            variable name
     * @param value
     *            program to evaluate to compute the value
     * @param isDefinition
     *            true if it is a let
     */
    public CreateVar(final Metadata metadata, final Reference name, final AnnotatedTree<?> value, final boolean isDefinition) {
        super(metadata, value);
        var = name;
        definition = isDefinition;
    }

    @Override
    public AnnotatedTree<Object> copy() {
        return new CreateVar(getMetadata(), var, deepCopyBranches().get(0), definition);
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        projectAndEval(context);
        final Object res = getBranch(0).getAnnotation();
        context.returnFromCallFrame();
        context.putVariable(var, res, isDefinition());
        context.newCallStackFrame(getBytecode().getCode());
        setAnnotation(res);
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
        return getName() + ' ' + var + " = " + stringFor(getBranch(0));
    }

    /**
     * @return true if it is a let
     */
    public boolean isDefinition() {
        return definition;
    }

    @Override
    public Bytecode getBytecode() {
        return Bytecode.CREATE_VARIABLE;
    }

}
