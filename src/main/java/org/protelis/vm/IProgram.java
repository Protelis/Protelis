package org.protelis.vm;

import java.io.Serializable;
import java.util.Map;

import org.danilopianini.lang.util.FasterString;
import org.protelis.lang.datatype.FunctionDefinition;

public interface IProgram extends Serializable {
	
	Object getCurrentValue();
	
	void compute(ExecutionContext context);
	
	Map<FasterString, FunctionDefinition> getKnownFunctions();
	
	FasterString getName();

}
