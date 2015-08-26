/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import java.util.Locale;

import org.protelis.lang.datatype.Field;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.util.HoodOp;
import org.protelis.vm.ExecutionContext;

/**
 *	Reduce a field into a local value by reduction using a {@link HoodOp}.
 */
public class HoodCall extends AbstractAnnotatedTree<Object> {
	
	private static final long serialVersionUID = -4925767634715581329L;
	private final HoodOp function;
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
		function = func;
		inclusive = includeSelf;
	}
	
	@Override
	public AnnotatedTree<Object> copy() {
		return new HoodCall(body.copy(), function, inclusive);
	}

	@Override
	public void eval(final ExecutionContext context) {
		projectAndEval(context);
		setAnnotation(function.run(body.getAnnotation(), inclusive ? null : context.getDeviceUID()));
	}

	@Override
	protected void asString(final StringBuilder sb, final int i) {
		sb.append(function.toString().toLowerCase(Locale.US));
		sb.append("Hood (");
		fillBranches(sb, i, ',');
		sb.append(')');
	}
	
}
