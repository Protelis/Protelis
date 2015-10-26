/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import org.danilopianini.lang.util.FasterString;
import org.protelis.vm.ExecutionContext;

/**
 * Access a variable.
 */
public class Variable extends AbstractAnnotatedTree<Object> {

    private static final long serialVersionUID = -3739014755916345132L;
    private final FasterString name;

    /**
     * @param varName
     *            variable name
     */
    public Variable(final FasterString varName) {
        super();
        name = varName;
    }

    /**
     * @param varName
     *            variable name
     */
    public Variable(final String varName) {
        this(new FasterString(varName));
    }

    @Override
    public Variable copy() {
        return new Variable(name);
    }

    @Override
    public void eval(final ExecutionContext context) {
        Object val = context.getVariable(name);
        if (val == null) {
            /*
             * The variable cannot be read. Most probably, it is some node
             * variable that is not set here. Defaults to false.
             */
            val = false;
        }
        /*
         * TODO: Restrict variable to aligned fields
         */
        setAnnotation(val);
    }

    @Override
    protected void asString(final StringBuilder sb, final int i) {
        sb.append(name);
    }

}
