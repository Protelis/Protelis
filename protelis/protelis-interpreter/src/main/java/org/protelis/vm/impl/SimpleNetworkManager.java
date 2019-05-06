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

import java.util.Collections;
import java.util.Map;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.CodePath;
import org.protelis.vm.NetworkManager;

/**
 * Stub network manager for testing, in which there are no neighbors and no
 * messages are ever sent.
 */
public final class SimpleNetworkManager implements NetworkManager {

    @Override
    public Map<DeviceUID, Map<CodePath, Object>> getNeighborState() {
        return Collections.emptyMap();
    }

    @Override
    public void shareState(final Map<CodePath, Object> toSend) {
    }

}
