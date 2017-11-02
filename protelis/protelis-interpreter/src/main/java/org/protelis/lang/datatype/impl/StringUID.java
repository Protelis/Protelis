package org.protelis.lang.datatype.impl;

import org.protelis.lang.datatype.DeviceUID;

/** Simple string UIDs. */
public class StringUID extends AbstractComparableDeviceUID<String> {
    private static final long serialVersionUID = 1L;

    /**
     * Create {@link DeviceUID} from a string.
     * 
     * @param uid
     *            the value
     */
    public StringUID(final String uid) {
        super(uid);
    }
}
