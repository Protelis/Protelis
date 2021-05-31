/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.vm;

/**
 * A virtual machine for executing a Protelis program on a particular device
 * (context).
 */
public class ProtelisVM {

    private final ProtelisProgram prog;
    private final ExecutionContext ctx;

    /**
     * Create a virtual machine for executing a Protelis program in a particular
     * context.
     * 
     * @param program
     *            Protelis program to be executed
     * @param context
     *            Environment in which this program will be executed
     */
    public ProtelisVM(final ProtelisProgram program, final ExecutionContext context) {
        prog = program;
        ctx = context;
    }

    /**
     * Run one execution cycle of the VM, in which the computation is run
     * atomically against the most recent neighbor and environment information,
     * producing a new state to be committed to the environment and sent to
     * neighbors.
     */
    public void runCycle() {
        // 1. Take the messages received by neighbors
        ctx.setup();
        // 2. Compute
        prog.compute(ctx);
        // 3. Finalize the new environment and send Messages away
        ctx.commit();
    }

    /**
     * Return the value computed in the most recent execution cycle.
     * 
     * @return Last value computed
     */
    public Object getCurrentValue() {
        return prog.getCurrentValue();
    }

}
