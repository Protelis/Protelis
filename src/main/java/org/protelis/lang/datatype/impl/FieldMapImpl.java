/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.datatype.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.math3.util.Pair;
import org.protelis.lang.datatype.DeviceUID;

/**
 * Field implementation based on neighbor/value pairs stored in a hash table.
 */
public class FieldMapImpl extends AbstractField {

    private static final long serialVersionUID = -2947000086262191216L;
    private final Map<DeviceUID, Object> fieldContents;

    /**
     * @param size
     *            The initial size of the hash table used internally to
     *            implement the field.
     * @param loadFactor
     *            The load factor of the hash table used internally to implement
     *            the field.
     */
    public FieldMapImpl(final int size, final float loadFactor) {
        super();
        fieldContents = new LinkedHashMap<>(size, loadFactor);
    }

    @Override
    public void addSample(final DeviceUID n, final Object v) {
        fieldContents.put(n, v);
    }

    @Override
    public boolean containsNode(final DeviceUID n) {
        return fieldContents.containsKey(n);
    }

    @Override
    public Iterable<Pair<DeviceUID, Object>> coupleIterator() {
        return fieldContents.entrySet().stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue()))
                .collect(() -> new ArrayList<>(size()), (a, e) -> a.add(e), (a1, a2) -> a1.addAll(a2));
    }

    @Override
    public Class<?> getExpectedType() {
        if (fieldContents.isEmpty()) {
            return null;
        }
        return fieldContents.values().iterator().next().getClass();
    }

    @Override
    public Object getSample(final DeviceUID n) {
        return fieldContents.get(n);
    }

    @Override
    public boolean isEmpty() {
        return fieldContents.isEmpty();
    }

    @Override
    public Iterable<DeviceUID> nodeIterator() {
        return fieldContents.keySet();
    }

    @Override
    public Object removeSample(final DeviceUID n) {
        return fieldContents.remove(n);
    }

    @Override
    public int size() {
        return fieldContents.size();
    }

    @Override
    public Iterable<Object> valIterator() {
        return fieldContents.values();
    }

}
