package org.protelis.lang.util;

import java.util.List;
import java.util.stream.Collectors;

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
     * @param fd
     *            an {@link AnnotatedTree} with the {@link FunctionDefinition}
     *            to instance and use
     * @param args
     *            the function arguments
     * @return the result of the evaluation
     */
    public static Object runProtelisFunction(final ExecutionContext ctx, final AnnotatedTree<FunctionDefinition> fd,
            final List<AnnotatedTree<?>> args) {
        final DotOperator dot = DotOperator.makeApply(fd, args);
        dot.eval(ctx);
        return dot.getAnnotation();
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
    public static Object runProtelisFunction(final ExecutionContext ctx, final FunctionDefinition fd,
            final List<?> args) {
        final List<AnnotatedTree<?>> arguments = args.stream().map(Constant<Object>::new).collect(Collectors.toList());
        return runProtelisFunction(ctx, new Constant<>(fd), arguments);
    }

}
