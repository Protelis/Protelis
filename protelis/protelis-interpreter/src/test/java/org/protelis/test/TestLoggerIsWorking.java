package org.protelis.test;

import org.junit.Assert;
import org.junit.Test;
import org.protelis.lang.ProtelisLoader;
import org.slf4j.LoggerFactory;

public class TestLoggerIsWorking {

    @Test
    public void testLoggerImplementationIsNotNOP() {
        final String loggerName = LoggerFactory.getLogger(ProtelisLoader.class).getClass().getName();
        Assert.assertFalse(
            "The logger is defaulting to NOP implementation " + loggerName,
            loggerName.contains("NOP")
        );
    }

}
