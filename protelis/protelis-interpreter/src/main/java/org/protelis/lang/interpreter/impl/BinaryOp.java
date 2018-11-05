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
import org.protelis.lang.util.Op2;
import org.protelis.vm.ExecutionContext;

/**
 * Two-argument (inline) operator, such as addition.
 */
public final class BinaryOp extends AbstractAnnotatedTree<Object> {

    private static final long serialVersionUID = 2803028109250981637L;
    private final Op2 op;

    /**
     * @param name
     *            operation name
     * @param branch1
     *            left operand
     * @param branch2
     *            right operand
     */
    public BinaryOp(final String name, final AnnotatedTree<?> branch1, final AnnotatedTree<?> branch2) {
        this(Op2.getOp(name), branch1, branch2);
    }

    private BinaryOp(final Op2 operator, final AnnotatedTree<?> branch1, final AnnotatedTree<?> branch2) {
        super(branch1, branch2);
        Objects.requireNonNull(branch1);
        Objects.requireNonNull(branch2);
        op = operator;
    }

    @Override
    public AnnotatedTree<Object> copy() {
        final List<AnnotatedTree<?>> branches = deepCopyBranches();
        return new BinaryOp(op, branches.get(0), branches.get(1));
    }

    @Override
    public void eval(final ExecutionContext context) {
        projectAndEval(context);
        setAnnotation(op.run(getBranch(0).getAnnotation(), getBranch(1).getAnnotation()));
    }

    @Override
    protected void asString(final StringBuilder sb, final int i) {
        getBranch(0).toString(sb, i);
        sb.append('\n');
        indent(sb, i);
        sb.append(op.toString());
        sb.append('\n');
        getBranch(1).toString(sb, i);
    }

}
