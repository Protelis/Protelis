/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import it.unibo.alchemist.language.protelis.datatype.Field;
import it.unibo.alchemist.language.protelis.vm.ExecutionContext;
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
