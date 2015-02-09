/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;


import it.unibo.alchemist.language.protelis.datatype.Field;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.danilopianini.lang.TriFunction;

/**
 * @author Danilo Pianini
 *
 */
public enum Op3 {
	
	MUX("mux", Op3::mux);
	
	private static final int[] BOTH = new int[]{0,1};
	private static final int[] LEFT = new int[]{0};
	private static final int[] RIGHT = new int[]{1};
	private static final int[] NONE = new int[]{};
	private static final Map<String, Op3> MAP = new ConcurrentHashMap<>();
	private final TriFunction<Object, Object, Object, Object> fun;
	private final String n;
	
	Op3(final String name, final TriFunction<Object, Object, Object, Object> function) {
		fun = function;
		n = name;
	}
	
	public Object run(final Object a, final Object b, final Object c) {
		final boolean afield = a instanceof Field;
		final boolean bfield = b instanceof Field;
		final int[] fields = afield && bfield ? BOTH : afield ? LEFT : bfield? RIGHT : NONE;
		if(fields.length > 0) {
			return Field.apply(fun, fields, a, b, c);
		}
		return fun.apply(a, b, c);
	}
	
	@Override
	public String toString() {
		return n;
	}

	public static Op3 getOp(final String name) {
		Op3 op = MAP.get(name);
		if(op == null) {
			op = Arrays.stream(values()).parallel().filter(o -> o.n.equals(name)).findFirst().get();
			MAP.put(name, op);
		}
		return op;
	}
	
	private static Object mux(final Object a, final Object b, final Object c) {
		if(a instanceof Boolean) {
			if((Boolean) a) {
				return b;
			}
			return c;
		} else {
			if(a != null) {
				return b;
			}
			return c;
		}
	}
}
