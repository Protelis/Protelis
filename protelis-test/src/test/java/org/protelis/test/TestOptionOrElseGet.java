package org.protelis.test;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.junit.Test;
import org.protelis.lang.datatype.Option;

/**
 * Tests for {@link Option#orElseGet(java.util.function.Supplier)}.
 */
public class TestOptionOrElseGet {

    /**
     * The supplier should not be invoked when the option is present.
     */
    @Test
    public void testOrElseGetDoesNotInvokeSupplierWhenPresent() {
        Option<String> opt = Option.of("value");
        AtomicInteger counter = new AtomicInteger();
        Supplier<String> supplier = () -> {
            counter.incrementAndGet();
            return "fallback";
        };
        String result = opt.orElseGet(supplier);
        assertEquals("value", result);
        assertEquals(0, counter.get());
    }

    /**
     * The supplier should be invoked when the option is empty.
     */
    @Test
    public void testOrElseGetInvokesSupplierWhenEmpty() {
        Option<String> opt = Option.empty();
        AtomicInteger counter = new AtomicInteger();
        Supplier<String> supplier = () -> {
            counter.incrementAndGet();
            return "fallback";
        };
        String result = opt.orElseGet(supplier);
        assertEquals("fallback", result);
        assertEquals(1, counter.get());
    }
}
