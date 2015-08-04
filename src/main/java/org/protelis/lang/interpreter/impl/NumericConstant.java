/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

/**
 * @author Danilo Pianini
 *
 */
public class NumericConstant extends Constant<Double> {

	private static final long serialVersionUID = 7005881609489257450L;
	
	/**
	 * @param number
	 *            the number
	 */
	public NumericConstant(final String number) {
		this(Double.parseDouble(number));
	}
	
	/**
	 * @param number
	 *            the number
	 */
	public NumericConstant(final double number) {
		super(number);
	}
	
	@Override
	public NumericConstant copy() {
		return new NumericConstant(getInternalObject());
	}

}
