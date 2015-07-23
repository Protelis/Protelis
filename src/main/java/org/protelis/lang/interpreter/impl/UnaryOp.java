/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package org.protelis.lang.interpreter.impl;

import java.util.Objects;

import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.util.Op1;
import org.protelis.vm.ExecutionContext;

/**
 * @author Danilo Pianini
 *
 */
public class UnaryOp extends AbstractAnnotatedTree<Object> {

	private static final long serialVersionUID = 2803028109250981637L;
	private final Op1 op;
	
	/**
	 * @param name
	 *            operator name
	 * @param branch
	 *            the operand
	 */
	public UnaryOp(final String name, final AnnotatedTree<?> branch) {
		this(Op1.getOp(name), branch);
	}
	
	private UnaryOp(final Op1 operator, final AnnotatedTree<?> branch) {
		super(branch);
		Objects.requireNonNull(branch);
		op = operator;
	}

	@Override
	public UnaryOp copy() {
		return new UnaryOp(op, getBranch(0).copy());
	}

	@Override
	public void eval(final ExecutionContext context) {
		projectAndEval(context);
		setAnnotation(op.run(getBranch(0).getAnnotation()));
	}

	@Override
	protected void asString(final StringBuilder sb, final int i) {
		sb.append(op);
		getBranch(0).toString(sb, i);
	}

}
