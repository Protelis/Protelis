package org.protelis.lang.datatype.impl;

import org.protelis.lang.datatype.DeviceUID;

/**
 * Simple UID container class.
 * @param <T>  Type to be used for storing UID
 */
public class AbstractComparableDeviceUID<T extends Comparable<T>> implements DeviceUID, Comparable<AbstractComparableDeviceUID<T>> {
    private static final long serialVersionUID = 1L;
    private final T uid;

    /**
     * @param uid
     *            the string to use as the UID
     */
    public AbstractComparableDeviceUID(final T uid) {
        this.uid = uid;
    }

    /**
     * 
     * @return the underlying UID
     */
    public T getUID() {
        return uid;
    }

    @Override
    public boolean equals(final Object alt) {
        if (this == alt) {
            return true;
        } else if (alt instanceof AbstractComparableDeviceUID<?>) {
            return this.uid.equals(((AbstractComparableDeviceUID<T>) alt).uid);
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
    public int compareTo(final AbstractComparableDeviceUID<T> other) {
        return uid.compareTo(other.uid);
    }
}
