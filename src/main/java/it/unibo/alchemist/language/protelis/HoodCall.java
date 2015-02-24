/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import gnu.trove.list.TByteList;
import gnu.trove.map.TIntObjectMap;
import it.unibo.alchemist.language.protelis.datatype.Field;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.model.interfaces.INode;

import java.util.Locale;
import java.util.Map;

/**
 * @author Danilo Pianini
 *
 */
public class HoodCall extends AbstractAnnotatedTree<Object> {
	
	private static final long serialVersionUID = -4925767634715581329L;
	private final HoodOp f;
	private final AnnotatedTree<Field> body;
	private final boolean inclusive;

	/**
	 * @param arg the argument to evaluate (must return a {@link Field}).
	 * @param func the {@link HoodOp} to apply
	 * @param includeSelf if true, sigma won't be excluded
	 */
	public HoodCall(final AnnotatedTree<Field> arg, final HoodOp func, final boolean includeSelf) {
		super(arg);
		body = arg;
		f = func;
		inclusive = includeSelf;
	}
	
	@Override
	public AnnotatedTree<Object> copy() {
		return new HoodCall(body.copy(), f, inclusive);
	}

	@Override
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		evalEveryBranchWithProjection(sigma, theta, gamma, lastExec, newMap, currentPosition);
		setAnnotation(f.run(body.getAnnotation(), inclusive ? null : sigma));
	}

	@Override
	protected String asString() {
		return f.toString().toLowerCase(Locale.US) + "Hood( " + body.toString() + " )";
	}
	
}
