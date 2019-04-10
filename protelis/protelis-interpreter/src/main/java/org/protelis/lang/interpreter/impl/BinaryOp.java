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
import org.protelis.lang.loading.Metadata;
import org.protelis.lang.util.Op2;
import org.protelis.vm.ExecutionContext;

/**
 * Two-argument (inline) operator, such as addition.
 */
public final class BinaryOp extends AbstractAnnotatedTree<Object> {

    private static final long serialVersionUID = 2803028109250981637L;
    private final Op2 op;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param name
     *            operation name
     * @param branch1
     *            left operand
     * @param branch2
     *            right operand
     */
    public BinaryOp(final Metadata metadata, final String name, final AnnotatedTree<?> branch1, final AnnotatedTree<?> branch2) {
        this(metadata, Op2.getOp(name), branch1, branch2);
    }

    private BinaryOp(final Metadata metadata, final Op2 operator, final AnnotatedTree<?> branch1, final AnnotatedTree<?> branch2) {
        super(metadata, branch1, branch2);
        Objects.requireNonNull(branch1);
        Objects.requireNonNull(branch2);
        op = operator;
    }

    @Override
    public AnnotatedTree<Object> copy() {
        final List<AnnotatedTree<?>> branches = deepCopyBranches();
        return new BinaryOp(getMetadata(), op, branches.get(0), branches.get(1));
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        projectAndEval(context);
        setAnnotation(op.run(getBranch(0).getAnnotation(), getBranch(1).getAnnotation()));
    }

    @Override
    public String getName() {
        return op.toString();
    }

    @Override
    public String toString() {
        return stringFor(getBranch(0)) + ' ' + getName() + ' ' + stringFor(getBranch(1));
    }

}
