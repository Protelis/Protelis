/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.model.implementations.nodes;

import it.unibo.alchemist.language.protelis.vm.NetworkManager;
import it.unibo.alchemist.language.protelis.vm.simulatorvm.AlchemistNetworkManager;
import it.unibo.alchemist.model.implementations.actions.ProtelisProgram;
import it.unibo.alchemist.model.implementations.concentrations.Local;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Danilo Pianini
 *
 */
public class ProtelisNode extends GenericNode<Object> {

	private static final long serialVersionUID = 7411790948884770553L;
	private final Map<ProtelisProgram, AlchemistNetworkManager> netmgrs = new ConcurrentHashMap<>();

	/**
	 * Builds a new {@link ProtelisNode}.
	 */
	public ProtelisNode() {
		super(true);
	}

	@Override
	protected Local createT() {
		return null;
	}

	@Override
	public String toString() {
		return Long.toString(getId());
	}

	/**
	 * Adds a new {@link NetworkManager}.
	 * 
	 * @param program
	 *            the {@link ProtelisProgram}
	 * @param netmgr
	 *            the {@link AlchemistNetworkManager}
	 */
	public void addNetworkManger(final ProtelisProgram program, final AlchemistNetworkManager netmgr) {
		netmgrs.put(program, netmgr);
	}

	/**
	 * @param program
	 *            the {@link ProtelisProgram}
	 * @return the {@link AlchemistNetworkManager} for this specific
	 *         {@link ProtelisProgram}
	 */
	public AlchemistNetworkManager getNetworkManager(final ProtelisProgram program) {
		Objects.requireNonNull(program);
		return netmgrs.get(program);
	}
	
}
