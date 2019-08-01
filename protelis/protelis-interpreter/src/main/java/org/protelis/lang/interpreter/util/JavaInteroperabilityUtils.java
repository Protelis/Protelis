package org.protelis.lang.interpreter.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.impl.Constant;
import org.protelis.lang.interpreter.impl.Invoke;
import org.protelis.lang.interpreter.impl.MethodCall;
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
     * @param target
     *            the {@link AnnotatedTree} on which annotation the method will
     *            be invoked
     * @param clazz
     *            the class where to search for the method
     * @param method
     *            a valid {@link java.lang.reflect.Method} name
     * @param args
     *            the arguments for the method
     * @return the result of the evaluation
     */
    private static Object runMethodWithProtelisArguments(
            final ExecutionContext ctx,
            final AnnotatedTree<?> target,
            final Class<?> clazz,
            final String method,
            final List<AnnotatedTree<?>> args) {
        if (target != null && !clazz.isAssignableFrom(target.getClass())) {
            throw new IllegalArgumentException(
                    "The target object class(" + target.getClass() + ") is not compatible with " + clazz);
        }
        final MethodCall dot = new MethodCall(METADATA, clazz, method, target == null, args);
        dot.eval(ctx);
        return dot.getAnnotation();
    }

    /**
     * @param ctx
     *            {@link ExecutionContext}
     * @param clazz
     *            the class where to search for the method
     * @param method
     *            a valid {@link java.lang.reflect.Method} name
     * @param args
     *            the arguments for the method
     * @return the result of the evaluation
     */
    public static Object runStaticMethodWithProtelisArguments(
            final ExecutionContext ctx,
            final Class<?> clazz,
            final String method,
            final AnnotatedTree<?>... args) {
        Objects.requireNonNull(clazz);
        return runMethodWithProtelisArguments(ctx, null, clazz, method, Arrays.asList(args));
    }

    /**
     * @param ctx
     *            {@link ExecutionContext}
     * @param target
     *            the {@link AnnotatedTree} on which annotation the method will
     *            be invoked
     * @param method
     *            a valid {@link java.lang.reflect.Method} name
     * @param args
     *            the arguments for the method
     * @return the result of the evaluation
     */
    public static Object runMethodWithProtelisArguments(
            final ExecutionContext ctx,
            final AnnotatedTree<?> target,
            final String method,
            final AnnotatedTree<?>... args) {
        Objects.requireNonNull(target);
        return runMethodWithProtelisArguments(ctx, target, target.getClass(), method, Arrays.asList(args));
    }

    private static List<AnnotatedTree<?>> toAnnotatedTree(final Object[] a) {
        return Arrays.stream(a).map(it -> new Constant<>(METADATA, it)).collect(Collectors.toList());
    }

    /**
     * @param ctx
     *            {@link ExecutionContext}
     * @param fd
     *            an {@link AnnotatedTree} with the {@link FunctionDefinition}
     *            to instance and use
     * @param args
     *            the function arguments
     * @return the result of the evaluation
     */
    public static Object runProtelisFunction(
            final ExecutionContext ctx,
            final AnnotatedTree<FunctionDefinition> fd,
            final List<AnnotatedTree<?>> args) {
        final Invoke dot = Invoke.makeApply(fd, args);
        dot.eval(ctx);
        return dot.getAnnotation();
    }

    /**
     * @param ctx
     *            {@link ExecutionContext}
     * @param fd
     *            an {@link AnnotatedTree} with the {@link FunctionDefinition}
     *            to instance and use
     * @param args
     *            the function arguments
     * @return the result of the evaluation
     */
    public static Object runProtelisFunctionWithJavaArguments(
            final ExecutionContext ctx,
            final AnnotatedTree<FunctionDefinition> fd,
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
        final List<AnnotatedTree<?>> arguments = args.stream().map(it -> new Constant<>(METADATA, it)).collect(Collectors.toList());
        return runProtelisFunction(ctx, new Constant<>(METADATA, fd), arguments);
    }

}
