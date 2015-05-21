/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import it.unibo.alchemist.language.protelis.vm.ExecutionContext;


/**
 * @author Danilo Pianini
 *
 */
public class Self extends AbstractAnnotatedTree<ExecutionContext> {

	private static final long serialVersionUID = -5050040892058340950L;

	@Override
	public Self copy() {
		return new Self();
	}

	@Override
	public void eval(final ExecutionContext context) {
		if (isErased()) {
			setAnnotation(context);
		}
	}

	@Override
	protected void asString(final StringBuilder sb, final int i) {
		sb.append("self");
	}

}
