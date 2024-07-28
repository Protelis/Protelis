/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.lang.datatype.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Lazy implementation of a Field.
 *
 * @param <T> field type
 */
public final class LazyField<T> extends AbstractField<T> {

    private static final long serialVersionUID = 1L;
    @Nonnull
    private transient LoadingCache<DeviceUID, T> neighbors = CacheBuilder.newBuilder()
            .build(new CacheLoader<>() {
                @Nonnull
                @Override
                public T load(@Nonnull final DeviceUID key) {
                    return mapper.apply(key);
                }
            });
    @Nullable
    private transient Function<DeviceUID, T> mapper;
    @Nonnull
    private final ImmutableCollection<DeviceUID> origin;
    @Nonnull
    private final DeviceUID localDevice;

    /**
     * @param origin the field on which this lazy field is mapping
     * @param mapper the mapping function
     */
    public LazyField(@Nonnull final Field<?> origin, @Nonnull final Function<DeviceUID, T> mapper) {
        final Iterable<DeviceUID> keys = origin.keys();
        this.origin = keys instanceof ImmutableCollection ? (ImmutableCollection<DeviceUID>) keys : ImmutableSet.copyOf(keys);
        this.mapper = mapper;
        this.localDevice = origin.getLocalDevice();
    }

    private boolean isShortCircuited() {
        final boolean result = neighbors.size() == origin.size();
        if (result) {
            /*
             * If all the entries have been computed, evict the mapper and save memory
             */
            mapper = null;
        }
        return result;
    }

    @Override
    public Iterable<? extends Map.Entry<DeviceUID, T>> iterable() {
        if (isShortCircuited()) {
            return Collections.unmodifiableMap(neighbors.asMap()).entrySet();
        }
        return iterateWith(id -> new ImmutablePair<>(id, get(id)));
    }

    @Override
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "The field is immutable")
    public Iterable<DeviceUID> keys() {
        return origin;
    }

    @Override
    public Stream<DeviceUID> keyStream() {
        return origin.stream();
    }

    @Override
    public Stream<T> valueStream() {
        return StreamSupport.stream(values().spliterator(), false);
    }

    @Override
    public Iterable<T> values() {
        if (isShortCircuited()) {
            return Collections.unmodifiableMap(neighbors.asMap()).values();
        }
        return iterateWith(this::get);
    }

    @Override
    public Stream<? extends Map.Entry<DeviceUID, T>> stream() {
        return StreamSupport.stream(iterable().spliterator(), false);
    }

    @Override
    public Optional<T> getIfPresent(@Nonnull final DeviceUID id) {
        final Optional<T> result = Optional.of(neighbors.getUnchecked(id));
        /*
         * Check for cache population completion
         */
        isShortCircuited();
        return result;
    }

    @Nonnull
    @Override
    public DeviceUID getLocalDevice() {
        return localDevice;
    }

    private <R> Iterable<R> iterateWith(@Nonnull final Function<DeviceUID, R> extractor) {
        return () -> new Iterator<>() {
            private final Iterator<DeviceUID> originKeys = keys().iterator();

            @Override
            public boolean hasNext() {
                return originKeys.hasNext();
            }

            @Override
            public R next() {
                return extractor.apply(originKeys.next());
            }
        };
    }

    @Override
    public boolean containsKey(final DeviceUID id) {
        return origin.contains(id);
    }

    @Override
    public int size() {
        return origin.size();
    }

    private void writeObject(final ObjectOutputStream stream) throws IOException, ExecutionException {
        stream.defaultWriteObject();
        stream.writeObject(neighbors.getAll(origin));
    }

    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        @SuppressWarnings("unchecked")
        final ImmutableMap<DeviceUID, T> allValues = (ImmutableMap<DeviceUID, T>) stream.readObject();
        neighbors = CacheBuilder.newBuilder().build(new CacheLoader<>() {
            @Override
            @Nonnull
            @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
            public T load(@Nonnull final DeviceUID key) throws Exception {
                return Objects.requireNonNull(
                    allValues.get(key),
                    "Field broken after de-serialization! Available values: " + allValues + ", requested id: " + key
                );
            }
        });
    }

}
