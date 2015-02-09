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
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TIntObjectMap;
import it.unibo.alchemist.language.protelis.datatype.Field;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.model.interfaces.INode;
import it.unibo.alchemist.utils.L;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.util.FastMath;


/**
 * @author Danilo Pianini
 *
 */
public class DotOperator extends AbstractSATree<Object, Object> {
	
	private static final long serialVersionUID = -9128116355271771986L;
	private static final byte LEFT_POS = -1;
	private static final int DOWNCAST_INT = 0;
	private static final int DOWNCAST_FLOAT = 2;
	private static final int DOWNCAST_LONG = 1;
	private final boolean isApply;
	private final String methodName;
	private final AnnotatedTree<?> left;
	@SuppressWarnings("unchecked")
	private final Function<Object[], Object> unwrapper = (Function<Object[], Object> & Serializable) (a) -> {
		evalOnTarget(a[0], (INode<Object>) a[1], (TIntObjectMap<Map<CodePath, Object>>) a[2], (Stack) a[3], (Map<CodePath, Object>) a[4], (Map<CodePath, Object>) a[5], (TByteList)a[6]);
		return getAnnotation();
	};


	public DotOperator(final String name, final AnnotatedTree<?> target, final List<AnnotatedTree<?>> args) {
		super(args);
		Objects.requireNonNull(name);
		isApply = name.equals("apply");
		methodName = name;
		left = target;
	}
	
	@Override
	public AnnotatedTree<Object> copy() {
		final DotOperator res = new DotOperator(methodName, left.copy(), deepCopyBranches());
		res.setSuperscript(getSuperscript());
		return res;
	}

	@Override
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		/*
		 * Eval left
		 */
		currentPosition.add(LEFT_POS);
		left.eval(sigma, theta, gamma, lastExec, newMap, currentPosition);
		removeLast(currentPosition);
		/*
		 * If it is a function pointer, then create a new function call
		 */
		final Object target = left.getAnnotation();
		if (isApply && target instanceof FunctionDefinition) {
			final FunctionDefinition fd = (FunctionDefinition) target;
			/*
			 * Currently, there is no change in the codepath when superscript is
			 * executed: f.apply(...) is exactly equivalent to f(...).
			 */
			final FunctionCall fc;
			final boolean hasCall = getSuperscript() instanceof FunctionCall;
			final FunctionCall prevFC = hasCall ? (FunctionCall) getSuperscript() : null;
			if (hasCall && fd.equals(prevFC.getFunctionDefinition())) {
				fc = prevFC;
			} else {
				fc = new FunctionCall(fd, deepCopyBranches());
			}
			setSuperscript(fc);
			fc.eval(sigma, theta, gamma, lastExec, newMap, currentPosition);
			setAnnotation(fc.getAnnotation());
		} else {
			/*
			 * Otherwise, evaluate branches and proceed to evaluation
			 */
			evalEveryBranchWithProjection(sigma, theta, gamma, lastExec, newMap, currentPosition);
			// Check everything for fields
			final Stream<?> argsstr = getBranchesAnnotationStream();
			final Object[] args = argsstr.toArray();
			// collect any field indices
			Stream<Object> str = Arrays.stream(args).parallel();
			str = str.filter(o -> Field.class.isAssignableFrom(o.getClass()));
			int[] fieldIndexes = str.mapToInt(o -> ArrayUtils.indexOf(args, o)).toArray();
			// if there are any fields, do a field apply:
			// TODO: figure out how to deal with field application of dot calls
			final boolean fieldTarget = target == null ? false : Field.class.isAssignableFrom(target.getClass());
			if (fieldTarget || fieldIndexes.length > 0) {
				/*
				 * Run on every element of the field, and at each iteration use the
				 * current annotation as corresponding element for the field. Once
				 * done, set the entire field as annotation.
				 */
				final Field res = Field.apply(unwrapper, fieldTarget, fieldIndexes, target, sigma, theta, gamma, lastExec, newMap, currentPosition);
				setAnnotation(res);
			} else {
				evalOnTarget(target, sigma, theta, gamma, lastExec, newMap, currentPosition);
			}
		}
	}
	
	private void evalOnTarget(final Object target, final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		final Object[] args = getBranchesAnnotationStream().toArray();
		final Method[] ms = target.getClass().getMethods();
		Method bestMethod = null;
		int score = Integer.MIN_VALUE;
		/*
		 * Keep track of required downcast
		 */
		TIntList[] downCast = null;
		for (final Method m : ms) {
			if (m.getParameterCount() == args.length && methodName.equals(m.getName())) {
				final Class<?>[] params = m.getParameterTypes();
				int p = 0;
				boolean compatible = true;
				TIntList[] dc = null;
				for (int i = 0; compatible && i < args.length; i++) {
					final Class<?> expected = params[i];
					final Object actual = args[i];
					if (expected.isAssignableFrom(actual.getClass())) {
						p++;
					} else if ((expected.equals(Integer.TYPE) || expected.equals(Integer.class)) && actual instanceof Double) {
						dc = lazyInit(dc, DOWNCAST_INT, args.length, i);
					} else if ((expected.equals(Long.TYPE) || expected.equals(Long.class)) && actual instanceof Double) {
						dc = lazyInit(dc, DOWNCAST_LONG, args.length, i);
					} else if ((expected.equals(Float.TYPE) || expected.equals(Float.class)) && actual instanceof Double) {
						dc = lazyInit(dc, DOWNCAST_FLOAT, args.length, i);
					} else {
						/*
						 * Not compatible
						 */
						compatible = false;
					}
				}
				if (compatible && p > score) {
					bestMethod = m;
					score = p;
					downCast = dc;
				}
			}
		}
		if (bestMethod == null) {
			throw new NoSuchMethodError("Method " + methodName + "/" + args.length + " does not exist in class " + target.getClass());
		}
		if (downCast != null) {
			for (int i = 0; i < downCast.length; i++) {
				final TIntList cast = downCast[i];
				if (cast != null) {
					final int type = i;
					cast.forEach((j) -> {
						switch (type) {
						case DOWNCAST_INT:
							args[j] = (int) FastMath.round((Double) args[j]);
							break;
						case DOWNCAST_LONG:
							args[j] = FastMath.round((Double) args[j]);
							break;
						case DOWNCAST_FLOAT:
							args[j] = ((Double) args[j]).floatValue();
							break;
						default:
							throw new IllegalStateException();
						}
						return true;
					});
				}
			}
		}
		try {
			setAnnotation(bestMethod.invoke(target, args));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			L.error(e);
			throw new IllegalStateException("Cannot invoke " + bestMethod + " with arguments " + Arrays.toString(args) + " on " + target, e);
		}
	}
	
	private static TIntList[] lazyInit(final TIntList[] dc, final int type, final int max, final int cur) {
		TIntList[] res = dc == null ? new TIntList[3] : dc;
		if (res[type] == null) {
			res[type] = new TIntArrayList(max - cur);
		}
		res[type].add(cur);
		return res;
	}
	
	
	@Override
	protected String innerAsString() {
		return left + "." + methodName + "(" + getBranches() + ")";
	}

}
