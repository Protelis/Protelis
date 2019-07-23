package org.protelis.lang.interpreter.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.xtext.common.types.JvmOperation;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.interpreter.util.ReflectionUtils;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Call an external Java static method.
 */
public final class MethodCall extends AbstractAnnotatedTree<Object> {

    private static final long serialVersionUID = -2299070628855971997L;
    private final Class<?> clazz;
    private transient Method method;
    private final String methodName;
    private final boolean ztatic;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param clazz
     *            the class where to search for the method
     * @param methodName
     *            the method name
     * @param ztatic
     *            true if the method is static
     * @param branch
     *            method arguments
     */
    public MethodCall(
            final Metadata metadata,
            final Class<?> clazz,
            final String methodName,
            final boolean ztatic,
            final List<AnnotatedTree<?>> branch) {
        super(metadata, branch);
        this.clazz = clazz;
        this.methodName = methodName;
        this.ztatic = ztatic;
        extractMethod();
    }

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param jvmOp
     *            the method to call
     * @param branch
     *            the Protelis sub-programs
     * @throws ClassNotFoundException
     *             in case the {@link JvmOperation} reference class couldn't be
     *             found in the classpath
     */
    public MethodCall(final Metadata metadata, final JvmOperation jvmOp, final List<AnnotatedTree<?>> branch) {
        super(metadata, branch);
        final String classname = jvmOp.getDeclaringType().getQualifiedName();
        try {
            clazz = Class.forName(classname);
        } catch (ClassNotFoundException e) {
           throw new IllegalStateException(e);
        }
        ztatic = jvmOp.isStatic();
        methodName = jvmOp.getSimpleName();
        extractMethod();
    }

    @Override
    public MethodCall copy() {
        return new MethodCall(getMetadata(), clazz, methodName, ztatic, deepCopyBranches());
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        projectAndEval(context);
        // Obtain target and arguments
        final Object target = ztatic ? null : getBranch(0).getAnnotation();
        final Object[] s = getBranchesAnnotations();
        final Object[] args = ztatic ? s : Arrays.copyOfRange(s, 1, s.length);
        setAnnotation(method == null
                ? ReflectionUtils.invokeFieldable(context, clazz, methodName, target, args)
                : ReflectionUtils.invokeFieldable(context, method, target, args));
    }

    private void extractMethod() {
        extractMethod(getBranchesNumber() + (ztatic ? 0 : 1));
    }

    private void extractMethod(final int parameterCount) {
        Stream<Method> methods = Arrays.stream(clazz.getMethods()); // NOPMD: this is not an I/O stream
        if (ztatic) {
            methods = methods.filter(m -> Modifier.isStatic(m.getModifiers()));
        } else {
            methods = methods.filter(m -> !Modifier.isStatic(m.getModifiers()));
        }
        /*
         * Filter to same name and same number of arguments (or compatible, for varargs)
         */
        final List<Method> matches = methods
                .filter(m -> minArgCount(m) <= parameterCount && parameterCount <= maxArgCount(m))
                .filter(m -> m.getName().equals(methodName))
                .collect(Collectors.toList());
        if (matches.isEmpty()) {
            throw new IllegalArgumentException("No "
                    + (ztatic ? "static " : "")
                    + "method named " + methodName + " with " + parameterCount
                    + " parameters is available in " + clazz);
        }
        if (matches.size() == 1) {
            /*
             * Only take it if there are no overloads
             */
            method = matches.get(0);
        }
    }

    @Override
    public Bytecode getBytecode() {
        return Bytecode.METHOD_CALL;
    }

    @Override
    public String getName() {
        return methodName;
    }

    @Override
    protected boolean isNullable() {
        return true;
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        extractMethod();
    }

    @Override
    public String toString() {
        return (ztatic ? "" : stringFor(getBranch(0)) + '.')
            + getName()
            + getBranches().stream()
                .skip(ztatic ? 0 : 1)
                .map(MethodCall::stringFor)
                .collect(Collectors.joining(", ", "(", ")"));
    }

    private static boolean isInjectable(final Method m) {
        return m.getParameterCount() > 0 && ExecutionContext.class.isAssignableFrom(m.getParameterTypes()[0]);
    }

    private static int maxArgCount(final Method m) {
        if (m.isVarArgs()) {
            return Integer.MAX_VALUE;
        }
        return m.getParameterCount();
    }

    private static int minArgCount(final Method m) {
        return m.getParameterCount() - (m.isVarArgs() ? 1 : 0) - (isInjectable(m) ? 1 : 0);
    }

}
