package org.protelis.lang.datatype.impl;

import java.util.UUID;

/** DeviceUID based on UUIDs. */
public class DeviceUUID extends AbstractComparableDeviceUID<UUID> {
    private static final long serialVersionUID = 1L;

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
        super(uid);
    }
}
