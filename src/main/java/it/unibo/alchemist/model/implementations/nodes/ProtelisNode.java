/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.model.implementations.nodes;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import it.unibo.alchemist.language.protelis.vm.simulatorvm.AlchemistNetworkManager;
import it.unibo.alchemist.model.implementations.actions.ProtelisProgram;
import it.unibo.alchemist.model.implementations.concentrations.Local;

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

	public void addNetworkManger(ProtelisProgram program, AlchemistNetworkManager netmgr) {
		netmgrs.put(program, netmgr);
	}
	
	public AlchemistNetworkManager getNetworkManager(ProtelisProgram program) {
		Objects.requireNonNull(program);
		return netmgrs.get(program);
	}

}
