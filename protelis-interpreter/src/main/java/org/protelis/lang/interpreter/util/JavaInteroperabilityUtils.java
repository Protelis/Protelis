/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.lang.interpreter.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.datatype.JVMEntity;
import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.impl.Constant;
import org.protelis.lang.interpreter.impl.FunctionCall;
import org.protelis.lang.interpreter.impl.Invoke;
import org.protelis.lang.interpreter.impl.JvmConstant;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Collection of utilities that ease the interoperability with Java.
 *
 */
public final class JavaInteroperabilityUtils {

    /**
     * A fake Metadata object to be used when no data or original source code is available.
     */
    public static final Metadata METADATA = new Metadata() {
        private static final long serialVersionUID = 1L;
        @Override
        public int getStartLine() {
            return -1;
        }
        @Override
        public int getEndLine() {
            return -1;
        }
    };

    private JavaInteroperabilityUtils() {
    }

    /**
     * Executes a static Java method using Protelis-produced arguments.
     *
     * @param context
     *            {@link ExecutionContext}
     * @param method
     *            a valid {@link java.lang.reflect.Method} name
     * @param arguments
     *            the arguments for the method
     * @return the result of the evaluation
     */
    public static Object runStaticMethodWithProtelisArguments(
        final ExecutionContext context,
        final Method method,
        final ProtelisAST<?>... arguments
    ) {
        Objects.requireNonNull(method);
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new IllegalArgumentException("Method " + method + " cannot be invoked statically.");
        }
        return new Invoke(new JvmConstant(METADATA, new JVMEntity(method)), Arrays.asList(arguments)).eval(context);
    }

    /**
     * @param context
     *            {@link ExecutionContext}
     * @param target
     *            the {@link ProtelisAST} on which annotation the method will
     *            be invoked
     * @param method
     *            a valid {@link java.lang.reflect.Method} name
     * @param arguments
     *            the arguments for the method
     * @return the result of the evaluation
     */
    public static Object runMethodWithProtelisArguments(
        final ExecutionContext context,
        final ProtelisAST<?> target,
        final String method,
        final ProtelisAST<?>... arguments
    ) {
        return new Invoke(METADATA, method, target, Arrays.asList(arguments)).eval(context);
    }

    private static List<ProtelisAST<?>> toAnnotatedTree(final Object[] a) {
        return Arrays.stream(a).map(it -> new Constant<>(METADATA, it)).collect(Collectors.toList());
    }

    /**
     * Runs a Protelis function from Java, provided context and arguments.
     *
     * @param ctx
     *            {@link ExecutionContext}
     * @param fd
     *            an {@link ProtelisAST} with the {@link FunctionDefinition}
     *            to instance and use
     * @param args
     *            the function arguments
     * @return the result of the evaluation
     */
    @Nonnull
    public static Object runProtelisFunction(
        final ExecutionContext ctx,
        final ProtelisAST<FunctionDefinition> fd,
        final List<ProtelisAST<?>> args
    ) {
        return new Invoke(fd, args).eval(ctx);
    }

    /**
     * @param context
     *            {@link ExecutionContext}
     * @param function
     *            an {@link ProtelisAST} with the {@link FunctionDefinition}
     *            to instance and use
     * @param arguments
     *            the function arguments
     * @return the result of the evaluation
     */
    public static Object runProtelisFunctionWithJavaArguments(
        final ExecutionContext context,
        final ProtelisAST<FunctionDefinition> function,
        final Object... arguments
    ) {
        return runProtelisFunction(context, function, toAnnotatedTree(arguments));
    }

    /**
     * @param context
     *            {@link ExecutionContext}
     * @param function
     *            the {@link FunctionDefinition} to instance and use
     * @param arguments
     *            the function arguments
     * @return the result of the evaluation
     */
    @Nonnull
    public static Object runProtelisFunctionWithJavaArguments(
        @Nonnull final ExecutionContext context,
        @Nonnull final FunctionDefinition function,
        @Nonnull final List<?> arguments
    ) {
        final List<ProtelisAST<?>> actualArguments = arguments.stream()
            .map(it -> new Constant<>(METADATA, it))
            .collect(Collectors.toList());
        return runProtelisFunction(context, new Constant<>(METADATA, function), actualArguments);
    }

    /**
     * Converts a protelis {@link FunctionDefinition} to a Java {@link BinaryOperator}.
     * @param context the {@link ExecutionContext}
     * @param binaryOperator the {@link FunctionDefinition} to convert to Java
     * @return a java {@link BinaryOperator}
     */
    public static BinaryOperator<Object> toBinaryOperator(
        final ExecutionContext context,
        final FunctionDefinition binaryOperator
    ) {
        if (binaryOperator.getParameterCount() == 2) {
            final AtomicInteger counter = new AtomicInteger();
            return (first, second) -> {
                final List<ProtelisAST<?>> arguments = ImmutableList.of(
                    new Constant<>(JavaInteroperabilityUtils.METADATA, first),
                    new Constant<>(JavaInteroperabilityUtils.METADATA, second)
                );
                final FunctionCall call = new FunctionCall(JavaInteroperabilityUtils.METADATA, binaryOperator, arguments);
                return context.runInNewStackFrame(counter.getAndIncrement(), call::eval);
            };
        }
        throw new IllegalArgumentException("Reducing function must take two parameters.");
    }

    /**
     * Converts a protelis {@link FunctionDefinition} to a Java {@link Function}.
     * @param context the {@link ExecutionContext}
     * @param function the {@link FunctionDefinition} to convert to Java
     * @param <R> Return type of the function
     * @return a java {@link Function}
     */
    @SuppressWarnings("unchecked")
    public static <R> Function<Object, R> toFunction(
        final ExecutionContext context,
        final FunctionDefinition function
    ) {
        if (function.getParameterCount() <= 1) {
            final AtomicInteger counter = new AtomicInteger();
            return (argument) -> {
                final List<ProtelisAST<?>> arguments = ImmutableList.of(
                    new Constant<>(JavaInteroperabilityUtils.METADATA, argument)
                );
                final FunctionCall call = new FunctionCall(JavaInteroperabilityUtils.METADATA, function, arguments);
                return (R) context.runInNewStackFrame(counter.getAndIncrement(), call::eval);
            };
        }
        throw new IllegalArgumentException("Reducing function must take two parameters.");
    }
}
