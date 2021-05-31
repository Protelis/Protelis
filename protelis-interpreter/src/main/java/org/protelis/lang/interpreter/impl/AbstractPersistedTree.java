/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.lang.interpreter.impl;

import java.util.List;
import java.util.function.Supplier;

import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Basic implementation of a persisted tree.
 *
 * @param <S>
 *            Superscript type
 * @param <T>
 *            Annotation type
 */
public abstract class AbstractPersistedTree<S, T> extends AbstractProtelisAST<T> {

    private static final long serialVersionUID = 457607604000217166L;
    private transient S superscript;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param branches
     *            branches of this {@link AbstractPersistedTree}
     */
    protected AbstractPersistedTree(final Metadata metadata, final ProtelisAST<?>... branches) {
        super(metadata, branches);
    }

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param branches
     *            branches of this {@link AbstractPersistedTree}
     */
    protected AbstractPersistedTree(final Metadata metadata, final List<ProtelisAST<?>> branches) {
        super(metadata, branches);
    }

    /**
     * Retrieves the function state from last round, or produces a new state otherwise.
     *
     * @param context the {@link ExecutionContext}
     * @param ifAbsent a 0-ary function producing the value if none is stored
     * @return the previous state, if present, or the state computed by ifAbsent otherwise
     */
    protected final S loadState(final ExecutionContext context, final Supplier<S> ifAbsent) {
        superscript = context.getPersistent(ifAbsent);
        return superscript;
    }

    /**
     * Stores the function state for the next round.
     *
     * @param context the {@link ExecutionContext}
     * @param obj the state
     */
    protected final void saveState(final ExecutionContext context, final S obj) {
        context.setPersistent(obj);
        superscript = obj;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return super.toString() + "{ "
            + (superscript instanceof ProtelisAST
                ? stringFor((ProtelisAST<?>) superscript)
                : superscript == null ? "..." : superscript)
            + " }";
    }

}
