package org.protelis.vm.impl.linking;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.impl.MyDeviceUID;
import org.protelis.vm.impl.SpatiallyEmbeddedContext;
import org.protelis.vm.impl.TestDeviceUID;
import org.protelis.vm.util.CodePath;

/**
 * Devices are linked with respect to a star topology where `Device 0` is the
 * root.
 */
public class LinkingStar extends AbstractLinkingStrategy {

    @Override
    public Map<DeviceUID, Map<CodePath, Object>> getNeighborsContent(final DeviceUID id,
                    final Map<DeviceUID, Map<CodePath, Object>> allDevices) {
        final int n = ((TestDeviceUID) id).getId();
        final Map<DeviceUID, Map<CodePath, Object>> res = MAPMAKER.makeMap();
        if (n == 0) {
            IntStream.range(1, allDevices.size()).forEach(i -> {
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

    @Override
    public SpatiallyEmbeddedContext[] getNeighborsContext(final DeviceUID id,
                    final SpatiallyEmbeddedContext[] allContexts) {
        final List<SpatiallyEmbeddedContext> res = new LinkedList<>();
        final int n = ((TestDeviceUID) id).getId();
        if (n == 0) {
            for (int i = 0; i < allContexts.length; i++) {
                res.add(allContexts[i]);
            }
        } else {
            res.add(allContexts[0]);
        }
        return (SpatiallyEmbeddedContext[]) res.toArray();
    }

    @Override
    public Double[] findPosition(final DeviceUID deviceID, final int deviceNumber) {
        return new Double[] { 0.0, 0.0 };
    }

}
