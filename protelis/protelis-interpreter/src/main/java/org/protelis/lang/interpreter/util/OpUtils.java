/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.lang.interpreter.util;

/**
 * Utility class that OpN classes use for reporting certain failure patterns.
 */
public final class OpUtils {

    private OpUtils() {
    }

    /**
     * This method builds a meaningful exception and throws it immediately.
     * 
     * @param op
     *            The name of the operation
     * @param a
     *            The arguments that have been passed, and that made the
     *            operation fail.
     * @param <T>
     *            Return type. This function never returns, this type is a
     *            placeholder that allows you to write stuff like
     *            "return unsupported(op, args)".
     * @throws UnsupportedOperationException
     *             ALWAYS
     * @return Nothing, since it throws an exception
     */
    public static <T> T unsupported(final String op, final Object... a) {
        final StringBuilder msg = new StringBuilder("Nobody told me how to run ");
        msg.append(op);
        if (a.length > 0) {
            msg.append(" with parameters of class: ");
            boolean first = true;
            for (final Object o : a) {
                if (first) {
                    first = false;
                } else {
                    msg.append(", ");
                }
                msg.append(o == null ? "null" : o.getClass().getSimpleName());
            }
            msg.append('.');
        }
        throw new UnsupportedOperationException(msg.toString());
    }
}
