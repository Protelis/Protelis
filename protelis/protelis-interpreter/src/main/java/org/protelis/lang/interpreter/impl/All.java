/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import java.util.List;

import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Evaluate a sequence of statements, returning the value of the last statement.
 */
public final class All extends AbstractAnnotatedTree<Object> {

    private static final long serialVersionUID = -210610136469863525L;
    private final int last;

    /**
     * Block of statements.
     * 
     * @param statements
     *            the statements
     */
    public All(final Metadata metadata, final List<AnnotatedTree<?>> statements) {
        super(metadata, statements);
        last = statements.size() - 1;
    }

    @Override
    public AnnotatedTree<Object> copy() {
        return new All(getMetadata(), deepCopyBranches());
    }

    @Override
    public void eval(final ExecutionContext context) {
        if (getBranchesNumber() > 1) {
            /*
             * Prevents the same nbr operation on multiple lines to conflict
             */
            forEachWithIndex((i, b) -> {
                context.newCallStackFrame(i.byteValue());
                b.eval(context);
                /*
                 * Do not return immediately, or the lets won't be available to
                 * further branches.
                 */
            });
            /*
             * Once finished, cleanup the stack
             */
            forEach(b -> context.returnFromCallFrame());
        } else {
            getBranch(last).eval(context);
        }
        setAnnotation(getBranch(last).getAnnotation());
    }

    @Override
    protected void asString(final StringBuilder sb, final int i) {
        if (getBranchesNumber() == 1) {
            getBranch(0).toString(sb, i);
        } else if (getBranchesNumber() > 1) {
            fillBranches(sb, i, ';');
        }
    }

}
