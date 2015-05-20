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
import it.unibo.alchemist.language.protelis.vm.ExecutionContext;

import java.util.List;

/**
 * @author Danilo Pianini
 *
 */
public class All extends AbstractAnnotatedTree<Object> {
	
	private static final long serialVersionUID = -210610136469863525L;
	private final int last;

	/**
	 * Block of statements.
	 * 
	 * @param statements the statements
	 */
	public All(final List<AnnotatedTree<?>> statements) {
		super(statements);
		last = statements.size() - 1;
	}

	@Override
	public AnnotatedTree<Object> copy() {
		return new All(deepCopyBranches());
	}

	@Override
	public void eval(final ExecutionContext context) {
		context.pushOnVariablesStack();
		evalEveryBranchWithProjection(context);
		context.popOnVariableStack();
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
