package org.protelis.test;

import org.junit.Test;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

/**
 * Testing Protelis core libraries.
 */
public class TestParseError {

    private static final Multimap<String, String> COMMENT_STYLES = ImmutableMultimap.of(
        "//", "\n",
        "/*", "*/\n",
        "/*\n", "*/",
        "/*\n", "*/\n"
    );

    /**
     * Test that commented out java imports are not considered.
     */
    @Test
    public void testCommentedJavaImports() {
        testCommentedImportLine("import java.lang.Math.sin", "sin(0)", "sin");
    }

    /**
     * Test that commented out Protelis imports are not considered.
     */
    @Test
    public void testCommentedProtelisImports() {
        testCommentedImportLine("import protelis:lang:time", "cyclicTimer(10,1)", "cyclicTimer");
    }

    /**
     * Test parseError1.
     * 
     * @throws Exception
     *             in case test does not fail
     */
    @Test(expected = Exception.class)
    public void testParseError1() {
        test("parseError1");
    }

    /**
     * Test parseError2.
     */
    @Test(expected = AssertionError.class)
    public void testParseError2() {
        test("parseError2");
    }

    /**
     * Test parseError3.
     * 
     * @throws Exception
     *             in case test does not fail
     */
    @Test(expected = AssertionError.class)
    public void testParseError3() {
        test("parseError3");
    }

    /**
     * Test parseError4.
     * 
     * @throws Exception
     *             in case test does not fail
     */
    @Test(expected = AssertionError.class)
    public void testParseError4() {
        test("parseError4");
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
    @Test(expected = AssertionError.class)
    public void testParseError6() throws Exception {
        test("parseError6");
    }

    /**
     * Test parseError7.
     */
    @Test
    public void testParseError7() {
        test("parseError7");
    }

    private static void test(final String file) {
        test(file, InfrastructureTester.SIMULATION_STEPS, InfrastructureTester.STABILITY_STEPS);
    }

    private static void test(final String file, final int simulationSteps, final int stabilitySteps) {
        InfrastructureTester.runTest(file, simulationSteps, stabilitySteps);
    }

    private static void testCommentedImportLine(final String importLine, final String programLine, final String... errorMessages) {
        ProgramTester.runProgram(importLine + "\n" + programLine, 1);
        COMMENT_STYLES.entries().forEach(entry -> ProgramTester.runExpectingErrors(
                entry.getKey() + importLine + entry.getValue() + programLine,
                Exception.class, errorMessages));
    }
}
