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
    Field nbrVector();
}
