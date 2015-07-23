/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package org.protelis.lang.interpreter.impl;

import java.util.function.Function;

import org.protelis.lang.datatype.Field;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.vm.ExecutionContext;

/**
 * @author Danilo Pianini
 *
 * Implementation of 'nbr' operator
 */
public class NBRCall extends AbstractAnnotatedTree<Field> {

	private static final long serialVersionUID = 5255917527687990281L;
	private static final byte BRANCH = 1;

	/**
	 * @param body
	 *            body of nbr
	 */
	public NBRCall(final AnnotatedTree<?> body) {
		super(body);
	}
	
	@Override
	public NBRCall copy() {
		return new NBRCall(deepCopyBranches().get(0));
	}

	@Override
	public void eval(final ExecutionContext context) {
		final AnnotatedTree<?> branch = getBranch(0);
		context.newCallStackFrame(BRANCH);
		branch.eval(context);
		context.returnFromCallFrame();
		final Object childVal = branch.getAnnotation();
		final Field res = context.buildField(Function.identity(), childVal);
		setAnnotation(res);
	}

	@Override
	protected void asString(final StringBuilder sb, final int i) {
		sb.append("nbr (");
		fillBranches(sb, i, ',');
		sb.append(')');
	}
}
