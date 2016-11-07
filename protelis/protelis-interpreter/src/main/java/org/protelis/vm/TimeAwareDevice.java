package org.protelis.vm;

import org.protelis.lang.datatype.Field;

/**
 * A device embedded in space.
 */
public interface TimeAwareDevice extends ExecutionContext {

    /**
     * Get the communication latencies between the current device and its
     * neighbors. Latencies must be positive.
     * 
     * @return field of communication latencies
     */
    Field nbrLag();
}
