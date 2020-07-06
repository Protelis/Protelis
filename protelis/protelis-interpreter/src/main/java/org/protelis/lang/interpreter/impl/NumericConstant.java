/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.loading.Metadata;

/**
 * A numerical constant.
 */
public final class NumericConstant extends Constant<Double> {

    private static final long serialVersionUID = 7005881609489257450L;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param number
     *            the number
     */
    public NumericConstant(final Metadata metadata, final String number) {
        this(metadata, Double.parseDouble(number));
    }

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param number
     *            the number
     */
    public NumericConstant(final Metadata metadata, final double number) {
        super(metadata, number);
    }

}
