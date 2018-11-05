package org.protelis.test;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Testing Protelis core libraries.
 */
public class TestParseError {
    private final Exception e = new IllegalArgumentException("Must fail!");

    private static void test(final String file) {
        test(file, InfrastructureTester.SIMULATION_STEPS, InfrastructureTester.STABILITY_STEPS);
    }

    private static void test(final String file, final int simulationSteps, final int stabilitySteps) {
        InfrastructureTester.runTest(file, simulationSteps, stabilitySteps);
    }

    /**
     * Test parseError1.
     * 
     * @throws Exception
     *             in case test does not fail
     */
    @Test
    public void testParseError1() throws Exception {
        try {
            test("parseError1");
            throw e;
        } catch (Exception e) {
            assertFalse(e.getMessage().isEmpty());
        }
    }

    /**
     * Test parseError2.
     * 
     * @throws Exception
     *             in case test does not fail
     */
    @Test
    public void testParseError2() throws Exception {
        try {
            test("parseError2");
            throw e;
        } catch (AssertionError e) {
            assertFalse(e.getMessage().isEmpty());
        }
    }

    /**
     * Test parseError3.
     * 
     * @throws Exception
     *             in case test does not fail
     */
    @Test
    public void testParseError3() throws Exception {
        try {
            test("parseError3");
            throw e;
        } catch (AssertionError e) {
            assertFalse(e.getMessage().isEmpty());
        }
    }

    /**
     * Test parseError4.
     * 
     * @throws Exception
     *             in case test does not fail
     */
    @Test
    public void testParseError4() throws Exception {
        try {
            test("parseError4");
            throw e;
        } catch (AssertionError e) {
            assertFalse(e.getMessage().isEmpty());
        }
    }

// TODO check this error
//    /**
//     * Test parseError5.
//     * 
//     * @throws Exception
//     *             in case test does not fail
//     */
//    @Test
//    public void testParseError5() throws Exception {
//        try {
//            test("parseError5");
//            throw e;
//        } catch (AssertionError e) {
//            assertFalse(e.getMessage().isEmpty());
//        }
//    }

    /**
     * Test parseError6.
     * 
     * @throws Exception
     *             in case test does not fail
     */
    @Test
    public void testParseError6() throws Exception {
        try {
            test("parseError6");
            throw e;
        } catch (AssertionError e) {
            assertFalse(e.getMessage().isEmpty());
        }
    }

    /**
     * Test parseError7.
     */
    @Test
    public void testParseError7() {
        test("parseError7");
    }
}
