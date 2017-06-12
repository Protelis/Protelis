package org.protelis.lang.datatype.impl;

import org.protelis.lang.datatype.DeviceUID;

/** Simple integer UIDs. */
public class IntegerUID implements DeviceUID, Comparable<IntegerUID> {
    private static final long serialVersionUID = 1L;
    private final int uid;

    /**
     * Create {@link DeviceUID} from an integer.
     * 
     * @param uid
     *            the value
     */
    public IntegerUID(final int uid) {
        this.uid = uid;
    }

    /**
     * @return the underlying integer
     */
    public int getUID() {
        return uid;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof IntegerUID) {
            return this.uid == ((IntegerUID) o).uid;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return uid;
    }

    @Override
    public String toString() {
        return Integer.toString(uid);
    }

    @Override
    public int compareTo(final IntegerUID other) {
        return Integer.compare(uid, other.uid);
    }
}
