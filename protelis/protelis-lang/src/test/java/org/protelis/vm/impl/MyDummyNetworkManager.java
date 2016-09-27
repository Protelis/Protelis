/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
/**
 * 
 */
package org.protelis.vm.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.NetworkManager;
import org.protelis.vm.util.CodePath;

/**
 * DummyNetworkManager2.
 *
 */
public class MyDummyNetworkManager implements NetworkManager {
    private final MyDummyEnvironment env;
    private long nodeId;
    private Map<DeviceUID, Map<CodePath, Object>> msgs;
//    private Map<CodePath, Object> toBeSent;

    /**
     * DummyNetworkManager2.
     * 
     * @param nodeId
     *            node id
     */
    public MyDummyNetworkManager(MyDummyEnvironment env, final long nodeId) {
        this.env = env;
        this.nodeId = nodeId;
        msgs = new LinkedHashMap<>();
    }

    private static DeviceUID getUID(final long n) {
        return new DeviceUID() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return n + "";
            };
        };
    }

    @Override
    public Map<DeviceUID, Map<CodePath, Object>> getNeighborState() {
        final Map<DeviceUID, Map<CodePath, Object>> res = msgs;
        msgs = new LinkedHashMap<>();
        return res;
    }

    @Override
    public void shareState(final Map<CodePath, Object> toSend) {
        Objects.requireNonNull(toSend);
        if (!toSend.isEmpty()) {
            Map<Long, AbstractExecutionContext> map = env.getNeighborhood(nodeId);
            map.keySet().forEach(k -> {
                MyDummyNetworkManager destination = (MyDummyNetworkManager) map.get(k).getNetworkManager();
                    if (destination != null) {
                        /*
                         * The node is running the program. Otherwise, the
                         * program is discarded
                         */
                        destination.msgs.put(getUID(nodeId), toSend);
                    }
                });
        }
    }

}
