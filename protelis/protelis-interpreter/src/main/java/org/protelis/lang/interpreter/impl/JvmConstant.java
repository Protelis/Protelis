/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.datatype.JVMEntity;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Access a variable.
 */
public final class JvmConstant extends AbstractProtelisAST<Object> {

    private static final long serialVersionUID = 1L;
    private final JVMEntity javaFeature;

    /**
     * @param metadata    A {@link Metadata} object containing information about the
     *                    code that generated this AST node.
     * @param javaFeature the java entity to refer to
     */
    public JvmConstant(final Metadata metadata, final JVMEntity javaFeature) {
        super(metadata);
        this.javaFeature = javaFeature;
    }

    @Override
    public Object evaluate(final ExecutionContext context) {
        return javaFeature.getValue();
    }

    @Override
    public Bytecode getBytecode() {
        return Bytecode.VARIABLE_ACCESS;
    }

    @Override
    public String getName() {
        return javaFeature.getMemberName();
    }

    @Override
    protected boolean isNullable() {
        return true;
    }

    @Override
    public String toString() {
        return javaFeature.toString();
    }
}
