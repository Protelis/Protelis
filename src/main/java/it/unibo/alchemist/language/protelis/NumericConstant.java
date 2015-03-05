/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

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
