/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.model.implementations.actions;

import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.model.implementations.nodes.ProtelisNode;
import it.unibo.alchemist.model.interfaces.IEnvironment;
import it.unibo.alchemist.model.interfaces.INeighborhood;
import it.unibo.alchemist.model.interfaces.INode;
import it.unibo.alchemist.model.interfaces.IReaction;

import java.util.Map;
import java.util.Objects;

/**
 * @author Danilo Pianini
 *
 */
public class SendToNeighbor extends AbstractLocalAction<Object> {
	
	private static final long serialVersionUID = -8826563176323247613L;
	private final ProtelisNode myNode;
	private final ProtelisProgram prog;
	private final IEnvironment<Object> env;
//	private INeighborhood<Object> neighCache;// = new TIntArrayList();
	private Map<CodePath, Object> astCache;

	public SendToNeighbor(final ProtelisNode node, final ProtelisProgram program) {
		super(node);
		Objects.requireNonNull(program);
		prog = program;
		env = prog.getEnvironment();
		myNode = node;
	}

	@Override
	public SendToNeighbor cloneOnNewNode(final INode<Object> n, final IReaction<Object> r) {
		return new SendToNeighbor((ProtelisNode)n, prog);
	}

	@Override
	public void execute() {
		final INeighborhood<Object> neigh = env.getNeighborhood(getNode());
		final Map<CodePath, Object> ast = prog.getLastProgramExecution();
		if(!neigh.isEmpty()) {
			/*
			 * Note: since both astCache and neighCache are null at the first
			 * execution, this is false and is sufficient for preventing
			 * NullPointerExceptions in the code below unless conditions on the
			 * neighborhood cache are evaluated prior to checking this boolean.
			 * Also, if the initialization phase changes, this may turn not
			 * sufficient.
			 */
			final boolean sameExec = ast.equals(astCache);
			for(final INode<Object> n: neigh) {
				if(n instanceof ProtelisNode) {
					final ProtelisNode node = ((ProtelisNode)n);
					final int id = node.getId();
					// Disable update caching for now, since it seems to be causing problems
					//final boolean noUpdate = sameExec && neighCache.contains(id);
					final boolean noUpdate = false; 
					node.updateAST(prog, myNode, noUpdate ? null : ast);
				}
			}
		}
		/*
		 * Update cache system
		 */
//		neighCache = neigh;
		astCache = ast;
		/*
		 * Reset internal program
		 */
		prog.setCycleComplete();
	}
	
	@Override
	public ProtelisNode getNode(){
		return myNode;
	}

}
