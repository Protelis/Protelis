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
import it.unibo.alchemist.utils.FasterString;

import java.util.Map;

/**
 * @author Danilo Pianini
 *
 */
public class CreateVar extends AbstractAnnotatedTree<Object> {

	private static final long serialVersionUID = -7298208661255971616L;
	private final FasterString var;
	private final boolean definition;
	
	public CreateVar(final String name, final AnnotatedTree<?> value, final boolean isDefinition) {
		this(new FasterString(name), value, isDefinition);
	}

	public CreateVar(final FasterString name, final AnnotatedTree<?> value, final boolean isDefinition) {
		super(value);
		var = name;
		definition = isDefinition;
	}

	@Override
	public AnnotatedTree<Object> copy() {
		return new CreateVar(var, deepCopyBranches().get(0), definition);
	}

	@Override
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		evalEveryBranchWithProjection(sigma, theta, gamma, lastExec, newMap, currentPosition);
		final Object res = getBranch(0).getAnnotation();
		gamma.put(var, res, isDefinition());
		setAnnotation(res);
	}

	@Override
	protected String asString() {
		return var + " = " + getBranch(0);
	}
	
	public boolean isDefinition() {
		return definition;
	}
	
	public FasterString getVarName() {
		return var;
	}

}
