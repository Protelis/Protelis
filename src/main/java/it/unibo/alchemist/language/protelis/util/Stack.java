/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis.util;

import java.io.Serializable;
import java.util.Map;

import it.unibo.alchemist.utils.FasterString;

/**
 * @author Danilo Pianini
 *
 */
public interface Stack extends Serializable {
	
	void push();
	
	void pop();
	
	Object put(FasterString var, Object val, boolean canShadow);
	
	void putAll(Map<FasterString, ? extends Object> map);
	
	Object get(FasterString var);

}
