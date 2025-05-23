/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.vm.impl

import org.protelis.lang.datatype.DeviceUID
import org.protelis.vm.CodePath
import org.protelis.vm.NetworkManager

/**
 * Stub network manager for testing, in which there are no neighbors and no
 * messages are ever sent.
 */
class SimpleNetworkManager : NetworkManager {

    override fun getNeighborState(): Map<DeviceUID, Map<CodePath, Any>> = emptyMap()

    override fun shareState(toSend: Map<CodePath, Any>) {
        // no-op
    }
}
