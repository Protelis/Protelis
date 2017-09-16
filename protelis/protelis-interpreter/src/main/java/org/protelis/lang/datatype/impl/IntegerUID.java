package org.protelis.lang.datatype.impl;

import org.protelis.lang.datatype.DeviceUID;

/** Simple integer UIDs. */
public class IntegerUID extends AbstractComparableDeviceUID<Integer> {
    private static final long serialVersionUID = 1L;

    /**
     * Create {@link DeviceUID} from an integer.
     * 
     * @param uid
     *            the value
     */
    public IntegerUID(final int uid) {
        super(uid);
    }
}
