/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.datatype;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.protelis.lang.datatype.impl.LazyField;

import com.google.common.collect.ImmutableMap;

/**
 * A field associates a collection of devices to values.
 * One such device is the local one.
 *
 * @param <T> the data type carried by field values
 */
public interface Field<T> extends Serializable {

    /**
     * Verifies if some device has an entry in this field.
     * Note to interface implementors: the default version of this method is not optimized for performance and should be overriden.
     * 
     * @param id a device UID
     * @return true if there is an entry in this field for the device
     */
    default boolean containsKey(DeviceUID id) {
        return foldExcludingLocal(false, it -> it.getKey().equals(id), Boolean::logicalOr);
    }

    /**
     * Reduction operation with base value (fold), which does not consider the local
     * value, and whose computation has base as initial element.
     * 
     * @param base     base value for the computation
     * @param combiner reduction function, takes two R and returns a single R
     * @return the result of the reduction
     */
    default Map.Entry<DeviceUID, T> foldExcludingLocal(Map.Entry<DeviceUID, T> base, BinaryOperator<Map.Entry<DeviceUID, T>> combiner) {
        return foldExcludingLocal(base, Function.identity(), combiner);
    }

    /**
     * Reduction operation with base value (fold), which does not consider the local
     * value, and whose computation has base as initial element. This is the most
     * generic version, actually performing a map + reduce operation.
     * 
     * @param <R>      Return type
     * @param base     base value for the computation
     * @param mapper   mapping function for pre-processing field entries
     * @param combiner reduction function, takes two R and returns a single R
     * @return the result of the reduction
     */
    default <R> R foldExcludingLocal(
            R base,
            Function<Map.Entry<DeviceUID, T>, R> mapper,
            BinaryOperator<R> combiner
    ) {
        return stream()
            .filter(it -> !it.getKey().equals(getLocalDevice()))
            .reduce(base, (r, t) -> combiner.apply(mapper.apply(t), r), combiner);
    }

    /**
     * Reduction operation with base value (fold), which uses the local value as
     * initial element.
     * 
     * @param combiner reduction function, takes two R and returns a single R
     * @return the result of the reduction
     */
    default Map.Entry<DeviceUID, T> foldIncludingLocal(BinaryOperator<Map.Entry<DeviceUID, T>> combiner) {
        return foldIncludingLocal(Function.identity(), combiner);
    }

    /**
     * Reduction operation with base value (fold), which uses the local value as
     * initial element. This is the most generic version, actually performing a map
     * + reduce operation.
     * 
     * @param <R>      Return type
     * @param mapper   mapping function for pre-processing field entries
     * @param combiner reduction function, takes two R and returns a single R
     * @return the result of the reduction
     */
    default <R> R foldIncludingLocal(
            Function<Map.Entry<DeviceUID, T>, R> mapper,
            BinaryOperator<R> combiner
    ) {
        return stream()
            .map(mapper)
            .reduce(combiner)
            .orElseThrow(() -> new IllegalStateException("Field with no local. This is a bug in Protelis."));
    }

    /**
     * Reduction operation over keys with base value (fold), which does not consider the local
     * key, and whose computation has base as initial element.
     * 
     * @param base     base value for the computation
     * @param combiner reduction function, takes two DeviceUID and returns a single one
     * @return the result of the reduction
     */
    default DeviceUID foldKeysExcludingLocal(DeviceUID base, BinaryOperator<DeviceUID> combiner) {
        return foldExcludingLocal(base, Map.Entry::getKey, combiner);
    }

    /**
     * Reduction operation over keys with base value (fold), which uses the local
     * value as initial element.
     * 
     * @param combiner reduction function, takes two DeviceUID and returns a single
     *                 one
     * @return the result of the reduction
     */
    default DeviceUID foldKeysIncludingLocal(BinaryOperator<DeviceUID> combiner) {
        return foldIncludingLocal(Map.Entry::getKey, combiner);
    }

    /**
     * Reduction operation over values with base value (fold), which does not consider the local
     * key, and whose computation has base as initial element.
     * 
     * @param base     base value for the computation
     * @param combiner reduction function, takes two Ts and returns a single one
     * @return the result of the reduction
     */
    default T foldValuesExcludingLocal(T base, BinaryOperator<T> combiner) {
        return foldExcludingLocal(base, Map.Entry::getValue, combiner);
    }

    /**
     * Reduction operation over values with base value (fold), which uses the local
     * value as initial element.
     * 
     * @param combiner reduction function, takes two Ts and returns a single one
     * @return the result of the reduction
     */
    default T foldValuesIncludingLocal(BinaryOperator<T> combiner) {
        return foldIncludingLocal(Map.Entry::getValue, combiner);
    }

    /**
     * @param id the DeviceUID
     * @return the associated value
     * @throws NoSuchElementException if the field does not have the provided id
     *                                among its keys
     */
    default T get(@Nonnull DeviceUID id) {
        return stream().filter(it -> it.getKey().equals(id)).findFirst()
            .map(Map.Entry::getValue)
            .orElseThrow(() -> new NoSuchElementException("Device " + id + " is not available in field " + this));
    }

    /**
     * @return The type of the values of this field
     */
    @SuppressWarnings("unchecked")
    default Class<? extends T> getExpectedType() {
        return (Class<? extends T>) getLocal().getValue().getClass();
    }

    /**
     * @return the local <DeviceUID, T> entry
     */
    default Map.Entry<DeviceUID, T> getLocal() {
        return new ImmutablePair<>(getLocalDevice(), getLocalValue());
    }

    /**
     * @return the local Device
     */
    DeviceUID getLocalDevice();

    /**
     * @return the value associated with the local device
     */
    default T getLocalValue() {
        return get(getLocalDevice());
    }

    /**
     * @return True if there are no neighbors
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * @return An iterator over the set of neighbor/value pairs
     */
    Iterable<? extends Map.Entry<DeviceUID, T>> iterable();

    /**
     * @return An {@link Iterable} for the field keys
     */
    default Iterable<DeviceUID> keys() {
        return () -> keyStream().iterator();
    }

    /**
     * @return A {@link Stream} for the field keys
     */
    default Stream<DeviceUID> keyStream() {
        return stream().map(Map.Entry::getKey);
    }

    /**
     * Map operation over this field, returning a new field whose values are
     * provided by the mapper function.
     * 
     * @param <R>    data type yield by the resulting field
     * @param mapper mapping function
     * @return a new field whose values are provided by the mapper function
     */
    default <R> Field<R> map(@Nonnull Function<DeviceUID, R> mapper) {
        return new LazyField<>(this, mapper);
    }

    /**
     * Project this field over a restricted domain. If the domain is larger than
     * this field, an exception is thrown.
     * 
     * @param restricted the restricted domain
     * @return a new field with the same values, but with possibly less entries. If
     *         the restriction is operated over the same domain of the current
     *         field, the field itself can be returned.
     */
    default Field<T> projectOn(@Nonnull Field<?> restricted) {
        if (restricted.size() > size()) {
            throw new IllegalArgumentException("Field cannot possibly get extended");
        }
        if (restricted.size() == size()) {
            return this;
        }
        return new LazyField<>(restricted, this::get);
    }

    /**
     * Reduction operation over the field. Ignores the local value and returns an
     * {@link Option}.
     * 
     * @param combiner the reduction function, taking two entries and returning a
     *                 single one.
     * @return An {@link Option} with the computed value, or an empty option if the
     *         field was containing only the local value
     */
    default Option<Map.Entry<DeviceUID, T>> reduce(@Nonnull BinaryOperator<Map.Entry<DeviceUID, T>> combiner) {
        return reduce(Function.identity(), combiner);
    }

    /**
     * Reduction operation over the field. Ignores the local value and returns an
     * {@link Option}. This is the most general version, operating a map + reduce
     * operation.
     * 
     * @param <R> resulting option type
     * @param mapper the mapping function
     * @param combiner the reduction function, taking two Rs and returning a
     *                 single one.
     * @return An {@link Option} with the computed value, or an empty option if the
     *         field was containing only the local value
     */
    default <R> Option<R> reduce(
            @Nonnull Function<Map.Entry<DeviceUID, T>, R> mapper,
            @Nonnull BinaryOperator<R> combiner
    ) {
        return Option.fromJavaUtil(stream()
                .filter(it -> !it.getKey().equals(getLocalDevice()))
                .map(mapper)
                .reduce(combiner));
                //foldExcludingLocal(Option.empty(), a -> Option.of(mapper.apply(a)), (a, b) -> a.merge(b, combiner));
    }

    /**
     * Reduction operation over the field's keys. Ignores the local value and
     * returns an {@link Option}.
     * 
     * @param combiner the reduction function, taking two entries and returning a
     *                 single one.
     * @return An {@link Option} with the computed value, or an empty option if the
     *         field was containing only the local value
     */
    default Option<DeviceUID> reduceKeys(@Nonnull BinaryOperator<DeviceUID> combiner) {
        return reduce(Map.Entry::getKey, combiner);
    }

    /**
     * Reduction operation over the field's values. Ignores the local value and
     * returns an {@link Option}.
     * 
     * @param combiner the reduction function, taking two entries and returning a
     *                 single one.
     * @return An {@link Option} with the computed value, or an empty option if the
     *         field was containing only the local value
     */
    default Option<T> reduceValues(@Nonnull BinaryOperator<T> combiner) {
        return reduce(Map.Entry::getValue, combiner);
    }

    /**
     * The number of neighbors in this field. Hence, a field that only contains the
     * local value has size 0.
     * 
     * Note to Field implementors: the default implementation is inefficient and
     * should be overridden.
     * 
     * @return Number of neighbors with values in the field
     */
    default int size() {
        return (int) stream().count() - 1;
    }

    /**
     * @return A {@link Stream} over the set of neighbor/value pairs
     */
    default Stream<? extends Map.Entry<DeviceUID, T>> stream() {
        return StreamSupport.stream(iterable().spliterator(), false);
    }

    /**
     * @return a map version of the field. The map must not return a mutable view,
     *         namely, changes to this map must not affect the field.
     */
    default Map<DeviceUID, T> toMap() {
        return stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * @return An {@link Iterable} set of values
     */
    default Iterable<T> values() {
        return () -> valueStream().iterator();
    }

    /**
     * @return A {@link Stream} over the set of values
     */
    default Stream<T> valueStream() {
        return stream().map(Map.Entry::getValue);
    }

    /**
     * @param <T> resulting field type
     */
    interface Builder<T> {
        Builder<T> add(DeviceUID key, T value);
        Field<T> build(DeviceUID localKey, T localValue);
    }

}
