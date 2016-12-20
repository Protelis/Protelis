package org.protelis.test.matcher;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.tuple.Pair;
import org.protelis.test.InfrastructureTester;
import org.protelis.test.observer.ExceptionObserver;

/**
 * Check if the simulation and expected results match.
 */
public class TestEqual implements BiConsumer<Map<String, Object>, List<Pair<String, String>>> {
    private final ExceptionObserver obs;

    /**
     * @param obs
     *            exception observer
     */
    public TestEqual(final ExceptionObserver obs) {
        this.obs = obs;
    }

    @Override
    public void accept(final Map<String, Object> simulationRes, final List<Pair<String, String>> expectedResult) {
        assert expectedResult.size() == simulationRes.values().size() : obs.exceptionThrown(new IllegalArgumentException(
                        "expectedResult.length [" + expectedResult.size() + "] != simulationResult.length [" + simulationRes.values().size() + "]"));
        for (final Pair<String, String> pair : expectedResult) {
            if (!pair.getRight().equals(InfrastructureTester.DC)) {
                final Object singleNodeResult = simulationRes.get(pair.getLeft());
                assert singleNodeResult != null : obs.exceptionThrown(new IllegalArgumentException("Node" + pair.getLeft() + ": result can't be null!"));
                final String err = "Simulation result:\n" + getMessage(simulationRes, expectedResult) + "\n[Node" + pair.getLeft() + "]";
                if (singleNodeResult instanceof Integer || singleNodeResult instanceof Double) {
                    final Double tmp = singleNodeResult instanceof Integer ? ((Integer) singleNodeResult).doubleValue() : (double) singleNodeResult;
                    assert Math.abs(Double.parseDouble(pair.getRight()) - tmp) < InfrastructureTester.DELTA
                                    || Double.parseDouble(pair.getRight()) == tmp.doubleValue() 
                                    : obs.exceptionThrown(new IllegalStateException(err + " expected: " + Double.parseDouble(pair.getRight()) + " found: " + tmp));
                } else if (singleNodeResult instanceof Boolean) {
                    final String v = pair.getRight();
                    final Boolean expected = Boolean.parseBoolean(v.equals("T") ? "true" : v.equals("F") ? "false" : pair.getRight());
                    assert expected.booleanValue() == ((Boolean) singleNodeResult).booleanValue() 
                                    : obs.exceptionThrown(new IllegalStateException(err + " expected: " + expected + " found: " + singleNodeResult));
                } else {
                    assert pair.getRight().equals(singleNodeResult) 
                                : obs.exceptionThrown(new IllegalStateException(err + " expected: " + pair.getRight() + " found: " + singleNodeResult));
                }
            }
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