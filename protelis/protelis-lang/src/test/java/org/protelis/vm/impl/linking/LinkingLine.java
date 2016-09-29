package org.protelis.vm.impl.linking;

import java.util.Map;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.impl.MyDeviceUID;
import org.protelis.vm.impl.SpatiallyEmbeddedContext;
import org.protelis.vm.impl.TestDeviceUID;
import org.protelis.vm.util.CodePath;

/**
 * Link devices as if they were in a straight line. Example: Device A <-> Device
 * B <-> Device C A is linked with B. B is linked with both A and C
 */
public class LinkingLine extends AbstractLinkingStrategy {

    @Override
    public Map<DeviceUID, Map<CodePath, Object>> getNeighborsContent(final DeviceUID id,
                    final Map<DeviceUID, Map<CodePath, Object>> allDevices) {
        final int n = ((TestDeviceUID) id).getId();
        final Map<DeviceUID, Map<CodePath, Object>> res = MAPMAKER.makeMap();
        int nodeNumber = allDevices.size();
        if (n < nodeNumber - 1) {
            DeviceUID nextD = new MyDeviceUID(n + 1);
            if (allDevices.get(nextD) != null) {
                res.put(nextD, allDevices.get(nextD));
            }
        }
        if (n > 0) {
            DeviceUID prevD = new MyDeviceUID(n - 1);
            if (allDevices.get(prevD) != null) {
                res.put(prevD, allDevices.get(prevD));
            }
        }
        return res;
    }

    @Override
    public SpatiallyEmbeddedContext[] getNeighborsContext(final DeviceUID id,
                    final SpatiallyEmbeddedContext[] allContexts) {
        final int n = ((TestDeviceUID) id).getId();
        if (n == 0) {
            return allContexts.length > 0 ? new SpatiallyEmbeddedContext[] { allContexts[1] }
                            : new SpatiallyEmbeddedContext[] {};
        } else if (n < allContexts.length - 1) {
            return new SpatiallyEmbeddedContext[] { allContexts[n - 1], allContexts[n + 1] };
        } else {
            return new SpatiallyEmbeddedContext[] { allContexts[n - 1] };
        }
    }

    @Override
    public Double[] findPosition(final DeviceUID deviceID, final int deviceNumber) {
        final Integer n = ((TestDeviceUID) deviceID).getId();
        return new Double[] { 10.0, n.doubleValue() };
    }
}
