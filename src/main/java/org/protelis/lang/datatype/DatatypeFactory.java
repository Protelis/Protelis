package org.protelis.lang.datatype;

import java.util.List;

import org.protelis.lang.datatype.impl.ArrayTupleImpl;

/**
 * Static factory for Protelis data types.
 */
public final class DatatypeFactory {

    private DatatypeFactory() {
    }

    /**
     * @param l
     *            the elements
     * @return a new tuple
     */
    public static Tuple create(final List<?> l) {
        return create(l.toArray());
    }

    /**
     * @param l
     *            the elements
     * @return a new tuple
     */
    @SafeVarargs
    public static Tuple create(final Object... l) {
        return new ArrayTupleImpl(l);
    }

    /**
     * Create a Tuple with all elements initialized to the same value.
     * 
     * @param value
     *            Value to which all elements will be initialized
     * @param length
     *            Size of the tuple
     * @return a new tuple
     */
    public static Tuple fill(final Object value, final int length) {
        return new ArrayTupleImpl(value, length);
    }

}
