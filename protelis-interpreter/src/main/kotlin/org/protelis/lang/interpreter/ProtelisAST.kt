/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.lang.interpreter

import org.protelis.lang.loading.Metadata
import org.protelis.vm.ExecutionContext
import java.io.Serializable

/**
 * Data structure representing the field calculus semantics of annotating
 * expressions with their evaluated values.
 *
 * @param T return type of this sub-program
 */
interface ProtelisAST<T> : Serializable {

    /**
     * Evaluates the program using the passed [ExecutionContext].
     *
     * @param context the execution context
     * @return the AST evaluation
     */
    fun eval(context: ExecutionContext): T

    /**
     * @param i the index
     * @return the i-th branch of the evaluation tree
     */
    fun getBranch(i: Int): ProtelisAST<*>

    /**
     * @return a view of the branches of the tree
     */
    fun getBranches(): List<ProtelisAST<*>>

    /**
     * @return A [Metadata] object containing information about the code that generated this AST node.
     */
    fun getMetadata(): Metadata

    /**
     * @return The name of the operation
     */
    fun getName(): String
}

