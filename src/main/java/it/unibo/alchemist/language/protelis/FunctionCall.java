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
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.model.interfaces.INode;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Danilo Pianini
 *
 */
public class FunctionCall extends AbstractSATree<AnnotatedTree<?>, Object> {

	private static final long serialVersionUID = 4143090001260538814L;
	private final FunctionDefinition fd;
	private final byte[] stackCode;

	public FunctionCall(final FunctionDefinition functionDefinition, final List<AnnotatedTree<?>> args) {
		super(args);
		Objects.requireNonNull(functionDefinition);
		fd = functionDefinition;
		stackCode = fd.getStackCode();
	}

	@Override
	public FunctionCall copy() {
		/*
		 * Deep copy the arguments
		 */
		final FunctionCall res = new FunctionCall(fd, deepCopyBranches());
		if(!isErased()) {
			res.setAnnotation(null);
			res.setSuperscript(getSuperscript().copy());
		}
		return res;
	}

	protected final AnnotatedTree<?> getBody() {
		return fd.getBody();
	}

	@Override
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		/*
		 * 1. Evaluate all the arguments
		 */
		evalEveryBranchWithProjection(sigma, theta, gamma, lastExec, newMap, currentPosition);
		/*
		 * Inner gamma must hold param values
		 */
		gamma.push();
		forEachWithIndex((i, b) -> {
			gamma.put(fd.getInternalName(i), b.getAnnotation(), true);
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
		final int depth = currentPosition.size();
		currentPosition.addAll(stackCode);
		getSuperscript().eval(sigma, theta, gamma, lastExec, newMap, currentPosition);
		currentPosition.remove(depth, stackCode.length);
		gamma.pop();
		setAnnotation(getSuperscript().getAnnotation());
	}

	@Override
	protected String innerAsString() {
		return fd.getName() + "/" + fd.getArgNumber();
	}
	
	public FunctionDefinition getFunctionDefinition() {
		return fd;
	}

}
