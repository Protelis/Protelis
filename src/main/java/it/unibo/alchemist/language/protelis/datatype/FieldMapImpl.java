/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis.datatype;

import it.unibo.alchemist.language.protelis.util.DeviceUID;
import it.unibo.alchemist.model.interfaces.INode;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.Pair;

/**
 * @author Danilo Pianini
 *
 */
public class FieldMapImpl extends AbstractField {

	private static final long serialVersionUID = -2947000086262191216L;
	private final Map<DeviceUID, Object> fld;

	public FieldMapImpl() {
		super();
		fld = new HashMap<>();
	}
	
	public FieldMapImpl(final int size, final float loadFactor) {
		super();
		fld = new HashMap<>(size, loadFactor);
	}
	
	@Override
	public void addSample(final DeviceUID n, final Object v) {
		fld.put(n, v);
	}

	@Override
	public boolean containsNode(final DeviceUID n) {
		return fld.containsKey(n);
	}

	@Override
	public boolean containsNode(final long n) {
		return fld.keySet().stream().anyMatch(k -> k.getId() == n);
	}

	@Override
	public Iterable<Pair<DeviceUID, Object>> coupleIterator() {
		return fld.entrySet().stream().map(e -> new Pair<>(e.getKey(), e.getValue())).collect(Collectors.toList());
	}

	@Override
	public Class<?> getExpectedType() {
		if(fld.isEmpty()) {
			return null;
		}
		return fld.values().iterator().next().getClass();
	}

	@Override
	public Object getSample(final DeviceUID n) {
		return fld.get(n);
	}

	@Override
	public boolean isEmpty() {
		return fld.isEmpty();
	}

	@Override
	public Iterable<DeviceUID> nodeIterator() {
		return fld.keySet();
	}

	@Override
	public Object removeSample(final DeviceUID n) {
		return fld.remove(n);
	}

	@Override
	public int size() {
		return fld.size();
	}

	@Override
	public Iterable<Object> valIterator() {
		return fld.values();
	}
	
}
