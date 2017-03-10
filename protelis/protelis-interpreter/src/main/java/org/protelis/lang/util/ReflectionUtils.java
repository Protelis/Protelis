/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.util;

import static java8.util.stream.StreamSupport.stream;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.util.Pair;
import org.danilopianini.lang.PrimitiveUtils;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import java8.util.Optional;

/**
 * Utilities that make easier to cope with Java Reflection.
 */
public final class ReflectionUtils {

    private static final Logger L = LoggerFactory.getLogger(ReflectionUtils.class);
    private static final int CACHE_MAX_SIZE = 1000;
    private static final LoadingCache<Triple<Class<?>, String, List<Class<?>>>, Method> METHOD_CACHE = CacheBuilder
            .newBuilder().maximumSize(CACHE_MAX_SIZE).expireAfterAccess(1, TimeUnit.HOURS)
            .build(new CacheLoader<Triple<Class<?>, String, List<Class<?>>>, Method>() {
                @Override
                public Method load(final Triple<Class<?>, String, List<Class<?>>> key) {
                    final List<Class<?>> al = key.getRight();
                    final Class<?>[] args = new Class<?>[al.size()];
                    return loadBestMethod(key.getLeft(), key.getMiddle(), al.toArray(args));
                }
            });

    private ReflectionUtils() {
    }

    /**
     * @param methodName
     *            the method to be invoked
     * @param target
     *            the target object. It can not be null
     * @param args
     *            the arguments for the method
     * @return the result of the invocation, or an {@link IllegalStateException}
     *         if something goes wrong.
     */
    public static Object invokeBestNotStatic(final Object target, final String methodName, final Object[] args) {
        Objects.requireNonNull(target);
        return invokeBestMethod(target.getClass(), methodName, target, args);
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
    public static Object invokeBestStatic(final Class<?> clazz, final String methodName, final Object... args) {
        return invokeBestMethod(clazz, methodName, null, args);
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
    public static Method searchBestMethod(final Class<?> clazz, final String methodName, final Object... args) {
        return searchBestMethod(clazz, methodName, Arrays.asList(args));
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
    public static Method searchBestMethod(final Class<?> clazz, final String methodName, final List<Object> args) {
        final List<Class<?>> originalClasses = new ArrayList<>(args.size());
        final List<Class<?>> fieldedClasses = new ArrayList<>(args.size());
        boolean atLeastOneField = false;
        for (final Object arg: args) {
            final Class<?> argClass = arg.getClass();
            if (arg instanceof Field) {
                fieldedClasses.add(((Field) arg).getExpectedType());
                atLeastOneField = true;
            } else {
                fieldedClasses.add(argClass);
            }
            originalClasses.add(argClass);
        }
        try {
            return METHOD_CACHE.get(new ImmutableTriple<>(clazz, methodName, originalClasses));
        } catch (UncheckedExecutionException | ExecutionException outerException) {
            if (atLeastOneField) {
                try {
                    return METHOD_CACHE.get(new ImmutableTriple<>(clazz, methodName, fieldedClasses));
                } catch (ExecutionException e) {
                    throw new NoSuchMethodError("No" + methodName + originalClasses
                            + " nor " + methodName + fieldedClasses + " exist in " + clazz
                            + ".\nYou tried to invoke it with arguments " + args);
                }
            }
            throw new NoSuchMethodError(methodName + originalClasses + " does not exist in " + clazz
                    + ".\nYou tried to invoke it with arguments " + args);
        }
    }

    private static Method loadBestMethod(final Class<?> clazz, final String methodName, final Class<?>[] argClass) {
        Objects.requireNonNull(clazz, "The class on which the method will be invoked can not be null.");
        Objects.requireNonNull(methodName, "Method name can not be null.");
        Objects.requireNonNull(argClass, "Method arguments can not be null.");
        /*
         * If there is a matching method, return it
         */
        try {
            return clazz.getMethod(methodName, argClass);
        } catch (NoSuchMethodException | SecurityException e) {
            /*
             * Look it up on the cache
             */
            /*
             * Deal with Java method overloading scoring methods
             */
            final Method[] candidates = clazz.getMethods();
            final List<Pair<Integer, Method>> lm = new ArrayList<>(candidates.length);
            for (final Method m : candidates) {
                if (compatibleLength(m, argClass) && methodName.equals(m.getName())) {
                    int p = 0;
                    boolean compatible = true;
                    for (int i = 0; compatible && i < argClass.length; i++) {
                        final Class<?> expected = nthArgumentType(m, i);
                        final Class<?> actual = argClass[i];
                        if (expected.isAssignableFrom(actual)) {
                            /*
                             * No downcast nor coercion required, there is compatibility
                             */
                            p += 3;
                        } else if (PrimitiveUtils.classIsPrimitive(expected) && PrimitiveUtils.classIsWrapper(actual)) {
                            p += computePointsForWrapper(expected, actual);
                        } else if (PrimitiveUtils.classIsPrimitive(actual) && PrimitiveUtils.classIsWrapper(expected)) {
                            p += computePointsForWrapper(actual, expected);
                        } else if (!(PrimitiveUtils.classIsNumber(expected) && PrimitiveUtils.classIsWrapper(actual))) {
                            /*
                             * At least one is not a number: conversion with precision loss does not apply.
                             */
                            compatible = false;
                        }
                    }
                    if (compatible) {
                        lm.add(new Pair<>(p, m));
                    }
                }
            }
            /*
             * Find best
             */
            final Optional<Method> best = stream(lm).max((pm1, pm2) -> pm1.getFirst().compareTo(pm2.getFirst()))
                    .map(Pair::getSecond);
            if (best.isPresent()) {
                return best.get();
            }
        }
        throw new IllegalStateException();
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

    /**
     * @param clazz
     *            the class where to search for suitable methods
     * @param methodName
     *            the method to be invoked
     * @param target
     *            the target object. It can be null, if the method which is
     *            being invoked is static
     * @param args
     *            the arguments for the method
     * @return the result of the invocation, or an {@link IllegalStateException}
     *         if something goes wrong.
     */
    public static Object invokeBestMethod(
            final Class<?> clazz,
            final String methodName,
            final Object target,
            final Object[] args) {
        return invokeMethod(searchBestMethod(clazz, methodName, args), target, args);
    }

    /**
     * Invokes a method. If there are fields involved, field operations are
     * applied
     * 
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
            final Class<?> clazz,
            final String methodName,
            final Object target,
            final Object[] args) {
        if (Field.class.isAssignableFrom(clazz) && target instanceof Field) {
            return invokeFieldable(
                    ((Field) target).valIterator().iterator().next().getClass(),
                    methodName,
                    target,
                    args);
        }
        return invokeFieldable(searchBestMethod(clazz, methodName, args), target, args);
    }

    /**
     * Invokes a method. If there are fields involved, field operations are
     * applied
     * 
     * @param toInvoke
     *            the method to be invoked
     * @param target
     *            the target object (can be null in case of static invocation)
     * @param args
     *            the arguments for the method
     * @return the result of the method invocation
     */
    public static Object invokeFieldable(
            final Method toInvoke,
            final Object target,
            final Object[] args) {
        if (!compatibleLength(toInvoke, args)) {
            throw new IllegalArgumentException("Number of parameters of " + toInvoke
                    + " does not match the provided array " + Arrays.toString(args));
        }
        final boolean fieldTarget = target instanceof Field;
        final TIntList fieldIndexes = new TIntArrayList(args.length);
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Field
                    && !Field.class.isAssignableFrom(nthArgumentType(toInvoke, i))) {
                fieldIndexes.add(i);
            }
        }
        if (fieldTarget || fieldIndexes.size() > 0) {
            return Fields.apply(
                    (actualT, actualA) -> ReflectionUtils.invokeMethod(toInvoke, actualT, actualA),
                    fieldTarget,
                    fieldIndexes.toArray(),
                    target,
                    args);
        }
        return ReflectionUtils.invokeMethod(toInvoke, target, args);
    }

    /**
     * @param method
     *            the methods to invoke
     * @param target
     *            the target object. It can be null, if the method which is
     *            being invoked is static
     * @param args
     *            the arguments for the method
     * @return the result of the invocation, or an {@link IllegalStateException}
     *         if something goes wrong.
     */
    public static Object invokeMethod(final Method method, final Object target, final Object[] args) {
        Object[] useArgs = repackageIfVarArgs(method, args);
        // TODO: this is necessary for getting through interface masking, but problematic
        method.setAccessible(true);
        try {
            return method.invoke(target, useArgs);
        } catch (Exception exc) {
            /*
             * Failure: maybe some cast was required?
             */
            final Class<?>[] params = method.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                final Class<?> expected = params[i];
                final Object actual = useArgs[i];
                if (!expected.isAssignableFrom(actual.getClass()) && PrimitiveUtils.classIsNumber(expected)) {
                    useArgs[i] = PrimitiveUtils.castIfNeeded(expected, (Number) actual).get();
                }
            }
            try {
                return method.invoke(target, useArgs);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                L.error("Error invoking method", e);
                throw new IllegalStateException(
                        "Cannot invoke " + method + " with arguments " + Arrays.toString(useArgs) + " on " + target, e);
            }
        }
    }

    private static boolean compatibleLength(final Method m, final Object[] args) {
        if (m.isVarArgs()) {
            return args.length >= (m.getParameterTypes().length - 1);
        } else {
            return m.getParameterTypes().length == args.length;
        }
    }

    private static Class<?> nthArgumentType(final Method m, final int n) {
        final Class<?>[] expectedArgs = m.getParameterTypes();
        if (m.isVarArgs() && n >= (expectedArgs.length - 1)) {
            Class<?> varargType = expectedArgs[expectedArgs.length - 1];
            return varargType.getComponentType();
        } else {
            return expectedArgs[n];
        }
    }

    private static Object[] repackageIfVarArgs(final Method m, final Object[] args) {
        if (!m.isVarArgs()) {
            return args;
        } else {
            final Class<?>[] expectedArgs = m.getParameterTypes();
            // We will repackage into an array of the expected length
            Object[] newargs = new Object[expectedArgs.length];
            for (int i = 0; i < expectedArgs.length - 1; i++) { // repackage all the base args
                newargs[i] = args[i];
            }
            // Determine how many arguments need repackaging
            int numVarArgs = args.length - (expectedArgs.length - 1);
            // Make an array of the appropriate type, then fill it in
            Class<?> varargType = expectedArgs[expectedArgs.length - 1];
            Object[] vararg = (Object[]) Array.newInstance(varargType.getComponentType(), numVarArgs);
            for (int i = 0; i < numVarArgs; i++) {
                vararg[i] = args[i + expectedArgs.length - 1];
            }
            // Put the new array in the last argument and return
            newargs[newargs.length - 1] = vararg;
            return newargs;
        }
    }

}
