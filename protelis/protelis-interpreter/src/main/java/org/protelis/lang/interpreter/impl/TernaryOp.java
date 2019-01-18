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
import java.util.Objects;

import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.util.Op3;
import org.protelis.vm.ExecutionContext;

/**
 * Three-argument syntactic operator such as multiplexing (mux).
 */
public final class TernaryOp extends AbstractAnnotatedTree<Object> {

    private static final long serialVersionUID = 2803028109250981637L;
    private final Op3 op;

    /**
     * @param name
     *            Operator name
     * @param branch1
     *            first argument
     * @param branch2
     *            second argument
     * @param branch3
     *            third argument
     */
    public TernaryOp(
            final String name,
            final AnnotatedTree<?> branch1,
            final AnnotatedTree<?> branch2,
            final AnnotatedTree<?> branch3) {
        this(Op3.getOp(name), branch1, branch2, branch3);
    }

    private TernaryOp(
            final Op3 operator,
            final AnnotatedTree<?> branch1,
            final AnnotatedTree<?> branch2,
            final AnnotatedTree<?> branch3) {
        super(branch1, branch2, branch3);
        Objects.requireNonNull(branch1);
        Objects.requireNonNull(branch2);
        Objects.requireNonNull(branch3);
        op = operator;
    }

    @Override
    public AnnotatedTree<Object> copy() {
        final List<AnnotatedTree<?>> branches = deepCopyBranches();
        return new TernaryOp(op, branches.get(0), branches.get(1), branches.get(2));
    }

    @Override
    public void eval(final ExecutionContext context) {
        projectAndEval(context);
        setAnnotation(op.run(getBranch(0).getAnnotation(), getBranch(1).getAnnotation(), getBranch(2).getAnnotation()));
    }

    @Override
    protected void asString(final StringBuilder sb, final int i) {
        sb.append(op.toString()).append('(');
        fillBranches(sb, i, ',');
        sb.append(')');
    }

}
