/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
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
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.model.interfaces.INode;
import it.unibo.alchemist.utils.L;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.danilopianini.lang.PrimitiveUtils;

public class MethodCall extends AbstractAnnotatedTree<Object> {

	private static final long serialVersionUID = -2299070628855971997L;
	private final Method method;
	private final boolean fieldComposable;
	private final boolean ztatic;

	/**
	 * @param m
	 * @param branch
	 * @param isStatic if true, the first branch must contain the AnnotatedTree whose annotation will contain the object on which the method will be invoked
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
		Stream<Object> s = getBranches().stream().map(AnnotatedTree::getAnnotation);
		final Object[] args = ztatic ? s.toArray() : s.skip(1).toArray();
		/*
		 * Check if any of the parameters is a field
		 */
		try {
			if (fieldComposable) {
				final boolean fieldTarget = target==null ? false : Field.class.isAssignableFrom(target.getClass());
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
					setAnnotation(Field.apply(target, method, fieldTarget, fieldIndexes, args));
					return;
				}
			}
			/*
			 * Handle down-casting
			 */
			final Class<?>[] parTypes = method.getParameterTypes();
			for (int i = 0; i < args.length; i++) {
				final Class<?> wanted = parTypes[i];
				final Class<?> actual = args[i].getClass();
				if (!wanted.isAssignableFrom(actual) && PrimitiveUtils.classIsNumber(wanted) && PrimitiveUtils.classIsNumber(actual)) {
					final Number wrapArg = (Number) args[i];
					args[i] = PrimitiveUtils.castIfNeeded(wanted, wrapArg).orElse(wrapArg);
				}
			}
			// TODO: NEED TO HANDLE DOWN-CASTING OF PRIMITIVES
			setAnnotation(method.invoke(target, args));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			L.error(e);
			throw new IllegalStateException("Cannot execute " + method + " on " + Arrays.toString(args));
		}
	}

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

}
