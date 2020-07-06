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
import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Implementation of 'nbr' operator.
 *
 * @param <T> field type
 */
public final class NBRCall<T> extends AbstractProtelisAST<Field<T>> {

    private static final long serialVersionUID = 5255917527687990281L;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param body
     *            body of nbr
     */
    public NBRCall(final Metadata metadata, final ProtelisAST<T> body) {
        super(metadata, body);
    }

    @Override
    public Field<T> evaluate(final ExecutionContext context) {
        return context.buildField(Function.identity(), (T) getBranch(0).eval(context));
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
