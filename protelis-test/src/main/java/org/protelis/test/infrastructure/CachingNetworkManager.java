/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.test.infrastructure;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.CodePath;
import org.protelis.vm.NetworkManager;

/**
 * Abstraction of networking used by the virtual machine: at each execution
 * round, the VM needs to be able to access the most recent state received from
 * neighbors and to be able to update the state that it is exporting to
 * neighbors.
 *
 * <p>
 * Note, however, that there is no requirement that state actually be sent or
 * received in each round: it is up to the individual implementation of a
 * NetworkManager to best optimize to best trade off between effective
 * state sharing and efficiency.
 *
 *  <p>
 * This implementation just tracks the most recent message sent from this
 * device and the most recent messages received from each neighbor.
 */
public class CachingNetworkManager implements NetworkManager {
    private ImmutableMap<CodePath, Object> sendCache;
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
     * External access to put messages into the received messages cache.
     *
     * @param neighbor
     *            sender
     * @param message
     *            message to be received
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
        return Collections.unmodifiableMap(receiveCache);
    }

    /**
     * Called by {@link org.protelis.vm.ProtelisVM} during execution to send its current shared
     * state to neighbors. The call is serial within the execution, so this
     * should probably queue up a message to be sent, rather than actually
     * carrying out a lengthy operation during this call.
     *
     * @param toSend
     *            Shared state to be transmitted to neighbors.
     */
    @Override
    public void shareState(final Map<CodePath, Object> toSend) {
        sendCache = ImmutableMap.copyOf(toSend);
    }

}
