/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import org.protelis.vm.ExecutionContext;


/**
 * @author Danilo Pianini
 *
 * @param <T>
 */
public class Constant<T> extends AbstractAnnotatedTree<T> {
	
	private static final long serialVersionUID = 2101316473738120102L;
	private final T o;
	
	/**
	 * @param obj the constant to be associated
	 */
	public Constant(final T obj) {
		super();
		o = obj;
	}

	@Override
	public Constant<T> copy() {
		return new Constant<>(o);
	}

	@Override
	public void eval(final ExecutionContext context) {
		if (isErased()) {
			setAnnotation(o);
		}
	}
	
	/**
	 * @return the constant value
	 */
	protected T getInternalObject() {
		return o;
	}

	@Override
	protected void asString(final StringBuilder sb, final int i) {
		sb.append(o);
	}

}
