/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis.interfaces;

import gnu.trove.list.TByteList;
import gnu.trove.map.TIntObjectMap;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.model.interfaces.INode;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Danilo Pianini
 *
 */
public interface AnnotatedTree<T> extends Serializable {
	
	T getAnnotation();
	
	/**
	 * Prepares for a new computation round
	 */
	void reset();
	
	/**
	 * |e| operation.
	 */
	void erase();
	
	boolean isErased();
	
	AnnotatedTree<T> copy();
	
	void eval(INode<Object> sigma, TIntObjectMap<Map<CodePath, Object>> theta, Stack gamma, Map<CodePath, Object> lastExec, Map<CodePath, Object> newMap, TByteList currentPosition);

	AnnotatedTree<?> getBranch(int i);
	
	public void toString(StringBuilder sb, int i);
	
}
