/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

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
    private static final String ERROR_TEMPLATE = "%n --- Simulation result%n --- %s%n[N%s] expected: %s, found: %s";
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
                if (!InfrastructureTester.DC.equals(pair.getRight())) {
                    final Object singleNodeResult = simulationRes.get(pair.getLeft());
                    try {
                        assertNotNull(singleNodeResult);
                        if (singleNodeResult instanceof Integer || singleNodeResult instanceof Double) {
                            final double tmp = singleNodeResult instanceof Integer ? ((Integer) singleNodeResult).doubleValue() : (double) singleNodeResult;
                            assertEquals(Double.parseDouble(pair.getRight()), tmp, InfrastructureTester.DELTA);
                        } else if (singleNodeResult instanceof Boolean) {
                            final String v = pair.getRight();
                            final boolean expected = Boolean.parseBoolean("T".equals(v) ? "true" : "F".equals(v) ? "false" : pair.getRight());
                            assertEquals(expected, (Boolean) singleNodeResult);
                        } else {
                            assertEquals(pair.getRight(), singleNodeResult);
                        }
                    } catch (Exception | AssertionError e) { // NOPMD
                        obs.exceptionThrown(new IllegalStateException(String.format(
                            ERROR_TEMPLATE,
                            getMessage(simulationRes, expectedResult),
                            pair.getLeft(),
                            pair.getRight(),
                            singleNodeResult
                        )));
                        break;
                    }
                }
            }
        } catch (AssertionError e) {
            obs.exceptionThrown(new IllegalStateException("expectedResult.length [" + expectedResult.size() + "] != simulationResult.length [" + simulationRes.values().size() + "]"));
        }
    }

    private String getMessage(final Map<String, Object> simulationRes, final List<Pair<String, String>> expectedResult) {
        final StringBuilder res = new StringBuilder(50);
        res.append('[');
        for (int i = 0; i < expectedResult.size(); i++) {
            final Pair<String, String> pair = expectedResult.get(i);
            res.append('N')
                .append(pair.getLeft())
                .append(": ")
                .append(simulationRes.get(pair.getLeft()))
                .append(i < expectedResult.size() - 1 ? ", " : "");
        }
        return res.append(']').toString();
    }
}
