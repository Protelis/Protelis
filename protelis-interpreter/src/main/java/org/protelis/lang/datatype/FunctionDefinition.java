/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.lang.datatype;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gnu.trove.list.array.TByteArrayList;
import org.protelis.lang.ProtelisLoadingUtilities;
import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.parser.protelis.FunctionDef;
import org.protelis.parser.protelis.Lambda;
import org.protelis.parser.protelis.ShortLambda;
import org.protelis.parser.protelis.VarDef;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * First-class Protelis function.
 */
@SuppressFBWarnings(value = "SE_TRANSIENT_FIELD_NOT_RESTORED",
justification = "No need to recover the field, as the body is always generated before serialization")
public final class FunctionDefinition implements Serializable {

    private static final long serialVersionUID = 1;
    private final int argNumber;
    private final List<Reference> args;
    private final String functionName;
    private final boolean initializeIt;
    private final TByteArrayList stackCode;
    private final transient Supplier<ProtelisAST<?>> bodySupplier;
    private ProtelisAST<?> cleanBody;

    /**
     * @param functionDefinition original parsed function
     * @param bodySupplier body calculator
     */
    public FunctionDefinition(final FunctionDef functionDefinition, final Supplier<ProtelisAST<?>> bodySupplier) {
        this(ProtelisLoadingUtilities.qualifiedNameFor(functionDefinition),
            Optional.ofNullable(functionDefinition.getArgs())
                .<List<VarDef>>flatMap(it -> Optional.ofNullable(it.getArgs()))
                .orElseGet(Collections::emptyList)
                .stream()
                .map(Reference::new)
                .collect(Collectors.toList()),
            bodySupplier, false
        );
    }

    /**
     * @param lambda lambda expression
     * @param args arguments for this lambda
     * @param body the function body
     */
    public FunctionDefinition(final Lambda lambda, final List<Reference> args, final ProtelisAST<?> body) {
        this(ProtelisLoadingUtilities.qualifiedNameFor(lambda), args, () -> body, lambda instanceof ShortLambda);
    }

    private FunctionDefinition(
            final String name,
            final List<Reference> args,
            final Supplier<ProtelisAST<?>> bodySupplier,
            final boolean maybeOneArgument
    ) {
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
        final byte[] asciibytes = functionName.getBytes(StandardCharsets.UTF_8);
        final ByteBuffer bb = ByteBuffer.allocate(asciibytes.length + 1);
        bb.put((byte) argNumber);
        bb.put(asciibytes);
        stackCode = new TByteArrayList(bb.array());
        this.bodySupplier = bodySupplier;
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

    /**
     * @return the body of the function as defined. All annotations for the body
     *         are cleared. No side effects.
     */
    public ProtelisAST<?> getBody() {
        if (cleanBody == null) {
            cleanBody = bodySupplier.get();
        }
        return cleanBody;
    }

    /**
     * @return function name
     */
    public String getName() {
        return functionName;
    }

    /**
     * @return number of arguments
     */
    public int getParameterCount() {
        return argNumber;
    }

    /**
     * @return a special hash code to identify this function in the call stack
     */
    public byte[] getStackCode() {
        return stackCode.toArray();
    }

    @Override
    public int hashCode() {
        return functionName.hashCode() + argNumber;
    }

    /**
     * @return true if this function definition was originated from a Kotlin-style
     *         lambda without explicit arguments, hence may be invoked with a single
     *         parameter 'it'
     */
    public boolean invokerShouldInitializeIt() {
        return initializeIt;
    }

    @Override
    public String toString() {
        return functionName + "/" + argNumber;
    }

    private void writeObject(final java.io.ObjectOutputStream stream) throws IOException {
        getBody();
        stream.defaultWriteObject();
    }
}
