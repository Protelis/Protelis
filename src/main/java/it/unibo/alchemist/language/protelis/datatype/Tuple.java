/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis.datatype;

import java.io.Serializable;
import java.util.List;
import java.util.function.BinaryOperator;

/**
 * @author Danilo Pianini
 *
 */
public interface Tuple extends Iterable<Object>, Serializable, Comparable<Tuple> {

	/**
	 * @param l
	 *            the elements
	 * @return a new tuple
	 */
	static Tuple create(final List<?> l) {
		return create(l.toArray());
	}

	/**
	 * @param l the elements
	 * @return a new tuple
	 */
	@SafeVarargs
	static Tuple create(Object... l) {
		return new ArrayTupleImpl(l);
	}

	Tuple append(Object element);

	Object get(int i);

	int size();
	
	boolean isEmpty();

	boolean contains(Object element);
	
	Tuple insert(int i, Object element);

	Tuple set(int i, Object element);

	Tuple subTupleStart(int i);

	Tuple subTupleEnd(int i);

	Tuple subTuple(int i, int j);

	Tuple mergeAfter(Tuple tuple);
	
	Tuple unwrap(int i);
	
	// Set arithmetic for Tuples
	Tuple union(Tuple t);

	Tuple intersection(Tuple t);
	
	Tuple subtract(Tuple t);

	static Tuple union(Tuple t1, Tuple t2) {
		return t1.union(t2);
	}

	static Tuple intersection(Tuple t1, Tuple t2) {
		return t1.intersection(t2);
	}

	static Tuple subtract(Tuple t1, Tuple t2) {
		return t1.subtract(t2);
	}

	static Tuple pairOperation(Tuple t1, Tuple t2, BinaryOperator<Object> fun) {
		final boolean t1Bigger = t1.size() > t2.size();
		final Tuple big = t1Bigger ? t1 : t2;
		final Tuple small = t1Bigger ? t2 : t1;
		final int max = (int) big.size();
		final int min = (int) small.size();
		final Object[] res = new Object[max];
		for (int i = 0; i < max; i++) {
			res[i] = i < min ? fun.apply(t1.get(i), t2.get(i)) : big.get(i);
		}
		return create(res);
	}

	// This is a kludge, and must be removed when we get DotOperator working correctly for fields
	Field fcontains(Field element);

	static boolean scontains(Tuple t1, Object element) {
		return t1.contains(element);
	}
}
