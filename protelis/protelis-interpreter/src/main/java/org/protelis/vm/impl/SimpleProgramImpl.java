/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
/**
 * 
 */
package org.protelis.vm.impl;

import java.util.Objects;
import java.util.Optional;

import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.parser.protelis.ProtelisModule;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ProtelisProgram;

/**
 * Base implementation of {@link ProtelisProgram}.
 */
public final class SimpleProgramImpl implements ProtelisProgram {

    private static final long serialVersionUID = -986976491484860840L;
    private static final String DEFAULT_PROGRAM_NAME = "default_module:default_program";
    private final AnnotatedTree<?> prog;
    private final String name;

    /**
     * @param source
     *            Original {@link ProtelisProgram} parsed by Xtext. Used to get the
     *            module name.
     * @param program
     *            evaluation tree
     */
    public SimpleProgramImpl(
            final ProtelisModule source,
            final AnnotatedTree<?> program) {
        this(Optional.ofNullable(source).map(ProtelisModule::getName).orElse(DEFAULT_PROGRAM_NAME), program);
    }

    /**
     * @param pName
     *            Program name
     * @param program
     *            evaluation tree
     * @param functions
     *            available functions
     */
    public SimpleProgramImpl(
            final String pName,
            final AnnotatedTree<?> program) {
        name = Objects.requireNonNull(pName);
        prog = Objects.requireNonNull(program);
    }

    @Override
    public Object getCurrentValue() {
        return prog.getAnnotation();
    }

    @Override
    public void compute(final ExecutionContext context) {
        prog.eval(context);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "\n" + prog;
    }

}
