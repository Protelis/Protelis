/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.test;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Public helpers used by Protelis programs testing Java null interoperability.
 */
public final class TestJavaNullFunctions {

    private TestJavaNullFunctions() {
    }

    /**
     * @param o must be null
     * @return null
     */
    public static Object expectsNull(final Object o) {
        assertNull(o);
        return o;
    }

    /**
     * Runs without returning a value.
     */
    public static void returnsVoid() {
    }

    /**
     * @return null
     */
    public static String returnsNull() {
        return null;
    }

    /**
     * @return with 50% probability null, with 50% probability the "notNull" String.
     */
    public static String maybeNull() {
        return Math.random() > 0.5 ? null : "notNull";
    }

}
