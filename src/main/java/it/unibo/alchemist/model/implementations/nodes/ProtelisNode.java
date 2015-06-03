/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.model.implementations.nodes;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.vm.simulatorvm.AlchemistNetworkManager;
import it.unibo.alchemist.model.implementations.actions.ProtelisProgram;
import it.unibo.alchemist.model.implementations.concentrations.Local;

import java.util.Map;
import java.util.Objects;

/**
 * @author Danilo Pianini
 *
 */
public class ProtelisNode extends GenericNode<Object> {

	private static final long serialVersionUID = 7411790948884770553L;
	/**
	 * For each neighbor
	 */
	private final TLongObjectMap<TIntObjectMap<Map<CodePath, Object>>> trees = new TLongObjectHashMap<>(2, 1f);
	private int prevSize = 16;

	/**
	 * Builds a new {@link ProtelisNode}.
	 */
	public ProtelisNode() {
		super(true);
	}

	/**
	 * Updates a {@link ProtelisProgram}'s last execution tree on another node.
	 * 
	 * @param prog
	 *            the program
	 * @param source
	 *            the node sending the execution summary
	 * @param ast
	 *            the relevant part of the other node's program execution
	 */
	public void updateAST(final ProtelisProgram prog, final ProtelisNode source, final Map<CodePath, Object> ast) {
		Objects.requireNonNull(ast);
		final long pid = prog.getId();
		TIntObjectMap<Map<CodePath, Object>> cur = trees.get(pid);
		if (cur == null) {
			cur = new TIntObjectHashMap<>(prevSize, 1f);
			trees.put(pid, cur);
		}
		cur.put(source.getId(), ast);
	}
	
	/**
	 * @param prog
	 *            the program you want to get the theta
	 * @return the last {@link CodePath} -> result Map for the selected program,
	 *         or null if such structure would be identical to the latest
	 *         retrieved.
	 */
	public TIntObjectMap<Map<CodePath, Object>> getTheta(final ProtelisProgram prog) {
		final long pid = prog.getId();
		final TIntObjectMap<Map<CodePath, Object>> temp = trees.remove(pid);
		final TIntObjectMap<Map<CodePath, Object>> res = temp == null ? new TIntObjectHashMap<>(prevSize, 1f) : temp;
		/*
		 * Compute optimal map size for the next round
		 */
		prevSize = res.size() * 3 / 2;
		return res;
	}
	
	@Override
	protected Local createT() {
		return null;
	}
	
	@Override
	public String toString() {
		return Long.toString(getId());
	}

	public AlchemistNetworkManager getNetworkManger() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
