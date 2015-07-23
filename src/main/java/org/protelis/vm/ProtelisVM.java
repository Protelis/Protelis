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
