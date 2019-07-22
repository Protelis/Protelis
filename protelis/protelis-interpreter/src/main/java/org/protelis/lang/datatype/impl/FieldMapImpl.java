/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.datatype.impl;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Field implementation based on neighbor/value pairs stored in an {@link ImmutableMap}.
 *
 * @param <T> field type
 */
public final class FieldMapImpl<T> extends AbstractField<T> {

    private static final long serialVersionUID = -2947000086262191216L;
    @Nonnull
    private final DeviceUID local;
    @Nonnull
    private final ImmutableMap<DeviceUID, T> values;

    private FieldMapImpl(@Nonnull final DeviceUID local, @Nonnull final ImmutableMap<DeviceUID, T> values) {
        this.local = local;
        this.values = values;
    }

    @Override
    public boolean containsKey(final DeviceUID id) {
        return values.containsKey(id);
    }

    @Override
    public DeviceUID getLocalDevice() {
        return local;
    }

    @Override
    public T getLocalValue() {
        return get(getLocalDevice());
    }

    @Override
    public ImmutableSet<? extends Map.Entry<DeviceUID, T>> iterable() {
        return values.entrySet();
    }

    @Override
    public ImmutableSet<DeviceUID> keys() {
        return values.keySet();
    }

    @Override
    public Stream<DeviceUID> keyStream() {
        return values.keySet().stream();
    }

    @Override
    public int size() {
        return values.size() - 1;
    }

    @Override
    public Stream<? extends Map.Entry<DeviceUID, T>> stream() {
        return values.entrySet().stream();
    }

    @Override
    public ImmutableMap<DeviceUID, T> toMap() {
        return values;
    }

    @Override
    public ImmutableCollection<T> values() {
        return values.values();
    }

    @Override
    public Stream<T> valueStream() {
        return values.values().stream();
    }

    /**
     * Builder for an immutable field.
     * 
     * @param <T>
     */
    public static final class Builder<T> implements Field.Builder<T> {

        private final ImmutableMap.Builder<DeviceUID, T> builder = ImmutableMap.builder();
        private boolean consumed;

        @Override
        public Field.Builder<T> add(final DeviceUID key, final T value) {
            builder.put(key, value);
            return this;
        }

        @Override
        public Field<T> build(final DeviceUID localKey, final T localValue) {
            if (consumed) {
                throw new IllegalStateException("A field builder can build only one field");
            }
            consumed = true;
            builder.put(localKey, localValue);
            return new FieldMapImpl<>(localKey, builder.build());
        }
    }
}
