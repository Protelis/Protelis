/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import static java8.util.stream.StreamSupport.parallelStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.protelis.lang.ProtelisRuntimeException;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java8.util.function.BiConsumer;
import java8.util.function.Consumer;
import java8.util.stream.IntStream;
import java8.util.stream.IntStreams;

/**
 * Basic implementation of an {@link AnnotatedTree}.
 *
 * @param <T>
 *            annotation type
 */
public abstract class AbstractAnnotatedTree<T> implements AnnotatedTree<T> {

    private static final Logger L = LoggerFactory.getLogger(AbstractAnnotatedTree.class);
    private static final long serialVersionUID = -8156985119843359212L;
    private T annotation;
    private final List<AnnotatedTree<?>> branches;
    private boolean erased = true;
    private final Metadata metadata;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param branch
     *            the branches for this tree
     */
    protected AbstractAnnotatedTree(final Metadata metadata, final AnnotatedTree<?>... branch) {
        this(metadata, Arrays.asList(branch));
    }

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param branch
     *            the branches for this tree
     */
    protected AbstractAnnotatedTree(final Metadata metadata, final List<AnnotatedTree<?>> branch) {
        this.metadata = Objects.requireNonNull(metadata);
        Objects.requireNonNull(branch);
        branches = branch;
    }

    protected String branchesToString() {
        return branchesToString(", ", "(", ")");
    }

    protected final String branchesToString(CharSequence separator, CharSequence prefix, CharSequence postfix) {
        final StringBuilder sb = new StringBuilder(prefix);
        if (branches.size() > 0) {
            forEachWithIndex((i, branch) -> {
                sb.append(stringFor(branch));
                if (i < branches.size() - 1) {
                    sb.append(separator);
                }
            });
        }
        return sb.append(postfix).toString();
    }

    /**
     * Subclasses must use this method.
     * 
     * @return a deep copy of the branches
     */
    protected final List<AnnotatedTree<?>> deepCopyBranches() {
        final List<AnnotatedTree<?>> res = new ArrayList<>(branches.size());
        for (final AnnotatedTree<?> br : branches) {
            res.add(br.copy());
        }
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void erase() {
        for (final AnnotatedTree<?> b : branches) {
            b.erase();
        }
        annotation = null;
        erased = true;
    }

    @Override
    public final void eval(final ExecutionContext context) {
        try {
            evaluate(context);
        } catch (ProtelisRuntimeException e) {
            e.calledBy(this);
            throw e;
        } catch (Exception e) {
            throw new ProtelisRuntimeException(e, this);
        }
    }

    @Override
    public final void evalInNewStackFrame(final ExecutionContext context, final byte frameId) {
        context.newCallStackFrame(frameId);
        eval(context);
        context.returnFromCallFrame();
    }

    /**
     * Evaluates this AST node. This method can throw any exception,
     * {@link AbstractAnnotatedTree} takes care of storing the necessary metadata.
     * 
     * @param context the execution context
     */
    protected abstract void evaluate(ExecutionContext context);

    /**
     * Facility to run lambdas across all the branches.
     * 
     * @param action
     *            the Consumer to execute
     */
    protected final void forEach(final Consumer<? super AnnotatedTree<?>> action) {
        for (final AnnotatedTree<?> subProgram: branches) {
            action.accept(subProgram);
        }
    }

    /**
     * Facility to run lambdas across all the branches.
     * 
     * @param action
     *            the Consumer to execute
     */
    protected final void forEachWithIndex(final BiConsumer<Integer, ? super AnnotatedTree<?>> action) {
        for (int i = 0; i < getBranchesNumber(); i++) {
            action.accept(i, getBranch(i));
        }
    }

    @Override
    public final T getAnnotation() {
        return annotation;
    }

    @Override
    public final AnnotatedTree<?> getBranch(final int i) {
        return branches.get(i);
    }

    /**
     * @return Directly accesses the {@link List} where branches are stored:
     *         modifications on branches will reflect in the internal branch
     *         representation. Be careful.
     */
    protected List<AnnotatedTree<?>> getBranches() {
        return branches;
    }

    /**
     * @return the current branches annotations
     */
    protected final Object[] getBranchesAnnotations() {
        final Object[] annotations = branches.toArray();
        for (int i = 0; i < annotations.length; i++) {
            annotations[i] = ((AnnotatedTree<?>) annotations[i]).getAnnotation();
        }
        return annotations;
    }

    /**
     * @return the number of branches
     */
    protected final int getBranchesNumber() {
        return branches.size();
    }

    @Override
    public final Metadata getMetadata() {
        return metadata;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName().toLowerCase();
    }

    private IntStream indexStream() {
        return IntStreams.range(0, getBranchesNumber());
    }

    @Override
    public final boolean isErased() {
        return erased;
    }

    /**
     * Facility to run lambdas across all the branches in a PARALELL fashion. Be
     * EXTREMELY careful with this. If you are not sure whether or not you
     * should use this, you should not.
     * 
     * @param action
     *            the Consumer to execute
     */
    protected final void parallelForEach(final Consumer<? super AnnotatedTree<?>> action) {
        parallelStream(branches).forEach(action);
    }

    /**
     * Facility to run lambdas across all the branches in a PARALELL fashion. Be
     * EXTREMELY careful with this. If you are not sure whether or not you
     * should use this, you should not.
     * 
     * @param action
     *            the Consumer to execute
     */
    protected final void parallelForEachWithIndex(final BiConsumer<Integer, ? super AnnotatedTree<?>> action) {
        indexStream().parallel().forEach(i -> action.accept(i, getBranch(i)));
    }

    /**
     * Runs eval() sequentially on every branch, creating a new stack frame for
     * each one.
     * 
     * @param context
     *            the execution context
     */
    protected final void projectAndEval(final ExecutionContext context) {
        forEachWithIndex((i, branch) -> {
            branch.evalInNewStackFrame(context, i.byteValue());
        });
    }

    @Override
    public final void reset() {
        for (final AnnotatedTree<?> b : branches) {
            b.reset();
        }
        annotation = null;
    }

    /**
     * @param obj
     *            the annotation to set
     */
    protected final void setAnnotation(final T obj) {
        annotation = obj;
        erased = false;
    }

    @Override
    public String toString() {
        return getName() + branchesToString();
    }

    /**
     * Utility for indenting lines.
     * 
     * @param target
     *            the {@link StringBuilder} containing
     * @param i
     *            the level of indentation
     */
    protected static void indent(final Appendable target, final int i) {
        for (int j = 0; j < i; j++) {
            try {
                target.append('\t');
            } catch (IOException e) {
                L.error("There is a bug.", e);
            }
        }
    }

    protected static final String stringFor(AnnotatedTree<?> branch) {
        return branch.isErased() ? branch.getName() : branch.getAnnotation().toString();
    }
}
