/**
 * 
 */
package it.unibo.alchemist.language.protelis.vm.simulatorvm;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import it.unibo.alchemist.language.protelis.protelis.Program;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.IProgram;
import it.unibo.alchemist.language.protelis.vm.NetworkManager;
import it.unibo.alchemist.model.implementations.actions.ProtelisProgram;
import it.unibo.alchemist.model.implementations.nodes.ProtelisNode;
import it.unibo.alchemist.model.interfaces.IEnvironment;

/**
 * Emulates a {@link NetworkManager}. This particular network manager does not
 * send messages istantly. Instead, it records the last message to send, and
 * only when {@link #simulateMessageArrival()} is called the transfer is
 * actually done.
 * 
 * @author Danilo Pianini
 *
 */
public final class AlchemistNetworkManager implements NetworkManager, Serializable {
	
	private static final long serialVersionUID = -7028533174885876642L;
	private final IEnvironment<Object> env;
	private final ProtelisNode node;
	private final ProtelisProgram prog;
	private TLongObjectMap<Map<CodePath, Object>> msgs = new TLongObjectHashMap<>();
	private Map<CodePath, Object> toBeSent;

	public AlchemistNetworkManager(final IEnvironment<Object> environment, final ProtelisNode local, final ProtelisProgram program) {
		env = environment;
		node = local;
		prog = program;
	}
	
	@Override
	public TLongObjectMap<Map<CodePath, Object>> takeMessages() {
		final TLongObjectMap<Map<CodePath, Object>> res = msgs;
		msgs = new TLongObjectHashMap<>();
		return res;
	}

	@Override
	public void sendMessage(final Map<CodePath, Object> toSend) {
		toBeSent = toSend;
	}
	
	/**
	 *  
	 */
	public void simulateMessageArrival() {
		Objects.requireNonNull(toBeSent);
		env.getNeighborhood(node).forEach(n -> {
			if (n instanceof ProtelisNode) {
				final AlchemistNetworkManager destination = ((ProtelisNode) n).getNetworkManager(prog);
				Objects.requireNonNull(destination);
				destination.msgs.put(node.getId(), toBeSent);
			}
		});
		toBeSent = null;
	}
	
}
