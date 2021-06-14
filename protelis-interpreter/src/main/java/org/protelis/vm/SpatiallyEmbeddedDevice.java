/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.vm;

import org.protelis.lang.datatype.Field;

/**
 * A device embedded in space.
 *
 * @param <D> number type
 */
public interface SpatiallyEmbeddedDevice<D extends Number> extends ExecutionContext {

    /**
     * Get the distance between the current device and its neighbors. Distance
     * must be positive.
     * 
     * @return field of distances
     */
    Field<D> nbrRange();
}
