import org.protelis.test.IntegerUID;

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
     * distance.pt result.
     */
    public static final Object[][] DISTANCE = {
                    { 5.0, 5.0, 5.0 },
                    { 5.0, 5.0, 5.0 },
                    { 5.0, 5.0, 5.0 },
                    { 5.0, 5.0, 5.0 }
    };
}
