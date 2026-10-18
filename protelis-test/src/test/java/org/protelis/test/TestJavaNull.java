/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Tests for Option and null.
 */
final class TestJavaNull {

    /**
     * Runs all tests in protelis/option.
     */
    @Test
    void testProtelisOption() throws IOException, URISyntaxException {
        final String folder = "/protelis/option";
        final Path root = Path.of(Objects.requireNonNull(TestJavaNull.class.getResource(folder)).toURI());
        final List<String> programs;
        try (Stream<Path> files = Files.list(root)) {
            programs = files
                .map(it -> root.relativize(it).toString())
                .filter(it -> it.endsWith(".pt"))
                .map(it -> folder + '/' + it)
                .toList();
        }
        programs.stream()
            .filter(it -> !it.contains("error"))
            .forEach(ProgramTester::runFile);
        programs.stream()
            .filter(it -> it.contains("error"))
            .forEach(it -> ProgramTester.runExpectingErrors(it, Throwable.class));
    }

}
