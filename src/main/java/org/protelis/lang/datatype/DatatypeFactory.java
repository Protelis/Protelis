package org.protelis.lang.datatype;

import java.util.List;

import org.protelis.lang.datatype.impl.ArrayTupleImpl;
import org.protelis.lang.datatype.impl.FieldMapImpl;

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
    public static Tuple createTuple(final List<?> l) {
        return createTuple(l.toArray());
    }

    /**
     * @param l
     *            the elements
     * @return a new tuple
     */
    @SafeVarargs
    public static Tuple createTuple(final Object... l) {
        return new ArrayTupleImpl(l);
    }

    /**
     * @param defaultSize
     *            creates a new and empty {@link Field}, defaulting on the
     *            specified size
     * @return an empty {@link Field}
     */
    public static Field createField(final int defaultSize) {
        return new FieldMapImpl(defaultSize + 1, 1f);
    }

}
