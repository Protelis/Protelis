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
import it.unibo.alchemist.model.interfaces.IReaction;

import java.util.Map;
import java.util.Objects;

/**
 * Delta time.
 * 
 * @author Danilo Pianini
 *
 */
public class Dt extends AbstractAnnotatedTree<Double> {

	private static final long serialVersionUID = -583345937082081400L;
	private double lastT;
	private final IReaction<Object> r;
	
	/**
	 * @param reaction the reaction that will be used to compute the current time
	 */
	public Dt(final IReaction<Object> reaction) {
		super();
		Objects.requireNonNull(reaction);
		r = reaction;
	}
	
	@Override
	public AnnotatedTree<Double> copy() {
		return new Dt(r);
	}

	@Override
	public void eval(final INode<Object> sigma, final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec, final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		setAnnotation(r.getTau().toDouble() - lastT);
		lastT = r.getTau().toDouble();
	}

	@Override
	protected String asString() {
		return "dT";
	}

}
