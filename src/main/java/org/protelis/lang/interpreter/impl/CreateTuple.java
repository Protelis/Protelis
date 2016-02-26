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
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.vm.ExecutionContext;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

/**
 * Construct a Tuple.
 */
public class CreateTuple extends AbstractAnnotatedTree<Object> {

    private static final long serialVersionUID = -5018807023306859866L;

    /**
     * @param args
     *            tuple arguments
     */
    public CreateTuple(final AnnotatedTree<?>... args) {
        super(args);
    }

    /**
     * @param args
     *            tuple arguments
     */
    public CreateTuple(final List<AnnotatedTree<?>> args) {
        super(args);
    }

    @Override
    public AnnotatedTree<Object> copy() {
        return new CreateTuple(deepCopyBranches());
    }

    @Override
    public void eval(final ExecutionContext context) {
        final Object[] a = new Object[getBranchesNumber()];
        final TIntList fieldIndexes = new TIntArrayList();
        forEachWithIndex((i, branch) -> {
            branch.evalInNewStackFrame(context, i.byteValue());
            final Object elem = branch.getAnnotation();
            a[i] = elem;
            if (elem instanceof Field) {
                fieldIndexes.add(i);
            }
        });
        if (fieldIndexes.isEmpty()) {
            setAnnotation(DatatypeFactory.create(a));
        } else {
            final Field res = Field.apply(DatatypeFactory::create, fieldIndexes.toArray(), a);
            setAnnotation(res);
        }
    }

    @Override
    protected void asString(final StringBuilder sb, final int i) {
        sb.append('[');
        fillBranches(sb, i, ',');
        sb.append(']');
    }

}
