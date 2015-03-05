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
import it.unibo.alchemist.utils.FasterString;

import java.util.List;
import java.util.Map;

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
	public AnnotatedTree<T> copy() {
		final List<AnnotatedTree<?>> branches = deepCopyBranches();
		final RepCall<T> res = new RepCall<>(xName, branches.get(W_BRANCH), branches.get(A_BRANCH));
		if (!isErased()) {
			res.setSuperscript(getSuperscript());
			res.setAnnotation(null);
		}
		return res;
	}

	@Override
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		gamma.push();
		if (isErased()) {
			/*
			 * Evaluate the initial value for the field. This is either a variable or a constant, so no projection is required.
			 */
			final AnnotatedTree<?> w = getBranch(W_BRANCH);
			currentPosition.add(W_BRANCH);
			w.eval(sigma, theta, gamma, lastExec, newMap, currentPosition);
			removeLast(currentPosition);
			gamma.put(xName, w.getAnnotation(), true);
		} else {
			gamma.put(xName, getSuperscript(), true);
		}
		final AnnotatedTree<?> body = getBranch(A_BRANCH);
		currentPosition.add(A_BRANCH);
		body.eval(sigma, theta, gamma, lastExec, newMap, currentPosition);
		removeLast(currentPosition);
		gamma.pop();
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
