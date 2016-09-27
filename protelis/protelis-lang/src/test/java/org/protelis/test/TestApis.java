/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package org.protelis.test; // NOPMD by jakebeal on 8/25/15 12:41 PM

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.regex.Pattern;

import org.danilopianini.io.FileUtilities;
import org.junit.Test;
import org.protelis.lang.ProtelisLoader;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ProtelisProgram;
import org.protelis.vm.ProtelisVM;
import org.protelis.vm.impl.MyDummyEnvironment;
import org.protelis.vm.impl.MyDummyEnvironment.LinkingRule;

/**
 * Main collection of tests for the Protelis language and VM.
 */
public class TestApis {

    private static final String SL_NAME = "singleLineComment";
    private static final String ML_NAME = "multilineComment";
    private static final String EXPECTED = "EXPECTED_RESULT:";
    private static final Pattern EXTRACT_RESULT = Pattern.compile(".*?" + EXPECTED + "\\s*(?<" + ML_NAME
            + ">.*?)\\s*\\*\\/|\\/\\/\\s*" + EXPECTED + "\\s*(?<" + SL_NAME + ">.*?)\\s*\\n", Pattern.DOTALL);
    private static final Pattern CYCLE = Pattern.compile("\\$CYCLE");
    private static final int MIN_CYCLE_NUM = 1;
    private static final int MAX_CYCLE_NUM = 100;

    /**
     * Test distance to.
     */
    /*
     * @Test public void testDistanceTo() { final MyDummyEnvironment env =
     * MyDummyEnvironment.build(); env.getNodeEnvironment(1).put("source",
     * true); testFileWithExplicitResult("distanceTo", 1, 0, MAX_CYCLE_NUM,
     * env.getExecutionContexts()); }
     */

    @Test
    public void testNeighborhood() {
        final MyDummyEnvironment env = MyDummyEnvironment.build(3, LinkingRule.LINE);
        testFileWithExplicitResult("neighborhood", new Double[] { 1.0, 2.0, 1.0 }, env.getExecutionContexts());
    }

    /*
     * From this point the rest of the file is not tests, but utility methods
     */

    private void testFileWithExplicitResult(String s, Object[] result, ExecutionContext[] executionContexts) {
        final int length = executionContexts.length;
        final ProtelisVM[] vms = new ProtelisVM[length];
        for (int i = 0; i < length; i++) {
            final ProtelisProgram program = ProtelisLoader.parse(s);
            try {
                FileUtilities.serializeObject(program);
            } catch (Exception e) {
                fail();
            }
            vms[i] = new ProtelisVM(program, executionContexts[i]);
        }
        for (int j = 0; j < MAX_CYCLE_NUM; j++) {
            for (int i = 0; i < length; i++) {
                vms[i].runCycle();
            }
        }
        Object[] res = new Object[length];
        for (int i = 0; i < length; i++) {
            res[i] = vms[i].getCurrentValue();
        }
        assertEquals(result, res);
    }

}
