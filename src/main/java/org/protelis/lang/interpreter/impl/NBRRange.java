/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.datatype.Field;
import org.protelis.vm.ExecutionContext;

import it.unibo.alchemist.model.interfaces.IPosition;

/**
 * @author Danilo Pianini
 *
 */
public class NBRRange extends AbstractAnnotatedTree<Field> {

	private static final long serialVersionUID = -4289267098921035028L;
	
	@Override
	public NBRRange copy() {
		return new NBRRange();
	}

	@Override
	public void eval(final ExecutionContext context) {
		final IPosition myPosition = context.getDevicePosition();
		final Field res = context.buildField(pos -> myPosition.getDistanceTo(pos), myPosition);
		setAnnotation(res);
	}

	@Override
	protected void asString(final StringBuilder sb, final int i) {
		sb.append("nbrRange");
	}

}
