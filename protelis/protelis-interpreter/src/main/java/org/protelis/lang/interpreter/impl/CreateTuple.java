/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.lang.interpreter.impl;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.Fields;
import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

import java.util.List;

/**
 * Construct a Tuple.
 */
public final class CreateTuple extends AbstractProtelisAST<Object> {

    private static final long serialVersionUID = -5018807023306859866L;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param args
     *            tuple arguments
     */
    public CreateTuple(final Metadata metadata, final List<ProtelisAST<?>> args) {
        super(metadata, args);
    }

    @Override
    public Object evaluate(final ExecutionContext context) {
        final Object[] evaluationResults = new Object[getBranchesNumber()];
        final TIntList fieldIndices = new TIntArrayList(getBranchesNumber());
        forEachWithIndex((i, branch) -> {
            final Object elem = context.runInNewStackFrame(i, branch::eval);
            evaluationResults[i] = elem;
            if (elem instanceof Field) {
                fieldIndices.add(i);
            }
        });
        if (fieldIndices.isEmpty()) {
            return DatatypeFactory.createTuple(evaluationResults);
        } else {
            return Fields.apply(DatatypeFactory::createTuple, fieldIndices.toArray(), evaluationResults);
        }
    }

    @Override
    public Bytecode getBytecode() {
        return Bytecode.CREATE_TUPLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return branchesToString(", ", "[", "]");
    }

}
