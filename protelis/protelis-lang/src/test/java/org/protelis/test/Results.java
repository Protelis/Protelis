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
    private static final String DC = TestConfig.DC;
    private static final Boolean S = true;
    private static final Boolean D = true;
    private static final Boolean F = false;
    private static final Boolean T = true;
    private static final Boolean O = false;
    /**
     * Each device considers only his adjacent devices as neighbors.
     */
    public static final Double MANHATTAN_BLOCK = 1.0;

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
     * addRange.pt result.
     */
    public static final TestConfig ADDRANGE = TestConfig.create("addRange")
                    .setProperties("n", 1)
                    .setExpectedResult(new Object[][]{{ 2.0, 2.0 }}
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
     * voronoiPartitioning.pt test configuration.
     */
    public static final TestConfig VORONOI_PARTITIONING = TestConfig.create("voronoiPartitioning")
                    .setProperties("seed", true, 0, 3, 12, 15)
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
     * distance.pt result.
     */
    public static final TestConfig CHANNEL = TestConfig.create("channel", EXECUTION_ROUND, 1.9)
                    .setProperties("source", true, 13)
                    .setProperties("destination", true, 16)
                    .setProperties("obstacle", true, 3, 9, 15, 21)
                    .setProperties("width", 1)
                    .setProperties("thr", 1)
                    .setExpectedResult(new Object[][] {
                      // 00, 01, 02, 03, 04, 05, 
                        { F,  F,  F,  O,  F,  F },
                      // 06, 07, 08, 09, 10, 11, 
                        { F,  S,  T,  O,  T,  F },
                      // 12, 13, 14, 15, 16, 17, 
                        { T,  T,  T,  O,  D,  T },
                      // 18, 19, 20, 21, 22, 23, 
                        { T,  T,  T,  O,  T,  T },
                      // 24, 25, 26, 27, 28, 29, 
                        { F,  T,  T,  T,  T,  T },
                      // 30, 31, 32, 33, 34, 35, 
                        { F,  F,  T,  T,  T,  F },
                      // 36, 37, 38, 39, 40, 41,
                        { F,  F,  F,  F,  F,  F }
                    }
    );

    /**
     * T.pt result.
     */
    public static final TestConfig TFUN = TestConfig.create("T", 6)
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
     * tfilter.pt result.
     */
    public static final TestConfig TFILTER = TestConfig.create("tfilter", 1)
                    .setExpectedResult(new Object[][] {{ 0.0 }}
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
                                    { DC, DC, DC, DC },
                                    { DC, 16.0, DC, DC },
                                    { DC, DC, DC, DC },
                                    { DC, DC, DC, DC } 
    });

    /**
     * opinionFeedback.pt result.
     * | X | A | B | X |
     * | A | A | B | B |
     * | C | C | D | D |
     * | X | C | D | X |
     */
    public static final TestConfig OPINION_FEEDBACK = TestConfig.create("opinionFeedback")
                    .setProperties("acts", true, 0, 3, 12, 15)
                    .setProperties("feedback", 1)
                    .setProperties("feedback", 0, 0, 5, 9, 10, 14, 15) // override the previous feedbacks
                    .setExpectedResult(new Object[][] { 
                                    { 2.0, DC, DC, 4.0 },
                                    { DC, DC, DC, DC },
                                    { DC, DC, DC, DC },
                                    { 3.0, DC, DC, 1.0 } 
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