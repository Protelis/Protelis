/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

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
