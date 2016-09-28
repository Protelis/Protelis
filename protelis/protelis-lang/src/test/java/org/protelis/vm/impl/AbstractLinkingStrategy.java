package org.protelis.vm.impl;

import java.util.concurrent.ConcurrentMap;
import com.google.common.collect.MapMaker;

/**
 * Common background for the {@link LinkingStrategy} sub-classes.
 */
public abstract class AbstractLinkingStrategy implements LinkingStrategy {
    /**
     * A builder of {@link ConcurrentMap}.
     */
    protected static final MapMaker MAPMAKER = new MapMaker();
}
