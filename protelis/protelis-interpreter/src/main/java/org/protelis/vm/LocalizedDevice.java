package org.protelis.vm;

import org.protelis.lang.datatype.Tuple;

/**
 * A device embedded in a space which allows coordinates.
 */
public interface LocalizedDevice extends ExecutionContext {

    /**
     * @return coordinates of the current device
     */
    Tuple getCoordinates();

    /**
     * @param device other device
     * @return direction to other device
     */
    Tuple getVectorToDevice(LocalizedDevice device);
}
