package org.protelis.lang.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import java8.util.J8Arrays;
import java8.util.stream.Collectors;

import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.impl.Constant;
import org.protelis.lang.interpreter.impl.DotOperator;
import org.protelis.lang.interpreter.impl.MethodCall;
import org.protelis.vm.ExecutionContext;

import static java8.util.stream.StreamSupport.stream;

/**
 * Collection of utilities that ease the interoperability with Java.
 *
 */
public final class JavaInteroperabilityUtils {

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
     *            a valid {@link Method} name
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
        final MethodCall dot = new MethodCall(clazz, method, target == null, args);
        dot.eval(ctx);
        return dot.getAnnotation();
    }

    /**
     * @param ctx
     *            {@link ExecutionContext}
     * @param clazz
     *            the class where to search for the method
     * @param method
     *            a valid {@link Method} name
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
     *            a valid {@link Method} name
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
        return J8Arrays.stream(a).map(Constant<Object>::new).collect(Collectors.toList());
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
        final DotOperator dot = DotOperator.makeApply(fd, args);
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
            final ExecutionContext ctx,
            final FunctionDefinition fd,
            final List<?> args) {
        final List<AnnotatedTree<?>> arguments = stream(args).map(Constant<Object>::new).collect(Collectors.toList());
        return runProtelisFunction(ctx, new Constant<>(fd), arguments);
    }

}
