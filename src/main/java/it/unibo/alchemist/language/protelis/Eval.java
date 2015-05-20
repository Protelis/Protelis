/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.ProtelisLoader;
import it.unibo.alchemist.language.protelis.vm.ExecutionContext;
import it.unibo.alchemist.utils.FasterString;
import it.unibo.alchemist.utils.L;

import java.util.Map;

import org.apache.commons.math3.util.Pair;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author Danilo Pianini
 *
 */
public class Eval extends AbstractAnnotatedTree<Object> {

	private static final long serialVersionUID = 8811510896686579514L;
	private static final byte DYN_CODE_INDEX = -1;
	
	/**
	 * @param arg argument whose annotation will be used as a string representing a program
	 */
	public Eval(final AnnotatedTree<?> arg) {
		super(arg);
	}
	
	@Override
	public Eval copy() {
		return new Eval(deepCopyBranches().get(0));
	}

	@Override
	public void eval(final ExecutionContext context) {
		projectAndEval(context);
		final String program = getBranch(0).getAnnotation().toString();
		final Resource progResource = ProtelisLoader.resourceFromString(program);
		try {
			final Pair<AnnotatedTree<?>, Map<FasterString, FunctionDefinition>> result = ProtelisLoader.parse(progResource);
			context.newCallStackFrame(DYN_CODE_INDEX);
			context.putMultipleVariables(result.getSecond());
			final AnnotatedTree<?> toRun = result.getFirst();
			toRun.eval(context);
			setAnnotation(toRun.getAnnotation());
			context.returnFromCallFrame();
		} catch (IllegalArgumentException e) {
			L.error(e);
			throw new IllegalStateException("The following program can't be parsed:\n" + program, e);
		}
	}

	@Override
	protected void asString(final StringBuilder sb, final int i) {
		sb.append("eval(\n");
		getBranch(0).toString(sb, i + 1);
		sb.append(')');
	}

}
