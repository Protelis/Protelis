/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import static com.google.common.collect.ImmutableList.of;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.apache.commons.math3.util.Pair.create;
import it.unibo.alchemist.language.protelis.datatype.Field;
import it.unibo.alchemist.language.protelis.datatype.Tuple;
import it.unibo.alchemist.model.interfaces.INode;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.math3.util.Pair;

/**
 * @author Danilo Pianini
 *
 */
public enum HoodOp {
	
	/**
	 * Minimum.
	 */
	MIN(HoodOp::min,
		() -> POSITIVE_INFINITY,
		of(create(Number.class, () -> POSITIVE_INFINITY)),
		of(create(Tuple.class, t -> fTup(POSITIVE_INFINITY, (Tuple) t)))
	),
	/**
	 * Maximum.
	 */
	MAX(HoodOp::max,
		() -> NEGATIVE_INFINITY,
		of(create(Number.class, () -> NEGATIVE_INFINITY)),
		of(create(Tuple.class, t -> fTup(NEGATIVE_INFINITY, (Tuple) t)))
	),
	/**
	 * Any value.
	 */
	ANY(HoodOp::any,
		HoodOp::no,
		of(create(Boolean.class, () -> false)),
		of()
	),
	/**
	 * All values.
	 */
	ALL(HoodOp::all,
		HoodOp::no,
		of(create(Boolean.class, () -> true)),
		of()
	),
	/**
	 * Mean of values.
	 */
	MEAN(HoodOp::mean,
		() -> NaN,
		of(create(Number.class, () -> NaN)),
		of(create(Tuple.class, t -> fTup(NaN, (Tuple) t)))
	),
	/**
	 * Sum of values.
	 */
	SUM(HoodOp::sum,
		() -> 0d,
		of(create(Number.class, () -> 0d)),
		of(create(Tuple.class, t -> fTup(0d, (Tuple) t)))
	),
	/**
	 * Union of values.
	 */
	UNION(HoodOp::union,
		Tuple::create,
		of(create(Object.class, Tuple::create)),
		of()
	);
	private final BiFunction<Field, INode<Object>, Object> f;
	private final Function<Field, Object> defs;
	
	/**
	 * @param fun
	 * @param empty
	 *            function that generates a default in case of empty field
	 * @param suppliers
	 *            list of pairs mapping classes to 0-ary functions that provide
	 *            a default
	 * @param cloners
	 *            list of pairs mapping classes to 1-ary functions that, given
	 *            an element of the field as input, provide a comparison. Such
	 *            functions are used in case there is no supplier that can
	 *            provide a specific value-agnostic default
	 */
	private HoodOp(final BiFunction<Field, INode<Object>, Object> fun, final Supplier<Object> empty, final List<Pair<Class<?>, Supplier<Object>>> suppliers, final List<Pair<Class<?>, Function<Object, Object>>> cloners) {
		f = fun;
		defs = (field) -> {
			/*
			 * Field empty: generate a default.
			 */
			if (field.isEmpty()) {
				return empty.get();
			}
			final Class<?> type = field.getExpectedType();
			for (Pair<Class<?>, Supplier<Object>> sup : suppliers) {
				if (sup.getFirst().isAssignableFrom(type)) {
					/*
					 * Field has compatible type
					 */
					return sup.getSecond().get();
				}
			}
			for (Pair<Class<?>, Function<Object, Object>> cloner : cloners) {
				if (cloner.getFirst().isAssignableFrom(type)) {
					return cloner.getSecond().apply(field.valIterator().iterator().next());
				}
			}
			return no(type);
		};
	}
	
	private <T> T no(final Class<?> c) {
		throw new UnsupportedOperationException(this + " cannot compute on " + c);
	}
	
	private static Object no() {
		throw new UnsupportedOperationException("Unsupported operation on empty fields.");
	}
	
	private static Tuple fTup(final Object defVal, final Tuple in) {
		return cTup(defVal, in.size());
	}
	
	private static Tuple cTup(final Object v, final int size) {
		final Object[] r = new Object[size];
		Arrays.fill(r, v);
		return Tuple.create(r);
	}
	
	/**
	 * @param reducer the desired operator
	 * @return the corresponding {@link HoodOp}
	 */
	public static HoodOp get(final String reducer) {
		return Arrays.stream(values()).filter(ho -> ho.name().equalsIgnoreCase(reducer)).findFirst().orElse(null);
	}

	/**
	 * @param o
	 *            the field
	 * @param n
	 *            the node on which the field is sampled
	 * @return the Object resulting in the hood application
	 */
	public Object run(final Field o, final INode<Object> n) {
		return f.apply(o, n);
	}
	
	private static Object min(final Field f, final INode<Object> n) {
		return f.reduceVals(Op2.MIN.getFunction(), n, MIN.defs.apply(f));
	}
	
	private static Object max(final Field f, final INode<Object> n) {
		return f.reduceVals(Op2.MAX.getFunction(), n, MAX.defs.apply(f));
	}
	
	private static Object any(final Field f, final INode<Object> n) {
		return f.reduceVals(Op2.OR.getFunction(), n, ANY.defs.apply(f));
	}
	
	private static Object all(final Field f, final INode<Object> n) {
		return f.reduceVals(Op2.AND.getFunction(), n, ALL.defs.apply(f));
	}
	
	private static Object sum(final Field f, final INode<Object> n) {
		return f.reduceVals(Op2.PLUS.getFunction(), n, SUM.defs.apply(f));
	}
	
	private static Object mean(final Field f, final INode<Object> n) {
		if (f.isEmpty()) {
			return Double.NaN;
		}
		return Op2.DIVIDE.getFunction().apply(sum(f, n), f.size());
	}
	private static Object union(final Field f, final INode<Object> n) {
		return f.reduceVals(
				(a, b) -> {
			final Tuple at = a instanceof Tuple ? (Tuple) a : Tuple.create(a);
			final Tuple bt = b instanceof Tuple ? (Tuple) b : Tuple.create(b);
			return Tuple.union(at, bt);
		},
		n, UNION.defs.apply(f));
	}
	
}


