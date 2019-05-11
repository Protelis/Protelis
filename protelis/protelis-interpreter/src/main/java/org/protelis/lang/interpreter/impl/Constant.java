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
public class Constant<T> extends AbstractAnnotatedTree<T> {

    private static final long serialVersionUID = 2101316473738120102L;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Constant<T> copy() {
        return new Constant<>(getMetadata(), constantValue);
    }

    @Override
    public final void evaluate(final ExecutionContext context) {
        if (isErased()) {
            setAnnotation(constantValue);
        }
    }

    @Override
    public final Bytecode getBytecode() {
        return Bytecode.CONSTANT;
    }

    /**
     * @return the constant value
     */
    protected final T getInternalObject() {
        return constantValue;
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

}
