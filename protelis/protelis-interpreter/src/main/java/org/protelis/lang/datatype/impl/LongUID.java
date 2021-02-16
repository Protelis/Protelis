/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

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
