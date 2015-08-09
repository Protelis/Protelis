/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.vm.util;

import java.io.Serializable;
import java.util.Map;

import org.danilopianini.lang.util.FasterString;

/**
 * @author Danilo Pianini
 * Stack implementation used by the Protelis VM for tracking local variable values during execution
 */
public interface Stack extends Serializable {
	
	/**
	 * Enter a new nested lexical scope.
	 */
	void push();
	
	/**
	 * Exit the current most-nested lexical scope.
	 */
	void pop();
	
	Object put(FasterString var, Object val, boolean canShadow);
	
	void putAll(Map<FasterString, ? extends Object> map);
	
	Object get(FasterString var);

}
