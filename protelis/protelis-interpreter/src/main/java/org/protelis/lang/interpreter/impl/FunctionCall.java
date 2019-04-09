/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import java.util.List;
import java.util.Objects;

import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

/**
 * Call a Protelis function.
 */
public final class FunctionCall extends AbstractSATree<AnnotatedTree<?>, Object> {

    private static final long serialVersionUID = 4143090001260538814L;
    private final FunctionDefinition fd;
    private final byte[] stackCode;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param functionDefinition
     *            the definition of the function
     * @param args
     *            the arguments. Must be in the same number of the
     *            {@link FunctionDefinition}'s expected arguments
     */
    public FunctionCall(final Metadata metadata, final FunctionDefinition functionDefinition, final List<AnnotatedTree<?>> args) {
        super(metadata, args);
        Objects.requireNonNull(functionDefinition);
        fd = functionDefinition;
        if (fd.getArgNumber() != args.size()) {
            throw new IllegalArgumentException(fd + " must be invoked with " + fd.getArgNumber()
                    + " arguments, but was invoked with " + args + ", which are " + args.size());
        }
        stackCode = fd.getStackCode();
    }

    @Override
    public FunctionCall copy() {
        /*
         * Deep copy the arguments
         */
        final FunctionCall res = new FunctionCall(getMetadata(), fd, deepCopyBranches());
        if (!isErased()) {
            res.setAnnotation(null);
            res.setSuperscript(getSuperscript().copy());
        }
        return res;
    }

    /**
     * @return the function body
     */
    protected AnnotatedTree<?> getBody() {
        return fd.getBody();
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        /*
         * 1. Evaluate all the arguments
         */
        projectAndEval(context);
        /*
         * Inner gamma must hold param values
         */
        context.newCallStackFrame(stackCode);
        forEachWithIndex((i, b) -> {
            context.putVariable(fd.getArgumentByPosition(i), b.getAnnotation(), true);
        });
        /*
         * 2. Load a fresh body as superscript
         */
        if (isErased()) {
            setSuperscript(getBody());
        }
        /*
         * Evaluate the body and copy its result in the annotation
         */
        getSuperscript().eval(context);
        context.returnFromCallFrame();
        setAnnotation(getSuperscript().getAnnotation());
    }

    @Override
    protected void innerAsString(final StringBuilder sb, final int indent) {
        sb.append(fd.getName())
            .append('/')
            .append(fd.getArgNumber())
            .append('(');
        fillBranches(sb, indent, ',');
        sb.append(')');
    }

    /**
     * @return the {@link FunctionDefinition}
     */
    public FunctionDefinition getFunctionDefinition() {
        return fd;
    }

}
