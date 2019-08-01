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
