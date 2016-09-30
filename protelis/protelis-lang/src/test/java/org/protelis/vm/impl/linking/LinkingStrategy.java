package org.protelis.vm.impl.linking;

import java.util.Map;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.impl.SpatiallyEmbeddedContext;
import org.protelis.vm.util.CodePath;

/**
 * Strategy according to which the devices are linked together.
 */
public interface LinkingStrategy {

  /**
   * 
   * @param deviceID
   *          current device
   * @param deviceNumber
   *          number of devices
   * @return estimate a position for the given device
   */
  Double[] findPosition(final DeviceUID deviceID, final int deviceNumber);

  /**
   * Get the neighbors of the current device.
   * 
   * @param id
   *          current device
   * @param allDevices
   *          map containing all the devices
   * @return neighbors of the current device
   */
  Map<DeviceUID, Map<CodePath, Object>> getNeighborsContent(final DeviceUID id,
      final Map<DeviceUID, Map<CodePath, Object>> allDevices);

  /**
   * Get the {@link SpatiallyEmbeddedContext}s of the neighbors.
   * 
   * @param id
   *          current device
   * @param allContexts
   *          all contexts
   * @return {@link SpatiallyEmbeddedContext}s of the neighbors
   */
  SpatiallyEmbeddedContext[] getNeighborsContext(final DeviceUID id, final SpatiallyEmbeddedContext[] allContexts);

}
