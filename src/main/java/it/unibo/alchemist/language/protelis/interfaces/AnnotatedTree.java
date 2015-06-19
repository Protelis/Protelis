/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis.interfaces;

import it.unibo.alchemist.language.protelis.vm.ExecutionContext;

import java.io.Serializable;

/**
 * @author Danilo Pianini
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
