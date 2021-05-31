/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.lang.interpreter;

import java.io.Serializable;
import java.util.List;

import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Data structure representing the field calculus semantics of annotating
 * expressions with their evaluated values.
 * 
 * @param <T>
 */
public interface ProtelisAST<T> extends Serializable {

    /**
     * Evaluates the program using the passed {@link ExecutionContext}.
     * 
     * @param context
     *            the execution context
     * @return the AST evaluation
     */
    T eval(ExecutionContext context);

    /**
     * @param i
     *            the index
     * @return the i-th branch of the evaluation tree
     */
    ProtelisAST<?> getBranch(int i);

    /**
     * @return a view of the branches of the tree
     */
    List<ProtelisAST<?>> getBranches();

    /**
     * @return A {@link Metadata} object containing information about the code that generated this AST node.
     */
    Metadata getMetadata();

    /**
     * @return The name of the operation
     */
    String getName();

}
