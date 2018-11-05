/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.vm.util;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java8.util.Maps;
import java8.util.function.Function;

import org.protelis.lang.util.Reference;

/**
 * Basic implementation of a {@link Stack}.
 */
public final class StackImpl implements Stack {

    private final Deque<Map<Reference, Object>> stack = new LinkedList<>();

    /**
     * @param gamma
     *            Initial set of variables
     */
    @SuppressWarnings("unchecked")
    public StackImpl(final Map<Reference, ?> gamma) {
        stack.push((Map<Reference, Object>) gamma);
    }

    @Override
    public void push() {
        stack.push(null);
    }

    @Override
    public void pop() {
        stack.pop();
    }

    @Override
    public Object put(final Reference var, final Object val, final boolean canShadow) {
        if (canShadow) {
            /*
             * Overrides the previous value only if it is at this depth in the
             * stack
             * 
             * let c = 0; if(true) { let c = 1 } else { 1 } // c = 0
             * 
             */
            Map<Reference, Object> cur = stack.pop();
            if (cur == null) {
                cur = new LinkedHashMap<>();
            }
            final Object res = cur.put(var, val);
            stack.push(cur);
            return res;
        }
        /*
         * Overrides the previous value, regardless its position in the stack
         * e.g.
         * 
         * let c = 0; if(true) { c = 1 } else { 1 } // c = 1
         * 
         */
        return stackOperation(varMap -> Maps.computeIfPresent(varMap, var, (k, v) -> val));
    }

    private Object stackOperation(final Function<Map<Reference, Object>, Object> op) {
        for (final Map<Reference, Object> varMap : stack) {
            if (varMap != null) {
                final Object res = op.apply(varMap);
                if (res != null) {
                    return res;
                }
            }
        }
        return null;
    }

    @Override
    public Object get(final Reference var) {
        return stackOperation(varMap -> varMap.get(var));
    }

    @Override
    public String toString() {
        return stack.toString();
    }

    @Override
    public void putAll(final Map<Reference, ?> map) {
        Map<Reference, Object> cur = stack.pop();
        if (cur == null) {
            cur = new HashMap<>();
        }
        cur.putAll(map);
        stack.push(cur);
    }

}
