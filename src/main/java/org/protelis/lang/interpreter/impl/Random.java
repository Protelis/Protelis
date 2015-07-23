/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package org.protelis.lang.interpreter.impl;

import org.protelis.vm.ExecutionContext;


/**
 * @author Danilo Pianini
 *
 */
public class Random extends AbstractAnnotatedTree<Double> {

	private static final long serialVersionUID = -5050040892058340950L;
	
	@Override
	public void eval(final ExecutionContext context) {
		setAnnotation(context.nextRandomDouble());
	}

	@Override
	protected void asString(final StringBuilder sb, final int i) {
		sb.append("random");
	}

	@Override
	public Random copy() {
		return new Random();
	}

}
