/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import java.util.Map;

import org.apache.commons.math3.util.Pair;

import gnu.trove.list.TByteList;
import gnu.trove.map.TIntObjectMap;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.model.implementations.nodes.ProtelisNode;
import it.unibo.alchemist.model.interfaces.IEnvironment;
import it.unibo.alchemist.model.interfaces.INode;
import it.unibo.alchemist.model.interfaces.IReaction;
import it.unibo.alchemist.utils.FasterString;
import it.unibo.alchemist.utils.L;
import it.unibo.alchemist.utils.ParseUtils;

/**
 * @author Danilo Pianini
 *
 */
public class Eval extends AbstractAnnotatedTree<Object> {

	private static final long serialVersionUID = 8811510896686579514L;
	private static final byte DYN_CODE_INDEX = -1;
	private final IEnvironment<Object> env;
	private final ProtelisNode node;
	private final IReaction<Object> reaction;
	
	/**
	 * @param arg argument whose annotation will be used as a string representing a program
	 * @param e environment
	 * @param n node
	 * @param r reaction
	 */
	public Eval(final AnnotatedTree<?> arg, final IEnvironment<Object> e, final ProtelisNode n, final IReaction<Object> r) {
		super(arg);
		env = e;
		node = n;
		reaction = r;
	}
	
	@Override
	public Eval copy() {
		return new Eval(deepCopyBranches().get(0), env, node, reaction);
	}

	@Override
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		evalEveryBranchWithProjection(sigma, theta, gamma, lastExec, newMap, currentPosition);
		final String program = getBranch(0).getAnnotation().toString();
		try {
			final Pair<AnnotatedTree<?>, Map<FasterString, FunctionDefinition>> result = ParseUtils.parse(env, node, reaction, program);
			gamma.push();
			gamma.putAll(result.getSecond());
			final AnnotatedTree<?> toRun = result.getFirst();
			currentPosition.add(DYN_CODE_INDEX);
			toRun.eval(sigma, theta, gamma, lastExec, newMap, currentPosition);
			setAnnotation(toRun.getAnnotation());
			removeLast(currentPosition);
			gamma.pop();
		} catch (IllegalArgumentException e) {
			L.error(e);
			throw new IllegalStateException("The following program can't be parsed:\n" + program, e);
		}
	}

	@Override
	protected void asString(final StringBuilder sb, final int i) {
		sb.append("eval(\n");
		getBranch(0).toString(sb, i + 1);
		sb.append(')');
	}

}
