/**
 * 
 */
package it.unibo.alchemist.language.protelis.vm;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import it.unibo.alchemist.external.cern.jet.random.engine.MersenneTwister;
import it.unibo.alchemist.external.cern.jet.random.engine.RandomEngine;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.model.implementations.nodes.ProtelisNode;
import it.unibo.alchemist.model.implementations.positions.Continuous2DEuclidean;
import it.unibo.alchemist.model.interfaces.IMolecule;
import it.unibo.alchemist.model.interfaces.INode;
import it.unibo.alchemist.model.interfaces.IPosition;
import it.unibo.alchemist.model.interfaces.IReaction;
import it.unibo.alchemist.utils.FasterString;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Danilo Pianini
 *
 */
public class LocalDummyContext extends AbstractExecutionContext {
	
	private final RandomEngine rng = new MersenneTwister();
	
	private static class DummyDevice implements INode<Object> {
		private static final long serialVersionUID = -4804905144759361059L;
		private static final List<IReaction<Object>> EMPTY = Collections.emptyList();
		private final Map<IMolecule, Object> environment = new LinkedHashMap<>();
		@Override
		public Iterator<IReaction<Object>> iterator() {
			return EMPTY.iterator();
		}
		@Override
		public int compareTo(final INode<Object> o) {
			return 0;
		}
		@Override
		public void addReaction(final IReaction<Object> r) {
			throw new UnsupportedOperationException();
		}
		@Override
		public boolean contains(final IMolecule mol) {
			return false;
		}
		@Override
		public int getChemicalSpecies() {
			return 0;
		}
		@Override
		public Object getConcentration(final IMolecule mol) {
			return environment.get(mol);
		}
		@Override
		public int getId() {
			return 0;
		}
		@Override
		public List<? extends IReaction<Object>> getReactions() {
			return EMPTY;
		}
		@Override
		public void removeReaction(final IReaction<Object> r) {
			throw new UnsupportedOperationException();
		}
		@Override
		public void setConcentration(final IMolecule mol, final Object c) {
			environment.put(mol, c);
		}
		@Override
		public Map<IMolecule, Object> getContents() {
			return new LinkedHashMap<>(environment);
		}
		@Override
		public boolean equals(final Object o) {
			return o instanceof DummyDevice;
		}
		@Override
		public int hashCode() {
			return 0;
		}
	}
	
	private INode<Object> dummy = new DummyDevice();
	
	public LocalDummyContext(final Map<FasterString, ? extends Object> availableFunctions) {
		super(availableFunctions, new TIntObjectHashMap<>());
	}

	@Override
	public INode<Object> getLocalDevice() {
		return new ProtelisNode();
	}

	@Override
	public double getCurrentTime() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double distanceTo(final INode<Object> target) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public IPosition getDevicePosition() {
		return new Continuous2DEuclidean(0, 0);
	}
	
	@Override
	public double nextRandomDouble() {
		return rng.nextDouble();
	}

	@Override
	protected AbstractExecutionContext restrictedInstance(final TIntObjectMap<Map<CodePath, Object>> restrictedTheta) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected INode<Object> deviceFromId(final int id) {
		return dummy;
	}

}
