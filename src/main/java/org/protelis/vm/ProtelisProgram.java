/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.vm;

import java.io.Serializable;
import java.util.Map;

import org.danilopianini.lang.util.FasterString;
import org.protelis.lang.datatype.FunctionDefinition;

/**
 * Executable representation of a Protelis program.
 */
public interface ProtelisProgram extends Serializable {
	
	/**
	 * @return The value computed during the most recent invocation of {@link compute}
	 */
	Object getCurrentValue();
	
	/**
	 * Execute one round of computation of this Protelis program.
	 * @param context
	 * 		The virtual machine environment in which computation will take place.
	 */
	void compute(ExecutionContext context);
	
	/**
	 * @return Set of named functions defined in this program
	 */
	Map<FasterString, FunctionDefinition> getNamedFunctions();
	
	/**
	 * @return Name of the program, or some default name if no specific name is provided
	 */
	FasterString getName();

}
