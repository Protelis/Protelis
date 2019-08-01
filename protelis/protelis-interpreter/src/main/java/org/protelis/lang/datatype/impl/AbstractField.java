/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.datatype.impl;

import java.util.Map;
import java.util.stream.Collectors;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

/**
 * Core functionality for implementing a field that cannot be included in the interface.
 *
 * @param <T> field type
 */
public abstract class AbstractField<T> implements Field<T> {

    private static final long serialVersionUID = 7507440716878809781L;
    private int hash;

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Field) {
            final Field<?> other = (Field<?>) o;
            return size() == other.size()
                && getLocalDevice() == other.getLocalDevice()
                && toMap().equals(other.toMap());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        if (hash == 0) {
            final Hasher hasher = Hashing.murmur3_32().newHasher();
            for (final Map.Entry<DeviceUID, T> pv : iterable()) {
                hasher.putInt(pv.hashCode());
            }
            hash = hasher.hash().asInt();
        }
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString();
    }

}
