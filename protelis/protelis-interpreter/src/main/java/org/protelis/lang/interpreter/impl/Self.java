/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import org.protelis.vm.ExecutionContext;

/**
 * Access to the evaluation context, which is used for interfacing with sensors,
 * actuators, and the rest of the external non-static programmatic environment
 * outside of Protelis.
 */
public final class Self extends AbstractAnnotatedTree<ExecutionContext> {

    private static final long serialVersionUID = -5050040892058340950L;

    @Override
    public Self copy() {
        return new Self();
    }

    @Override
    public void eval(final ExecutionContext context) {
        setAnnotation(context);
    }

    @Override
    protected void asString(final StringBuilder sb, final int i) {
        sb.append("self");
    }

}
