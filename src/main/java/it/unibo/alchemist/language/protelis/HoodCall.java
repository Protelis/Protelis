/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import gnu.trove.list.TByteList;
import gnu.trove.map.TIntObjectMap;
import it.unibo.alchemist.language.protelis.datatype.Field;
import it.unibo.alchemist.language.protelis.datatype.Tuple;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.model.interfaces.INode;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Danilo Pianini
 * @param <T>
 *
 */
public class HoodCall extends AbstractAnnotatedTree<Object> {
	
	private static final long serialVersionUID = -4925767634715581329L;
	private final HoodOp f;
	private final AnnotatedTree<Field> body;
	private final boolean inclusive;

	public HoodCall(final AnnotatedTree<Field> arg, final HoodOp func, final boolean includeSelf) {
		super(arg);
		body = arg;
		f = func;
		inclusive = includeSelf;
	}
	
	@Override
	public AnnotatedTree<Object> copy() {
		return new HoodCall(body.copy(), f, inclusive);
	}

	@Override
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		evalEveryBranchWithProjection(sigma, theta, gamma, lastExec, newMap, currentPosition);
		setAnnotation(f.run(body.getAnnotation(), inclusive? null : sigma));
	}

	@Override
	protected String asString() {
		return f.toString().toLowerCase()+"Hood( "+body.toString()+")";
	}
	
	public enum HoodOp {
		
		MIN(HoodOp::min, (c) -> {
			if(Number.class.isAssignableFrom(c)) {
				return Double.POSITIVE_INFINITY;
			}
			if(Tuple.class.isAssignableFrom(c)) {
				return Tuple.create(Double.POSITIVE_INFINITY);
			}
			throw new UnsupportedOperationException(c.toString());
		}),
		MAX(HoodOp::max, (c) -> {
			if(Number.class.isAssignableFrom(c)) {
				return Double.NEGATIVE_INFINITY;
			}
			if(Tuple.class.isAssignableFrom(c)) {
				return Tuple.create(Double.NEGATIVE_INFINITY);
			}
			throw new UnsupportedOperationException(c.toString());
		}),
		ANY(HoodOp::any, (c) -> {
			if(Boolean.class.isAssignableFrom(c)) {
				return Boolean.FALSE;
			}
			throw new UnsupportedOperationException(c.toString());
		}),
		ALL(HoodOp::all, (c) -> {
			if(Boolean.class.isAssignableFrom(c)) {
				return Boolean.TRUE;
			}
			throw new UnsupportedOperationException(c.toString());
		}),
		MEAN(HoodOp::mean, (c) -> {
			if(Number.class.isAssignableFrom(c)) {
				return Double.NaN;
			}
			if(Tuple.class.isAssignableFrom(c)) {
				return Tuple.create(Double.NaN);
			}
			throw new UnsupportedOperationException(c.toString());
		}),
		SUM(HoodOp::sum, (c) -> {
			if(Number.class.isAssignableFrom(c)) {
				return 0d;
			}
			if(Tuple.class.isAssignableFrom(c)) {
				return Tuple.create(0d);
			}
			throw new UnsupportedOperationException(c.toString());
		}),
		UNION(HoodOp::union, (c) -> {
			if(Tuple.class.isAssignableFrom(c)) {
				return Tuple.create();
			}
			throw new UnsupportedOperationException(c.toString());
		});
		
		private final BiFunction<Field, INode<Object>, Object> f;
		private final Function<Field, Object> defs;
		
		private HoodOp(final BiFunction<Field, INode<Object>, Object> fun, Function<Class<?>, Object> defaults) {
			f = fun;
			defs = (f) -> {
				if(f.isEmpty()) {
					return defaults.apply(Double.class);
				}
				return defaults.apply(f.getExpectedType());
			};
		}
		
		public static HoodOp get(final String reducer) {
			return Arrays.stream(values()).filter(ho -> ho.name().equalsIgnoreCase(reducer)).findFirst().orElse(null);
		}

		public Object run(final Field o, final INode<Object> n) {
			return f.apply(o, n);
		}
		
		private final static Object min(final Field f, final INode<Object> n) {
			return f.reduceVals(Op2.MIN.getFunction(), n, MIN.defs.apply(f));
		}
		
		private final static Object max(final Field f, final INode<Object> n) {
			return f.reduceVals(Op2.MAX.getFunction(), n, MAX.defs.apply(f));
		}
		
		private final static Object any(final Field f, final INode<Object> n) {
			return f.reduceVals(Op2.OR.getFunction(), n, ANY.defs.apply(f));
		}
		
		private final static Object all(final Field f, final INode<Object> n) {
			return f.reduceVals(Op2.AND.getFunction(), n, ALL.defs.apply(f));
		}
		
		private final static Object sum(final Field f, final INode<Object> n) {
			return f.reduceVals(Op2.PLUS.getFunction(), n, SUM.defs.apply(f));
		}
		
		private final static Object mean(final Field f, final INode<Object> n) {
			if(f.size() == 0) {
				return Double.NaN;
			}
			return Op2.DIVIDE.getFunction().apply(sum(f, n), f.size());
		}
		private final static Object union(final Field f, final INode<Object> n) {
			return f.reduceVals((a,b) -> {return Tuple.union((Tuple)a,(Tuple)b);}, n, UNION.defs.apply(f));
		}
		
	}
	
}
