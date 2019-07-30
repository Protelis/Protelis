/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.datatype;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.protelis.lang.ProtelisLoadingUtilities;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.lang.interpreter.util.SerializableSupplier;
import org.protelis.parser.protelis.FunctionDef;
import org.protelis.parser.protelis.Lambda;
import org.protelis.parser.protelis.ShortLambda;
import org.protelis.parser.protelis.VarDef;

import gnu.trove.list.array.TByteArrayList;

/**
 * First-class Protelis function.
 */
public final class FunctionDefinition implements Serializable {

    private static final long serialVersionUID = 1;
    private final String functionName;
    private final int argNumber;
    private final List<Reference> args;
    private final TByteArrayList stackCode;
    private final transient Supplier<AnnotatedTree<?>> bodySupplier;
    private final boolean initializeIt;
    private AnnotatedTree<?> body;

    /**
     * @param name         function name
     * @param args         arguments
     * @param bodySupplier function providing a body when needed
     */
    private FunctionDefinition(final String name, final List<Reference> args, final SerializableSupplier<AnnotatedTree<?>> bodySupplier, final boolean maybeOneArgument) {
        argNumber = Objects.requireNonNull(args).size();
        if (maybeOneArgument && argNumber != 0) {
            throw new IllegalArgumentException("Function has optional 'it' parameter bit requires arguments");
        }
        initializeIt = maybeOneArgument;
        if (argNumber > Byte.MAX_VALUE) {
            throw new IllegalArgumentException("Currently the maximum number of allowed parameters for a function is "
                    + Byte.MAX_VALUE
                    + " " + name + " has " + argNumber + " parameters.");
        }
        functionName = Objects.requireNonNull(name);
        this.args = args;
        final byte[] asciibytes = functionName.getBytes(StandardCharsets.US_ASCII);
        final ByteBuffer bb = ByteBuffer.allocate(asciibytes.length + 1);
        bb.put((byte) argNumber);
        bb.put(asciibytes);
        stackCode = new TByteArrayList(bb.array());
        this.bodySupplier = bodySupplier;
    }

    /**
     * @param functionDefinition original parsed function
     * @param bodyProvider body calculator
     */
    public FunctionDefinition(final FunctionDef functionDefinition, final SerializableSupplier<AnnotatedTree<?>> bodyProvider) {
        this(ProtelisLoadingUtilities.qualifiedNameFor(functionDefinition),
            Optional.ofNullable(functionDefinition.getArgs())
                .<List<VarDef>>flatMap(it -> Optional.ofNullable(it.getArgs()))
                .orElseGet(Collections::emptyList)
                .stream()
                .map(Reference::new)
                .collect(Collectors.toList()),
            bodyProvider, false
        );
    }

    /**
     * @param lambda lambda expression
     * @param args arguments for this lambda
     * @param bodyProvider function providing a body when needed
     */
    public FunctionDefinition(final Lambda lambda, final List<Reference> args, final SerializableSupplier<AnnotatedTree<?>> bodyProvider) {
        this(ProtelisLoadingUtilities.qualifiedNameFor(lambda), args, bodyProvider, lambda instanceof ShortLambda);
    }

    /**
     * @return number of arguments
     */
    public int getParameterCount() {
        return argNumber;
    }

    /**
     * @return the body of the function as defined. All annotations for the body
     *         are cleared. No side effects.
     */
    public AnnotatedTree<?> getBody() {
        if (body == null) {
            body = bodySupplier.get();
        }
        return body;
    }

    /**
     * @return function name
     */
    public String getName() {
        return functionName;
    }

    @Override
    public String toString() {
        return functionName + "/" + argNumber;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof FunctionDefinition) {
            final FunctionDefinition fd = (FunctionDefinition) o;
            return functionName.equals(fd.functionName) && argNumber == fd.argNumber;
        }
        return false;
    }

    /**
     * @param i
     *            argument position
     * @return argument internal name
     */
    public Reference getArgumentByPosition(final int i) {
        return args.get(i);
    }

    @Override
    public int hashCode() {
        return functionName.hashCode() + argNumber;
    }

    /**
     * @return a special hash code to identify this function in the call stack
     */
    public byte[] getStackCode() {
        return stackCode.toArray();
    }

    public boolean invokerShouldInitializeIt() {
        return initializeIt;
    }

    private void writeObject(final ObjectOutputStream o) throws IOException {
        getBody(); // Forces body initialization
        o.defaultWriteObject();
    }

}
