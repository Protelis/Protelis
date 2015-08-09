/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.util;

/**
 * @author Danilo Pianini
 * Utility class that OpN classes use for reporting certain failure
 */
public final class OpUtils {

	// Not constructable: can only be used for static methods
	private OpUtils() {
	}
	
	// Made this package-only 
	static <T> T unsupported(final String op, final Object... a) {
		String msg = "Nobody told me how to run " + op;
		if (a.length > 0) {
			msg += " with parameters of class: ";
			boolean first = true;
			for (final Object o : a) {
				if (first) { 
					first = false; 
				} else { 
					msg += ", "; 
				}
				msg += (o == null ? "null" : o.getClass().getSimpleName());
			}
			msg += '.';
		}
		throw new UnsupportedOperationException(msg.toString());
	}	
}
