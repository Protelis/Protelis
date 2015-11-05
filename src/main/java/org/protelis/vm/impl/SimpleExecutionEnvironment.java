package org.protelis.vm.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.danilopianini.lang.util.FasterString;
import org.protelis.vm.ExecutionEnvironment;

/**
 *
 * Simple implementation of {@link ExecutionEnvironment}.
 *
 */
public final class SimpleExecutionEnvironment implements ExecutionEnvironment {

    private final Map<FasterString, Object> env = new LinkedHashMap<>();
    
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
        return env.put(new FasterString(id), v) != null;
    }

    @Override
    public Object remove(final String id) {
        return env.remove(new FasterString(id));
    }

    @Override
    public void commit() {
    }

    @Override
    public void setup() {
    }


}
