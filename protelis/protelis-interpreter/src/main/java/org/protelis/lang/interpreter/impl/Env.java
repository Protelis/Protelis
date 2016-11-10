/**
 * 
 */
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ExecutionEnvironment;

/**
 *
 */
public class Env extends AbstractAnnotatedTree<ExecutionEnvironment> {

    private static final long serialVersionUID = 636239540800669478L;

    @Override
    public AnnotatedTree<ExecutionEnvironment> copy() {
        return new Env();
    }

    @Override
    public void eval(final ExecutionContext context) {
        assert context != null;
        setAnnotation(context.getExecutionEnvironment());
    }

    @Override
    protected void asString(final StringBuilder sb, final int indent) {
        sb.append("env");
    }

}
