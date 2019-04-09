package org.protelis.lang.interpreter.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java8.util.stream.Collectors;

import java8.util.J8Arrays;
import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

import org.eclipse.xtext.common.types.JvmOperation;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.loading.Metadata;
import org.protelis.lang.util.ReflectionUtils;
import org.protelis.vm.ExecutionContext;

/**
 * Call an external Java static method.
 */
public final class MethodCall extends AbstractAnnotatedTree<Object> {

    private static final long serialVersionUID = -2299070628855971997L;
    private final boolean ztatic;
    private final Class<?> clazz;
    private final String methodName;
    private transient Method method;

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

    private void extractMethod() {
        extractMethod(getBranchesNumber() + (ztatic ? 0 : 1));
    }

    private void extractMethod(final int parameterCount) {
        Stream<Method> methods = J8Arrays.stream(clazz.getMethods());
        if (ztatic) {
            methods = methods.filter(m -> Modifier.isStatic(m.getModifiers()));
        } else {
            methods = methods.filter(m -> !Modifier.isStatic(m.getModifiers()));
        }
        /*
         * Filter to same name and same number of arguments (or compatible, for varargs)
         */
        final List<Method> matches = methods.filter(
                m -> m.getParameterTypes().length == parameterCount
                      || m.isVarArgs() && m.getParameterTypes().length >= parameterCount - 1)
                .filter(m -> m.getName().equals(methodName)).collect(Collectors.toList());
        if (matches.isEmpty()) {
            throw new IllegalArgumentException("No "
                    + (ztatic ? "static " : "")
                    + "method named " + methodName + " with " + parameterCount
                    + " parameters is available in " + clazz);
        }
        if (matches.size() == 1) {
            method = matches.get(0);
        }
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        projectAndEval(context);
        // Obtain target and arguments
        final Object target = ztatic ? null : getBranch(0).getAnnotation();
        final Object[] s = getBranchesAnnotations();
        final Object[] args = ztatic ? s : Arrays.copyOfRange(s, 1, s.length);
        setAnnotation(method == null
                ? ReflectionUtils.invokeFieldable(clazz, methodName, target, args)
                : ReflectionUtils.invokeFieldable(method, target, args));
    }

    @Override
    public MethodCall copy() {
        return new MethodCall(getMetadata(), clazz, methodName, ztatic, deepCopyBranches());
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        extractMethod();
    }

    @Override
    public String getName() {
        return methodName;
    }

    @Override
    public String toString() {
        return (ztatic ? "" : stringFor(getBranch(0)) + '.')
            + getName()
            + StreamSupport.stream(getBranches())
                .skip(ztatic ? 0 : 1)
                .map(MethodCall::stringFor)
                .collect(Collectors.joining(", ", "(", ")"));
    }

}
