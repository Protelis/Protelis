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
