package org.protelis.lang.datatype;

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

public class Option<E> {

    private static final Option<Object> EMPTY = new Option<>(null);
    private static final ImmutableMap<String, Boolean> TESTERS = ImmutableMap.of(
            "isPresent", true,
            "isNotPresent", false,
            "isEmpty", false,
            "isAbsent", false);
    private final Optional<E> internal;

    private Option() {
        this(null);
    }

    private Option(E o) {
        internal = Optional.fromNullable(o); 
    }

    public Set<E> asSet() {
        return internal.asSet();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || internal.equals(((Option<?>) obj).internal);
    }

    public Option<E> filter(ExecutionContext ctx, FunctionDefinition test) {
        return runProtelis(ctx, test, value -> {
            if (value instanceof Boolean) {
                boolean condition = (boolean) value;
                if (condition) {
                    return this;
                }
                return empty();
            }
            throw new IllegalStateException("Filter functions must return boolean. Illegal: " + test);
        });
    }

    public Option<E> filterNot(ExecutionContext ctx, FunctionDefinition test) {
        return filter(ctx, test).isEmpty() ? this : empty();
    }

    public Option<E> filter(Predicate<? super E> test) {
        if (isPresent() && test.test(get())) {
            return this;
        }
        return empty();
    }

    public Option<E> filterNot(Predicate<? super E> test) {
        return filter(e -> !test.test(e));
    }

    @SuppressWarnings("unchecked")
    public <X> Option<X> flatMap(ExecutionContext ctx, FunctionDefinition fun) throws IllegalAccessException, InvocationTargetException {
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
                    isPresent = (boolean) tester.invoke(value);
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

    public <X> Option<X> flatMap(Function<? super E, Option<X>> fun) {
        if (isPresent()) {
            return fun.apply(get());
        }
        return empty();
    }

    public E get() {
        if (isPresent()) {
            return internal.get();
        }
        throw new NullPointerException("Cannot access value of empty Option");
    }

    @Override
    public int hashCode() {
        return internal.hashCode();
    }

    public void ifPresent(Consumer<? super E> consumer) {
        if (isPresent()) {
            consumer.accept(get());
        }
    }

    public Object ifPresent(ExecutionContext ctx, FunctionDefinition consumer) {
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

    public Option<Object> map(ExecutionContext ctx, FunctionDefinition mapper) {
        return runProtelis(ctx, mapper, Option::of);
    }

    public <X> Option<X> map(Function<? super E,? extends X> mapper) {
        return flatMap(it -> of(mapper.apply(it)));
    }

    public Object or(ExecutionContext ctx, FunctionDefinition other) {
        return orElseGet(ctx, other);
    }

    public Option<? extends E> or(java.util.Optional<? extends E> other) {
        return isPresent() ? this : other.map(Option::of).orElseGet(Option::empty);
    }

    public Option<? extends E> or(Option<? extends E> other) {
        return isPresent() ? this : other;
    }

    public Option<? extends E> or(Optional<? extends E> other) {
        return isPresent() ? this : of(other.orNull());
    }

    public E or(Supplier<? extends E> other) {
        return orElseGet(other);
    }

    public E orElse(E other) {
        return internal.or(other);
    }

    public Object orElseGet(ExecutionContext ctx, FunctionDefinition other) {
        return ifPresent(ctx, other);
    }

    public E orElseGet(Supplier<? extends E> other) {
        return internal.or(other.get());
    }

    public <X extends RuntimeException> E orElseThrow(Supplier<? extends X> exceptionSupplier) {
        if (isPresent()) {
            return get();
        }
        throw exceptionSupplier.get();
    }

    private <X> Option<X> runProtelis(ExecutionContext ctx, FunctionDefinition fun, Function<Object, Option<X>> converter) {
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

    public Option<Object> transform(ExecutionContext ctx, FunctionDefinition mapper) {
        return map(ctx, mapper);
    }

    public <X> Option<X> transform(Function<? super E,? extends X> mapper) {
        return map(mapper);
    }

    public static <E> Option<E> absent() {
        return empty();
    }

    @SuppressWarnings("unchecked")
    public static <E> Option<E> empty() {
        return (Option<E>) EMPTY;
    }

    public static <E> Option<E> fromNullable(E o) {
        return ofNullable(o);
    }

    public static Object none() {
        return null;
    }

    public static <E> Option<E> of(E o) {
        return new Option<>(o);
    }

    public static <E> Option<E> ofNullable(E o) {
        return of(o);
    }
}
