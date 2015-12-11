package org.protelis.lang.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * Implements a serializable reference to an Object. This implementation is
 * fragile, and should be substituted as soon as the Java board decides what to
 * do with sun.misc.Unsafe (that might get dropped).
 */
public final class Reference implements Serializable {

    private static final long serialVersionUID = 8294777860793746504L;
    private final int uid;
    private String strRep;

    /**
     * @param obj the object to refer to
     */
    public Reference(final Object obj) {
        Objects.requireNonNull(obj);
        uid = System.identityHashCode(obj);
    }

    @Override
    public int hashCode() {
        return uid;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Reference && ((Reference) obj).uid == uid;
    }

    @Override
    public String toString() {
        if (strRep == null) {
            strRep = "Var@" + Integer.toString(uid, Character.MAX_RADIX);
        }
        return strRep;
    }

}
