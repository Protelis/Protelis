/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import gnu.trove.list.TByteList;
import gnu.trove.map.TIntObjectMap;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.model.interfaces.INode;

import java.util.Map;

/**
 * @author Danilo Pianini
 *
 */
public class If<T> extends AbstractAnnotatedTree<T> {

	private static final long serialVersionUID = -4830593657731078743L;
	private static final byte COND = 0, THEN = 1, ELSE = 2;
	private final AnnotatedTree<Boolean> c;
	private final AnnotatedTree<T> t, e;

	public If(final AnnotatedTree<Boolean> cond, final AnnotatedTree<T> then, final AnnotatedTree<T> otherwise) {
		super(cond, then, otherwise);
		c = cond;
		t = then;
		e = otherwise;
	}

	@Override
	public AnnotatedTree<T> copy() {
		return new If<>(c.copy(), t.copy(), e.copy());
	}

	@Override
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		currentPosition.add(COND);
		c.eval(sigma, theta, gamma, lastExec, newMap, currentPosition);
		removeLast(currentPosition);
		final Object actualResult = c.getAnnotation();
		final boolean bool = actualResult instanceof Boolean ? c.getAnnotation() : actualResult != null;
		setAnnotation(bool ? choice(THEN, t, e, sigma, theta, gamma, lastExec, newMap, currentPosition) : choice(ELSE, e, t, sigma, theta, gamma, lastExec, newMap, currentPosition));
	}

	private static <T> T choice(final byte branch, final AnnotatedTree<T> selected, final AnnotatedTree<T> erased, final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		currentPosition.add(branch);
		selected.eval(sigma, theta, gamma, lastExec, newMap, currentPosition);
		removeLast(currentPosition);
		erased.erase();
		return selected.getAnnotation();
	}

	@Override
	protected String asString() {
		return "if ( " + c + " ) {\n\t" + t + "\n} else {\n\t" + e + "}";
	}

}
