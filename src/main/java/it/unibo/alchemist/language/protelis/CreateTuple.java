/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import java.util.List;
import java.util.Map;

import gnu.trove.list.TByteList;
import gnu.trove.map.TIntObjectMap;
import it.unibo.alchemist.language.protelis.datatype.Tuple;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.model.interfaces.INode;

/**
 * @author Danilo Pianini
 *
 */
public class CreateTuple extends AbstractAnnotatedTree<Tuple> {
	
	private static final long serialVersionUID = -5018807023306859866L;

	public CreateTuple(AnnotatedTree<?>... args) {
		super(args);
	}
	
	public CreateTuple(List<AnnotatedTree<?>> args) {
		super(args);
	}
	
	@Override
	public AnnotatedTree<Tuple> copy() {
		return new CreateTuple(deepCopyBranches());
	}

	@Override
	public void eval(INode<Object> sigma, TIntObjectMap<Map<CodePath, Object>> theta, Stack gamma, Map<CodePath, Object> lastExec, Map<CodePath, Object> newMap, TByteList currentPosition) {
		Object[] a = new Object[getBranchesNumber()];
		forEachWithIndex((i, branch) -> {
			currentPosition.add(i.byteValue());
			branch.eval(sigma, theta, gamma, lastExec, newMap, currentPosition);
			removeLast(currentPosition);
			a[i] = branch.getAnnotation();
		});
		setAnnotation(Tuple.create(a));
	}

	@Override
	protected String asString() {
		return getBranches().toString();
	}

}
