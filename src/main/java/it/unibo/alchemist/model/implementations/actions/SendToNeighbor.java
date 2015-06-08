/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.model.implementations.actions;

import it.unibo.alchemist.language.protelis.vm.simulatorvm.AlchemistNetworkManager;
import it.unibo.alchemist.model.implementations.nodes.ProtelisNode;
import it.unibo.alchemist.model.interfaces.INode;
import it.unibo.alchemist.model.interfaces.IReaction;

import java.util.Objects;

/**
 * @author Danilo Pianini
 *
 */
public class SendToNeighbor extends AbstractLocalAction<Object> {
	
	private static final long serialVersionUID = -8826563176323247613L;
	private final ProtelisNode myNode;
	private final ProtelisProgram prog;

	public SendToNeighbor(final ProtelisNode node, final ProtelisProgram program) {
		super(node);
		Objects.requireNonNull(program);
		prog = program;
		myNode = node;
	}

	@Override
	public SendToNeighbor cloneOnNewNode(final INode<Object> n, final IReaction<Object> r) {
		return new SendToNeighbor((ProtelisNode) n, prog);
	}

	@Override
	public void execute() {
		final AlchemistNetworkManager mgr = myNode.getNetworkManager(prog);
		Objects.requireNonNull(mgr);
		mgr.simulateMessageArrival();
		prog.prepareForComputationalCycle();
	}
	
	@Override
	public ProtelisNode getNode() {
		return myNode;
	}

}
