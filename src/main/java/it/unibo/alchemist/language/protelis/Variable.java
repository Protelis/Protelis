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
import it.unibo.alchemist.model.interfaces.IEnvironment;
import it.unibo.alchemist.model.interfaces.INode;
import it.unibo.alchemist.utils.FasterString;

import java.util.Map;

/**
 * @author Danilo Pianini
 *
 */
public class Variable extends AbstractAnnotatedTree<Object> {

	private static final long serialVersionUID = -3739014755916345132L;
	private final FasterString name;
	private final INode<Object> node;
	private final IEnvironment<Object> env;
	
	public Variable(final FasterString varName, final INode<Object> host, final IEnvironment<Object> environment) {
		super();
		name = varName;
		node = host;
		env = environment;
	}
	
	public Variable(final String varName, final INode<Object> host, final IEnvironment<Object> environment) {
		this(new FasterString(varName), host, environment);
	}
	
	@Override
	public Variable copy() {
		return new Variable(name, node, env);
	}

	@Override
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		Object val = gamma.get(name);
		if(val == null) {
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
