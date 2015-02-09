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
import java.util.Objects;

/**
 * @author Danilo Pianini
 *
 */
public class UnaryOp extends AbstractAnnotatedTree<Object> {

	private static final long serialVersionUID = 2803028109250981637L;
	private final Op1 op;
	
	public UnaryOp(final String name, final AnnotatedTree<?> branch) {
		this(Op1.getOp(name), branch);
	}
	
	private UnaryOp(final Op1 operator, final AnnotatedTree<?> branch) {
		super(branch);
		Objects.requireNonNull(branch);
		op = operator;
	}

	@Override
	public AnnotatedTree<Object> copy() {
		return new UnaryOp(op, getBranch(0).copy());
	}

	@Override
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		evalEveryBranchWithProjection(sigma, theta, gamma, lastExec, newMap, currentPosition);
		setAnnotation(op.run(getBranch(0).getAnnotation()));
	}

	@Override
	protected String asString() {
		return op.toString() + getBranch(0);
	}

}
