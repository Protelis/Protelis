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
import it.unibo.alchemist.model.implementations.actions.ProtelisProgram;
import it.unibo.alchemist.model.implementations.concentrations.Local;
import it.unibo.alchemist.model.implementations.molecules.Molecule;
import it.unibo.alchemist.model.interfaces.IMolecule;
import it.unibo.alchemist.model.interfaces.INode;
import it.unibo.alchemist.model.interfaces.IReaction;
import it.unibo.alchemist.utils.FasterString;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
	
	private static <T> T unsupported() {
		throw new UnsupportedOperationException("For security reasons, this operation is not available.");
	}
	
	/**
	 * @return a safe view of this node's internals
	 */
	public Self getSelf() {
		return new Self(this);
	}
	
	/**
	 * Returns a safe view of the node.
	 * 
	 * @author Danilo Pianini
	 * @author Jacob Beal
	 *
	 */
	public static final class Self implements INode<Object> {
		
		private static final long serialVersionUID = 4930377407674040670L;
		private final Map<FasterString, Object> gamma;
		private final ProtelisNode parent;
		
		private Self(final ProtelisNode target) {
			parent = target;
			gamma = parent.getContents().entrySet().stream()
					.filter(e -> e.getKey() instanceof Molecule)
					.collect(Collectors.toMap(e -> ((Molecule) e.getKey()).toFasterString() , Map.Entry::getValue));
		}
		
		@Override
		public Iterator<IReaction<Object>> iterator() {
			unsupported();
			return null;
		}

		@Override
		public int compareTo(final INode<Object> o) {
			return parent.compareTo(o);
		}
		
		@Override
		public boolean equals(final Object o) {
			if (o instanceof Self) {
				return getId() == ((Self) o).getId();
			}
			return false;
		}

		@Override
		public int hashCode() {
			return parent.hashCode();
		}
		
		@Override
		public void addReaction(final IReaction<Object> r) {
			unsupported();
		}

		@Override
		public boolean contains(final IMolecule mol) {
			return parent.contains(mol);
		}

		@Override
		public int getChemicalSpecies() {
			return parent.getChemicalSpecies();
		}

		@Override
		public Object getConcentration(final IMolecule mol) {
			unsupported();
			return null;
		}

		@Override
		public int getId() {
			return parent.getId();
		}

		@Override
		public List<? extends IReaction<Object>> getReactions() {
			unsupported();
			return null;
		}

		@Override
		public void removeReaction(final IReaction<Object> r) {
			unsupported();
		}

		@Override
		public void setConcentration(final IMolecule mol, final Object c) {
			unsupported();
		}

		@Override
		public Map<IMolecule, Object> getContents() {
			unsupported();
			return null;
		}
		
		/**
		 * @param id
		 *            the variable name
		 * @return true if the variable is present
		 */
		public boolean hasEnvironmentVariable(final String id) {
			return gamma.containsKey(new FasterString(id));
		}
		
		/**
		 * @param id
		 *            the variable name
		 * @return the value of the variable if present, false otherwise
		 */
		public Object getEnvironmentVariable(final String id) {
			return Optional.ofNullable(gamma.get(new FasterString(id))).orElse(false);
		}

		/**
		 * @param id
		 *            the variable name
		 * @param v
		 *            the value that should be associated
		 */
		public void putEnvironmentVariable(final String id, final Object v) {
			gamma.put(new FasterString(id), v);
		}
		
		/**
		 * @param id
		 *            the variable name
		 * @param v
		 *            the value that should be associated
		 */
		public void removeEnvironmentVariable(final String id, final Object v) {
			gamma.put(new FasterString(id), v);
		}
		
		/**
		 * Writes the current environment permanent.
		 */
		public void commit() {
			gamma.forEach((k, v) -> {
				parent.setConcentration(new Molecule(k), v);
			});
		}
		
		/**
		 * @return a safe view of the internal environment
		 */
		public Map<FasterString, Object> getGamma() {
			return new HashMap<>(gamma);
		}
		
		@Override
		public String toString() {
			return parent.toString() + "[Safe View]";
		}

		
	}
	
}
