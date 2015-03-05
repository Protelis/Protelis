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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.danilopianini.lang.TriFunction;

/**
 * @author Danilo Pianini
 *
 */
public enum Op3 {
	
	/**
	 * Functional mux.
	 */
	MUX("mux", Op3::mux);
	
	private static final Map<String, Op3> MAP = new ConcurrentHashMap<>();
	private final TriFunction<Object, Object, Object, Object> fun;
	private final String n;
	
	Op3(final String name, final TriFunction<Object, Object, Object, Object> function) {
		fun = (Serializable & TriFunction<Object, Object, Object, Object>) function;
		n = name;
	}
	
	/**
	 * @param a
	 *            first argument
	 * @param b
	 *            second argument
	 * @param c
	 *            third argument
	 * @return the function applied to the three arguments. Any of them can be a
	 *         {@link Field}.
	 */
	public Object run(final Object a, final Object b, final Object c) {
		final Object[] args = {a, b, c};
		Optional<TIntList> idx = Optional.empty();
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof Field) {
				if (!idx.isPresent()) {
					idx = Optional.of(new TIntArrayList(3));
				}
				idx.get().add(i);
			}
		}
		if (idx.isPresent()) {
			return Field.apply(fun, idx.get().toArray(), a, b, c);
		}
		return fun.apply(a, b, c);
	}
	
	@Override
	public String toString() {
		return n;
	}

	/**
	 * @param name the operation name
	 * @return the corresponding {@link Op3}
	 */
	public static Op3 getOp(final String name) {
		Op3 op = MAP.get(name);
		if (op == null) {
			op = Arrays.stream(values()).parallel().filter(o -> o.n.equals(name)).findFirst().get();
			MAP.put(name, op);
		}
		return op;
	}
	
	private static Object mux(final Object a, final Object b, final Object c) {
		if (a instanceof Boolean) {
			if ((Boolean) a) {
				return b;
			}
			return c;
		} else {
			if (a != null) {
				return b;
			}
			return c;
		}
	}
}
