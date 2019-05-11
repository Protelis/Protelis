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

import java.util.Map;
import java.util.Objects;

import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.parser.protelis.ProtelisModule;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ProtelisProgram;

import java8.util.Optional;

/**
 * Base implementation of {@link ProtelisProgram}.
 */
public final class SimpleProgramImpl implements ProtelisProgram {

    private static final long serialVersionUID = -986976491484860840L;
    private static final String DEFAULT_PROGRAM_NAME = "default_module:default_program";
    private final AnnotatedTree<?> prog;
    private final Map<Reference, ?> funs;
    private final String name;

    /**
     * @param source
     *            Original {@link ProtelisProgram} parsed by Xtext. Used to get the
     *            module name.
     * @param program
     *            evaluation tree
     * @param functions
     *            available functions
     */
    public SimpleProgramImpl(
            final ProtelisModule source,
            final AnnotatedTree<?> program,
            final Map<Reference, ?> functions) {
        this(Optional.of(source).map(ProtelisModule::getName).orElse(DEFAULT_PROGRAM_NAME), program, functions);
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
            final AnnotatedTree<?> program,
            final Map<Reference, ?> functions) {
        name = Objects.requireNonNull(pName);
        prog = Objects.requireNonNull(program);
        funs = Objects.requireNonNull(functions);
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
    public Map<Reference, ?> getGloballyAvailableReferences() {
        return funs;
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
