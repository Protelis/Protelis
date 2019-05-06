/**
 * 
 */
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ExecutionEnvironment;

/**
 *
 */
public final class Env extends AbstractAnnotatedTree<ExecutionEnvironment> {

    private static final long serialVersionUID = 636239540800669478L;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     */
    public Env(final Metadata metadata) {
        super(metadata);
    }

    @Override
    public AnnotatedTree<ExecutionEnvironment> copy() {
        return new Env(getMetadata());
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        assert context != null;
        setAnnotation(context.getExecutionEnvironment());
    }

    @Override
    public Bytecode getBytecode() {
        return Bytecode.ENV;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName();
    }


}
