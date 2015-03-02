/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis.datatype;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.danilopianini.lang.HashUtils;

import com.google.common.collect.Iterators;

/**
 * @author Danilo Pianini
 *
 */
public class ArrayTupleImpl implements Tuple {
	
	private static final long serialVersionUID = 5453783531251313649L;
	private final Object[] a;
	private int hash;
	private String string;

	/**
	 * @param base the elements
	 */
	public ArrayTupleImpl(final Object... base) {
		this(base, true);
	}

	private ArrayTupleImpl(final Object[] base, final boolean copy) {
		a = copy ? Arrays.copyOf(base, base.length) : base;
	}
	
	@Override
	public Iterator<Object> iterator() {
		return Iterators.forArray(a);
	}

	@Override
	public Object get(final int i) {
		return a[(int) i];
	}

	@Override
	public int size() {
		return a.length;
	}

	@Override
	public ArrayTupleImpl subTupleEnd(final int i) {
		return subTuple(i, a.length);
	}

	@Override
	public ArrayTupleImpl subTupleStart(final int i) {
		return subTuple(0, i);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(final Tuple o) {
		int res = 0;
		for (int i = 0; res == 0 && i < a.length; i++) {
			final Object o1 = a[i];
			final Object o2 = o.get(i);
			if (o1 instanceof Comparable && o2 instanceof Comparable) {
				try {
					res = ((Comparable<Object>) o1).compareTo(((Comparable<?>) o2));
				} catch (ClassCastException ex) {
					/*
					 * Uncomparable, go lexicographically
					 */
					res = o1.toString().compareTo(o2.toString());
				}
			} else {
				/*
				 * Fall back to lexicographic comparison
				 */
				return o1.toString().compareTo(o2.toString());
			}
		}
		return res;
	}

	@Override
	public Tuple append(final Object element) {
		final int index = a.length + 1;
		Object[] copy = Arrays.copyOf(a, index);
		copy[index] = element;
		return new ArrayTupleImpl(copy, false);
	}

	@Override
	public Tuple insert(final int i, final Object element) {
		return new ArrayTupleImpl(ArrayUtils.add(a, (int) i, element), false);
	}

	@Override
	public Tuple set(final int i, final Object element) {
		final Object[] copy = Arrays.copyOf(a, a.length);
		copy[(int) i] = element;
		return new ArrayTupleImpl(copy, false);
	}

	@Override
	public ArrayTupleImpl subTuple(final int i, final int j) {
		return new ArrayTupleImpl(ArrayUtils.subarray(a, (int) i, (int) j), false);
	}

	@Override
	public Tuple mergeAfter(final Tuple tuple) {
		if (tuple instanceof ArrayTupleImpl) {
			return new ArrayTupleImpl(ArrayUtils.addAll(a, ((ArrayTupleImpl) tuple).a), false);
		}
		final Object[] copy = new Object[a.length + (int) tuple.size()];
		System.arraycopy(a, 0, copy, 0, a.length);
		for (int i = 0; i < copy.length; i++) {
			copy[i] = tuple.get(i - a.length);
		}
		return new ArrayTupleImpl(copy, false);
	}

	@Override
	public boolean isEmpty() {
		return a.length == 0;
	}

	@Override
	public boolean contains(final Object element) {
		for (int i = 0; i < a.length; i++) {
			if (a[i].equals(element)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Field fcontains(final Field element) {
		// TODO: this is a kludge, and must be removed:
		Field result = new FieldTroveMapImpl();
		((Field) element).coupleIterator().forEach(pair -> {
			result.addSample(pair.getFirst(), contains(pair.getSecond()));
		});
		return result;
	}
	
	@Override
	public String toString() {
		if (string == null) {
			final StringBuilder sb = new StringBuilder();
			sb.append('[');
			for (final Object o : a) {
				final boolean notNumber = !(o instanceof Number || o instanceof Tuple);
				final boolean isString = o instanceof String;
				if (isString) {
					sb.append('"');
				} else if (notNumber) {
					sb.append('\'');
				}
				sb.append(o.toString());
				if (isString) {
					sb.append('"');
				} else if (notNumber) {
					sb.append('\'');
				}
				sb.append(", ");
			}
			if (a.length > 0) {
				sb.delete(sb.length() - 2, sb.length());
			}
			sb.append(']');
			string = sb.toString();
		}
		return string;
	}
	
	@Override
	public boolean equals(final Object o) {
		if (o instanceof ArrayTupleImpl) {
			return Arrays.equals(a, ((ArrayTupleImpl) o).a);
		}
		if (o instanceof Tuple) {
			final Tuple t = (Tuple) o;
			if ((int) t.size() == a.length) {
				for (int i = 0; i < a.length; i++) {
					if (!a[i].equals(t.get(i))) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		if (hash == 0) {
			hash = HashUtils.djb2int32obj(a);
		}
		return hash;
	}

	@Override
	public Tuple unwrap(final int i) {
		return Tuple.create(Arrays.stream(a).map((o) -> {
			if (o instanceof Tuple) {
				return ((Tuple) o).get(i);
			}
			return o;
		}).toArray());
	}

	@Override
	public Tuple union(final Tuple t) {
		final Set<Object> l = new HashSet<>(Arrays.asList(a));
		for (final Object o : t) {
			l.add(o);
		}
		return Tuple.create(l.toArray());
	}

	@Override
	public Tuple intersection(final Tuple t) {
		final Set<Object> l = new HashSet<>();
		final Set<Object> lIn = new HashSet<>(Arrays.asList(a));
		for (final Object o : t) {
			if (lIn.contains(o)) {
				l.add(o);
			}
		}
		return Tuple.create(l.toArray());
	}

	@Override
	public Tuple subtract(final Tuple t) {
		final Set<Object> l = new HashSet<>(Arrays.asList(a));
		for (final Object o : t) {
			l.remove(o);
		}
		return Tuple.create(l.toArray());
	}

}
