/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.lang.interpreter.impl;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.protelis.lang.ProtelisLoader;
import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ProtelisProgram;

import static org.protelis.lang.interpreter.util.Bytecode.EVAL_DYNAMIC_CODE;

/**
 * Evaluate a Protelis sub-program.
 */
public final class Eval extends AbstractPersistedTree<Pair<String, ProtelisProgram>, Object> {

    private static final long serialVersionUID = 8811510896686579514L;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param arg
     *            argument whose annotation will be used as a string
     *            representing a program
     */
    public Eval(final Metadata metadata, final ProtelisAST<?> arg) {
        super(metadata, arg);
    }

    @Override
    public Object evaluate(final ExecutionContext context) {
        final String currentProgram = programText().eval(context).toString();
        final Pair<String, ProtelisProgram> previous = loadState(context, () -> createState(currentProgram));
        /*
         * Preserve state if program is constant
         */
        final Pair<String, ProtelisProgram> actualState = currentProgram.equals(previous.getKey())
                ? previous
                : createState(currentProgram);
        saveState(context, actualState);
        return context.runInNewStackFrame(EVAL_DYNAMIC_CODE.getCode(), ctx -> {
            actualState.getRight().compute(ctx);
        // TODO: figure out which references to pass down... and how to.
//          context.putMultipleVariables(result.getGloballyAvailableReferences());
            return actualState.getRight().getCurrentValue();
        });
    }

    private Pair<String, ProtelisProgram> createState(final String program) {
        try {
            return new ImmutablePair<>(program, ProtelisLoader.parseAnonymousModule(program));
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("The following program can't be parsed:\n" + program, e);
        }
    }

    @Override
    public Bytecode getBytecode() {
        return Bytecode.EVAL;
    }

    private ProtelisAST<?> programText() {
        return getBranch(0);
    }

    @Override
    public String toString() {
        return "eval(" + stringFor(getBranch(0)) + ")";
    }
}
