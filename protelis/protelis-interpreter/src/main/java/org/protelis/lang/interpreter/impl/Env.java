/**
 * 
 */
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ExecutionEnvironment;

/**
 *
 */
public final class Env extends AbstractAnnotatedTree<ExecutionEnvironment> {

    public Env(Metadata metadata) {
        super(metadata);
    }

    private static final long serialVersionUID = 636239540800669478L;

    @Override
    public AnnotatedTree<ExecutionEnvironment> copy() {
        return new Env(getMetadata());
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
