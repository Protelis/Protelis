/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

/**
 * Testing Protelis core libraries.
 */
class TestParseError {

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
    void testCommentedJavaImports() {
        testCommentedImportLine("import java.lang.Byte.parseByte", "parseByte(\"0\")", "parseByte");
    }

    /**
     * Test that commented out Protelis imports are not considered.
     */
    @Test
    void testCommentedProtelisImports() {
        testCommentedImportLine("import protelis:lang:time", "cyclicTimer(10,1)", "cyclicTimer");
    }

    /**
     * Test parseError1.
     */
    @Test
    void testParseError1() {
        Assertions.assertThrows(Exception.class, () -> test("parseError1"));
    }

    /**
     * Test parseError2.
     */
    @Test
    void testParseError2() {
        Assertions.assertThrows(AssertionError.class, () -> test("parseError2"));
    }

    /**
     * Test parseError3.
     */
    @Test
    void testParseError3() {
        Assertions.assertThrows(AssertionError.class, () -> test("parseError3"));
    }

    /**
     * Test parseError4.
     */
    @Test
    void testParseError4() {
        Assertions.assertThrows(AssertionError.class, () -> test("parseError4"));
    }

    /**
     * Test parseError6.
     */
    @Test
    void testParseError6() {
        Assertions.assertThrows(AssertionError.class, () -> test("parseError6"));
    }

    /**
     * Test parseError7.
     */
    @Test
    void testParseError7() {
        test("parseError7");
    }

    private static void test(final String file) {
        test(file, InfrastructureTester.SIMULATION_STEPS, InfrastructureTester.STABILITY_STEPS);
    }

    private static void test(final String file, final int simulationSteps, final int stabilitySteps) {
        InfrastructureTester.runTest(file, simulationSteps, stabilitySteps);
    }

    private static void testCommentedImportLine(
        final String importLine,
        final String programLine,
        final String... errorMessages
    ) {
        ProgramTester.runProgram(importLine + "\n" + programLine, 1);
        COMMENT_STYLES.entries().forEach(entry -> ProgramTester.runExpectingErrors(
                entry.getKey() + importLine + entry.getValue() + programLine,
                Exception.class, errorMessages)
        );
    }
}
