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

import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * An arbitrary object-valued constant.
 *
 * @param <T>
 */
public class Constant<T> extends AbstractProtelisAST<T> {

    private static final long serialVersionUID = 1L;
    private final T constantValue;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param obj
     *            the constant to be associated
     */
    public Constant(final Metadata metadata, final T obj) {
        super(metadata);
        Objects.requireNonNull(obj);
        constantValue = obj;
    }

    @Override
    public final T evaluate(final ExecutionContext context) {
        return constantValue;
    }

    @Override
    public final Bytecode getBytecode() {
        return Bytecode.CONSTANT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return constantValue.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * @return the value associated to this constant
     */
    public final T getConstantValue() {
        return constantValue;
    }

}
