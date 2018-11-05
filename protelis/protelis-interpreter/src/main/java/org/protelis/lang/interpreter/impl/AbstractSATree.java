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
import org.protelis.lang.interpreter.SuperscriptedAnnotatedTree;

/**
 * Basic implementation of a {@link SuperscriptedAnnotatedTree}.
 *
 * @param <S>
 *            Superscript type
 * @param <T>
 *            Annotation type
 */
public abstract class AbstractSATree<S, T> extends AbstractAnnotatedTree<T>
        implements SuperscriptedAnnotatedTree<S, T> {

    private static final long serialVersionUID = 457607604000217166L;
    private S superscript;

    /**
     * @param branches
     *            branches of this {@link AbstractSATree}
     */
    protected AbstractSATree(final AnnotatedTree<?>... branches) {
        super(branches);
    }

    /**
     * @param branches
     *            branches of this {@link AbstractSATree}
     */
    protected AbstractSATree(final List<AnnotatedTree<?>> branches) {
        super(branches);
    }

    @Override
    public final void erase() {
        setSuperscript(null);
        super.erase();
    }

    @Override
    public final S getSuperscript() {
        return superscript;
    }

    /**
     * @param obj
     *            the new superscript
     */
    protected final void setSuperscript(final S obj) {
        superscript = obj;
    }

    @Override
    protected final void asString(final StringBuilder sb, final int indent) {
        if (sb.length() > indent) {
            sb.delete(sb.length() - indent - 1, sb.length() - 1);
        }
        innerAsString(sb, indent);
        sb.append('\n');
        indent(sb, indent + 1);
        sb.append("^^^^^^^^^");
        if (isErased()) {
            sb.append('\n');
            indent(sb, indent + 1);
            sb.append('~');
        } else {
            if (superscript instanceof AbstractAnnotatedTree<?>) {
                sb.append('\n');
                ((AnnotatedTree<?>) superscript).toString(sb, indent + 1);
                sb.append('\n');
                indent(sb, indent + 1);
                sb.append("^^^^^^^^^");
            } else {
                sb.append(superscript);
            }
        }
    }

    /**
     * @param sb
     *            {@link StringBuilder} to fill
     * @param indent
     *            level of indentation
     */
    protected abstract void innerAsString(StringBuilder sb, int indent);
}
