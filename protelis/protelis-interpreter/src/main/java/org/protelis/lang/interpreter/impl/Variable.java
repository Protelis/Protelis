/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.loading.Metadata;
import org.protelis.lang.util.Reference;
import org.protelis.vm.ExecutionContext;

/**
 * Access a variable.
 */
public final class Variable extends AbstractAnnotatedTree<Object> {

    private static final long serialVersionUID = -3739014755916345132L;
    private final Reference name;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param ref
     *            variable name
     */
    public Variable(final Metadata metadata, final Reference ref) {
        super(metadata);
        name = ref;
    }

    @Override
    public Variable copy() {
        return new Variable(getMetadata(), name);
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        Object val = context.getVariable(name);
        if (val == null) {
            /*
             * The variable cannot be read. Most probably, it is some node
             * variable that is not set here. Defaults to false.
             */
            val = false;
        }
        /*
         * TODO: Restrict variable to aligned fields
         */
        setAnnotation(val);
    }

    @Override
    public String getName() {
        return name.toString();
    }

    @Override
    public String toString() {
        return getName();
    }

}
