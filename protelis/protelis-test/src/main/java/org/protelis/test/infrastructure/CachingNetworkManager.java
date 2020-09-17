package org.protelis.test.infrastructure;

import java.util.HashMap;
import java.util.Map;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.CodePath;
import org.protelis.vm.NetworkManager;

/**
 * Abstraction of networking used by the virtual machine: at each execution
 * round, the VM needs to be able to access the most recent state received from
 * neighbors and to be able to update the state that it is exporting to
 * neighbors.
 * 
 * Note, however, that there is no requirement that state actually be sent or
 * received in each round: it is up to the individual implementation of a
 * NetworkManager to best optimize in order to best trade off between effective
 * state sharing and efficiency.
 * 
 * This simple implementation just tracks the most recent message sent from this
 * device and the most recent messages received from each neighbor.
 */
public class CachingNetworkManager implements NetworkManager {
    private Map<CodePath, Object> sendCache;
    private final Map<DeviceUID, Map<CodePath, Object>> receiveCache = new HashMap<>();

    /**
     * External access to sending cache.
     * 
     * @return cache
     */
    public Map<CodePath, Object> getSendCache() {
        return sendCache;
    }

    /**
     * External access to put messages into receive cache.
     * 
     * @param neighbor
     *            sender
     * @param message
     *            messege to be received
     */
    public void receiveFromNeighbor(final DeviceUID neighbor, final Map<CodePath, Object> message) {
        receiveCache.put(neighbor, message);
    }

    /**
     * External access to note when a device is no longer a neighbor, wiping
     * cache.
     * 
     * @param neighbor
     *            to be removed
     */
    public void removeNeighbor(final DeviceUID neighbor) {
        receiveCache.remove(neighbor);
    }

    /**
     * Called by {@link org.protelis.vm.ProtelisVM} during execution to collect the most recent
     * information available from neighbors. The call is serial within the
     * execution, so this should probably poll state maintained by a separate
     * thread, rather than gathering state during this call.
     * 
     * @return A map associating each neighbor with its shared state. The object
     *         returned should not be modified, and {@link org.protelis.vm.ProtelisVM} will not
     *         change it either.
     */
    @Override
    public Map<DeviceUID, Map<CodePath, Object>> getNeighborState() {
        return receiveCache;
    }

    /**
     * Called by {@link org.protelis.vm.ProtelisVM} during execution to send its current shared
     * state to neighbors. The call is serial within the execution, so this
     * should probably queue up a message to be sent, rather than actually
     * carrying out a lengthy operations during this call.
     * 
     * @param toSend
     *            Shared state to be transmitted to neighbors.
     */
    @Override
    public void shareState(final Map<CodePath, Object> toSend) {
        sendCache = toSend;
    }

}
