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
import it.unibo.alchemist.language.protelis.datatype.Field;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.model.interfaces.IEnvironment;
import it.unibo.alchemist.model.interfaces.INode;

import java.util.Map;

/**
 * @author Danilo Pianini
 *
 */
public class NBRRange extends AbstractAnnotatedTree<Field> {

	private static final long serialVersionUID = -4289267098921035028L;
	private final IEnvironment<Object> env;
	
	/**
	 * @param environment
	 *            the environment
	 */
	public NBRRange(final IEnvironment<Object> environment) {
		super();
		env = environment;
	}
	
	@Override
	public AnnotatedTree<Field> copy() {
		final NBRRange res = new NBRRange(env);
		return res;
	}

	@Override
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		/*
		 * For the cache hit to happen, my position should remain the same of
		 * the previous execution, and I must have received acks (namely theta
		 * is null)
		 */
		final CodePath currentPath = new CodePath(currentPosition);
		final Field res;
		res = Field.create(theta.size() + 1);
		theta.forEachEntry((nodeId, pathMap) -> {
			if (pathMap.containsKey(currentPath)) {
				final INode<Object> node = env.getNodeByID(nodeId);
				res.addSample(node, env.getDistanceBetweenNodes(sigma, node));
			}
			return true;
		});
		res.addSample(sigma, 0d);
		/*
		 * It is only relevant to put a placeholder signaling that this node
		 * visited this code path. No need to store anything.
		 */
		newMap.put(currentPath, 0);
		setAnnotation(res);
	}

	@Override
	protected void asString(final StringBuilder sb, final int i) {
		sb.append("nbrRange");
	}

}
