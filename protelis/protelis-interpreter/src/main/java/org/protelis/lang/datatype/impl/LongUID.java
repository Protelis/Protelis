package org.protelis.lang.datatype.impl;

import org.protelis.lang.datatype.DeviceUID;

/** Simple long UIDs. */
public class LongUID extends AbstractComparableDeviceUID<Long> {
    private static final long serialVersionUID = 1L;

    /**
     * Create {@link DeviceUID} from a long.
     * 
     * @param uid
     *            the value
     */
    public LongUID(final long uid) {
        super(uid);
    }
}
