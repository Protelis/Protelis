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

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java8.util.Optional;

import org.danilopianini.lang.util.FasterString;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.util.Reference;
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
    private final Map<Reference, FunctionDefinition> funs;
    private final FasterString name;

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
            final Map<Reference, FunctionDefinition> functions) {
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
            final Map<Reference, FunctionDefinition> functions) {
        Objects.requireNonNull(pName);
        Objects.requireNonNull(program);
        Objects.requireNonNull(functions);
        prog = program;
        funs = Collections.unmodifiableMap(functions);
        name = new FasterString(pName);
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
    public Map<Reference, FunctionDefinition> getNamedFunctions() {
        return funs;
    }

    @Override
    public FasterString getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "\n" + prog;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof SimpleProgramImpl && ((SimpleProgramImpl) obj).name.equals(name);
    }

}
