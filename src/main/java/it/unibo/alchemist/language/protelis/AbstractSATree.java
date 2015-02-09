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

public abstract class AbstractSATree<S, T> extends AbstractAnnotatedTree<T> implements SuperscriptedAnnotatedTree<S, T> {

	private static final long serialVersionUID = 457607604000217166L;
	private S superscript;

	protected AbstractSATree(AnnotatedTree<?>... branches) {
		super(branches);
	}

	protected AbstractSATree(List<AnnotatedTree<?>> branches) {
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

	protected final void setSuperscript(S obj) {
		superscript = obj;
	}

	protected String asString() {
		if (isErased()) {
			return "[" + innerAsString() + "]^º";
		}
		return "[" + innerAsString() + "]^[" + superscript + "]";
	}

	protected abstract String innerAsString();

}
