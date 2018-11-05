package org.protelis.lang.datatype.impl;

/** Simple long UIDs. */
public class LongUID extends AbstractComparableDeviceUID<Long> {
    private static final long serialVersionUID = 1L;

    /**
     * Create {@link org.protelis.lang.datatype.DeviceUID} from a long.
     * 
     * @param uid
     *            the value
     */
    public LongUID(final long uid) {
        super(uid);
    }
}
