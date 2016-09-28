package org.protelis.vm.impl;

import java.util.Map;
import java.util.stream.IntStream;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.util.CodePath;

/**
 * Device are linked with respect to star topology where `Device 0` is the root.
 */
public class LinkingStar extends AbstractLinkingStrategy {

    @Override
    public Map<DeviceUID, Map<CodePath, Object>> getNeighbors(final DeviceUID id,
            final Map<DeviceUID, Map<CodePath, Object>> allDevices) {
        final int n = ((TestDeviceUID) id).getId();
        final Map<DeviceUID, Map<CodePath, Object>> res = MAPMAKER.makeMap();
        if (n == 0) {
            IntStream.range(1, allDevices.size()).forEachOrdered(i -> {
                DeviceUID child = new MyDeviceUID(i);
                if (allDevices.get(child) != null) {
                    res.put(child, allDevices.get(child));
                }
            });
        } else {
            DeviceUID parent = new MyDeviceUID(0);
            if (allDevices.get(parent) != null) {
                res.put(parent, allDevices.get(parent));
            }
        }
        return res;
    }

}
