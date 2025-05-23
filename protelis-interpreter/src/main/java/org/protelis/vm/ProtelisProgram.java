/*
 * Copyright (C) 2022, Danilo Pianini and contributors listed in the project's build.gradle.kts file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.vm;

import java.io.Serializable;

/**
 * Executable representation of a Protelis program.
 */
public interface ProtelisProgram extends Serializable {

    /**
     * Execute one computation round of this Protelis program.
     *
     * @param context The virtual machine environment in which computation will take place.
     * @return the result of the program's evaluation
     */
    Object compute(ExecutionContext context);

    /**
     * The name of the program.
     *
     * @return Name of the program, or some default name if no specific name is
     *         provided
     */
    String getName();

}
