/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.protelis.lang.datatype.Option;

/**
 * Tests for {@link Option} utilities.
 */
public final class TestOption {

    /**
     * The supplier passed to {@link Option#orElseGet(java.util.function.Supplier)} should not
     * be executed when the option already contains a value.
     */
    @Test
    public void testSupplierNotExecutedWhenPresent() {
        final AtomicBoolean executed = new AtomicBoolean(false);
        final Option<Integer> opt = Option.of(1);
        final int result = opt.orElseGet(() -> {
            executed.set(true);
            return 2;
        });
        assertFalse(executed.get());
        assertEquals(1, result);
    }

    /**
     * The supplier passed to {@link Option#orElseGet(java.util.function.Supplier)} should
     * be executed when the option is empty.
     */
    @Test
    public void testSupplierExecutedWhenEmpty() {
        final AtomicBoolean executed = new AtomicBoolean(false);
        final Option<Integer> opt = Option.empty();
        final int result = opt.orElseGet(() -> {
            executed.set(true);
            return 3;
        });
        assertTrue(executed.get());
        assertEquals(3, result);
    }
}
