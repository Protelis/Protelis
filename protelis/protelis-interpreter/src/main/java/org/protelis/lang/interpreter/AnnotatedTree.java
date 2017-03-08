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

import org.protelis.vm.ExecutionContext;

/**
 * Data structure representing the field calculus semantics of annotating
 * expressions with their evaluated values.
 * 
 * @param <T>
 */
public interface AnnotatedTree<T> extends Serializable {

    /**
     * @return the current value of this program
     */
    T getAnnotation();

    /**
     * Recursively deletes any existing annotation.
     */
    void reset();

    /**
     * |e| operation.
     */
    void erase();

    /**
     * @return true if this program has been erased
     */
    boolean isErased();

    /**
     * @return a copy of this program.
     */
    AnnotatedTree<T> copy();

    /**
     * Evaluates the program using the passed {@link ExecutionContext}.
     * 
     * @param context
     *            the execution context
     */
    void eval(ExecutionContext context);

    /**
     * Evaluates the program using the passed {@link ExecutionContext}, but
     * within a new scope pushed onto the stack. Returns to the current scope
     * after evaluation.
     * 
     * @param context
     *            the execution context
     * @param frameId
     *            id marker for new frame
     */
    void evalInNewStackFrame(ExecutionContext context, byte frameId);

    /**
     * @param i
     *            the index
     * @return the i-th branch of the evaluation tree
     */
    AnnotatedTree<?> getBranch(int i);

    /**
     * A faster toString, that only uses a single instance of
     * {@link StringBuilder}.
     * 
     * @param sb
     *            the {@link StringBuilder} where to load the {@link String}
     * @param i
     *            the number of indentations for the current level
     */
    void toString(StringBuilder sb, int i);

}
