/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package org.protelis.lang.interpreter.impl;

import org.danilopianini.lang.util.FasterString;
import org.protelis.vm.ExecutionContext;

/**
 * @author Danilo Pianini
 *
 */
public class Variable extends AbstractAnnotatedTree<Object> {

	private static final long serialVersionUID = -3739014755916345132L;
	private final FasterString name;
	
	/**
	 * @param varName
	 *            variable name
	 */
	public Variable(final FasterString varName) {
		super();
		name = varName;
	}
	
	/**
	 * @param varName
	 *            variable name
	 */
	public Variable(final String varName) {
		this(new FasterString(varName));
	}
	
	@Override
	public Variable copy() {
		return new Variable(name);
	}

	@Override
	public void eval(final ExecutionContext context) {
		Object val = context.getVariable(name);
		if (val == null) {
			/*
			 * The variable cannot be read. Most probably, it is some node variable that is not set here. Defaults to false.
			 */
			val = false;
		}
//		if(val instanceof Field) {
//			final CodePath curPath = new CodePath(currentPosition);
//			newMap.put(curPath, val);
//			final Field v = (Field)val;
//			
//			/*
//			 * theta cleaning
//			 */
//			theta.forEachEntry( (nid, cpMap) -> {
//				final INode<Object> node = env.getNodeByID(nid);
//				if(node == null || v.getSample(node) == null) {
//					theta.remove(nid);
//				} else if(!cpMap.containsKey(curPath)) {
//					theta.remove(nid);
//					v.removeSample(node);
//				}
//				return true;
//			});
//			
//			/*
//			 * Restrict variable to aligned fields
//			 */
//			final List<INode<Object>> toRemove = new ArrayList<>(v.size());
//			for(final INode<Object> n: v.nodeIterator()) {
//				if(!theta.containsKey(n.getId())) {
//					toRemove.add(n);
//				}
//			}
//			for(final INode<Object> n: toRemove) {
//				v.removeSample(n);
//			}
//		}
		setAnnotation(val);
	}

	@Override
	protected void asString(final StringBuilder sb, final int i) {
		sb.append(name);
	}

}
