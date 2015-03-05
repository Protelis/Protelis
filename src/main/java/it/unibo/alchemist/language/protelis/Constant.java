/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import gnu.trove.list.TByteList;
import gnu.trove.map.TIntObjectMap;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.model.interfaces.INode;

import java.util.Map;

/**
 * @author Danilo Pianini
 *
 */
public class Constant<T> extends AbstractAnnotatedTree<T> {
	
	private static final long serialVersionUID = 2101316473738120102L;
	private final T o;
	
	public Constant(final T obj) {
		super();
		o = obj;
	}

	@Override
	public Constant<T> copy() {
		return new Constant<>(o);
	}

	@Override
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		setAnnotation(o);
	}
	
	protected T getInternalObject(){
		return o;
	}

	@Override
	protected void asString(final StringBuilder sb, final int i) {
		sb.append(o);
	}

}
