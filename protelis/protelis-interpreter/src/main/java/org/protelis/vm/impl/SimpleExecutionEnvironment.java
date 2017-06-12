package org.protelis.vm.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.danilopianini.lang.util.FasterString;
import org.protelis.vm.ExecutionEnvironment;

/**
 *
 * Simple implementation of {@link ExecutionEnvironment}.
 *
 */
public final class SimpleExecutionEnvironment implements ExecutionEnvironment {

    private final Map<FasterString, Object> env = new LinkedHashMap<>();
    private final Set<String> keys = new HashSet<>();

    @Override
    public boolean has(final String id) {
        return env.containsKey(new FasterString(id));
    }

    @Override
    public Object get(final String id) {
        return env.get(new FasterString(id));
    }

    @Override
    public Object get(final String id, final Object defaultValue) {
        if (has(id)) {
            return env.get(new FasterString(id));
        } else {
            return defaultValue;
        }
    }

    @Override
    public boolean put(final String id, final Object v) {
        keys.add(id);
        return env.put(new FasterString(id), v) != null;
    }

    @Override
    public Object remove(final String id) {
        keys.remove(id);
        return env.remove(new FasterString(id));
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(keys);
    }

    @Override
    public void commit() {
    }

    @Override
    public void setup() {
    }


}
