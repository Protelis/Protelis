/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis.datatype;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import it.unibo.alchemist.language.protelis.util.DeviceUID;
import it.unibo.alchemist.model.interfaces.INode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.danilopianini.lang.HashUtils;
import org.danilopianini.lang.Pair;

/**
 * @author Danilo Pianini
 *
 */
public class FieldTroveMapImpl extends AbstractField implements Serializable {

	private static final long serialVersionUID = -2947000086262191216L;
	private final TLongObjectMap<Pair<DeviceUID, Object>> fld;

	public FieldTroveMapImpl() {
		super();
		fld = new TLongObjectHashMap<>();
	}
	
	public FieldTroveMapImpl(final int size, final float loadFactor) {
		super();
		fld = new TLongObjectHashMap<>(size, loadFactor);
	}
	
	@Override
	public void addSample(final DeviceUID n, final Object v) {
		fld.put(n.getId(), new Pair<>(n, v));
	}

	@Override
	public boolean containsNode(final DeviceUID n) {
		return containsNode(n.getId());
	}

	@Override
	public boolean containsNode(final long n) {
		return fld.containsKey(n);
	}

	@Override
	public Iterable<Pair<DeviceUID, Object>> coupleIterator() {
		return fld.valueCollection();
	}

	@Override
	public boolean equals(final Object o) {
		if (HashUtils.pointerEquals(this, o)) {
			return true;
		}
		if (o instanceof FieldTroveMapImpl) {
			return fld.equals(((FieldTroveMapImpl) o).fld);
		}
		return super.equals(o);
	}

	@Override
	public Class<?> getExpectedType() {
		if (fld.isEmpty()) {
			return null;
		}
		return fld.valueCollection().iterator().next().getSecond().getClass();
	}

	@Override
	public Object getSample(final DeviceUID n) {
		Objects.requireNonNull(n);
		final Pair<DeviceUID, Object> res = fld.get(n.getId());
		if (res == null) {
			throw new NoSuchElementException(n.toString());
		}
		return res.getSecond();
	}

	@Override
	public int hashCode() {
		return fld.hashCode();
	}

	@Override
	public boolean isEmpty() {
		return fld.isEmpty();
	}
	
	@Override
	public Iterable<DeviceUID> nodeIterator() {
		return new Iterable<DeviceUID>() {
			@Override
			public Iterator<DeviceUID> iterator() {
				return fld.valueCollection().stream().map(e -> e.getFirst()).iterator();
			}
		};
	}
	
	@Override
	public Object removeSample(final DeviceUID n) {
		return fld.remove(n.getId());
	}

	@Override
	public int size() {
		return fld.size();
	}

	@Override
	public Iterable<Object> valIterator() {
		return new Iterable<Object>() {
			@Override
			public Iterator<Object> iterator() {
				return fld.valueCollection().stream().map(e -> e.getSecond()).iterator();
			}
		};
	}
}
