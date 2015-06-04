/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.model.implementations.conditions;

import it.unibo.alchemist.model.implementations.actions.ProtelisProgram;
import it.unibo.alchemist.model.implementations.nodes.ProtelisNode;
import it.unibo.alchemist.model.interfaces.Context;
import it.unibo.alchemist.model.interfaces.INode;

/**
 * @author Danilo Pianini
 *
 */
public class ComputationalRoundComplete extends AbstractCondition<Object> {

	private static final long serialVersionUID = -4113718948444451107L;
	private final ProtelisProgram program;
	
	public ComputationalRoundComplete(final ProtelisNode node, final ProtelisProgram prog) {
		super(node);
		program = prog;
		addReadMolecule(program);
	}

	@Override
	public ComputationalRoundComplete cloneOnNewNode(final INode<Object> n) {
		return new ComputationalRoundComplete((ProtelisNode) n, program);
	}

	@Override
	public Context getContext() {
		return Context.LOCAL;
	}

	@Override
	public double getPropensityConditioning() {
		return isValid() ? 1 : 0;
	}

	@Override
	public boolean isValid() {
		return program.isComputationalCycleComplete();
	}
	
	@Override
	public ProtelisNode getNode() {
		return (ProtelisNode) super.getNode();
	}

}
