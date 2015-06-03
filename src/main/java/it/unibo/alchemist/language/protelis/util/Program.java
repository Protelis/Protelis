package it.unibo.alchemist.language.protelis.util;

import java.util.Map;

import it.unibo.alchemist.language.protelis.FunctionDefinition;
import it.unibo.alchemist.language.protelis.vm.ExecutionContext;
import it.unibo.alchemist.utils.FasterString;

public interface Program {
	
	Object getCurrentValue();
	
	void compute(ExecutionContext context);
	
	Map<FasterString, FunctionDefinition> getKnownFunctions();
	
	FasterString getName();

}
