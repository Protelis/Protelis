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

/**
 * Delta time.
 * 
 * @author Danilo Pianini
 *
 */
public class Dt extends AbstractAnnotatedTree<Double> {

	private static final long serialVersionUID = -583345937082081400L;
	private double lastT;
	
	@Override
	public AnnotatedTree<Double> copy() {
		return new Dt();
	}

	@Override
	public void eval(final ExecutionContext context) {
		final double now = context.getCurrentTime();
		setAnnotation(now - lastT);
		lastT = now;
	}

	@Override
	protected void asString(final StringBuilder sb, final int i) {
		sb.append(sb);
	}

}
