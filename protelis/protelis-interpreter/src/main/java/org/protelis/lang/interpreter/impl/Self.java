/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Access to the evaluation context, which is used for interfacing with sensors,
 * actuators, and the rest of the external non-static programmatic environment
 * outside of Protelis.
 */
public final class Self extends AbstractAnnotatedTree<ExecutionContext> {

    private static final long serialVersionUID = 1L;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     */
    public Self(final Metadata metadata) {
        super(metadata);
    }

    @Override
    public Self copy() {
        return new Self(getMetadata());
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        setAnnotation(context);
    }

    @Override
    public Bytecode getBytecode() {
        return Bytecode.SELF;
    }

    @Override
    public String toString() {
        return getName();
    }
}
