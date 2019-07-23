/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import java.util.List;

import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.Fields;
import org.protelis.lang.datatype.Tuple;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

/**
 * Construct a Tuple.
 */
public final class CreateTuple extends AbstractAnnotatedTree<Object> {

    private static final long serialVersionUID = -5018807023306859866L;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param args
     *            tuple arguments
     */
    public CreateTuple(final Metadata metadata, final AnnotatedTree<?>... args) {
        super(metadata, args);
    }

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param args
     *            tuple arguments
     */
    public CreateTuple(final Metadata metadata, final List<AnnotatedTree<?>> args) {
        super(metadata, args);
    }

    @Override
    public AnnotatedTree<Object> copy() {
        return new CreateTuple(getMetadata(), deepCopyBranches());
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        projectAndEval(context);
        final Object[] a = new Object[getBranchesNumber()];
        final TIntList fieldIndexes = new TIntArrayList(getBranchesNumber());
        forEachWithIndex((i, branch) -> {
            final Object elem = branch.getAnnotation();
            a[i] = elem;
            if (elem instanceof Field) {
                fieldIndexes.add(i);
            }
        });
        if (fieldIndexes.isEmpty()) {
            setAnnotation(DatatypeFactory.createTuple(a));
        } else {
            final Field<Tuple> res = Fields.apply(DatatypeFactory::createTuple, fieldIndexes.toArray(), a);
            setAnnotation(res);
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
