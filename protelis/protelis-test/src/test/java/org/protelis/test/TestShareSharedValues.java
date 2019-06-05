/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package org.protelis.test;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Assert;
import org.junit.Test;
import org.protelis.lang.ProtelisLoader;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.test.infrastructure.DummyContext;
import org.protelis.vm.CodePath;
import org.protelis.vm.NetworkManager;
import org.protelis.vm.ProtelisVM;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Main collection of tests for the Protelis language and VM.
 */
public class TestShareSharedValues {

    private static final String PROGRAM = "share (x, nx <- 0) { x + 1 }";

    /**
     * This test verifies that the values shared by a share call are those produced
     * by the evaluation of the body (in contrast to a classic rep + nbr cycle,
     * where the shared values are those computed within the nbr call).
     */
    @Test
    @SuppressFBWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
    public void testLatestHasBeenShared() {
        final MutableInt cycle = new MutableInt(0);
        final ProtelisVM vm = new ProtelisVM(ProtelisLoader.parse(PROGRAM), new DummyContext(new NetworkManager() {
            @Override
            public void shareState(final Map<CodePath, Object> toSend) {
                Assert.assertEquals(1, toSend.size());
                Assert.assertEquals(cycle.doubleValue() + 1, toSend.values().iterator().next());
            }
            @Override
            public Map<DeviceUID, Map<CodePath, Object>> getNeighborState() {
                return Collections.emptyMap();
            }
        }));
        for (; cycle.getValue() < 100; cycle.getAndIncrement()) {
            vm.runCycle();
        }
    }

}
