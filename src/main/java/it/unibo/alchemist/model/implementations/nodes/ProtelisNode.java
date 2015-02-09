/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.model.implementations.nodes;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.model.implementations.actions.ProtelisProgram;
import it.unibo.alchemist.model.implementations.concentrations.Local;
import it.unibo.alchemist.model.implementations.molecules.Molecule;
import it.unibo.alchemist.model.interfaces.IMolecule;
import it.unibo.alchemist.utils.FasterString;

import java.util.HashMap;
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
	private final Map<FasterString, Object> gamma = new HashMap<>();
	private final TLongObjectMap<TIntObjectMap<Map<CodePath, Object>>> trees = new TLongObjectHashMap<>(2, 1f);
	private final TLongObjectMap<TIntObjectMap<Map<CodePath, Object>>> treesCache = new TLongObjectHashMap<>(2, 1f);
	private final TLongObjectMap<TIntList> acks = new TLongObjectHashMap<>();
	private final TLongObjectMap<TIntList> acksCache = new TLongObjectHashMap<>();
	private int prevSize = 16;
	private boolean gotUpdate;

	public ProtelisNode() {
		super(true);
	}

	public void updateAST(final ProtelisProgram prog, final ProtelisNode source, final Map<CodePath, Object> ast) {
		/*
		 * If ast is null, this is just a notification and the previous value
		 * should be considered valid.
		 */
		final long pid = prog.getId();
		if(ast == null) {
			/*
			 * Alive ack: the node has computed the same value it did before
			 */
			TIntList progAcks = acks.get(pid);
			if(progAcks == null) {
				progAcks = new TIntArrayList(prevSize);
				acks.put(pid, progAcks);
			}
			progAcks.add(source.getId());
		} else {
			gotUpdate = true;
			TIntObjectMap<Map<CodePath, Object>> cur = trees.get(pid);
			if(cur == null) {
				cur = new TIntObjectHashMap<>(prevSize, 1f);
				trees.put(pid, cur);
			}
			cur.put(source.getId(), ast);
		}
	}
	
	public Map<FasterString, Object> getGamma() {
		return new HashMap<>(gamma);
	}
	
	/**
	 * @param prog
	 *            the program you want to get the theta
	 * @return the last {@link CodePath} -> result Map for the selected program,
	 *         or null if such structure would be identical to the latest
	 *         retrieved.
	 */
	public TIntObjectMap<Map<CodePath, Object>> getTheta(final ProtelisProgram prog) {
		/*
		 * If !gotUpdate && everybody notified -> null
		 */
		final long pid = prog.getId();
		final TIntList receivedAcks = acks.get(pid);
		if(!gotUpdate) {
			if (receivedAcks == null) {
				return null;
			} else {
				final TIntList expectedAcks = acksCache.get(pid);
				if(expectedAcks.size() == receivedAcks.size()) {
					receivedAcks.sort();
					if(receivedAcks.equals(expectedAcks)) {
						/*
						 * Cache hit!
						 */
						receivedAcks.clear();
						return null;
					}
				}
			}
		}
		/*
		 * The value of the previous computation for every node that has
		 * notified must be added to the returned ast
		 */
		final TIntObjectMap<Map<CodePath, Object>> temp = trees.remove(pid);
		final TIntObjectMap<Map<CodePath, Object>> res = temp == null? new TIntObjectHashMap<>(prevSize, 1f) : temp;
		final TIntObjectMap<Map<CodePath, Object>> previous = treesCache.get(pid);
		if(receivedAcks != null) {
			receivedAcks.forEach( (id) -> {
				/*
				 * I must have had at least one update to enter here, so no null-check is needed.
				 */
				res.put(id, previous.get(id));
				return true;
			});
			receivedAcks.clear();
		}
		final TIntList expectedAcks = new TIntArrayList(res.keys());
		expectedAcks.sort();
		acksCache.put(pid, expectedAcks);
//		updateCache(pid, receivedAcks);
		treesCache.put(pid, res);
		/*
		 * Compute optimal map size for the next round
		 */
		prevSize = res.size() * 3 / 2;
		gotUpdate = false;
		return res;
	}
	
	@Override
	protected Local createT() {
		return null;
	}
	
	@Override
	public void setConcentration(final IMolecule m, final Object v) {
		Objects.requireNonNull(m);
		super.setConcentration(m, v);
		if( v == null ) {
			gamma.remove(m);
		} else if(m instanceof Molecule) {
			final Molecule mol = (Molecule)m;
			gamma.put(mol.toFasterString(), v);
		} else if(m instanceof ProtelisProgram) {
			final ProtelisProgram mol = (ProtelisProgram)m;
			gamma.put(mol.getProgramIDAsFasterString(), v);
		}
	}
	
	@Override
	public String toString() {
		return Long.toString(getId());
	}

}
