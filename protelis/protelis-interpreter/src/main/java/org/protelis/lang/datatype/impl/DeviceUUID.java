package org.protelis.lang.datatype.impl;

import java.util.UUID;

import org.protelis.lang.datatype.DeviceUID;

/** DeviceUID based on UUIDs. */
public class DeviceUUID implements DeviceUID, Comparable<DeviceUUID> {
    private static final long serialVersionUID = 1L;
    private final UUID uid;

    /**
     * Generate a random UUID.
     */
    public DeviceUUID() {
        this(UUID.randomUUID());
    }

    /**
     * @param uid
     *            the string to use as the UID
     */
    public DeviceUUID(final UUID uid) {
        this.uid = uid;
    }

    /**
     * 
     * @return the underlying string
     */
    public UUID getUID() {
        return uid;
    }

    @Override
    public boolean equals(final Object alt) {
        if (this == alt) {
            return true;
        } else if (alt instanceof DeviceUUID) {
            return this.uid == ((DeviceUUID) alt).uid;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.uid.hashCode();
    }

    @Override
    public String toString() {
        return uid.toString();
    }

    @Override
    public int compareTo(final DeviceUUID other) {
        return uid.compareTo(other.uid);
    }
}
