/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

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
     * @param <T> field type
     * @return a builder for an immutable field
     */
    public static <T> Field.Builder<T> createFieldBuilder() {
        return new FieldMapImpl.Builder<>();
    }

    /**
     * @param l the elements
     * @return a new tuple
     */
    public static Tuple createTuple(final List<?> l) {
        return createTuple(l.toArray());
    }

    /**
     * @param l the elements
     * @return a new tuple
     */
    @SafeVarargs
    public static Tuple createTuple(final Object... l) {
        return new ArrayTupleImpl(l);
    }

}
