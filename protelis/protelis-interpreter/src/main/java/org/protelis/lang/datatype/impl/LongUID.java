package org.protelis.lang.datatype.impl;

import org.protelis.lang.datatype.DeviceUID;

/** Simple long UIDs. */
public class LongUID implements DeviceUID, Comparable<LongUID> {
    private static final long serialVersionUID = 1L;
    private final long uid;

    /**
     * Create {@link DeviceUID} from an integer.
     * 
     * @param uid
     *            the value
     */
    public LongUID(final long uid) {
        this.uid = uid;
    }

    /**
     * @return the underlying long
     */
    public long getUID() {
        return uid;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof LongUID) {
            return this.uid == ((LongUID) o).uid;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Long.hashCode(uid);
    }

    @Override
    public String toString() {
        return Long.toString(uid);
    }

    @Override
    public int compareTo(final LongUID other) {
        return Long.compare(uid, other.uid);
    }
}
