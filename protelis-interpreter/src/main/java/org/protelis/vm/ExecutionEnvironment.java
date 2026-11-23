/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.vm;

import java.util.Set;

/**
 * The {@link ExecutionEnvironment} is responsible of managing environment variables.
 *
 */
public interface ExecutionEnvironment {

    /**
     * Checks whether a variable is present.
     *
     * @param id
     *            the variable name
     * @return true if the variable is present
     */
    boolean has(String id);

    /**
     * Returns the value of a variable.
     *
     * @param id
     *            the variable name
     * @return the value of the variable
     */
    Object get(String id);

    /**
     * Returns the value of a variable, or a default value if absent.
     *
     * @param id
     *            the variable name
     * @param defaultValue
     *            a parameterizable default value
     * @return the value of the variable if present, defaultValue otherwise
     */
    Object get(String id, Object defaultValue);

    /**
     * Associates a value with a variable.
     *
     * @param id
     *            the variable name
     * @param v
     *            the value that should be associated with id
     * @return true if there was previously a value associated with id, and
     *         false if not.
     */
    boolean put(String id, Object v);

    /**
     * Removes a variable.
     *
     * @param id
     *            the variable name
     * @return Returns the value to which this map previously associated the
     *         key, or null if the map contained no mapping for the key.
     */
    Object remove(String id);

    /**
     * Returns an unmodifiable set of the keys in the ExecutionEnvironment.
     *
     * @return An unmodifiable set of the keys in the ExecutionEnvironment
     */
    Set<String> keySet();

    /**
     * Called just after the VM is executed, to finalize information of the
     * execution for the environment.
     */
    void commit();

    /**
     * Called just before the VM is executed, to enable any preparations needed
     * in the environment.
     */
    void setup();
}
