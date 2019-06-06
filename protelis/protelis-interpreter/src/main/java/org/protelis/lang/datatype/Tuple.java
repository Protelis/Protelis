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
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import org.protelis.vm.ExecutionContext;

/**
 * Implementation of mathematical tuples as indexed objects T = [element0,
 * element1, element2 ...] .
 */
public interface Tuple extends Iterable<Object>, Serializable, Comparable<Tuple> {

    /**
     * Add an element to the end of a tuple. Equivalent to insert(size,
     * element).
     * 
     * @param element
     *            Element to be added
     * @return A new tuple with equal to the current plus the new element added
     *         to the end
     */
    Tuple append(Object element);

    /**
     * Tests whether the tuple has an element equal to its argument.
     * 
     * @param element
     *            Element to test whether it is equal to any element in the
     *            tuple
     * @return true if the tuple contains an element equal to "element"
     */
    boolean contains(Object element);

    /**
     * Tests whether the tuple has an element equal to its argument.
     * 
     * @param element
     *            Element to test whether it is equal to any element in the
     *            tuple
     * @return true if the tuple contains an element equal to "element"
     */
    boolean containsAll(Iterable<?> element);

    /**
     * Given a tuple and a predicate, returns a new tuple containing only the
     * elements that match the predicate.
     * 
     * @param ctx
     *            the current execution context
     * @param fun
     *            the predicate
     * @return a new tuple containing only the elements that match the predicate
     */
    Tuple filter(ExecutionContext ctx, FunctionDefinition fun);

    /**
     * Given a tuple and a {@link Predicate}, returns a new tuple containing
     * only the elements that match the predicate.
     * 
     * @param fun
     *            the {@link Predicate}
     * @return a new tuple containing only the elements that match the
     *         {@link Predicate}
     */
    Tuple filter(Predicate<Object> fun);

    /**
     * Retrieve the object at one of a tuple's indices.
     * 
     * @param i
     *            Zero-based index of the object to be retrieved
     * @return The object at index i
     */
    Object get(int i);

    /**
     * @return the first element of the tuple if present, throws an exception otherwise
     * 
     * @throws ArrayIndexOutOfBoundsException if there are no elements in the tuple
     */
    Object head();

    /**
     * Searches for an element in a tuple, returning its index if found.
     * 
     * @param element
     *            The object to be searched for
     * @return The first index where the search object is found, or -1 if not
     *         found
     */
    int indexof(Object element);

    /**
     * Add an element to an arbitrary location within a tuple, moving all
     * elements after it down.
     * 
     * @param i
     *            Zero-based index at which the element is to be added
     * @param element
     *            Element to be added
     * @return A new tuple with equal to the current plus the new element added
     *         at the given index
     */
    Tuple insert(int i, Object element);

    /**
     * Set arithmetic: compute intersection of set of tuple elements with set of
     * elements of another tuple.
     * 
     * @param t
     *            tuple to take intersection with
     * @return a new tuple containing elements appearing in both tuples, in
     *         arbitrary order
     */
    Tuple intersection(Tuple t);

    /**
     * @return Returns true iff the size of the tuple is zero
     */
    boolean isEmpty();

    /**
     * Maps a tuple to another same-length tuple by applying a function to each
     * element.
     * 
     * @param ctx
     *            the current execution context
     * @param fun
     *            the function to apply
     * @return a new tuple containing the values
     */
    Tuple map(ExecutionContext ctx, FunctionDefinition fun);

    /**
     * Maps a tuple to another same-length tuple by applying a function to each
     * element.
     * 
     * @param fun
     *            the function to apply
     * @return a new tuple containing the values
     */
    Tuple map(Function<Object, Object> fun);

    /**
     * @param def
     *            the return in case max is not defined
     * @return the biggest element of this tuple, if defined, or default
     *         otherwise
     */
    Object max(Object def);

    /**
     * Append elements of another tuple to the end of this tuple.
     * 
     * @param tuple
     *            tuple to be added
     * @return a new tuple
     */
    Tuple mergeAfter(Tuple tuple);

    /**
     * @param def
     *            the return in case min is not defined
     * @return the smallest element of this tuple, if defined, or default
     *         otherwise
     */
    Object min(Object def);

    /**
     * Add an element to the start of a tuple. Equivalent to insert(0, element).
     * 
     * @param element
     *            Element to be added
     * @return A new tuple with equal to the current plus the new element added
     *         to the start
     */
    Tuple prepend(Object element);

    /**
     * Performs a reduction operation, that maps a tuple to a single value.
     * 
     * @param ctx
     *            the current execution context
     * @param defVal
     *            the value that will be returned if the tuple is empty
     * @param fun
     *            the {@link BinaryOperator} that will be applied to each couple
     *            of values
     * @return a single value of this tuple, or the default value if empty
     */
    Object reduce(ExecutionContext ctx, Object defVal, FunctionDefinition fun);

    /**
     * Performs a reduction operation, that maps a tuple to a single value.
     * 
     * @param defVal
     *            the value that will be returned if the tuple is empty
     * @param fun
     *            the {@link BinaryOperator} that will be applied to each couple
     *            of values
     * @return a single value of this tuple, or the default value if empty
     */
    Object reduce(Object defVal, BinaryOperator<Object> fun);

    /**
     * Replace an element in a tuple.
     * 
     * @param i
     *            Zero-based index at which the element is to be replaced
     * @param element
     *            Element to replace the current element
     * @return A new tuple with the element at the ith index replaced by
     *         "element"
     */
    Tuple set(int i, Object element);

    /**
     * @return Number of elements in the tuple
     */
    int size();

    /**
     * @return a sorted version of this tuple
     */
    Tuple sort();

    /**
     * Set arithmetic: compute which elements in this tuple are not in another
     * tuple.
     * 
     * @param t
     *            tuple to subtract
     * @return a new tuple containing elements not in t, in arbitrary order
     */
    Tuple subtract(Tuple t);

    /**
     * Produces a new Tuple containing the elements between the start and end
     * indices. The start index is inclusive, the end index exclusive.
     * 
     * @param i
     *            start index
     * @param j
     *            end index
     * @return a new tuple containing elements i through j-1
     */
    Tuple subTuple(int i, int j);

    /**
     * Produces a new Tuple containing all elements from i onward. Equivalent to
     * subTuple(i,Tuple.size()).
     * 
     * @param i
     *            First element to take for new tuple
     * @return a new tuple containing elements i through size-1
     */
    Tuple subTupleEnd(int i);

    /**
     * Produces a new Tuple containing the first i elements. Equivalent to
     * subTuple(0,i).
     * 
     * @param i
     *            Number of elements to take
     * @return a new tuple containing elements 0 through i-1
     */
    Tuple subTupleStart(int i);

    /**
     * @return a tuple with all the element of this tuple except the first
     * 
     * @throws ArrayIndexOutOfBoundsException if there are no elements in the tuple
     */
    Tuple tail();

    /**
     * @return an array with a copy of the contents of the tuple
     */
    Object[] toArray();

    /**
     * Set arithmetic: compute union of set of tuple elements with set of
     * elements of another tuple.
     * 
     * @param t
     *            tuple to take union with
     * @return a new tuple containing elements appearing in either tuple, in
     *         arbitrary order
     */
    Tuple union(Tuple t);

    /**
     * For all elements that are tuples, substitute the ith sub-element. For
     * example: [[1,2],3,[4,5],6].unwrap(1) produces [2,3,5,6].
     * 
     * @param i
     *            index of sub-element to substitute
     * @return a new tuple with substituted elements
     */
    Tuple unwrap(int i);
}
