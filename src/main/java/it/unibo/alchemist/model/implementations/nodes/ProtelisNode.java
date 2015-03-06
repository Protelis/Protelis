/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
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
	private int prevSize = 16;

	public ProtelisNode() {
		super(true);
	}

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
	public void setConcentration(final IMolecule m, final Object v) {
		Objects.requireNonNull(m);
		super.setConcentration(m, v);
		if (v == null) {
			final FasterString mfs;
			if (m instanceof Molecule) {
				mfs = ((Molecule) m).toFasterString();
			} else if (m instanceof ProtelisProgram) {
				mfs = ((ProtelisProgram) m).getProgramIDAsFasterString();
			} else {
				mfs = new FasterString(m.toString());
			}
			gamma.remove(mfs);
		} else if (m instanceof Molecule) {
			final Molecule mol = (Molecule) m;
			gamma.put(mol.toFasterString(), v);
		} else if (m instanceof ProtelisProgram) {
			final ProtelisProgram mol = (ProtelisProgram) m;
			gamma.put(mol.getProgramIDAsFasterString(), v);
		}
	}
	
	@Override
	public String toString() {
		return Long.toString(getId());
	}
	
}
