package org.protelis.vm.impl;

import org.protelis.lang.datatype.DeviceUID;

/**
 * Extends {@link DeviceUID} to get an integer representation of the id.
 */
public interface TestDeviceUID extends DeviceUID {
    /**
     * @return {@link Integer} representation of the id
     */
    Integer getId();
}
