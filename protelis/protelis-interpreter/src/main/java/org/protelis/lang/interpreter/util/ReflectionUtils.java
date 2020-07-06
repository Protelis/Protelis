/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.Fields;
import org.protelis.lang.datatype.Unit;
import org.protelis.vm.ExecutionContext;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Primitives;
import com.google.common.util.concurrent.UncheckedExecutionException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

/**
 * Utilities that make easier to cope with Java Reflection.
 */
public final class ReflectionUtils {

    private static final int CACHE_MAX_SIZE = 1000;
    private static final LoadingCache<Triple<Class<?>, String, List<Class<?>>>, Method> METHOD_CACHE = CacheBuilder
            .newBuilder().maximumSize(CACHE_MAX_SIZE).expireAfterAccess(1, TimeUnit.HOURS)
            .build(new CacheLoader<Triple<Class<?>, String, List<Class<?>>>, Method>() {
                @Override
                public Method load(@Nonnull final Triple<Class<?>, String, List<Class<?>>> key) {
                    final List<Class<?>> al = key.getRight();
                    final Class<?>[] args = new Class<?>[al.size()];
                    return loadBestMethod(key.getLeft(), key.getMiddle(), al.toArray(args));
                }
            });
    private static final Map<Class<?>, Function<Number, ? extends Number>> NUMBER_CASTER = ImmutableMap
        .<Class<?>, Function<Number, ? extends Number>>builder()
        .put(Byte.class, Number::byteValue)
        .put(Byte.TYPE, Number::byteValue)
        .put(Short.class, Number::shortValue)
        .put(Short.TYPE, Number::shortValue)
        .put(Integer.class, Number::intValue)
        .put(Integer.TYPE, Number::intValue)
        .put(Long.class, Number::longValue)
        .put(Long.TYPE, Number::longValue)
        .put(Float.class, Number::floatValue)
        .put(Float.TYPE, Number::floatValue)
        .put(Double.class, Number::doubleValue)
        .put(Double.TYPE, Number::doubleValue)
        .build();

    private ReflectionUtils() {
    }

    private static Number castIfNeeded(final Class<?> dest, final Number arg) {
        Objects.requireNonNull(dest);
        Objects.requireNonNull(arg);
        if (dest.isAssignableFrom(arg.getClass())) {
            return arg;
        }
        final Function<Number, ? extends Number> cast = NUMBER_CASTER.get(dest);
        if (cast != null) {
            return cast.apply(arg);
        }
        throw new IllegalStateException("Impossible cast from " + arg.getClass() + " to " + dest);
    }

    /**
     * @param clazz
     *            the class under test
     * @return true if the class is a subclass of {@link Number} or it is a
     *         number having primitive representation in Java
     */
    private static boolean classIsNumber(final Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz) || NUMBER_CASTER.containsKey(clazz);
    }

    private static boolean classIsPrimitive(final Class<?> clazz) {
        return Primitives.allPrimitiveTypes().contains(clazz);
    }

    private static boolean classIsWrapper(final Class<?> clazz) {
        return Primitives.allWrapperTypes().contains(clazz);
    }

    private static boolean compatibleLength(@Nonnull final Method m, final int args, @Nullable final boolean toBeInjected) {
        final Class<?>[] paramTypes = Objects.requireNonNull(m, "Invoked method cannot be null.")
                .getParameterTypes();
        /*
         * The method must be invoked with enough arguments to match at least the count
         * of non-ExecutionContext parameters (except if varargs, in which case one less
         * argument is allowed), and at most the total number of parameters (unless it
         * is varargs, in which case there is no limit)
         */
        final int actualArgsLength = (toBeInjected ? 1 : 0) + args;
        return m.isVarArgs() ? actualArgsLength >= paramTypes.length - 1 : actualArgsLength == paramTypes.length;
    }

    private static boolean compatibleLength(@Nonnull final Method m, final int args, @Nullable final Class<?> firstArgType) {
        return compatibleLength(m, args, willBeInjected(m, firstArgType));
    }

    private static int computePointsForWrapper(final Class<?> primitive, final Class<?> wrapper) {
        final Class<?> wrapped = ClassUtils.primitiveToWrapper(primitive);
        if (wrapped.equals(wrapper)) {
            return 2;
        }
        if (wrapped.isAssignableFrom(wrapper)) {
            return 1;
        }
        return 0;
    }

    private static String formatArguments(final Object[] args) {
        return Arrays.stream(args)
            .map(it -> it + ": " + it.getClass().getSimpleName())
            .collect(Collectors.joining(",", "(", ")"));
    }

    /**
     * Invokes a method. If there are fields involved, field operations are
     * applied
     * 
     * @param context
     *            the current {@link ExecutionContext}
     * @param clazz
     *            the class to search for a method
     * @param methodName
     *            the name of the method
     * @param target
     *            the target object (can be null in case of static invocation)
     * @param args
     *            the arguments for the method
     * @return the result of the method invocation
     */
    public static Object invokeFieldable(
            final ExecutionContext context,
            final Class<?> clazz,
            final String methodName,
            final Object target,
            final Object[] args) {
        if (Field.class.isAssignableFrom(clazz) && target instanceof Field) {
            return invokeFieldable(
                    context,
                    ((Field<?>) target).getExpectedType(),
                    methodName,
                    target,
                    args);
        }
        return invokeFieldable(context, searchBestMethod(clazz, methodName, args), target, args);
    }

    /**
     * Invokes a method. If there are fields involved, field operations are
     * applied
     * 
     * @param context
     *            the current {@link ExecutionContext}
     * @param toInvoke
     *            the method to be invoked
     * @param target
     *            the target object (can be null in case of static invocation)
     * @param args
     *            the arguments for the method
     * @return the result of the method invocation
     */
    public static Object invokeFieldable(
            @Nonnull final ExecutionContext context,
            @Nonnull final Method toInvoke,
            @Nullable final Object target,
            @Nonnull final Object[] args) {
        final boolean toBeInjected = willBeInjected(toInvoke, args);
        if (!compatibleLength(toInvoke, args.length, toBeInjected)) {
            throw new IllegalArgumentException("Number of parameters of " + toInvoke
                    + " does not match the provided array " + Arrays.toString(args));
        }
        final boolean fieldTarget = target instanceof Field;
        final TIntList fieldIndexes = new TIntArrayList(args.length);
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Field
                    && !Field.class.isAssignableFrom(nthArgumentType(toInvoke, toBeInjected ? i + 1 : i))) {
                fieldIndexes.add(i);
            }
        }
        if (fieldTarget || fieldIndexes.size() > 0) {
            return Fields.apply(
                    (actualT, actualA) -> ReflectionUtils.invokeMethod(context, toInvoke, actualT, actualA),
                    fieldTarget,
                    fieldIndexes.toArray(),
                    target,
                    args);
        }
        return ReflectionUtils.invokeMethod(context, toInvoke, target, args);
    }

    @SuppressFBWarnings(value = "REC_CATCH_EXCEPTION", justification = "we need to intercept all runtime events")
    private static Object invokeMethod(
            @Nonnull final ExecutionContext context,
            @Nonnull final Method method,
            @Nullable final Object target,
            @Nonnull final Object[] args) {
        final Object[] useArgs = repackageIfRequired(context, method, args);
        try {
            return invokePossiblyVoidMethod(method, target, useArgs);
        } catch (Exception exc) { // NOPMD: Generic exception caught by purpose
            /*
             * Failure: maybe some cast was required, if arguments were actually passed?
             */
            if (useArgs.length == 0) {
                throw new IllegalStateException(exc);
            }
            final Class<?>[] params = method.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                final Class<?> expected = params[i];
                final Object actual = useArgs[i];
                if (!expected.isAssignableFrom(actual.getClass())
                        && classIsNumber(expected)
                        && actual instanceof Number) {
                    useArgs[i] = castIfNeeded(expected, (Number) actual);
                }
            }
            try {
                return invokePossiblyVoidMethod(method, target, useArgs);
            } catch (IllegalAccessException e) {
                throw new UnsupportedOperationException("Method " + method // NOPMD: false positive
                        + " cannot get invoked because it is not accessible.", e); 
            } catch (IllegalArgumentException e) {
                final boolean isStatic = target == null;
                final String errorMessage = Optional.ofNullable(e.getMessage())
                        .orElse("Probable argument type mismatch")
                    + ": cannot invoke "
                    + method
                    + " with arguments " + formatArguments(useArgs)
                    + (isStatic ? "" : " on " + target);
                throw new UnsupportedOperationException(errorMessage, e); // NOPMD: false positive
            } catch (InvocationTargetException e) {
                final Throwable rootCause = e.getCause();
                final String errorMessage = "Invocation of "
                    + method
                    + (target == null ? "" : " on " + target)
                    + " with arguments " + formatArguments(useArgs)
                    + " failed because of an internal "
                    + (rootCause == null ? "unidentified error" : rootCause.getClass().getSimpleName())
                    + "; please look at the stacktrace for further information";
                throw new UnsupportedOperationException(errorMessage, e); // NOPMD: false positive
            }
        }
    }

    private static Object invokePossiblyVoidMethod(
            @Nonnull final Method method,
            @Nullable final Object target,
            @Nonnull final Object[] args) throws IllegalAccessException, InvocationTargetException {
        final Object result = method.invoke(target, args);
        if (result == null && method.getReturnType().equals(Void.TYPE)) {
            return Unit.UNIT;
        }
        return result;
    }

    private static Method loadBestMethod(final Class<?> clazz, final String methodName, final Class<?>[] argClass) {
        Objects.requireNonNull(clazz, "The class on which the method will be invoked can not be null.");
        Objects.requireNonNull(methodName, "Method name can not be null.");
        Objects.requireNonNull(argClass, "Method arguments can not be null.");
        final Method[] candidates = Arrays.stream(clazz.getMethods())
            // Parameter number
            .filter(m -> compatibleLength(m, argClass.length, argClass.length > 0 ? argClass[0] : null))
            // Method name
            .filter(m -> m.getName().equals(methodName))
            // Only pick accessibile methods, mapping to superclass/interfaces if needed
            .map(MethodUtils::getAccessibleMethod)
            .filter(Objects::nonNull)
            .toArray(Method[]::new);
        if (candidates.length == 0) {
            throw new IllegalArgumentException("No accessible method named " + methodName
                    + " callable with " + Arrays.toString(argClass) + " parameters is available in " + clazz);
        }
        if (candidates.length == 1 && argClass.length == 0) {
            /*
             * In case of 0-arity, the single candidate can be selected directly
             */
            return candidates[0];
        }
        /*
         * Deal with Java method overloading scoring methods
         */
        final List<Pair<Integer, Method>> lm = new ArrayList<>(candidates.length);
        for (final Method m: candidates) {
            final Class<?>[] expectedParameters = m.getParameterTypes();
            final Class<?>[] actualArgClass; // NOPMD: false positive
            if (shouldPushContext(expectedParameters, argClass)) {
                /*
                 * Push "self" as implicit parameter
                 */
                actualArgClass = new Class<?>[argClass.length + 1];
                actualArgClass[0] = ExecutionContext.class;
                System.arraycopy(argClass, 0, actualArgClass, 1, argClass.length);
            } else {
                actualArgClass = argClass;
            }
            boolean compatible = true;
            int p = 0;
            for (int i = 0; compatible && i < actualArgClass.length; i++) {
                final Class<?> expected = nthArgumentType(m, i);
                final Class<?> actual = actualArgClass[i];
                if (actual == null && !classIsPrimitive(expected) || expected.isAssignableFrom(actual)) {
                    /*
                     * No downcast nor coercion required, there is compatibility
                     */
                    p += 3;
                } else if (ExecutionContext.class.isAssignableFrom(expected)) {
                    /*
                     * Expected "self", implicitly loaded
                     */
                    p += 3;
                } else if (classIsPrimitive(expected) && classIsWrapper(actual)) {
                    p += computePointsForWrapper(expected, actual);
                } else if (classIsPrimitive(actual) && classIsWrapper(expected)) {
                    p += computePointsForWrapper(actual, expected);
                } else if (!(classIsNumber(expected) && classIsWrapper(actual))) {
                    /*
                     * At least one is not a number: conversion with precision loss does not apply.
                     */
                    compatible = false;
                }
            }
            if (compatible) {
                /*
                 * Early intercept the case of single candidate
                 */
                if (candidates.length == 1) {
                    return m;
                }
                lm.add(new ImmutablePair<>(p, m));
            }
        }
        /*
         * Find best
         */
        return lm.stream()
                .max(Comparator.comparing(Pair::getKey))
                .map(Pair::getValue)
                .orElseThrow(() -> new IllegalStateException("Method selection for " + methodName
                    + " inside " + clazz
                    + " has been restricted to " + Arrays.toString(candidates)
                    + " however none of them is compatible with arguments " + Arrays.toString(argClass)));
    }

    private static Class<?> nthArgumentType(final Method m, final int n) {
        final Class<?>[] expectedArgs = m.getParameterTypes();
        if (m.isVarArgs() && n >= (expectedArgs.length - 1)) {
            final Class<?> varargType = expectedArgs[expectedArgs.length - 1];
            return varargType.getComponentType();
        } else {
            return expectedArgs[n];
        }
    }

    private static Object[] repackageIfRequired(@Nonnull final ExecutionContext context, @Nonnull final Method m, @Nonnull final Object[] args) {
        final Class<?>[] expectedArgs = m.getParameterTypes();
        final boolean pushContext = shouldPushContext(expectedArgs, args);
        if (m.isVarArgs() || pushContext) {
            // We will repackage into an array of the expected length
            final Object[] newargs = new Object[expectedArgs.length];
            // repackage all the base args
            final int start; // NOPMD: false positive
            if (pushContext) {
                newargs[0] = context;
                start = 1;
            } else {
                start = 0;
            }
            final int copiedArgCount = expectedArgs.length - start - (m.isVarArgs() ? 1 : 0);
            System.arraycopy(args, 0, newargs, start, Math.max(copiedArgCount, 0));
            if (m.isVarArgs()) {
                // Determine how many arguments need repackaging
                final int numVarArgs = args.length - copiedArgCount;
                // Make an array of the appropriate type, then fill it in
                final Class<?> varargType = expectedArgs[copiedArgCount];
                final Object[] vararg = (Object[]) Array.newInstance(varargType.getComponentType(), numVarArgs);
                if (numVarArgs >= 0) {
                    System.arraycopy(args, expectedArgs.length - 1, vararg, 0, numVarArgs);
                }
                // Put the new array in the last argument and return
                newargs[newargs.length - 1] = vararg;
            }
            return newargs;
        } else {
            return args;
        }
    }

    /**
     * @param clazz
     *            the class where to search for suitable methods
     * @param methodName
     *            the method to be invoked
     * @param args
     *            the arguments for the method. If a {@link Field} is passed,
     *            then the expected type of the field is used.
     * @return the result of the invocation, or an {@link IllegalStateException}
     *         if something goes wrong.
     */
    private static Method searchBestMethod(final Class<?> clazz, final String methodName, final List<Object> args) {
        final List<Class<?>> originalClasses = new ArrayList<>(args.size());
        final List<Class<?>> fieldedClasses = new ArrayList<>(args.size());
        boolean atLeastOneField = false;
        for (final Object arg: args) {
            if (arg == null) {
                originalClasses.add(null);
                fieldedClasses.add(null);
            } else {
                final Class<?> argClass = arg.getClass();
                if (arg instanceof Field) {
                    fieldedClasses.add(((Field<?>) arg).getExpectedType());
                    atLeastOneField = true;
                } else {
                    fieldedClasses.add(argClass);
                }
                originalClasses.add(argClass);
            }
        }
        try {
            return METHOD_CACHE.get(new ImmutableTriple<>(clazz, methodName, originalClasses));
        } catch (UncheckedExecutionException | ExecutionException outerException) {
            if (atLeastOneField) {
                try {
                    return METHOD_CACHE.get(new ImmutableTriple<>(clazz, methodName, fieldedClasses));
                } catch (ExecutionException e) {
                    throw new UnsupportedOperationException("No" + methodName + originalClasses // NOPMD: false positive
                            + " nor " + methodName + fieldedClasses + " exist in " + clazz
                            + ".\nYou tried to invoke it with arguments " + args, e);
                }
            }
            final StringBuilder paramClasses = new StringBuilder(originalClasses.toString());
            paramClasses.setCharAt(0, '(');
            paramClasses.setCharAt(paramClasses.length() - 1, ')');
            throw new UnsupportedOperationException(methodName + paramClasses + " does not exist in " + clazz
                    + ".\nYou tried to invoke it with arguments"
                    + args, outerException);
        }
    }

    /**
     * @param clazz
     *            the class where to search for suitable methods
     * @param methodName
     *            the method to be invoked
     * @param args
     *            the arguments for the method
     * @return the result of the invocation, or an {@link IllegalStateException}
     *         if something goes wrong.
     */
    private static Method searchBestMethod(final Class<?> clazz, final String methodName, final Object... args) {
        return searchBestMethod(clazz, methodName, Arrays.asList(args));
    }

    private static boolean shouldPushContext(final Class<?>[] expectedArgs, final Class<?>[] argClass) {
        return shouldPushContext(expectedArgs, argClass.length, argClass.length == 0 ? null : argClass[0]);
    }

    private static boolean shouldPushContext(final Class<?>[] expectedArgs, final int argLength, final Class<?> firstArgClass) {
        return expectedArgs.length == argLength + 1
                && ExecutionContext.class.isAssignableFrom(expectedArgs[0])
                && (firstArgClass == null || !ExecutionContext.class.isAssignableFrom(firstArgClass));
    }

    private static boolean shouldPushContext(final Class<?>[] expectedArgs, final Object[] args) {
        return shouldPushContext(expectedArgs,
                args.length,
                args.length == 0 || args[0] == null ? null : args[0].getClass());
    }

    private static boolean willBeInjected(@Nonnull final Method m, @Nullable final Class<?> firstArgType) {
        return m.getParameterTypes().length > 0
                && ExecutionContext.class.isAssignableFrom(m.getParameterTypes()[0])
                && (firstArgType == null || !ExecutionContext.class.isAssignableFrom(firstArgType));
    }

    private static boolean willBeInjected(@Nonnull final Method m, @Nonnull final Object[] args) {
        return willBeInjected(m, args.length > 0 && args[0] != null ? args[0].getClass() : null);
    }
}
