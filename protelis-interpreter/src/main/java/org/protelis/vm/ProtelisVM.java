/*
 * Copyright (C) 2022, Danilo Pianini and contributors listed in the project's build.gradle.kts file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.vm;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * A virtual machine for executing a Protelis program on a particular device
 * (context).
 */
public class ProtelisVM {

    private final ProtelisProgram program;
    private final ExecutionContext context;

    private @Nullable Object lastValue;

    /**
     * Create a virtual machine for executing a Protelis program in a particular
     * context.
     * 
     * @param program
     *            Protelis program to be executed
     * @param context
     *            Environment in which this program will be executed
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "This is intentional")
    public ProtelisVM(final ProtelisProgram program, final ExecutionContext context) {
        this.program = program;
        this.context = context;
    }

    /**
     * Run one execution cycle of the VM, in which the computation is run
     * atomically against the most recent neighbor and environment information,
     * producing a new state to be committed to the environment and sent to
     * neighbors.
     */
    public void runCycle() {
        // 1. Take the messages received by neighbors
        context.setup();
        // 2. Compute
        lastValue = program.compute(context);
        // 3. Finalize the new environment and send Messages away
        context.commit();
    }

    /**
     * Return the value computed in the most recent execution cycle.
     * 
     * @return Last value computed
     */
    @Nonnull
    public Object getCurrentValue() {
        return Objects.requireNonNull(
            lastValue,
            "No computation has happened so far, so no result is available yet. Call 'runCycle()' first."
        );
    }

}
