package org.protelis.lang.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.xtext.common.types.JvmOperation;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.impl.Constant;
import org.protelis.lang.interpreter.impl.DotOperator;
import org.protelis.vm.ExecutionContext;

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
     * @param method
     *            a valid {@link Method} name
     * @param args
     *            the arguments for the method
     * @return the result of the evaluation
     */
    public static Object runMethod(
            final ExecutionContext ctx,
            final AnnotatedTree<?> target,
            final String method,
            final List<AnnotatedTree<?>> args) {
        final DotOperator dot = new DotOperator(method, target, args);
        dot.eval(ctx);
        return dot.getAnnotation();
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
    public static Object runMethod(
            final ExecutionContext ctx,
            final AnnotatedTree<?> target,
            final String method,
            final AnnotatedTree<?>... args) {
        return runMethod(ctx, target, method, Arrays.asList(args));
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
    public static Object runMethod(
            final ExecutionContext ctx,
            final AnnotatedTree<?> target,
            final String method,
            final Object... args) {
        return runMethod(ctx, target, method, toAnnotatedTree(args));
    }

    private static List<AnnotatedTree<?>> toAnnotatedTree(final Object[] a) {
        return Arrays.stream(a).map(Constant<Object>::new).collect(Collectors.toList());
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
    public static Object runProtelisFunction(
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
    public static Object runProtelisFunction(
            final ExecutionContext ctx,
            final FunctionDefinition fd,
            final List<?> args) {
        final List<AnnotatedTree<?>> arguments = args.stream().map(Constant<Object>::new).collect(Collectors.toList());
        return runProtelisFunction(ctx, new Constant<>(fd), arguments);
    }

    /**
     * @param jvmOp
     *            the {@link JvmOperation} to convert
     * @return a list of matching {@link Method}s. If the list is longer than 1,
     *         then we have overloading
     * @throws ClassNotFoundException
     *             if the type can not be found
     */
    public static List<Method> jvmOperationToMethod(final JvmOperation jvmOp) throws ClassNotFoundException {
        final String classname = jvmOp.getDeclaringType().getQualifiedName();
        final Class<?> clazz = Class.forName(classname);
        /*
         * TODO: Check for return type and params: if param is Field and return
         * type is not then L.warn()
         */
        Stream<Method> methods = Arrays.stream(clazz.getMethods());
        final boolean ztatic = jvmOp.isStatic();
        if (ztatic) {
            methods = methods.filter(m -> Modifier.isStatic(m.getModifiers()));
        }
        /*
         * Same number of arguments
         */
        final int parameterCount = jvmOp.getParameters().size();
        methods = methods.filter(m -> m.getParameterCount() == parameterCount);
        /*
         * Same name
         */
        final String methodName = jvmOp.getSimpleName();
        methods = methods.filter(m -> m.getName().equals(methodName));
        return methods.collect(Collectors.toList());
    }

}
