/**
 * 
 */
package it.unibo.alchemist.language.protelis.vm.simulatorvm;

import it.unibo.alchemist.external.cern.jet.random.engine.RandomEngine;
import it.unibo.alchemist.language.protelis.util.Device;
import it.unibo.alchemist.language.protelis.util.SimpleDeviceImpl;
import it.unibo.alchemist.language.protelis.vm.AbstractExecutionContext;
import it.unibo.alchemist.model.implementations.molecules.Molecule;
import it.unibo.alchemist.model.implementations.nodes.ProtelisNode;
import it.unibo.alchemist.model.interfaces.IEnvironment;
import it.unibo.alchemist.model.interfaces.IMolecule;
import it.unibo.alchemist.model.interfaces.INode;
import it.unibo.alchemist.model.interfaces.IPosition;
import it.unibo.alchemist.model.interfaces.IReaction;
import it.unibo.alchemist.utils.FasterString;

import java.util.Map;

import com.google.common.collect.MapMaker;

/**
 * @author Danilo Pianini
 *
 */
public class AlchemistExecutionContext extends AbstractExecutionContext {
	
	private static final MapMaker MAPMAKER = new MapMaker();
	private final ProtelisNode node;
	private final IEnvironment<Object> env;
	private final IReaction<Object> react;
	private final Device device;
	private final RandomEngine rand;
	
	public AlchemistExecutionContext(
			final IEnvironment<Object> environment,
			final ProtelisNode localNode,
			final IReaction<Object> reaction,
			final RandomEngine random,
			final AlchemistNetworkManager netmgr) {
		super(netmgr);
		env = environment;
		node = localNode;
		device = new SimpleDeviceImpl(node.getId());
		react = reaction;
		rand = random;
	}
	
	@Override
	public Device getLocalDevice() {
		return device;
	}

	@Override
	public double getCurrentTime() {
		return react.getTau().toDouble();
	}

	@Override
	public double distanceTo(final Device target) {
		final INode<Object> dest = env.getNodeByID((int) target.getId());
		if (dest != null) {
			return env.getDistanceBetweenNodes(node, dest);
		}
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public IPosition getDevicePosition() {
		return env.getPosition(node);
	}

	@Override
	public double nextRandomDouble() {
		return rand.nextDouble();
	}

	@Override
	protected Map<FasterString, Object> currentEnvironment() {
		final Map<FasterString, Object> freshEnv = MAPMAKER.makeMap();
		node.getContents().entrySet().stream().parallel().forEach(e -> {
			final IMolecule key = e.getKey();
			if (key instanceof Molecule) {
				freshEnv.put(((Molecule) key).toFasterString(), e.getValue());
			}
		});
		return freshEnv;
	}

	@Override
	protected void setEnvironment(final Map<FasterString, Object> newEnvironment) {
		newEnvironment.entrySet().forEach(e -> {
			node.setConcentration(new Molecule(e.getKey()), e.getValue());
		});
	}

	@Override
	protected AbstractExecutionContext instance() {
		return new AlchemistExecutionContext(env, node, react, rand, (AlchemistNetworkManager) getNetworkManager());
	}

	@Override
	protected Device deviceFromId(final long id) {
		return new SimpleDeviceImpl(id);
	}
	
}
