/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis.datatype;

import it.unibo.alchemist.model.interfaces.INode;
import it.unibo.alchemist.utils.L;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.danilopianini.lang.Pair;
import org.danilopianini.lang.TriFunction;

/**
 * @author Danilo Pianini
 *
 */
public interface Field extends Serializable {
	
	static Field create(int defaultSize) {
		return new FieldTroveMapImpl(defaultSize + 1, 1f);
	}
	
	static Field apply(final Object target, final Method m, final boolean fieldTarget, final int[] fieldIndexes, final Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return apply((actualTarget,actualArgs) -> {
			try {
					return m.invoke(actualTarget, actualArgs);
				} catch (Exception e) {
					L.error(e);
				}
			return null;
		}, fieldTarget, fieldIndexes, target, args);
	}

	static Field applyWithSingleParam(UnaryOperator<Object> fun, int[] fields, Object a) {
		return apply((t,p) -> fun.apply(p[0]), false, fields, null, a);
	}

	static Field apply(BinaryOperator<Object> fun, int[] fields, Object a, Object b) {
		return apply((t,p) -> fun.apply(p[0], p[1]), false, fields, null, a, b);
	}

	static Field apply(final TriFunction<Object, Object, Object, Object> fun, int[] fields, Object a, Object b, Object c){
		return apply((t,p) -> fun.apply(p[0], p[1], p[2]), false, fields, null, a, b, c);
	}
	
	static Field apply(final Function<Object[], Object> fun, int[] fields, Object... args){
	    return apply(fun, false, fields, null, args);
	}

	static Field apply(final Function<Object[], Object> fun, final boolean fieldTarget, int[] fields, final Object target, Object... args){
	    return apply((t,p) -> fun.apply(p), fieldTarget, fields, target, args);
	}

	static Field apply(final BiFunction<Object, Object[], Object> fun, final boolean fieldTarget, final int[] fieldIndexes, final Object target, final Object... args) {
		if (fieldIndexes.length == 0) {
			throw new IllegalArgumentException("To use this field application at least one of the parameters must be a field.");
		}
		/*
		 * A consistency check may make sense here.
		 */
		final Field refField = ((Field) args[fieldIndexes[0]]);
		final Field result = create(refField.size());
		for(final INode<Object> node : refField.nodeIterator()){
			final Object actualTarget = fieldTarget ? (((Field)target).getSample(node)) : target;
			Object[] actualArgs = Arrays.copyOf(args, args.length);
			for (final int i : fieldIndexes) {
				final Object arg = ((Field) actualArgs[i]).getSample(node);
				if (arg == null) {
					throw new IllegalStateException("Field " + actualArgs[i] + " is not aligned with " + args[fieldIndexes[0]] + " (missing " + node + ")");
				}
				actualArgs[i] = arg;
			}
			result.addSample(node, fun.apply(actualTarget,actualArgs));
		}
		return result;
	}
	
	void addSample(INode<Object> n, Object v);
	
	Object removeSample(INode<Object> n);
	
	Object getSample(INode<Object> n);
	
	INode<Object> reduceKeys(final BinaryOperator<INode<Object>> op, final INode<Object> exclude);
	
	Object reduceVals(final BinaryOperator<Object> op, final INode<Object> exclude, final Object defaultVal);
	
	Pair<INode<Object>, Object> reducePairs(final BinaryOperator<Pair<INode<Object>, Object>> accumulator, final INode<Object> exclude);

	Iterable<INode<Object>> nodeIterator();

	Iterable<Object> valIterator();
	
	Iterable<Pair<INode<Object>, Object>> coupleIterator();

	int size();

	boolean isEmpty();
	
	boolean containsNode(INode<Object> n);
	
	boolean containsNode(int n);
	
	Class<?> getExpectedType();

	
}
