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
package org.protelis.vm;

/**
 * @author Danilo Pianini
 *
 */
public class ProtelisVM {

	private final IProgram prog;
	private final ExecutionContext ctx;
	
	public ProtelisVM(final IProgram program, final ExecutionContext context) {
		prog = program;
		ctx = context;
		ctx.setAvailableFunctions(program.getKnownFunctions());
	}
	
	public void runCycle(){
		/*
		 * 1. Take the messages received by neighbors
		 */
		ctx.setup();
		/*
 		 * 2. Compute
		 */
		prog.compute(ctx);
		/*
		 * 3. Finalize the new environment and send Messages away
		 */
		ctx.commit();
	}
	
	public Object getCurrentValue() {
		return prog.getCurrentValue();
	}

}
