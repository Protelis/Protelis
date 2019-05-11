/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.ProtelisLoader;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ProtelisProgram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java8.util.Optional;

/**
 * Evaluate a Protelis sub-program.
 */
public final class Eval extends AbstractSATree<ProtelisProgram, Object> {

    private static final byte DYN_CODE_INDEX = -1;
    private static final Logger L = LoggerFactory.getLogger(Eval.class);
    private static final long serialVersionUID = 8811510896686579514L;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param arg
     *            argument whose annotation will be used as a string
     *            representing a program
     */
    public Eval(final Metadata metadata, final AnnotatedTree<?> arg) {
        super(metadata, arg);
    }

    @Override
    public Eval copy() {
        return new Eval(getMetadata(), deepCopyBranches().get(0));
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        final Object previous = Optional.ofNullable(getBranch(0).getAnnotation())
            .map(Object::toString)
            .orElse(null);
        projectAndEval(context);
        final String program = getBranch(0).getAnnotation().toString();
        if (isErased() || !program.equals(previous)) {
            try {
                final ProtelisProgram result = ProtelisLoader.parseAnonymousModule(program);
                setSuperscript(result);
            } catch (IllegalArgumentException e) {
                L.error("Non parse-able program", e);
                throw new IllegalStateException("The following program can't be parsed:\n" + program, e);
            }
        }
        final ProtelisProgram result = getSuperscript();
        context.newCallStackFrame(DYN_CODE_INDEX);
        context.putMultipleVariables(result.getGloballyAvailableReferences());
        result.compute(context);
        setAnnotation(result.getCurrentValue());
        context.returnFromCallFrame();
    }

    @Override
    public Bytecode getBytecode() {
        return Bytecode.ENV;
    }

}
