/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.util.ReflectionUtils;
import org.protelis.vm.ExecutionContext;

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
		fieldComposable = !Arrays.stream(method.getParameterTypes()).parallel() // NOPMD by Danilo Pianini
				.anyMatch(clazz -> Field.class.isAssignableFrom(clazz)); 
	}

	@Override
	public void eval(final ExecutionContext context) {
		projectAndEval(context);
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
				setAnnotation(Field.apply((actualT, actualA) -> ReflectionUtils.invokeMethod(method, actualT, actualA), fieldTarget, fieldIndexes, target, args));
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
	protected void asString(final StringBuilder sb, final int i) {
		sb.append(method.getName());
		sb.append('/');
		sb.append(method.getParameterCount());
		sb.append(" (");
		fillBranches(sb, i, ',');
		sb.append(')');
	}
	
	private void writeObject(final ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(method.getDeclaringClass());
		out.writeUTF(method.getName());
		out.writeObject(method.getParameterTypes());
	}

	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		final Class<?> declaringClass = (Class<?>) in.readObject();
		final String methodName = in.readUTF();
		final Class<?>[] parameterTypes = (Class<?>[]) in.readObject();
		try {
			method = declaringClass.getMethod(methodName, parameterTypes);
		} catch (Exception e) {
			throw new IOException(String.format("Error occurred resolving deserialized method '%s.%s'", declaringClass.getSimpleName(), methodName), e);
		}
	}

}
