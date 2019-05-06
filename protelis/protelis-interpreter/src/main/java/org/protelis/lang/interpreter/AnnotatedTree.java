/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
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
public interface AnnotatedTree<T> extends Serializable {

    /**
     * @return a copy of this program.
     */
    AnnotatedTree<T> copy();

    /**
     * |e| operation.
     */
    void erase();

    /**
     * Evaluates the program using the passed {@link ExecutionContext}.
     * 
     * @param context
     *            the execution context
     */
    void eval(ExecutionContext context);

    /**
     * @return the current value of this program
     */
    T getAnnotation();

    /**
     * @param i
     *            the index
     * @return the i-th branch of the evaluation tree
     */
    AnnotatedTree<?> getBranch(int i);

    /**
     * @return a view of the branches of the tree
     */
    List<AnnotatedTree<?>> getBranches();

    /**
     * @return A {@link Metadata} object containing information about the code that generated this AST node.
     */
    Metadata getMetadata();

    /**
     * @return The name of the operation
     */
    String getName();

    /**
     * @return true if this program has been erased
     */
    boolean isErased();

    /**
     * Recursively deletes any existing annotation.
     */
    void reset();

}
