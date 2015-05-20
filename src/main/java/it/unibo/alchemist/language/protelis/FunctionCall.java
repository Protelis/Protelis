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
import it.unibo.alchemist.language.protelis.vm.ExecutionContext;

import java.util.List;
import java.util.Objects;

/**
 * @author Danilo Pianini
 *
 */
public class FunctionCall extends AbstractSATree<AnnotatedTree<?>, Object> {

	private static final long serialVersionUID = 4143090001260538814L;
	private final FunctionDefinition fd;
	private final byte[] stackCode;

	/**
	 * @param functionDefinition
	 *            the definition of the function
	 * @param args
	 *            the arguments. Must be in the same number of the
	 *            {@link FunctionDefinition}'s expected arguments
	 */
	public FunctionCall(final FunctionDefinition functionDefinition, final List<AnnotatedTree<?>> args) {
		super(args);
		Objects.requireNonNull(functionDefinition);
		fd = functionDefinition;
		if (fd.getArgNumber() != args.size()) {
			throw new IllegalArgumentException(fd + " must be invoked with " + fd.getArgNumber()
					+ " arguments. You have tried with " + args + ", which are " + args.size()
					+ ". You know " + args.size() + " is not equal to " + fd.getArgNumber() + ", don't you?");
		}
		stackCode = fd.getStackCode();
	}

	@Override
	public FunctionCall copy() {
		/*
		 * Deep copy the arguments
		 */
		final FunctionCall res = new FunctionCall(fd, deepCopyBranches());
		if (!isErased()) {
			res.setAnnotation(null);
			res.setSuperscript(getSuperscript().copy());
		}
		return res;
	}

	/**
	 * @return the function body
	 */
	protected final AnnotatedTree<?> getBody() {
		return fd.getBody();
	}

	@Override
	public void eval(final ExecutionContext context) {
		/*
		 * 1. Evaluate all the arguments
		 */
		projectAndEval(context);
		/*
		 * Inner gamma must hold param values
		 */
		context.newCallStackFrame(stackCode);
		forEachWithIndex((i, b) -> {
			context.putVariable(fd.getInternalName(i), b.getAnnotation(), true);
		});
		/*
		 * 2. Load a fresh body as superscript
		 */
		if (isErased()) {
			setSuperscript(getBody());
		}
		/*
		 * Evaluate the body and copy its result in the annotation
		 */
		getSuperscript().eval(context);
		context.returnFromCallFrame();
		setAnnotation(getSuperscript().getAnnotation());
	}

	@Override
	protected void innerAsString(final StringBuilder sb, final int indent) {
		sb.append(fd.getName());
		sb.append('/');
		sb.append(fd.getArgNumber());
		sb.append('(');
		fillBranches(sb, indent, ',');
		sb.append(')');
	}

	/**
	 * @return the {@link FunctionDefinition}
	 */
	public FunctionDefinition getFunctionDefinition() {
		return fd;
	}

}
