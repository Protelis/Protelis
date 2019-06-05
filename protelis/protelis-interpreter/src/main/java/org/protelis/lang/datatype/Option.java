package org.protelis.lang.datatype;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.protelis.lang.interpreter.util.JavaInteroperabilityUtils;
import org.protelis.vm.ExecutionContext;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

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
public final class Option<E> implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Option<Object> EMPTY_OPTION = new Option<>(null);
    private static final ImmutableMap<String, Boolean> TESTERS = ImmutableMap.of(
            "isPresent", true,
            "isNotPresent", false,
            "isEmpty", false,
            "isAbsent", false);
    private final Optional<E> internal;

    private Option() {
        this(null);
    }

    private Option(final E o) {
        internal = Optional.fromNullable(o); 
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
     * @param ctx  the execution context
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
     * Inverse filter operation using Protelis functions.
     * 
     * @param ctx  the execution context
     * @param test the function used as predicate. Must return boolean.
     * @return If the test passes, the Option is emptied, otherwise, it's left
     *         unchanged.
     */
    public Option<E> filterNot(final ExecutionContext ctx, final FunctionDefinition test) {
        return filter(ctx, test).isEmpty() ? this : empty();
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
     * @throws IllegalAccessException    in case of issues with Java reflection
     * @throws InvocationTargetException in case of error while invoking Java
     *                                   reflectively
     */
    @SuppressWarnings("unchecked")
    public <X> Option<X> flatMap(final ExecutionContext ctx, final FunctionDefinition fun) throws IllegalAccessException, InvocationTargetException {
        runProtelis(ctx, fun, value -> {
            if (value instanceof Option) {
                final Option<?> result = (Option<?>) value;
                if (result.isEmpty()) {
                    return Option.<X>empty();
                }
                return (Option<X>) value;
            } else if (value instanceof Optional) {
                final Optional<?> result = (Optional<?>) value;
                if (result.isPresent()) {
                    return new Option<X>((X) result.get());
                }
                return Option.<X>empty();
            } else if (value instanceof java.util.Optional) {
                final java.util.Optional<?> result = (java.util.Optional<?>) value;
                if (result.isPresent()) {
                    return new Option<X>((X) result.get());
                }
                return Option.<X>empty();
            } else {
                /*
                 * Attempt with structural typing. In case of failure, give up.
                 */
                final List<Method> methods = Arrays.stream(value.getClass().getMethods())
                    .map(MethodUtils::getAccessibleMethod)
                    .filter(it -> it != null)
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
                    isPresent = TESTERS.get(tester.getName()) == (boolean) tester.invoke(value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
                if (isPresent) {
                    final Method getter = methods.stream()
                        .filter(it -> it.getParameterCount() == 0)
                        .filter(it -> "get".equals(it.getName()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No method in " + value.getClass() + " named get with no parameter"));
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

    public void ifPresent(final Consumer<? super E> consumer) {
        if (isPresent()) {
            consumer.accept(get());
        }
    }

    public Object ifPresent(final ExecutionContext ctx, final FunctionDefinition consumer) {
        return runProtelis(ctx, consumer, Option::of)
            .orElseThrow(() -> new IllegalStateException("ifPresent must bind to a valid value. Problematic function: " + consumer));
    }

    public boolean isAbsent() {
        return isEmpty();
    }

    public boolean isEmpty() {
        return !isPresent();
    }

    public boolean isPresent() {
        return internal.isPresent();
    }

    public Option<Object> map(final ExecutionContext ctx, final FunctionDefinition mapper) {
        return runProtelis(ctx, mapper, Option::of);
    }

    public <X> Option<X> map(final Function<? super E,? extends X> mapper) {
        return flatMap(it -> of(mapper.apply(it)));
    }

    public Object or(final ExecutionContext ctx, final FunctionDefinition other) {
        return orElseGet(ctx, other);
    }

    public Option<? extends E> or(final java.util.Optional<? extends E> other) {
        return isPresent() ? this : other.map(Option::of).orElseGet(Option::empty);
    }

    public Option<? extends E> or(final Option<? extends E> other) {
        return isPresent() ? this : other;
    }

    public Option<? extends E> or(final Optional<? extends E> other) {
        return isPresent() ? this : of(other.orNull());
    }

    public E or(final Supplier<? extends E> other) {
        return orElseGet(other);
    }

    public E orElse(final E other) {
        return internal.or(other);
    }

    public Object orElseGet(final ExecutionContext ctx, final FunctionDefinition other) {
        return ifPresent(ctx, other);
    }

    public E orElseGet(final Supplier<? extends E> other) {
        return internal.or(other.get());
    }

    public <X extends RuntimeException> E orElseThrow(final Supplier<? extends X> exceptionSupplier) {
        if (isPresent()) {
            return get();
        }
        throw exceptionSupplier.get();
    }

    private <X> Option<X> runProtelis(final ExecutionContext ctx, final FunctionDefinition fun, final Function<Object, Option<X>> converter) {
        if (fun.getArgNumber() == 1) {
            if (isPresent()) {
                final Object value = JavaInteroperabilityUtils.runProtelisFunctionWithJavaArguments(ctx, fun, ImmutableList.of(get()));
                return converter.apply(value);
            }
            return empty();
        }
        throw new IllegalArgumentException("Protelis function over Option take a single argument. Illegal: " + fun);
    }

    @Override
    public String toString() {
        return isEmpty() ? "Option.None" : "Option.Some(" + internal.get() + ')';
    }

    public Option<Object> transform(final ExecutionContext ctx, final FunctionDefinition mapper) {
        return map(ctx, mapper);
    }

    public <X> Option<X> transform(final Function<? super E,? extends X> mapper) {
        return map(mapper);
    }

    public static <E> Option<E> absent() {
        return empty();
    }

    @SuppressWarnings("unchecked")
    public static <E> Option<E> empty() {
        return (Option<E>) EMPTY_OPTION;
    }

    public static <E> Option<E> fromNullable(final E o) {
        return ofNullable(o);
    }

    public static Object none() {
        return null;
    }

    public static <E> Option<E> of(final E o) {
        return new Option<>(o);
    }

    public static <E> Option<E> ofNullable(final E o) {
        return of(o);
    }

    public java.util.Optional<E> toJavaUtil() {
        return internal.toJavaUtil();
    }
    public Optional<E> toGuava() {
        return internal;
    }
}
