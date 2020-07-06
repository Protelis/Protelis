/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.JVMEntity;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Access a variable.
 */
public final class Variable extends AbstractProtelisAST<Object> {

    private static final long serialVersionUID = 1L;
    private final Reference name;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param ref
     *            variable name
     */
    public Variable(final Metadata metadata, final Reference ref) {
        super(metadata);
        name = ref;
    }

    @Override
    public Object evaluate(final ExecutionContext context) {
        Object val = context.getVariable(name);
        if (val == null) {
            /*
             * The variable cannot be read. Most probably, it is some node
             * variable that is not set here. Fail fast!
             */
            throw new IllegalStateException("Variable " + name + " cannot be resolved");
        }
        if (val instanceof JVMEntity) {
            val = ((JVMEntity) val).getValue();
        } else if (val instanceof Field) {
            /*
             * Variable restriction. See:
             * https://doi.org/10.1145/3285956
             * rule [E-FLD]
             */
            final Field<?> unrestricted = (Field<?>) val;
            final Field<Byte> restricted = context.buildField(it -> it, (byte) 0);
            val = unrestricted.projectOn(restricted);
        }
        return val;
    }

    @Override
    public Bytecode getBytecode() {
        return Bytecode.VARIABLE_ACCESS;
    }

    @Override
    public String getName() {
        return name.toString();
    }

    @Override
    public String toString() {
        return getName();
    }

}
