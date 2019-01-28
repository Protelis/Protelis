/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package org.protelis.test; // NOPMD by jakebeal on 8/25/15 12:41 PM

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Assert;
import org.junit.Test;
import org.protelis.lang.ProtelisLoader;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.test.infrastructure.DummyContext;
import org.protelis.vm.NetworkManager;
import org.protelis.vm.ProtelisVM;
import org.protelis.vm.util.CodePath;

/**
 * Main collection of tests for the Protelis language and VM.
 */
public class TestShareSharedValues {

    /**
     * This test verifies that the values shared by a share call are those produced
     * by the evaluation of the body (in contrast to a classic rep + nbr cycle,
     * where the shared values are those computed within the nbr call).
     */
    @Test
    public void testLatestHasBeenShared() {
        final String program = "share (x, nx <- 0) { x + 1 }";
        final MutableInt cycle = new MutableInt(0);
        final ProtelisVM vm = new ProtelisVM(ProtelisLoader.parse(program), new DummyContext(new NetworkManager() {
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
