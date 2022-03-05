/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.lang.datatype.impl;

import java.util.Objects;

import org.protelis.lang.datatype.DeviceUID;

/**
 * Simple container class for immutable, comparable UIDs.
 * @param <T>  Type to be used for storing UID
 */
public class AbstractComparableDeviceUID<T extends Comparable<T>>
    implements DeviceUID, Comparable<AbstractComparableDeviceUID<T>> {

    private static final long serialVersionUID = 1L;
    private final T uid;

    /**
     * @param uid
     *            the string to use as the UID
     */
    public AbstractComparableDeviceUID(final T uid) {
        this.uid = Objects.requireNonNull(uid);
    }

    /**
     * 
     * @return the underlying UID
     */
    public T getUID() {
        return uid;
    }

    @Override
    public final boolean equals(final Object alt) {
        if (alt != null) {
            if (this == alt) {
                return true;
            } else if (this.getClass() == alt.getClass()) {
                return this.uid.equals(((AbstractComparableDeviceUID<?>) alt).uid);
            }
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return this.uid.hashCode();
    }

    @Override
    public final String toString() {
        return uid.toString();
    }

    @Override
    public final int compareTo(final AbstractComparableDeviceUID<T> other) {
        return uid.compareTo(other.uid);
    }
}
