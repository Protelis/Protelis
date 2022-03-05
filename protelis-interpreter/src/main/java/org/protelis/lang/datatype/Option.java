/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.lang.datatype;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.protelis.vm.ExecutionContext;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.protelis.lang.interpreter.util.JavaInteroperabilityUtils.runProtelisFunctionWithJavaArguments;

/**
 * An immutable object that may contain a non-null reference to another object. Each instance of
 * this type either contains a non-null reference, or contains nothing (in which case we say that
 * the reference is "absent"); it is never said to "contain {@code null}".
 *
 * <p>A non-null {@code Option<E>} reference can be used as a replacement for a nullable {@code T}
 * reference. It allows you to represent "a {@code T} that must be present" and a "a {@code T} that
 * might be absent" as two distinct types in your program, which can aid clarity.
 * 
 * Protelis recommends to import and use Option when interacting with Java methods that may return null.
 *
 * @param <E> the type of instance that can be contained. {@code Option} is naturally covariant on
 *     this type, so it is safe to cast an {@code Option<T>} to {@code Option<S>} for any
 *     supertype {@code S} of {@code T}.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class Option<E> implements Serializable {

    /**
     * Returns the Java null value. To be used ONLY for interacting with Java.
     * Protelis is null-intolerant.
     */
    public static final Object JAVA_NULL = null;
    private static final Option<Object> EMPTY_OPTION = new Option<>(Optional.absent());
    private static final long serialVersionUID = 1L;
    private static final ImmutableMap<String, Boolean> TESTERS = ImmutableMap.of(
            "isPresent", true,
            "isNotPresent", false,
            "isEmpty", false,
            "isAbsent", false);
    private final Optional<E> internal;

    private Option(final E o) {
        this(Optional.fromNullable(o));
    }

    private Option(final java.util.Optional<E> o) {
        this(o.orElse(null));
    }

    private Option(final Optional<E> o) {
        internal = o;
    }

    /**
     * @return a set representation of the Option, for compatibility with {@link Optional}
     */
    public Set<E> asSet() {
        return internal.asSet();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || obj instanceof Option && internal.equals(((Option<?>) obj).internal);
    }

    /**
     * Filter operation using Protelis functions.
     * 
     * @param ctx {@link ExecutionContext}  the execution context
     * @param test the function used as predicate. Must return boolean.
     * @return If the test passes, the Option is unchanged. Otherwise, it's emptied.
     */
    public Option<E> filter(final ExecutionContext ctx, final FunctionDefinition test) {
        return runProtelis(ctx, test, value -> {
            if (value instanceof Boolean) {
                final boolean condition = (boolean) value;
                if (condition) {
                    return this;
                }
                return empty();
            }
            throw new IllegalStateException("Filter functions must return boolean. Illegal: " + test);
        });
    }

    /**
     * @see java.util.Optional#filter(Predicate)
     * 
     * @param test predicate to test
     * @return an {@code Option} describing the value of this {@code Option} if a
     *         value is present and the value matches the given predicate, otherwise
     *         an empty {@code Option}
     */
    public Option<E> filter(final Predicate<? super E> test) {
        if (isPresent() && test.test(get())) {
            return this;
        }
        return empty();
    }

    /**
     * Inverse filter operation using Protelis functions.
     * 
     * @param ctx {@link ExecutionContext}  the execution context
     * @param test the function used as predicate. Must return boolean.
     * @return If the test passes, the Option is emptied, otherwise, it's left
     *         unchanged.
     */
    public Option<E> filterNot(final ExecutionContext ctx, final FunctionDefinition test) {
        return filter(ctx, test).isEmpty() ? this : empty();
    }

    /**
     * Inverse of {@link #filter(Predicate)}.
     * 
     * @see java.util.Optional#filter(Predicate)
     * 
     * @param test predicate to test
     * @return an {@code Option} describing the value of this {@code Option} if a
     *         value is present and the value does not match the given predicate,
     *         otherwise an empty {@code Option}
     */
    public Option<E> filterNot(final Predicate<? super E> test) {
        return filter(e -> !test.test(e));
    }

    /**
     * {@link java.util.Optional#flatMap(Function)} function callable via Protelis.
     * 
     * @param <X> The type parameter to the {@code Optional} returned
     * @param ctx {@link ExecutionContext}
     * @param fun Function to run. Must accept a single parameter and return an
     *            Option, {@link Optional}, {@link java.util.Optional}, or a Java
     *            class with a get() method and one of the following methods
     *            isPresent(), isNotPresent(), isEmpty(), isAbsent():
     * @return the result of applying an {@code Optional}-bearing mapping function
     *         to the value of this {@code Optional}, if a value is present,
     *         otherwise an empty {@code Optional}
     */
    @SuppressWarnings("unchecked")
    public <X> Option<X> flatMap(
        final ExecutionContext ctx,
        final FunctionDefinition fun
    ) {
        runProtelis(ctx, fun, value -> {
            if (value instanceof Option) {
                final Option<?> result = (Option<?>) value;
                if (result.isEmpty()) {
                    return Option.empty();
                }
                return (Option<X>) value;
            } else if (value instanceof Optional) {
                final Optional<?> result = (Optional<?>) value;
                if (result.isPresent()) {
                    return new Option<>((X) result.get());
                }
                return Option.empty();
            } else if (value instanceof java.util.Optional) {
                final java.util.Optional<?> result = (java.util.Optional<?>) value;
                return result.map(o -> new Option<>((X) o)).orElseGet(Option::empty);
            } else {
                /*
                 * Attempt with structural typing. In case of failure, give up.
                 */
                final List<Method> methods = Arrays.stream(value.getClass().getMethods())
                    .map(MethodUtils::getAccessibleMethod)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
                final Method tester = methods.stream()
                    .filter(it -> it.getParameterCount() == 0)
                    .filter(it -> it.getReturnType().equals(Boolean.class) || it.getReturnType().equals(boolean.class))
                    .filter(it -> TESTERS.containsKey(it.getName()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No method in " + value.getClass()
                        + " has name in " + TESTERS.keySet()
                        + ", expects no parameter, and returns boolean."));
                boolean isPresent;
                try {
                    isPresent = Boolean.TRUE.equals(TESTERS.get(tester.getName())) == (boolean) tester.invoke(value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
                if (isPresent) {
                    final Method getter = methods.stream()
                        .filter(it -> it.getParameterCount() == 0)
                        .filter(it -> "get".equals(it.getName()))
                        .findFirst()
                        .orElseThrow(() ->
                            new IllegalStateException("No method in " + value.getClass() + " named get with no parameter")
                        );
                    Object result;
                    try {
                        result = getter.invoke(value);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalStateException(e);
                    }
                    if (result == null) {
                        throw new IllegalStateException(value + " is not empty, but its get() method returned null.");
                    }
                    return new Option<X>((X) result);
                } else {
                    return empty();
                }
            }
        });
        throw new IllegalArgumentException("Flat-mapping function must take one parameter and return Option.");
    }

    /**
     * @see java.util.Optional#flatMap(Function).
     * 
     * @param <X> The type parameter to the {@code Optional} returned
     * @param fun a mapping function to apply to the value, if present the mapping
     *            function
     * @return the result of applying an {@code Optional}-bearing mapping function
     *         to the value of this {@code Optional}, if a value is present,
     *         otherwise an empty {@code Optional}
     */
    public <X> Option<X> flatMap(final Function<? super E, Option<X>> fun) {
        if (isPresent()) {
            return fun.apply(get());
        }
        return empty();
    }

    /**
     * @return the internal value of the {@link Option}, or a {@link RuntimeException} if absent.
     */
    public E get() {
        return internal.get();
    }

    @Override
    public int hashCode() {
        return internal.hashCode();
    }

    /**
     * @return true if the Option has no value
     */
    public boolean isAbsent() {
        return isEmpty();
    }

    /**
     * @return true if the Option has no value
     */
    public boolean isEmpty() {
        return !isPresent();
    }

    /**
     * @return true if the Option is holding a value
     */
    public boolean isPresent() {
        return internal.isPresent();
    }

    /**
     * @param ctx    {@link ExecutionContext}
     * @param mapper a mapping function to apply to the value, if present
     * @return an {@code Option} describing the result of applying a mapping
     *         function to the value of this {@code Option}, if a value is present,
     *         otherwise an empty {@code Option}.
     */
    public Option<Object> map(final ExecutionContext ctx, final FunctionDefinition mapper) {
        return runProtelis(ctx, mapper, Option::of);
    }

    /**
     * @see java.util.Optional#map(Function)
     * 
     * @param <X>    The type of the result of the mapping function
     * @param mapper a mapping function to apply to the value, if present
     * @return an {@code Option} describing the result of applying a mapping
     *         function to the value of this {@code Option}, if a value is present,
     *         otherwise an empty {@code Option}
     */
    public <X> Option<X> map(final Function<? super E, ? extends X> mapper) {
        return flatMap(it -> of(mapper.apply(it)));
    }

    /**
     * If both options are present, the combiner function is executed on the Option
     * contents. Otherwise, if an Option is present, it is returned. Finally, if
     * both are empty, an empty Option is returned.
     * 
     * @param other    the other option
     * @param combiner the combining (reduction) function
     * @return Option.of(combiner(this.get(), other.get()) if both are present, the
     *         only present option iff one is present, an empty option otherwise
     */
    public Option<E> merge(final Option<E> other, final BinaryOperator<E> combiner) {
        if (isPresent()) {
            if (other.isPresent()) {
                return map(it -> combiner.apply(it, other.get()));
            }
            return this;
        }
        return other;
    }

    /**
     * Return the value if present, otherwise return {@code other}.
     *
     * @param other alternative
     * @return this {@code Optional} if it has a value present; {@code other}
     *         otherwise.
     */
    public Option<? extends E> or(final java.util.Optional<? extends E> other) {
        return isPresent() ? this : other.map(Option::of).orElseGet(Option::empty);
    }

    /**
     * Return the value if present, otherwise return {@code other}.
     *
     * @param other alternative
     * @return this {@code Optional} if it has a value present; {@code other}
     *         otherwise.
     */
    public Option<? extends E> or(final Option<? extends E> other) {
        return isPresent() ? this : other;
    }

    /**
     * Return the value if present, otherwise return {@code other}.
     *
     * @param other alternative
     * @return this {@code Optional} if it has a value present; {@code other}
     *         otherwise.
     */
    public Option<? extends E> or(final Optional<? extends E> other) {
        return isPresent() ? this : of(other.orNull());
    }

    /**
     * Return the value if present, otherwise return {@code other}.
     *
     * @param other the value to be returned if there is no value present
     * @return the value, if present, otherwise {@code other}
     */
    public E orElse(final E other) {
        return internal.or(other);
    }

    /**
     * Return the value if present, otherwise invoke {@code other} and return the
     * result of that invocation.
     *
     * @param ctx   {@link ExecutionContext}
     * @param other a 0-ary protelis function
     * @return the value if present otherwise the result of {@code other.get()}
     */
    @SuppressWarnings("unchecked")
    public E orElseGet(final ExecutionContext ctx, final FunctionDefinition other) {
        if (other.getParameterCount() == 0) {
            return internal.or(() -> (E) runProtelisFunctionWithJavaArguments(ctx, other, ImmutableList.of()));
        }
        throw new IllegalArgumentException("Optional supplier function must be 0-ary. Illegal function: " + other);
    }

    /**
     * Return the value if present, otherwise invoke {@code other} and return the
     * result of that invocation.
     *
     * @param other a {@code Supplier} whose result is returned if no value is
     *              present
     * @return the value if present otherwise the result of {@code other.get()}
     */
    public E orElseGet(final Supplier<? extends E> other) {
        return internal.or(other.get());
    }

    /**
     * Return the contained value, if present, otherwise throw an exception
     * to be created by the provided supplier.
     *
     * @param <X> Type of the exception to be thrown
     * @param exceptionSupplier The supplier which will return the exception to
     * be thrown
     * @return the present value
     */
    public <X extends RuntimeException> E orElseThrow(final Supplier<? extends X> exceptionSupplier) {
        if (isPresent()) {
            return get();
        }
        throw exceptionSupplier.get();
    }

    private <X> Option<X> runProtelis(
        final ExecutionContext ctx,
        final FunctionDefinition fun,
        final Function<Object, Option<X>> converter
    ) {
        if (fun.getParameterCount() == 1 || fun.invokerShouldInitializeIt()) {
            if (isPresent()) {
                final Object value = runProtelisFunctionWithJavaArguments(ctx, fun, ImmutableList.of(get()));
                return converter.apply(value);
            }
            return empty();
        }
        throw new IllegalArgumentException("Protelis function over Option takes a single argument. Illegal: " + fun);
    }

    /**
     * @return a Guava compatible view of this Option
     */
    public Optional<E> toGuava() {
        return internal;
    }

    /**
     * @return a {@link java.util.Optional} view of this Option
     */
    public java.util.Optional<E> toJavaUtil() {
        return internal.toJavaUtil();
    }

    @Override
    public String toString() {
        return isEmpty() ? "Option.None" : "Option.Some(" + internal.get() + ')';
    }

    /**
     * @param ctx    {@link ExecutionContext}
     * @param mapper a mapping function to apply to the value, if present
     * @return an {@code Option} describing the result of applying a mapping
     *         function to the value of this {@code Option}, if a value is present,
     *         otherwise an empty {@code Option}.
     */
    public Option<Object> transform(final ExecutionContext ctx, final FunctionDefinition mapper) {
        return map(ctx, mapper);
    }

    /**
     * @see java.util.Optional#map(Function)
     * 
     * @param <X>    The type of the result of the mapping function
     * @param mapper a mapping function to apply to the value, if present
     * @return an {@code Option} describing the result of applying a mapping
     *         function to the value of this {@code Option}, if a value is present,
     *         otherwise an empty {@code Option}
     */
    public <X> Option<X> transform(final Function<? super E, ? extends X> mapper) {
        return map(mapper);
    }

    /**
     * Returns an empty {@code Option} instance.  No value is present for this
     * Optional.
     *
     * @param <E> Type of the non-existent value
     * @return an empty {@code Option}
     */
    public static <E> Option<E> absent() {
        return empty();
    }

    /**
     * Returns an empty {@code Option} instance.  No value is present for this
     * Optional.
     *
     * @param <E> Type of the non-existent value
     * @return an empty {@code Option}
     */
    @SuppressWarnings("unchecked")
    public static <E> Option<E> empty() {
        return (Option<E>) EMPTY_OPTION;
    }

    /**
     * @param <E> option type
     * @param origin the guava Optional
     * @return a Protelis view of the Guava Option
     */
    public static <E> Option<E> fromGuava(final Optional<E> origin) {
        return new Option<>(origin);
    }

    /**
     * @param <E> option type
     * @param origin the Java Optional
     * @return a Protelis view of the Java Option
     */
    public static <E> Option<E> fromJavaUtil(final java.util.Optional<E> origin) {
        return new Option<>(origin);
    }

    /**
     * Returns an {@code Option} describing the specified value, if non-null,
     * otherwise returns an empty {@code Option}.
     *
     * @param <E>   the class of the value
     * @param value the possibly-null value to describe
     * @return an {@code Option} with a present value if the specified value is
     *         non-null, otherwise an empty {@code Option}
     */
    public static <E> Option<E> fromNullable(final E value) {
        return ofNullable(value);
    }

    /**
     * Returns an {@code Option} describing the specified value, if non-null,
     * otherwise returns an empty {@code Option}.
     *
     * @param <E>   the class of the value
     * @param value the possibly-null value to describe
     * @return an {@code Option} with a present value if the specified value is
     *         non-null, otherwise an empty {@code Option}
     */
    public static <E> Option<E> of(final E value) {
        return new Option<>(value);
    }

    /**
     * Returns an {@code Option} describing the specified value, if non-null,
     * otherwise returns an empty {@code Option}.
     *
     * @param <E>   the class of the value
     * @param value the possibly-null value to describe
     * @return an {@code Option} with a present value if the specified value is
     *         non-null, otherwise an empty {@code Option}
     */
    public static <E> Option<E> ofNullable(final E value) {
        return of(value);
    }
}
