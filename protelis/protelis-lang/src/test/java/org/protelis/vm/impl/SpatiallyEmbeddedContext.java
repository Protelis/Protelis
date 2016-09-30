package org.protelis.vm.impl;

import org.protelis.lang.datatype.Field;
import org.protelis.vm.ExecutionContext;

/**
 * A device embedded in space.
 */
public interface SpatiallyEmbeddedContext extends ExecutionContext {

    /**
     * Get the distance between the current device and its neighbors.
     * 
     * @return field of distances
     */
    Field nbrRange();
}
