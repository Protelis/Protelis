/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.lang.interpreter.impl

import org.protelis.lang.interpreter.util.Bytecode
import org.protelis.lang.loading.Metadata
import org.protelis.vm.ExecutionContext

/**
 * Access to the evaluation context, which is used for interfacing with sensors,
 * actuators, and the rest of the external non-static programmatic environment
 * outside of Protelis.
 */
class Self(metadata: Metadata) : AbstractProtelisAST<ExecutionContext>(metadata) {

    private companion object {
        private const val serialVersionUID = 1L
    }

    override fun evaluate(context: ExecutionContext): ExecutionContext = context

    override fun getBytecode(): Bytecode = Bytecode.SELF

    override fun toString(): String = getName()
}
