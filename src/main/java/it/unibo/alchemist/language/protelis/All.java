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

/**
 * @author Danilo Pianini
 *
 */
public class All extends AbstractAnnotatedTree<Object> {
	
	private static final long serialVersionUID = -210610136469863525L;
	private final int last;

	public All(final List<AnnotatedTree<?>> statements) {
		super(statements);
		last = statements.size() - 1;
	}

	@Override
	public AnnotatedTree<Object> copy() {
		return new All(deepCopyBranches());
	}

	@Override
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		gamma.push();
		evalEveryBranchWithProjection(sigma, theta, gamma, lastExec, newMap, currentPosition);
		gamma.pop();
		setAnnotation(getBranch(last).getAnnotation());
	}

	@Override
	protected String asString() {
		final StringBuilder sb = new StringBuilder();
		forEach((b) -> {
			sb.append(b);
			sb.append('\n');
		});
		return sb.toString();
	}

}
