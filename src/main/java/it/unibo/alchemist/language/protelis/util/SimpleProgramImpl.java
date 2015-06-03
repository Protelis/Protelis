/**
 * 
 */
package it.unibo.alchemist.language.protelis.util;

import it.unibo.alchemist.language.protelis.FunctionDefinition;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.protelis.Program;
import it.unibo.alchemist.language.protelis.vm.ExecutionContext;
import it.unibo.alchemist.utils.FasterString;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @author Danilo Pianini
 *
 */
public class SimpleProgramImpl implements IProgram {
	
	private static final long serialVersionUID = -986976491484860840L;
	private final AnnotatedTree<?> prog;
	private final Map<FasterString, FunctionDefinition> funs;
	private final FasterString name;
	
	public SimpleProgramImpl(final Program source, final AnnotatedTree<?> program, final Map<FasterString, FunctionDefinition> functions) {
		Objects.requireNonNull(program);
		Objects.requireNonNull(functions);
		Objects.requireNonNull(source);
		prog = program;
		funs = Collections.unmodifiableMap(functions);
		final String pName = source.getName();
		if (pName == null) {
		 	name = new FasterString("(default-module)::(default-program)");
		} else {
			name = new FasterString(pName);
		}
	}

	@Override
	public Object getCurrentValue() {
		return prog.getAnnotation();
	}

	@Override
	public void compute(final ExecutionContext context) {
		prog.eval(context);
	}

	@Override
	public Map<FasterString, FunctionDefinition> getKnownFunctions() {
		return funs;
	}

	@Override
	public FasterString getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name + "\n" + prog;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals(final Object obj) {
		return obj instanceof SimpleProgramImpl && ((SimpleProgramImpl) obj).name.equals(name);
	}

}
