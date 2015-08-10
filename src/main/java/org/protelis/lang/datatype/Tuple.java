/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.datatype;

import java.io.Serializable;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import org.protelis.lang.datatype.impl.ArrayTupleImpl;
import org.protelis.vm.ExecutionContext;

/**
 * @author Danilo Pianini
 * Implementation of mathematical tuples as indexed objects T = [element0, element1, element2 ...] 
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

	/**
	 * Add an element to the end of a tuple.
	 * Equivalent to insert(size, element).
	 * @param element
	 * 		Element to be added
	 * @return A new tuple with equal to the current plus the new element added to the end
	 */
	Tuple append(Object element);

	/**
	 * Add an element to the start of a tuple.
	 * Equivalent to insert(0, element).
	 * @param element
	 * 		Element to be added
	 * @return A new tuple with equal to the current plus the new element added to the start
	 */
	Tuple prepend(Object element);

	/**
	 * Retrieve the object at one of a tuple's indices.
	 * @param i
	 * 		Zero-based index of the object to be retrieved
	 * @return The object at index i
	 */
	Object get(int i);

	/**
	 * @return Number of elements in the tuple
	 */
	int size();
	
	/**
	 * @return Returns true iff the {@link size} of the tuple is zero
	 */
	boolean isEmpty();

	/**
	 * Tests whether the tuple has an element equal to its argument.
	 * @param element
	 * 		Element to test whether it is equal to any element in the tuple
	 * @return true if the tuple contains an element equal to "element"
	 */
	boolean contains(Object element);

	/**
	 * Searches for an element in a tuple, returning its index if found.
	 * @param element The object to be searched for
	 * @return The first index where the search object is found, or -1 if not found
	 */
	int indexof(Object element);

	/**
	 * Add an element to an arbitrary location within a tuple, moving all elements after it down.
	 * @param i
	 * 		Zero-based index at which the element is to be added
	 * @param element
	 * 		Element to be added
	 * @return A new tuple with equal to the current plus the new element added at the given index
	 */
	Tuple insert(int i, Object element);

	/**
	 * Replace an element in a tuple.
	 * @param i
	 * 		Zero-based index at which the element is to be replaced
	 * @param element
	 * 		Element to replace the current element
	 * @return A new tuple with the element at the ith index replaced by "element"
	 */
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

	Object reduce(ExecutionContext ctx, Object defVal, FunctionDefinition fun);

	Object reduce(Object defVal, BinaryOperator<Object> fun);

	Tuple map(ExecutionContext ctx, FunctionDefinition fun);

	Tuple map(Function<Object, Object> fun);

	Tuple filter(ExecutionContext ctx, FunctionDefinition fun);

	Tuple filter(Predicate<Object> fun);

}
