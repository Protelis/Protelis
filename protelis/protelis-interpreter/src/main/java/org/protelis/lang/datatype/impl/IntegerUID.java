package org.protelis.lang.datatype.impl;

/** Simple integer UIDs. */
public class IntegerUID extends AbstractComparableDeviceUID<Integer> {
    private static final long serialVersionUID = 1L;

    /**
     * Create {@link org.protelis.lang.datatype.DeviceUID} from an integer.
     * 
     * @param uid
     *            the value
     */
    public IntegerUID(final int uid) {
        super(uid);
    }
}
