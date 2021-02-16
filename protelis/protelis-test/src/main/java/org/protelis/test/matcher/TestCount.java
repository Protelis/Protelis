/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.test.matcher;

import java.util.Map;
import java.util.function.Function;

/**
 * Count the occurences of a given value.
 */
public final class TestCount implements Function<Map<String, Object>, Integer> {
    private final Object expectedValue;

    /**
     * 
     * @param expectedValue
     *            expected value
     */
    public TestCount(final Object expectedValue) {
        this.expectedValue = expectedValue;
    }

    @Override
    public Integer apply(final Map<String, Object> simulationRes) {
        final Long count = simulationRes.values().stream().filter(v -> v.equals(this.expectedValue)).count();
        return count.intValue();
    }
}
