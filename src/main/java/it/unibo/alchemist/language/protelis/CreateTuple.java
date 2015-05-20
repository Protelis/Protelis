/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import it.unibo.alchemist.language.protelis.datatype.Field;
import it.unibo.alchemist.language.protelis.datatype.Tuple;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.vm.ExecutionContext;

import java.util.List;

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
	public void eval(final ExecutionContext context) {
		final Object[] a = new Object[getBranchesNumber()];
		final TIntList fieldIndexes = new TIntArrayList();
		forEachWithIndex((i, branch) -> {
			context.newCallStackFrame(i.byteValue());
			branch.eval(context);
			context.returnFromCallFrame();
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
