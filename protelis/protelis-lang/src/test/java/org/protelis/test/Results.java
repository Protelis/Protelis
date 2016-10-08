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
     * Each device considers only his adjacent devices as neighbors.
     */
    public static final int MANHATTAN_BLOCK = 1;

    /**
     * Number of round to be executed.
     */
    public static final int EXECUTION_ROUND = 100;

    /**
     * 4x3 squared matrix made of FOO values.
     */
    public static final Object[][] BROADCAST = { 
                    { FOO, FOO, FOO }, 
                    { FOO, FOO, FOO }, 
                    { FOO, FOO, FOO },
                    { FOO, FOO, FOO } 
    };

    /**
     * nbrRange.pt result.
     */
    public static final Object[] NBRRANGE = { 1.0, 1.0 };

    /**
     * neighborhood.pt result.
     */
    public static final Object[][] NEIGHBORHOOD = { 
                    { 2.0, 3.0, 2.0 }, 
                    { 3.0, 4.0, 3.0 }, 
                    { 2.0, 3.0, 2.0 } 
    };

    private static final String S0 = new IntegerUID(0).toString(), 
                    S3 = new IntegerUID(3).toString(), 
                    S12 = new IntegerUID(12).toString(), 
                    S15 = new IntegerUID(15).toString();

    /**
     * G.pt result.
     */
    public static final Object[][] G = {
                    { S0, S0, S3, S3 },
                    { S0, S0, S3, S3 },
                    { S12, S12, S15, S15 },
                    { S12, S12, S15, S15 }
    };

    /**
     * G.pt test configuration.
     */
    public static final TestConfig GTC = TestConfig.create("G")
                    .setProperties("source", true, 0, 3, 12, 15)
                    .setExpectedResult(G);

    /**
     * distance.pt result.
     */
    public static final Object[][] DISTANCE = {
                    { 5.0, 5.0, 5.0 },
                    { 5.0, 5.0, 5.0 },
                    { 5.0, 5.0, 5.0 },
                    { 5.0, 5.0, 5.0 }
    };

    /**
     * T.pt result.
     */
    public static final Object[] T = { 0.0 };

    /**
     * cyclictimer.pt result.
     */
    public static final Object[] CYCLICTIMER1 = { true };

    /**
     * cyclictimer.pt result.
     */
    public static final Object[] CYCLICTIMER2 = { false };

    /**
     * C.pt result.
     * Distances from the source
     * | 2 | 1 | 2 | 3 |
     * | 1 | x | 1 | 2 |
     * | 2 | 1 | 2 | 3 |
     * | 3 | 2 | 3 | 4 |
     */
    public static final Object[][] C = {
                    { 0.0, 3,  2,  3 },
                    { 4, 120, 19, 10 },
                    { 20, 22, 21,  11 },
                    { 12, 13, 29,  15 }
    };

    /**
     * gossip_ever.pt test configuration.
     */
    public static final TestConfig GOSSIP_EVER = TestConfig.create("gossipEver")
                    .setProperties("temperature", 10)
                    .setProperties("temperature", 21, 0)
                    .setExpectedResult(new Object[][] {
                        {true, true, true, true},
                        {true, true, true, true},
                        {true, true, true, true},
                        {true, true, true, true}
                    });

}