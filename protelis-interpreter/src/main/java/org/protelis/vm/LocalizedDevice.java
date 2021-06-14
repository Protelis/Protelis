/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.vm;

import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.Tuple;

/**
 * A device embedded in a space which allows coordinates.
 */
public interface LocalizedDevice extends ExecutionContext {

    /**
     * TODO: define coordinate.
     * 
     * @return coordinates of the current device
     */
    Tuple getCoordinates();

    /**
     * TODO: define vector.
     * 
     * @return field of directions to other devices
     */
    Field<Tuple> nbrVector();
}
