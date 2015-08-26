/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter;

/**
 * Data structure representing the field calculus semantics of annotating expressions with 
 * their evaluated values, in this case a dual annotation of value and additional evaluation or state
 * in a superscript.
 *
 * @param <S>
 *            Superscript type
 * @param <T>
 *            Annotation type
 */
public interface SuperscriptedAnnotatedTree<S, T> extends AnnotatedTree<T> {

	/**
	 * @return the superscript
	 */
	S getSuperscript();

}
