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

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Danilo Pianini
 *
 */
public class BinaryOp extends AbstractAnnotatedTree<Object> {

	private static final long serialVersionUID = 2803028109250981637L;
	private final Op2 op;
	
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
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		evalEveryBranchWithProjection(sigma, theta, gamma, lastExec, newMap, currentPosition);
		setAnnotation(op.run(getBranch(0).getAnnotation(), getBranch(1).getAnnotation()));
	}

	@Override
	protected String asString() {
		return getBranch(0) + op.toString() + getBranch(1);
	}

}
