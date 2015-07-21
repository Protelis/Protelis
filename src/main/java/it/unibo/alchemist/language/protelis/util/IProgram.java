package it.unibo.alchemist.language.protelis.util;

import java.io.Serializable;
import java.util.Map;

import it.unibo.alchemist.language.protelis.FunctionDefinition;
import it.unibo.alchemist.language.protelis.vm.ExecutionContext;
import org.danilopianini.lang.util.FasterString;

public interface IProgram extends Serializable {
	
	Object getCurrentValue();
	
	void compute(ExecutionContext context);
	
	Map<FasterString, FunctionDefinition> getKnownFunctions();
	
	FasterString getName();

}
