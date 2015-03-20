/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import gnu.trove.list.TByteList;
import gnu.trove.map.TIntObjectMap;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.model.interfaces.INode;

import java.util.Map;

/**
 * @author Danilo Pianini
 *
 */
public class Self extends AbstractAnnotatedTree<Object> {

	private static final long serialVersionUID = -5050040892058340950L;

	@Override
	public AnnotatedTree<Object> copy() {
		return new Self();
	}

	@Override
	public void eval(
			final INode<Object> sigma,
			final TIntObjectMap<Map<CodePath, Object>> theta,
			final Stack gamma,
			final Map<CodePath, Object> lastExec,
			final Map<CodePath, Object> newMap,
			final TByteList currentPosition) {
		setAnnotation(sigma);
	}

	@Override
	protected void asString(final StringBuilder sb, final int i) {
		sb.append("self");
	}

}
