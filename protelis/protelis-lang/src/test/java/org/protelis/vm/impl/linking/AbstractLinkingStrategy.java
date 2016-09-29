package org.protelis.vm.impl.linking;

import java.util.Random;
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

    /**
     * @return a new {@link Random} object
     */
    public Random getRandom() {
        return r;
    }

    /**
     * Random field.
     */
    private final Random r = new Random(0);
}
