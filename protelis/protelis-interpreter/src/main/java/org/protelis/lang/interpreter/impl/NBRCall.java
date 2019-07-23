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
 */
public final class NBRCall extends AbstractAnnotatedTree<Field<?>> {

    private static final long serialVersionUID = 5255917527687990281L;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param body
     *            body of nbr
     */
    public NBRCall(final Metadata metadata, final AnnotatedTree<?> body) {
        super(metadata, body);
    }

    @Override
    public NBRCall copy() {
        return new NBRCall(getMetadata(), deepCopyBranches().get(0));
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        projectAndEval(context);
        final Object childVal = getBranch(0).getAnnotation();
        final Field<?> res = context.buildField(Function.identity(), childVal);
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
