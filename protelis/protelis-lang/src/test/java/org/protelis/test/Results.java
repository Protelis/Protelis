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
     * broadcast.pt test. 4x3 squared matrix made of FOO values.
     */
    public static final TestConfig BROADCAST = TestConfig.create("broadcast")
                    .setProperties("source", true, 0)
                    .setExpectedResult(new Object[][]{
                        { FOO, FOO, FOO }, 
                        { FOO, FOO, FOO }, 
                        { FOO, FOO, FOO },
                        { FOO, FOO, FOO } 
                    }
    );

    /**
     * distance.pt result.
     */
    public static final TestConfig DISTANCETO = TestConfig.create("distanceTo")
                    .setProperties("source", true, 3)
                    .setExpectedResult(new Object[][] {
                        { 1.0, 2.0, 3.0 },
                        { 0.0, 1.0, 2.0 },
                        { 1.0, 2.0, 3.0 },
                        { 2.0, 3.0, 4.0 },
                        { 3.0, 4.0, 5.0 }
                    }
    );

    /**
     * obstacle.pt result.
     */
    public static final TestConfig OBSTACLE = TestConfig.create("obstacle")
                    .setProperties("source", true, 3)
                    .setProperties("obstacle", true, 1, 4)
                    .setExpectedResult(new Object[][] {
                        { 1.0,  Double.POSITIVE_INFINITY,    5.0 },
                        { 0.0,  Double.POSITIVE_INFINITY,    4.0 },
                        { 1.0,  2.0,                         3.0 }
                    }
    );

    /**
     * nbrRange.pt result.
     */
    public static final TestConfig NBRRANGE = TestConfig.create("nbrRange")
                    .setExpectedResult(new Object[][]{{ 1.0, 1.0 }}
    );

    /**
     * neighborhood.pt result.
     */
    public static final TestConfig NEIGHBORHOOD = TestConfig.create("neighborhood")
                    .setExpectedResult(new Object[][]{
                        { 2.0, 3.0, 2.0 }, 
                        { 3.0, 4.0, 3.0 }, 
                        { 2.0, 3.0, 2.0 } 
                    }
    );

    private static final String S0 = new IntegerUID(0).toString(), 
                    S3 = new IntegerUID(3).toString(), 
                    S12 = new IntegerUID(12).toString(), 
                    S15 = new IntegerUID(15).toString();
    /**
     * G.pt test configuration.
     */
    public static final TestConfig G = TestConfig.create("G")
                    .setProperties("source", true, 0, 3, 12, 15)
                    .setExpectedResult(new Object[][]{
                        { S0, S0, S3, S3 },
                        { S0, S0, S3, S3 },
                        { S12, S12, S15, S15 },
                        { S12, S12, S15, S15 }
                    }
    );

    /**
     * distance.pt result.
     */
    public static final TestConfig DISTANCE = TestConfig.create("distance")
                    .setProperties("source", true, 0)
                    .setProperties("destination", true, 11)
                    .setExpectedResult(new Object[][] {
                        { 5.0, 5.0, 5.0 },
                        { 5.0, 5.0, 5.0 },
                        { 5.0, 5.0, 5.0 },
                        { 5.0, 5.0, 5.0 }
                    }
    );

    /**
     * T.pt result.
     */
    public static final TestConfig T = TestConfig.create("T", 6)
                    .setProperties("n", 5)
                    .setProperties("decay", 1)
                    .setExpectedResult(new Object[][] {{ 0.0 }}
    );

    /**
     * cyclictimer.pt result.
     */
    public static final TestConfig CYCLICTIMER1 = TestConfig.create("cyclicTimer", 5)
                    .setProperties("n", 5)
                    .setProperties("decay", 1)
                    .setExpectedResult(new Object[][] {{ false }}
    );

    /**
     * cyclictimer.pt result.
     */
    public static final TestConfig CYCLICTIMER2 = TestConfig.create("cyclicTimer", 6)
                    .setProperties("n", 5)
                    .setProperties("decay", 1)
                    .setExpectedResult(new Object[][] {{ true }}
    );

    /**
     * cyclictimer.pt result.
     */
    public static final TestConfig CYCLICTIMER3 = TestConfig.create("cyclicTimer", 7)
                    .setProperties("n", 5)
                    .setProperties("decay", 1)
                    .setExpectedResult(new Object[][] {{ false }}
    );

    /**
     * limitedMemory.pt result.
     */
    public static final TestConfig LIMITED_MEMORY1 = TestConfig.create("limitedMemory", 4)
                    .setProperties("value", FOO)
                    .setProperties("timeout", 5)
                    .setProperties("decay", 1)
                    .setExpectedResult(new Object[][] {{ FOO }}
    );

    /**
     * limitedMemory.pt result.
     */
    public static final TestConfig LIMITED_MEMORY2 = TestConfig.create("limitedMemory", 5)
                    .setProperties("value", FOO)
                    .setProperties("timeout", 5)
                    .setProperties("decay", 1)
                    .setExpectedResult(new Object[][] {{ false }}
    );

    /**
     * summarize.pt result.
     * Distances from the source
     * | 2 | 1 | 2 | 3 |
     * | 1 | x | 1 | 2 |
     * | 2 | 1 | 2 | 3 |
     * | 3 | 2 | 3 | 4 |
     */
    public static final TestConfig SUMMARIZE = TestConfig.create("summarize")
                    .setProperties("n", 1)
                    .setProperties("source", true, 5)
                    .setExpectedResult(new Object[][] {
                        { 1.0, 4.0,  2.0,  1.0 },
                        { 3.0, 16.0, 6.0, 1.0 },
                        { 2.0, 2.0, 4.0,  2.0 },
                        { 1.0, 1.0, 1.0,  1.0 }
    });
    /**
     * gossip_ever.pt test configuration.
     */
    public static final TestConfig GOSSIP_EVER = TestConfig.create("gossipEver")
                    .setProperties("temperature", 10)
                    .setProperties("temperature", 21, 0)
                    .addExpectedResult(Position.fromVector(0.0, 0.0), new Object[][] {
                        {true, true}, 
                        {true, true},
                    }).addExpectedResult(Position.fromVector(10.0, 10.0), new Object[][] {
                        {false, false}, 
                        {false, false},
                    });

}