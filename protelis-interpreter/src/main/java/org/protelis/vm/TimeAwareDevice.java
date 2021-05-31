/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.vm;

import org.protelis.lang.datatype.Field;

/**
 * A time-aware device.
 *
 * @param <D> number type
 */
public interface TimeAwareDevice<D extends Number> extends ExecutionContext {

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
    Field<D> nbrDelay();

    /**
     * Time backward view: how long ago information from neighbors was received.
     * 
     * For device's neighbors: nbrRange is the time of the computation minus the
     * timestamp on the packet. Dropped packets temporarily increase nbrLag. For
     * the current device: nbrRange is self.getDeltaTime().
     * 
     * @return field of communication latencies
     */
    Field<D> nbrLag();
}
