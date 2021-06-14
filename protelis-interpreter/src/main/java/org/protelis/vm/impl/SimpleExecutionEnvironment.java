/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.vm.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.protelis.vm.ExecutionEnvironment;

/**
 *
 * Simple implementation of {@link ExecutionEnvironment}.
 *
 */
public final class SimpleExecutionEnvironment implements ExecutionEnvironment {

    private final Map<String, Object> env = new LinkedHashMap<>();

    @Override
    public boolean has(final String id) {
        return env.containsKey(id);
    }

    @Override
    public Object get(final String id) {
        return env.get(id);
    }

    @Override
    public Object get(final String id, final Object defaultValue) {
        if (has(id)) {
            return env.get(id);
        } else {
            return defaultValue;
        }
    }

    @Override
    public boolean put(final String id, final Object v) {
        return env.put(id, v) != null;
    }

    @Override
    public Object remove(final String id) {
        return env.remove(id);
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(env.keySet());
    }

    @Override
    public void commit() {
    }

    @Override
    public void setup() {
    }


}
