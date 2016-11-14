package org.protelis.vm;

import org.protelis.lang.datatype.Field;

/**
 * A time-aware device.
 */
public interface TimeAwareDevice extends ExecutionContext {

    /**
     * Time forward view: expected time from the device computation to
     * neighbor's next computation incorporating that information.
     * 
     * For device's neighbors: nbrDelay is the best estimate that the underlying
     * system can provide. For the current device: nbrDelay is
     * self.getDeltaTime().
     * 
     * @return field of communication delays
     */
    Field nbrDelay();

    /**
     * Time backward view: how long ago information from you neighbor was
     * received.
     * 
     * For device's neighbors: nbrRange is the time of the computation minus the
     * timestamp on the packet. Dropped packets temporarily increase nbrLag. For
     * the current device: nbrRange is self.getDeltaTime().
     * 
     * @return field of communication latencies
     */
    Field nbrLag();
}
