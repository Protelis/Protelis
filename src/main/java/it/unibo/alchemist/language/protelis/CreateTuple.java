/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
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
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TIntObjectMap;
import it.unibo.alchemist.language.protelis.datatype.Field;
import it.unibo.alchemist.language.protelis.datatype.Tuple;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.model.interfaces.INode;

/**
 * @author Danilo Pianini
 *
 */
public class CreateTuple extends AbstractAnnotatedTree<Object> {
	
	private static final long serialVersionUID = -5018807023306859866L;

	/**
	 * @param args tuple arguments
	 */
	public CreateTuple(final AnnotatedTree<?>... args) {
		super(args);
	}
	
	/**
	 * @param args tuple arguments
	 */
	public CreateTuple(final List<AnnotatedTree<?>> args) {
		super(args);
	}
	
	@Override
	public AnnotatedTree<Object> copy() {
		return new CreateTuple(deepCopyBranches());
	}

	@Override
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		final Object[] a = new Object[getBranchesNumber()];
		final TIntList fieldIndexes = new TIntArrayList();
		forEachWithIndex((i, branch) -> {
			currentPosition.add(i.byteValue());
			branch.eval(sigma, theta, gamma, lastExec, newMap, currentPosition);
			removeLast(currentPosition);
			final Object elem = branch.getAnnotation();
			a[i] = elem;
			if (elem instanceof Field) {
				fieldIndexes.add(i);
			}
		});
		if (fieldIndexes.isEmpty()) {
			setAnnotation(Tuple.create(a));
		} else {
			final Field res = Field.apply(Tuple::create, fieldIndexes.toArray(), a);
			setAnnotation(res);
		}
	}

	@Override
	protected void asString(final StringBuilder sb, final int i) {
		sb.append('[');
		fillBranches(sb, i, ',');
		sb.append(']');
	}

}
