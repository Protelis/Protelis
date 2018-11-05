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
