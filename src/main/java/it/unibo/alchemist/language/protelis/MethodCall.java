/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import gnu.trove.list.TByteList;
import gnu.trove.map.TIntObjectMap;
import it.unibo.alchemist.language.protelis.datatype.Field;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.ReflectionUtils;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.model.interfaces.INode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Danilo Pianini
 *
 */
public class MethodCall extends AbstractAnnotatedTree<Object> {

	private static final long serialVersionUID = -2299070628855971997L;
	private transient Method method;
	private final boolean fieldComposable;
	private final boolean ztatic;

	/**
	 * @param m
	 *            the method to call
	 * @param branch
	 *            the Protelis sub-programs
	 * @param isStatic
	 *            if false, the first branch must contain the AnnotatedTree whose
	 *            annotation will contain the object on which the method will be
	 *            invoked
	 */
	public MethodCall(final Method m, final List<AnnotatedTree<?>> branch, final boolean isStatic) {
		super(branch);
		Objects.requireNonNull(m, "No compatible method found.");
		method = m;
		ztatic = isStatic;
		fieldComposable = !Arrays.stream(method.getParameterTypes()).parallel().anyMatch(clazz -> Field.class.isAssignableFrom(clazz));
	}

	@Override
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		evalEveryBranchWithProjection(sigma, theta, gamma, lastExec, newMap, currentPosition);
		// Obtain target and arguments
		final Object target = ztatic ? null : getBranch(0).getAnnotation();
		final Stream<?> s = getBranchesAnnotationStream();
		final Object[] args = ztatic ? s.toArray() : s.skip(1).toArray();
		/*
		 * Check if any of the parameters is a field
		 */
		if (fieldComposable) {
			final boolean fieldTarget = target == null ? false : Field.class.isAssignableFrom(target.getClass());
			Stream<Object> str = Arrays.stream(args).parallel();
			/*
			 * Filter the fields
			 */
			str = str.filter(o -> Field.class.isAssignableFrom(o.getClass()));
			/*
			 * Store their indexes
			 */
			final int[] fieldIndexes = str.mapToInt(o -> ArrayUtils.indexOf(args, o)).toArray();
			if (fieldTarget || fieldIndexes.length > 0) {
				setAnnotation(Field.apply(
						(actualT, actualA) -> ReflectionUtils.invokeMethod(method, actualT, actualA),
						fieldTarget, fieldIndexes, args));
				return;
			}
		}
		setAnnotation(ReflectionUtils.invokeMethod(method, target, args));
	}

	/**
	 * @return the method return type
	 */
	public Class<?> getReturnType() {
		return method.getReturnType();
	}

	@Override
	public MethodCall copy() {
		return new MethodCall(method, deepCopyBranches(), ztatic);
	}

	@Override
	protected String asString() {
		return method.getName() + "/" + method.getParameterCount();
	}
	
	private void writeObject(final ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(method.getDeclaringClass());
		out.writeUTF(method.getName());
		out.writeObject(method.getParameterTypes());
	}

	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		Class<?> declaringClass = (Class<?>) in.readObject();
		String methodName = in.readUTF();
		Class<?>[] parameterTypes = (Class<?>[]) in.readObject();
		try {
			method = declaringClass.getMethod(methodName, parameterTypes);
		} catch (Exception e) {
			throw new IOException(String.format("Error occurred resolving deserialized method '%s.%s'", declaringClass.getSimpleName(), methodName), e);
		}
	}

}
