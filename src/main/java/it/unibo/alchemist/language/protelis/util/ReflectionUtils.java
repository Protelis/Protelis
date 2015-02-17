/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis.util;

import it.unibo.alchemist.utils.L;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.danilopianini.lang.Pair;
import org.danilopianini.lang.PrimitiveUtils;

/**
 * Utilities that make easier to cope with Java Reflection.
 * 
 * @author Danilo Pianini
 *
 */
public final class ReflectionUtils {
	
	private ReflectionUtils() {
	}
	
	/**
	 * @param methodName
	 *            the method to be invoked
	 * @param target
	 *            the target object. It can not be null
	 * @param args
	 *            the arguments for the method
	 * @return the result of the invocation, or an {@link IllegalStateException}
	 *         if something goes wrong.
	 */
	public static Object invokeBestNotStatic(final Object target, final String methodName, final Object[] args) {
		Objects.requireNonNull(target);
		return invokeBestMethod(target.getClass(), methodName, target, args);
	}
	
	/**
	 * @param clazz
	 *            the class where to search for suitable methods
	 * @param methodName
	 *            the method to be invoked
	 * @param args
	 *            the arguments for the method
	 * @return the result of the invocation, or an {@link IllegalStateException}
	 *         if something goes wrong.
	 */
	public static Object invokeBestStatic(final Class<?> clazz, final String methodName, final Object[] args) {
		return invokeBestMethod(clazz, methodName, null, args);
	}
	
	/**
	 * @param clazz
	 *            the class where to search for suitable methods
	 * @param methodName
	 *            the method to be invoked
	 * @param target
	 *            the target object. It can be null, if the method which is
	 *            being invoked is static
	 * @param args
	 *            the arguments for the method
	 * @return the result of the invocation, or an {@link IllegalStateException}
	 *         if something goes wrong.
	 */
	public static Object invokeBestMethod(final Class<?> clazz, final String methodName, final Object target, final Object[] args) {
		Objects.requireNonNull(clazz, "The class on which the method will be invoked can not be null.");
		Objects.requireNonNull(methodName, "Method name can not be null.");
		Objects.requireNonNull(args, "Method arguments can not be null.");
		final Stream<Method> candidates = Arrays.stream(clazz.getMethods())
				.filter(m -> m.getParameterCount() == args.length && methodName.equals(m.getName()));
		/*
		 * Deal with Java method overloading scoring methods
		 */
		final Optional<Method> best = candidates.collect(
				/*
				 * Filter and score methods
				 */
				() -> new ArrayList<Pair<Integer, Method>>(),
				(l, m) -> {
					final Class<?>[] params = m.getParameterTypes();
					int p = 0;
					for (int i = 0; i < args.length; i++) {
						final Class<?> expected = params[i];
						final Object actual = args[i];
						if (expected.isAssignableFrom(actual.getClass())) {
							/*
							 * No downcast required, there is compatibility
							 */
							p++;
						} else if (!PrimitiveUtils.classIsNumber(expected)) {
							/*
							 * No downcast possible, incompatible method.
							 */
							return;
						}
					}
					l.add(new Pair<>(p, m));
				},
				List::addAll).stream()
				/*
				 * Find best
				 */
				.max((pm1, pm2) -> pm1.getFirst().compareTo(pm2.getFirst()))
				.map(Pair::getSecond);
		if (best.isPresent()) {
			return invokeMethod(best.get(), target, args);
		}
		final String argType = Arrays.stream(args).map(Object::getClass).collect(Collectors.toList()).toString();
		throw new NoSuchMethodError(methodName + "/" + args.length + argType + " does not exist in class " + clazz);
	}
	
	/**
	 * @param method
	 *            the methods to invoke
	 * @param target
	 *            the target object. It can be null, if the method which is
	 *            being invoked is static
	 * @param args
	 *            the arguments for the method
	 * @return the result of the invocation, or an {@link IllegalStateException}
	 *         if something goes wrong.
	 */
	public static Object invokeMethod(final Method method, final Object target, final Object[] args) {
		Object[] actualArgs = Arrays.copyOf(args, args.length);
		final Class<?>[] params = method.getParameterTypes();
		for (int i = 0; i < args.length; i++) {
			final Class<?> expected = params[i];
			final Object actual = args[i];
			if (!expected.isAssignableFrom(actual.getClass()) && PrimitiveUtils.classIsNumber(expected)) {
				actualArgs[i] = PrimitiveUtils.castIfNeeded(expected, (Number) actual).get();
			}
		}
		try {
			return method.invoke(target, actualArgs);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			L.error(e);
			throw new IllegalStateException("Cannot invoke " + method + " with arguments " + Arrays.toString(args) + " on " + target, e);
		}
	}


}
