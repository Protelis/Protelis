/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.model.implementations.actions;

import it.unibo.alchemist.external.cern.jet.random.engine.RandomEngine;
import it.unibo.alchemist.language.protelis.util.IProgram;
import it.unibo.alchemist.language.protelis.util.ProtelisLoader;
import it.unibo.alchemist.language.protelis.vm.ExecutionContext;
import it.unibo.alchemist.language.protelis.vm.ProtelisVM;
import it.unibo.alchemist.language.protelis.vm.simulatorvm.AlchemistExecutionContext;
import it.unibo.alchemist.language.protelis.vm.simulatorvm.AlchemistNetworkManager;
import it.unibo.alchemist.model.implementations.molecules.Molecule;
import it.unibo.alchemist.model.implementations.nodes.ProtelisNode;
import it.unibo.alchemist.model.interfaces.Context;
import it.unibo.alchemist.model.interfaces.IAction;
import it.unibo.alchemist.model.interfaces.IEnvironment;
import it.unibo.alchemist.model.interfaces.IMolecule;
import it.unibo.alchemist.model.interfaces.INode;
import it.unibo.alchemist.model.interfaces.IReaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

/**
 * @author Danilo Pianini
 *
 */
public class ProtelisProgram extends Molecule implements IAction<Object> {

	private static final long serialVersionUID = 2207914086772704332L;
	private final IEnvironment<Object> environment;
	private final ProtelisNode node;
	private final IReaction<Object> reaction;
	private final IProgram program;
	private final RandomEngine random;
	private transient ProtelisVM vm;
	private boolean computationalCycleComplete;
	
	/**
	 * @param env the environment
	 * @param n the node
	 * @param r the reaction
	 * @param rand the random engine
	 * @param prog the Protelis program
	 * @throws SecurityException if you are not authorized to load required classes
	 * @throws ClassNotFoundException if required classes can not be found
	 */
	public ProtelisProgram(final IEnvironment<Object> env, final ProtelisNode n, final IReaction<Object> r, final RandomEngine rand, final String prog) throws SecurityException, ClassNotFoundException {
		this(n, env, r, rand, programFromString(prog));
	}

	private static IProgram programFromString(final String s) {
		try {
			new URI(s);
			/*
			 * Valid URI: directly parse it.
			 */
			return ProtelisLoader.parse(s);
		} catch (URISyntaxException e) {
			/*
			 * URI is not valid: convert the string into a dummy:/ resource,
			 * then interpret it as a program.
			 */
			return ProtelisLoader.parse(ProtelisLoader.resourceFromString(s));
		}
	}
	
	private ProtelisProgram(final ProtelisNode n, final IEnvironment<Object> env, final IReaction<Object> r, final RandomEngine rand, final IProgram prog) {
		super(prog.getName());
		Objects.requireNonNull(env);
		Objects.requireNonNull(r);
		Objects.requireNonNull(n);
		Objects.requireNonNull(prog);
		Objects.requireNonNull(rand);
		program = prog;
		environment = env;
		node = n;
		random = rand;
		reaction = r;
		final AlchemistNetworkManager netmgr = new AlchemistNetworkManager(environment, node, this);
		node.addNetworkManger(this, netmgr);
		final ExecutionContext ctx = new AlchemistExecutionContext(env, n, r, rand, netmgr);
		vm = new ProtelisVM(prog, ctx);
	}
	
	@Override
	public ProtelisProgram cloneOnNewNode(final INode<Object> n, final IReaction<Object> r) {
		if (n instanceof ProtelisNode) {
			return new ProtelisProgram((ProtelisNode) n, environment, r, random, program);
		}
		throw new IllegalStateException("Can not load a Protelis program on a " + n.getClass() +
				". A " + ProtelisNode.class + " is required.");
	}
	
	@Override
	public void execute() {
		vm.runCycle();
		computationalCycleComplete = true;
	}

	/**
	 * @return the environment
	 */
	protected final IEnvironment<Object> getEnvironment() {
		return environment;
	}

	/**
	 * @return the node
	 */
	protected final ProtelisNode getNode() {
		return node;
	}

	@Override
	public List<? extends IMolecule> getModifiedMolecules() {
		/*
		 * A Protelis program may modify any molecule (global variable)
		 */
		return null;
	}

	@Override
	public Context getContext() {
		/*
		 * A Protelis program never writes in other nodes
		 */
		return Context.LOCAL;
	}

	/**
	 * @return true if the Program has finished its last computation, and is ready to send a new message (used for dependency management)
	 */
	public boolean isComputationalCycleComplete() {
		return computationalCycleComplete;
	}

	/**
	 * Resets the computation status (used for dependency management).
	 */
	public void prepareForComputationalCycle() {
		this.computationalCycleComplete = false;
	}

	private void readObject(final ObjectInputStream stream) throws ClassNotFoundException, IOException {
		stream.defaultReadObject();
		final AlchemistNetworkManager netmgr = new AlchemistNetworkManager(environment, node, this);
		node.addNetworkManger(this, netmgr);
		vm = new ProtelisVM(program, new AlchemistExecutionContext(environment, node, reaction, random, netmgr));
	}
	
}
