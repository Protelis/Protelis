/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.lang.interpreter.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Map;
import org.protelis.lang.ProtelisLoadingUtilities;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

import java.util.List;
import java.util.Objects;

import static com.google.common.collect.Maps.newLinkedHashMapWithExpectedSize;

/**
 * Call a Protelis function.
 */
public final class FunctionCall extends AbstractProtelisAST<Object> {

    private static final long serialVersionUID = 4143090001260538814L;
    private final FunctionDefinition functionDefinition;
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
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "FunctionDefinition is immutable")
    public FunctionCall(final Metadata metadata, final FunctionDefinition functionDefinition, final List<ProtelisAST<?>> args) {
        super(metadata, args);
        Objects.requireNonNull(functionDefinition);
        this.functionDefinition = functionDefinition;
        if (this.functionDefinition.invokerShouldInitializeIt()) {
            if (args.size() > 1) {
                throw new IllegalArgumentException(
                    functionDefinition
                    + " is a lambda expression invokable with none or one parameter (it)"
                    + ", but was invoked with " + args + ", which are " + args.size()
                );
            }
        } else if (this.functionDefinition.getParameterCount() != args.size()) {
            throw new IllegalArgumentException(
                functionDefinition + " must be invoked with " + functionDefinition.getParameterCount()
                + " arguments, but was invoked with " + args + ", which are " + args.size()
            );
        }
        stackCode = this.functionDefinition.getStackCode();
    }

    @Override
    public Object evaluate(final ExecutionContext context) {
        /*
         * 1. Evaluate all the arguments
         * Inner gamma must hold param values
         */
        context.newCallStackFrame(stackCode);
        if (functionDefinition.invokerShouldInitializeIt() && getBranchesNumber() == 1) {
            context.putVariable(ProtelisLoadingUtilities.IT, context.runInNewStackFrame(0, getBranch(0)::eval));
        } else {
            /*
             * All branches must get evaluated **before** their result is pushed to the variables' map.
             * Otherwise, further branch evaluation may overwrite previous variable assignments.
             */
            final Map<Reference, Object> arguments = newLinkedHashMapWithExpectedSize(getBranchesNumber());
            for (int i = 0; i < getBranchesNumber(); i++) {
                arguments.put(functionDefinition.getArgumentByPosition(i), context.runInNewStackFrame(i, getBranch(i)::eval));
            }
            context.putMultipleVariables(arguments);
        }
        /*
         * Evaluate the body and copy return its result
         */
        final Object result = functionDefinition.getBody().eval(context);
        context.returnFromCallFrame();
        return result;
    }

    @Override
    public Bytecode getBytecode() {
        return Bytecode.FUNCTION_CALL;
    }

    /**
     * @return the {@link FunctionDefinition}
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "The field is immutable")
    public FunctionDefinition getFunctionDefinition() {
        return functionDefinition;
    }

    @Override
    public String getName() {
        return functionDefinition.getName();
    }
}
