/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.interfaces.SuperscriptedAnnotatedTree;

import java.util.List;

/**
 * @author Danilo Pianini
 *
 * @param <S> Superscript type
 * @param <T> Annotation type
 */
public abstract class AbstractSATree<S, T> extends AbstractAnnotatedTree<T> implements SuperscriptedAnnotatedTree<S, T> {

	private static final long serialVersionUID = 457607604000217166L;
	private S superscript;

	/**
	 * @param branches branches of this {@link AbstractSATree}
	 */
	protected AbstractSATree(final AnnotatedTree<?>... branches) {
		super(branches);
	}

	/**
	 * @param branches branches of this {@link AbstractSATree}
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
	public S getSuperscript() {
		return superscript;
	}

	/**
	 * @param obj the new superscript
	 */
	protected final void setSuperscript(final S obj) {
		superscript = obj;
	}

	@Override
	protected String asString() {
		if (isErased()) {
			return "[" + innerAsString() + "]^~";
		}
		return "[" + innerAsString() + "]^[" + superscript + "]";
	}

	/**
	 * @return a string representation of the node
	 */
	protected abstract String innerAsString();

}
