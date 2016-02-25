/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.datatype;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;

import org.danilopianini.lang.LangUtils;
import org.danilopianini.lang.util.FasterString;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.util.Reference;

import gnu.trove.list.TByteList;
import gnu.trove.list.array.TByteArrayList;

/**
 * First-class Protelis function.
 */
public class FunctionDefinition implements Serializable {

    private static final long serialVersionUID = -4996419276551742628L;
    private final FasterString functionName;
    private final int argNumber;
    private final List<Reference> args;
    private final TByteList stackCode;
    private AnnotatedTree<?> functionBody;

    /**
     * @param name
     *            function name
     * @param args
     *            arguments
     */
    public FunctionDefinition(final FasterString name, final List<Reference> args) {
        LangUtils.requireNonNull(name, args);
        argNumber = args.size();
        functionName = name;
        this.args = args;
        //final ByteBuffer bb = ByteBuffer.allocate(Integer.BYTES + Long.BYTES + 1);
        final ByteBuffer bb = ByteBuffer.allocate((Integer.SIZE/Byte.SIZE + Long.SIZE/Byte.SIZE + 1));
        bb.putInt(functionName.hashCode());
        bb.putLong(functionName.hash64());
        bb.put((byte) argNumber);
        stackCode = new TByteArrayList(bb.array());
    }

    /**
     * @param name
     *            function name
     * @param args
     *            arguments
     */
    public FunctionDefinition(final String name, final List<Reference> args) {
        this(new FasterString(name), args);
    }

    /**
     * @return number of arguments
     */
    public final int getArgNumber() {
        return argNumber;
    }

    /**
     * @return the body of the function as defined. All annotations for the body
     *         are cleared. No side effects.
     */
    public AnnotatedTree<?> getBody() {
        return functionBody.copy();
    }

    /**
     * @param body
     *            the body of this function
     */
    public void setBody(final AnnotatedTree<?> body) {
        functionBody = body;
    }

    /**
     * @return function name
     */
    public FasterString getName() {
        return functionName;
    }

    @Override
    public String toString() {
        return functionName + "/" + argNumber;
    }

    @Override
    public boolean equals(final Object o) {
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
        assert i >= 0;
        assert i < args.size();
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

}
