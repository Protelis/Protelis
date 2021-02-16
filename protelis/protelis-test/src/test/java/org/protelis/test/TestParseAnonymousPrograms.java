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

/**
 *
 */
public final class TestParseAnonymousPrograms {

    /**
     * Test that cross referencing works, and that a message is created.
     */
    @Test
    public void testParseAnonymousPrograms() {
        try {
            ProtelisLoader.parse("wrongprogram");
            Assert.fail();
        } catch (final IllegalArgumentException e) {
            Assert.assertNotNull(e.getMessage());
            Assert.assertFalse(e.getMessage().isEmpty());
        }
    }

}
