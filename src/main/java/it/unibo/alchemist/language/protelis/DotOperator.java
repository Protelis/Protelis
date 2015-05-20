/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import it.unibo.alchemist.language.protelis.datatype.Field;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.ReflectionUtils;
import it.unibo.alchemist.language.protelis.vm.ExecutionContext;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;


/**
 * @author Danilo Pianini
 *
 */
public class DotOperator extends AbstractSATree<Object, Object> {
	
	private static final long serialVersionUID = -9128116355271771986L;
	private static final byte LEFT_POS = -1;
	private final boolean isApply;
	private final String methodName;
	private final AnnotatedTree<?> left;

	/**
	 * @param name function (or method) name
	 * @param target Protelis sub-program that annotates itself with the target of this call
	 * @param args arguments of the function
	 */
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
	public void eval(final ExecutionContext context) {
		/*
		 * Eval left
		 */
		context.newCallStackFrame(LEFT_POS);
		left.eval(context);
		context.returnFromCallFrame();
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
			fc.eval(context);
			setAnnotation(fc.getAnnotation());
		} else {
			/*
			 * Otherwise, evaluate branches and proceed to evaluation
			 */
			evalEveryBranchWithProjection(context);
			/*
			 *  Check everything for fields
			 */
			final Stream<?> argsstr = getBranchesAnnotationStream();
			final Object[] args = argsstr.toArray();
			/*
			 *  collect any field indices
			 */
			Stream<Object> str = Arrays.stream(args).parallel();
			str = str.filter(o -> Field.class.isAssignableFrom(o.getClass()));
			final int[] fieldIndexes = str.mapToInt(o -> ArrayUtils.indexOf(args, o)).toArray();
			/*
			 *  if there are any fields, do a field apply:
			 */
			final boolean fieldTarget = target instanceof Field;
			if (fieldTarget || fieldIndexes.length > 0) {
				/*
				 * Run on every element of the field, and at each iteration use the
				 * current annotation as corresponding element for the field. Once
				 * done, set the entire field as annotation.
				 */
				final Field res = Field.apply(
						(actualT, actualA) -> ReflectionUtils.invokeBestNotStatic(actualT, methodName, actualA),
						fieldTarget, fieldIndexes, target, args);
				setAnnotation(res);
			} else {
				final Object annotation = ReflectionUtils.invokeBestNotStatic(target, methodName, args);
				setAnnotation(annotation);
			}
		}
	}
	
	@Override
	protected void innerAsString(final StringBuilder sb, final int indent) {
		sb.append('\n');
		left.toString(sb, indent);
		sb.append('\n');
		indent(sb, indent);
		sb.append('.');
		sb.append(methodName);
		sb.append(" (");
		fillBranches(sb, indent, ',');
		sb.append(')');
	}

}
