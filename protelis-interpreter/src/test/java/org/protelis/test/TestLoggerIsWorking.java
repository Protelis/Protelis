/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.test;

import org.junit.Assert;
import org.junit.Test;
import org.protelis.lang.ProtelisLoader;
import org.slf4j.LoggerFactory;

/**
 * Verifies that the current Log4J implementation does not fall back to NOP.
 */
public class TestLoggerIsWorking {

    /**
     * Verifies that the current Log4J implementation does not fall back to NOP.
     */
    @Test
    public void testLoggerImplementationIsNotNOP() {
        final String loggerName = LoggerFactory.getLogger(ProtelisLoader.class).getClass().getName();
        Assert.assertFalse(
            "The logger is defaulting to NOP implementation " + loggerName,
            loggerName.contains("NOP")
        );
    }

}
