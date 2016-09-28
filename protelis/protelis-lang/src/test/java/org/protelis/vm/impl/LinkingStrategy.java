package org.protelis.vm.impl;

import java.util.Map;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.util.CodePath;

/**
 * Strategy according to which the devices are linked together.
 */
public interface LinkingStrategy {
    /**
     * Get the neighbors of the current device.
     * 
     * @param id
     *            current device
     * @param allDevices
     *            map containing all the devices
     * @return neighbors of the current device
     */
    Map<DeviceUID, Map<CodePath, Object>> getNeighbors(DeviceUID id, Map<DeviceUID, Map<CodePath, Object>> allDevices);
}
