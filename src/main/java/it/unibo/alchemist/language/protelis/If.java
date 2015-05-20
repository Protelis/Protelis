/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.vm.ExecutionContext;

/**
 * @author Danilo Pianini
 *
 * @param <T>
 */
public class If<T> extends AbstractAnnotatedTree<T> {

	private static final long serialVersionUID = -4830593657731078743L;
	private static final byte COND = 0, THEN = 1, ELSE = 2;
	private final AnnotatedTree<Boolean> c;
	private final AnnotatedTree<T> t, e;

	/**
	 * @param cond
	 *            condition
	 * @param then
	 *            branch to execute if condition is true (erase otherwise)
	 * @param otherwise
	 *            branch to execute if condition is false (erase otherwise)
	 */
	public If(final AnnotatedTree<Boolean> cond, final AnnotatedTree<T> then, final AnnotatedTree<T> otherwise) {
		super(cond, then, otherwise);
		c = cond;
		t = then;
		e = otherwise;
	}

	@Override
	public AnnotatedTree<T> copy() {
		return new If<>(c.copy(), t.copy(), e.copy());
	}

	@Override
	public void eval(final ExecutionContext context) {
		context.newCallStackFrame(COND);
		c.eval(context);
		context.returnFromCallFrame();
		final Object actualResult = c.getAnnotation();
		final boolean bool = actualResult instanceof Boolean ? c.getAnnotation() : actualResult != null;
		setAnnotation(bool ? choice(THEN, t, e, context) : choice(ELSE, e, t, context));
	}

	private static <T> T choice(final byte branch, final AnnotatedTree<T> selected, final AnnotatedTree<T> erased, final ExecutionContext context) {
		context.newCallStackFrame(branch);
		selected.eval(context);
		context.returnFromCallFrame();
		erased.erase();
		return selected.getAnnotation();
	}

	@Override
	protected void asString(final StringBuilder sb, final int i) {
		sb.append("if (\n");
		c.toString(sb, i + 1);
		sb.append(") {\n");
		t.toString(sb, i + 1);
		sb.append('\n');
		indent(sb, i);
		sb.append("} else {\n");
		e.toString(sb, i + 1);
		sb.append('\n');
		indent(sb, i);
		sb.append('}');
	}

}
