/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import org.danilopianini.lang.util.FasterString;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.vm.ExecutionContext;

import java.util.List;

/**
 * 
 * @author Danilo Pianini
 *
 * @param <T>
 */
public class RepCall<T> extends AbstractSATree<T, T> {

	private static final long serialVersionUID = 8643287734245198408L;
	private static final byte W_BRANCH = 0;
	private static final byte A_BRANCH = 1;
	private final FasterString xName;
	
	/**
	 * @param varName
	 *            variable name
	 * @param w
	 *            initial value
	 * @param body
	 *            body
	 */
	public RepCall(final FasterString varName, final AnnotatedTree<?> w, final AnnotatedTree<?> body) {
		super(w, body);
		xName = varName;
	}

	@Override
	public RepCall<T> copy() {
		final List<AnnotatedTree<?>> branches = deepCopyBranches();
		final RepCall<T> res = new RepCall<>(xName, branches.get(W_BRANCH), branches.get(A_BRANCH));
		if (!isErased()) {
			res.setSuperscript(getSuperscript());
			res.setAnnotation(null);
		}
		return res;
	}

	@Override
	public void eval(final ExecutionContext context) {
		if (isErased()) {
			/*
			 * Evaluate the initial value for the field. This is either a variable or a constant, so no projection is required.
			 */
			final AnnotatedTree<?> w = getBranch(W_BRANCH);
			context.newCallStackFrame(W_BRANCH);
			w.eval(context);
			context.returnFromCallFrame();
			context.putVariable(xName, w.getAnnotation(), true);
		} else {
			context.putVariable(xName, getSuperscript(), true);
		}
		final AnnotatedTree<?> body = getBranch(A_BRANCH);
		context.newCallStackFrame(A_BRANCH);
		body.eval(context);
		context.returnFromCallFrame();
		@SuppressWarnings("unchecked")
		final T result = (T) body.getAnnotation();
		setAnnotation(result);
		setSuperscript(result);
	}

	@Override
	protected void innerAsString(final StringBuilder sb, final int indent) {
		sb.append("rep (");
		sb.append(xName);
		sb.append(" <- \n");
		getBranch(W_BRANCH).toString(sb, indent + 1);
		sb.append(") {\n");
		getBranch(A_BRANCH).toString(sb, indent + 1);
		sb.append('\n');
		indent(sb, indent);
		sb.append('}');
	}


}
