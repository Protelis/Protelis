package org.protelis.test;

import org.protelis.lang.datatype.DeviceUID;

/** Simple integer UIDs. */
public class IntegerUID implements DeviceUID {
    private static final long serialVersionUID = 7168671027263227202L;
    private final int uid;

    /**
     * 
     * @param uid
     *            universal id
     */
    public IntegerUID(final int uid) {
        this.uid = uid;
    }

    /**
     * @return device id
     */
    public int getUID() {
        return uid;
    }

    @Override
    /**
     * @param alt
     *            other id
     * @return true if the ids are equal, false otherwise
     */
    public boolean equals(final Object alt) {
        return this.uid == ((IntegerUID) alt).uid;
    }

    @Override
    public int hashCode() {
        return uid;
    }

    @Override
    public String toString() {
        return Integer.toString(uid);
    }
}
