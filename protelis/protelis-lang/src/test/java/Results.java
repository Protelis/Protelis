package org.protelis.test;

/**
 * Well known results of the tests.
 */
public final class Results {
    private Results() {
    }

    /**
     * FOO value.
     */
    public static final String FOO = "foo";

    /**
     * 4x3 squared matrix made of FOO values.
     */
    public static final Object[][] BROADCAST = { { FOO, FOO, FOO }, { FOO, FOO, FOO }, { FOO, FOO, FOO },
                    { FOO, FOO, FOO } };

    /**
     * nbrRange result.
     */
    public static final Object[] NBRRANGE = { 1.0, 1.0 };

    /**
     * neighborhood result.
     */
    public static final Object[][] NEIGHBORHOOD = { { 2.0, 3.0, 2.0 }, { 3.0, 4.0, 3.0 }, { 2.0, 3.0, 2.0 } };
}
