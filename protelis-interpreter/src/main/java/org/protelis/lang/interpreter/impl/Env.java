/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

/**
 * 
 */
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ExecutionEnvironment;

/**
 *
 */
public final class Env extends AbstractProtelisAST<ExecutionEnvironment> {

    private static final long serialVersionUID = 636239540800669478L;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     */
    public Env(final Metadata metadata) {
        super(metadata);
    }

    @Override
    public ExecutionEnvironment evaluate(final ExecutionContext context) {
        return context.getExecutionEnvironment();
    }

    @Override
    public Bytecode getBytecode() {
        return Bytecode.ENV;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName();
    }


}
