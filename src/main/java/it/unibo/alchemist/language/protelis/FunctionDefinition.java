/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import gnu.trove.list.TByteList;
import gnu.trove.list.array.TByteArrayList;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.protelis.VAR;
import it.unibo.alchemist.utils.FasterString;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author Danilo Pianini
 *
 */
public class FunctionDefinition implements Serializable {

	private static final long serialVersionUID = -4996419276551742628L;
	private final FasterString n;
	private final int argNumber;
	private final FasterString[] internalNames;
	private final TByteList stackCode;
	private AnnotatedTree<?> b;
	
	/**
	 * @param name function name
	 * @param args arguments
	 */
	public FunctionDefinition(final FasterString name, final List<VAR> args) {
		argNumber = args.size();
		n = name;
		internalNames = new FasterString[argNumber];
		for (int i = 0; i < argNumber; i++) {
			internalNames[i] = new FasterString(args.get(i).getName());
		}
		final ByteBuffer bb = ByteBuffer.allocate(Integer.BYTES + Long.BYTES + 1);
		bb.putInt(n.hashCode());
		bb.putLong(n.hash64());
		bb.put((byte) argNumber);
		stackCode = new TByteArrayList(bb.array());
	}
	
	/**
	 * @param name function name
	 * @param args arguments
	 */
	public FunctionDefinition(final String name, final List<VAR> args) {
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
		return b.copy();
	}

	/**
	 * @param body the body of this function
	 */
	public void setBody(final AnnotatedTree<?> body) {
		b = body;
	}
	
	/**
	 * @return function name
	 */
	public FasterString getName() {
		return n;
	}
	
	@Override
	public String toString() {
		return n + "/" + argNumber;
	}
	
	@Override
	public boolean equals(final Object o) {
		if (o instanceof FunctionDefinition) {
			final FunctionDefinition fd = (FunctionDefinition) o;
			return n.equals(fd.n) && argNumber == fd.argNumber;
		}
		return false;
	}
	
	/**
	 * @param i argument position
	 * @return argument internal name
	 */
	public FasterString getInternalName(final int i) {
		return internalNames[i];
	}
	
	@Override
	public int hashCode() {
		return n.hashCode() + argNumber;
	}
	
	/**
	 * @return a special hash code to identify this function in the call stack
	 */
	public byte[] getStackCode() {
		return stackCode.toArray();
	}

}
