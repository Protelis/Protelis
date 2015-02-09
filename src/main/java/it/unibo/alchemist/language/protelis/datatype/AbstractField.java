/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis.datatype;

import java.util.Objects;
import java.util.function.BinaryOperator;

import it.unibo.alchemist.model.interfaces.INode;

import org.danilopianini.lang.HashUtils;
import org.danilopianini.lang.Pair;

public abstract class AbstractField implements Field {

	private static final long serialVersionUID = 7507440716878809781L;

	public INode<Object> reduceKeys(final BinaryOperator<INode<Object>> op, final INode<Object> exclude) {
		return reduce(nodeIterator(), op, exclude, null);
	}
	
	public Object reduceVals(final BinaryOperator<Object> op, final INode<Object> exclude, final Object defaultVal) {
		return reduce(valIterator(), op, exclude == null ? null : getSample(exclude), defaultVal);
	}
	
	public Pair<INode<Object>, Object> reducePairs(final BinaryOperator<Pair<INode<Object>, Object>> accumulator, final INode<Object> exclude) {
		return reduce(coupleIterator(), accumulator, exclude == null ? null : new Pair<>(exclude, getSample(exclude)), null);
	}
	
	protected static <T> T reduce(final Iterable<T> c, final BinaryOperator<T> op, final T exclude, final T defaultVal) {
		Objects.requireNonNull(c);
		Objects.requireNonNull(op);
		boolean filter = exclude != null;
		T result = defaultVal;
		for(final T el: c) {
			if (filter && el.equals(exclude)) {
				filter = false;
			} else {
				result = op.apply(result, el);
			}
		}
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("[ ");
		for(final Pair<INode<Object>, Object> entry: coupleIterator()) {
			sb.append(entry);
			sb.append(' ');
		}
		sb.append(']');
		return sb.toString();
	}
	
	@Override
	public boolean equals(final Object o) {
		if(HashUtils.pointerEquals(this, o)) {
			return true;
		}
		if(o instanceof Field) {
			final Field cmp = (Field) o;
			if (cmp.size() == size()) {
				for(final Pair<INode<Object>, Object> pv: coupleIterator()) {
					if( !pv.getSecond().equals(cmp.getSample(pv.getFirst()))) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	};
	
	@Override
	public int hashCode() {
		int[] hash = new int[size()];
		int i=0;
		for(final Pair<INode<Object>, Object> pv: coupleIterator()) {
			hash[i++] = pv.hashCode();
		}
		return HashUtils.djb2int32(hash);
	};
	
}
