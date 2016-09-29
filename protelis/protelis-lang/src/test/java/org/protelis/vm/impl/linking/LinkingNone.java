package org.protelis.vm.impl;

import java.util.Map;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.util.CodePath;

/**
 * 
 * There is no link between devices.
 *
 */
public class LinkingNone extends AbstractLinkingStrategy {

    @Override
    public Map<DeviceUID, Map<CodePath, Object>> getNeighbors(final DeviceUID id,
            final Map<DeviceUID, Map<CodePath, Object>> allDevices) {
        return MAPMAKER.makeMap();
    }

}
