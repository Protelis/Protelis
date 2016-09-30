package org.protelis.vm.impl.linking;

import java.util.Map;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.impl.SpatiallyEmbeddedContext;
import org.protelis.vm.impl.TestDeviceUID;
import org.protelis.vm.util.CodePath;

/**
 * 
 * There is no link between devices.
 *
 */
public class LinkingNone extends AbstractLinkingStrategy {

  @Override
  public Map<DeviceUID, Map<CodePath, Object>> getNeighborsContent(final DeviceUID id,
      final Map<DeviceUID, Map<CodePath, Object>> allDevices) {
    return MAPMAKER.makeMap();
  }

  @Override
  public SpatiallyEmbeddedContext[] getNeighborsContext(final DeviceUID id, final SpatiallyEmbeddedContext[] allContexts) {
    return new SpatiallyEmbeddedContext[] {};
  }

  @Override
  public Double[] findPosition(final DeviceUID deviceID, final int deviceNumber) {
    final Integer n = ((TestDeviceUID) deviceID).getId();
    return new Double[] { n.doubleValue(), n.doubleValue() };
  }

}
