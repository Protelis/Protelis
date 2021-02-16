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
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.datatype.JVMEntity;
import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.impl.Constant;
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
     * @param ctx
     *            {@link ExecutionContext}
     * @param method
     *            a valid {@link java.lang.reflect.Method} name
     * @param args
     *            the arguments for the method
     * @return the result of the evaluation
     */
    public static Object runStaticMethodWithProtelisArguments(
            final ExecutionContext ctx,
            final Method method,
            final ProtelisAST<?>... args) {
        Objects.requireNonNull(method);
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new IllegalArgumentException("Method " + method + " cannot be invoked statically.");
        }
        return new Invoke(new JvmConstant(METADATA, new JVMEntity(method)), Arrays.asList(args)).eval(ctx);
    }

    /**
     * @param ctx
     *            {@link ExecutionContext}
     * @param target
     *            the {@link ProtelisAST} on which annotation the method will
     *            be invoked
     * @param method
     *            a valid {@link java.lang.reflect.Method} name
     * @param args
     *            the arguments for the method
     * @return the result of the evaluation
     */
    public static Object runMethodWithProtelisArguments(
            final ExecutionContext ctx,
            final ProtelisAST<?> target,
            final String method,
            final ProtelisAST<?>... args) {
        return new Invoke(METADATA, method, target, Arrays.asList(args)).eval(ctx);
    }

    private static List<ProtelisAST<?>> toAnnotatedTree(final Object[] a) {
        return Arrays.stream(a).map(it -> new Constant<>(METADATA, it)).collect(Collectors.toList());
    }

    /**
     * @param ctx
     *            {@link ExecutionContext}
     * @param fd
     *            an {@link ProtelisAST} with the {@link FunctionDefinition}
     *            to instance and use
     * @param args
     *            the function arguments
     * @return the result of the evaluation
     */
    public static Object runProtelisFunction(
            final ExecutionContext ctx,
            final ProtelisAST<FunctionDefinition> fd,
            final List<ProtelisAST<?>> args) {
        return new Invoke(fd, args).eval(ctx);
    }

    /**
     * @param ctx
     *            {@link ExecutionContext}
     * @param fd
     *            an {@link ProtelisAST} with the {@link FunctionDefinition}
     *            to instance and use
     * @param args
     *            the function arguments
     * @return the result of the evaluation
     */
    public static Object runProtelisFunctionWithJavaArguments(
            final ExecutionContext ctx,
            final ProtelisAST<FunctionDefinition> fd,
            final Object... args) {
        return runProtelisFunction(ctx, fd, toAnnotatedTree(args));
    }

    /**
     * @param ctx
     *            {@link ExecutionContext}
     * @param fd
     *            the {@link FunctionDefinition} to instance and use
     * @param args
     *            the function arguments
     * @return the result of the evaluation
     */
    public static Object runProtelisFunctionWithJavaArguments(
            @Nonnull final ExecutionContext ctx,
            @Nonnull final FunctionDefinition fd,
            @Nonnull final List<?> args) {
        final List<ProtelisAST<?>> arguments = args.stream().map(it -> new Constant<>(METADATA, it)).collect(Collectors.toList());
        return runProtelisFunction(ctx, new Constant<>(METADATA, fd), arguments);
    }

}
