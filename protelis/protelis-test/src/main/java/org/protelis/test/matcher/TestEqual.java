package org.protelis.test.matcher;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.protelis.test.InfrastructureTester;
import org.protelis.test.observer.ExceptionObserver;

import java8.util.function.BiConsumer;

/**
 * Check if the simulation and expected results match.
 */
public final class TestEqual implements BiConsumer<Map<String, Object>, List<Pair<String, String>>> {
    private final ExceptionObserver obs;
    private static final String ERROR_TEMPLATE = "\n --- Simulation result\n --- %s\n[N%s] expected: %s, found: %s";
    /**
     * @param obs
     *            exception observer
     */
    public TestEqual(final ExceptionObserver obs) {
        this.obs = obs;
    }

    @Override
    public void accept(final Map<String, Object> simulationRes, final List<Pair<String, String>> expectedResult) {
        try {
            assertEquals(expectedResult.size(), simulationRes.values().size());
            for (final Pair<String, String> pair : expectedResult) {
                if (!pair.getRight().equals(InfrastructureTester.DC)) {
                    final Object singleNodeResult = simulationRes.get(pair.getLeft());
                    try {
                        assertNotNull(singleNodeResult);
                        if (singleNodeResult instanceof Integer || singleNodeResult instanceof Double) {
                            final Double tmp = singleNodeResult instanceof Integer ? ((Integer) singleNodeResult).doubleValue() : (double) singleNodeResult;
                            assertEquals(Double.parseDouble(pair.getRight()), tmp, InfrastructureTester.DELTA);
                        } else if (singleNodeResult instanceof Boolean) {
                            final String v = pair.getRight();
                            final Boolean expected = Boolean.parseBoolean(v.equals("T") ? "true" : v.equals("F") ? "false" : pair.getRight());
                            assertEquals(expected.booleanValue(), ((Boolean) singleNodeResult).booleanValue());
                        } else {
                            assertEquals(pair.getRight(), singleNodeResult);
                        }
                    } catch (Exception | AssertionError e) { // NOPMD
                        obs.exceptionThrown(new IllegalStateException(String.format(ERROR_TEMPLATE, getMessage(simulationRes, expectedResult), pair.getLeft(), pair.getRight(), singleNodeResult)));
                        break;
                    }
                }
            }
        } catch (AssertionError e) {
            obs.exceptionThrown(new IllegalStateException("expectedResult.length [" + expectedResult.size() + "] != simulationResult.length [" + simulationRes.values().size() + "]"));
        }
    }

    private String getMessage(final Map<String, Object> simulationRes, final List<Pair<String, String>> expectedResult) {
        String res = "[";
        for (int i = 0; i < expectedResult.size(); i++) {
            final Pair<String, String> pair = expectedResult.get(i);
            res += "N" + pair.getLeft() + ": " + simulationRes.get(pair.getLeft()) + (i < expectedResult.size() - 1 ? ", " : "");
        }
        return res + "]";
    }
}
