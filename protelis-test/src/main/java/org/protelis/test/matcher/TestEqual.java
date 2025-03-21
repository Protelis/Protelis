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
    private static final String ERROR_TEMPLATE = "%n --- Simulation result%n --- %s%n[N%s] expected: %s, found: %s";
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
        try {
            assertEquals(expectedResult.size(), simulationRes.size());
            for (final Pair<String, String> pair : expectedResult) {
                if (!InfrastructureTester.DC.equals(pair.getRight())) {
                    final Object singleNodeResult = simulationRes.get(pair.getLeft());
                    try {
                        assertNotNull(singleNodeResult);
                        if (singleNodeResult instanceof Integer) {
                            final double result = (Integer) singleNodeResult;
                            assertEquals(
                                Double.parseDouble(pair.getRight()),
                                result,
                                InfrastructureTester.DELTA
                            );
                        } else if (singleNodeResult instanceof Double) {
                            assertEquals(
                                Double.parseDouble(pair.getRight()),
                                (Double) singleNodeResult,
                                InfrastructureTester.DELTA
                            );
                        } else if (singleNodeResult instanceof Boolean) {
                            final String v = pair.getRight();
                            final boolean expected = Boolean.parseBoolean("T".equals(v)
                                ? "true"
                                : "F".equals(v) ? "false" : pair.getRight());
                            assertEquals(expected, singleNodeResult);
                        } else {
                            assertEquals(pair.getRight(), singleNodeResult);
                        }
                        // CHECKSTYLE: IllegalCatch OFF
                    } catch (Exception | AssertionError e) { // NOPMD
                        // CHECKSTYLE: IllegalCatch ON
                        final var expected = pair.getRight() + " (type: " + pair.getRight().getClass().getSimpleName() + ")";
                        final var actual = singleNodeResult == null
                            ? "null"
                            : singleNodeResult + " (type: " + singleNodeResult.getClass().getSimpleName() + ")";
                        obs.exceptionThrown(
                            new IllegalStateException(
                                String.format(
                                    ERROR_TEMPLATE,
                                    getMessage(simulationRes, expectedResult),
                                    pair.getLeft(),
                                    expected,
                                    actual
                                )
                            )
                        );
                        break;
                    }
                }
            }
        } catch (final AssertionError e) {
            obs.exceptionThrown(
                new IllegalStateException(
                    "expectedResult.length [" + expectedResult.size() + "] != simulationResult.length ["
                        + simulationRes.size() + "]"
                )
            );
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
