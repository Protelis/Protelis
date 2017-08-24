/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.vm.impl;

import org.protelis.lang.datatype.DeviceUID;

/**
 * Child context is used by AbstractExecutionContext.instance() to allow 
 * encapsulated evaluation of sub-programs, while passing through "self" 
 * accesses to its parent.
 */
public class ChildExecutionContext extends AbstractExecutionContext {
    private final AbstractExecutionContext parent;

    /**
     * Create a new child context.
     * @param parent the parent execution context to get information from.
     */
    public ChildExecutionContext(final AbstractExecutionContext parent) {
        super(parent.getExecutionEnvironment(), parent.getNetworkManager());
        this.parent = parent;
    }

    /** @return parent for this child context */
    public AbstractExecutionContext getParent() {
        return parent;
    }

    @Override
    protected AbstractExecutionContext instance() {
        return new ChildExecutionContext(parent);
    }

    @Override
    public DeviceUID getDeviceUID() {
        return parent.getDeviceUID();
    }

    @Override
    public Number getCurrentTime() {
        return parent.getCurrentTime();
    }

    @Override
    public double nextRandomDouble() {
        return parent.nextRandomDouble();
    }
}
