/**
 * 
 */
package it.unibo.alchemist.language.protelis.vm;

import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.CodePath;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Danilo Pianini
 *
 */
public class ProtelisVM {

	private final AnnotatedTree<?> prog;
	private final ExecutionContext ctx;
	
	public ProtelisVM(final AnnotatedTree<?> program, final ExecutionContext context) {
		prog = program;
		ctx = context;
	}
	
	public void runCycle(){
		/*
		 * 1. Take the messages received by neighbors
		 */
		ctx.setup();
		/*
 		 * 2. Compute
		 */
		prog.eval(ctx);
		/*
		 * 3. Finalize the new environment and send Messages away
		 */
		ctx.commit();
	}

}
