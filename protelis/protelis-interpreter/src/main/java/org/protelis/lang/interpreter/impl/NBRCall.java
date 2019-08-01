/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import java.util.function.Function;

import org.protelis.lang.datatype.Field;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Implementation of 'nbr' operator.
 *
 * @param <T> field type
 */
public final class NBRCall<T> extends AbstractAnnotatedTree<Field<T>> {

    private static final long serialVersionUID = 5255917527687990281L;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param body
     *            body of nbr
     */
    public NBRCall(final Metadata metadata, final AnnotatedTree<T> body) {
        super(metadata, body);
    }

    @SuppressWarnings("unchecked")
    @Override
    public NBRCall<T> copy() {
        return new NBRCall<>(getMetadata(), (AnnotatedTree<T>) deepCopyBranches().get(0));
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        projectAndEval(context);
        @SuppressWarnings("unchecked")
        final T childVal = (T) getBranch(0).getAnnotation();
        final Field<T> res = context.buildField(Function.identity(), childVal);
        setAnnotation(res);
    }

    @Override
    public Bytecode getBytecode() {
        return Bytecode.NBR;
    }

    @Override
    public String getName() {
        return "nbr";
    }

}
