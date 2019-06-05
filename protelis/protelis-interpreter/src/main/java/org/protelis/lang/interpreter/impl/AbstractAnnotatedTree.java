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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.interpreter.util.ProtelisRuntimeException;
import org.protelis.lang.interpreter.util.WithBytecode;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

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
public abstract class AbstractAnnotatedTree<T> implements AnnotatedTree<T>, WithBytecode {

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

    /**
     * @return a {@link String} representing the branches of this tree, in the
     *         format (b1, b2, ..., bn).
     */
    protected final String branchesToString() {
        return branchesToString(", ", "(", ")");
    }

    /**
     * Returns a {@link String} representing the branches of this tree with the
     * specified format.
     * 
     * @param separator the separator {@link CharSequence}
     * @param prefix    the prefix
     * @param postfix   the postfix
     * @return a {@link String} representing the branches of this tree with the
     *         specified format.
     */
    protected final String branchesToString(final CharSequence separator, final CharSequence prefix, final CharSequence postfix) {
        final StringBuilder sb = new StringBuilder(prefix);
        forEachWithIndex((i, branch) -> {
            sb.append(stringFor(branch));
            if (i < branches.size() - 1) {
                sb.append(separator);
            }
        });
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
            context.newCallStackFrame(getBytecode().getCode());
            evaluate(context);
            context.returnFromCallFrame();
        } catch (ProtelisRuntimeException e) {
            e.fillInStackFrame(this);
            throw e;
        } catch (Exception e) { // NOPMD: this is wanted.
            throw new ProtelisRuntimeException(e, this);
        }
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
     * @return true if this node can get annotated with null values - namely, if it is an interaction with Java
     */
    protected boolean isNullable() {
        return false;
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

    @Override
    public final List<AnnotatedTree<?>> getBranches() {
        return Collections.unmodifiableList(branches);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return getClass().getSimpleName().toLowerCase(Locale.ENGLISH);
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
            evalInNewStackFrame(branch, context, i);
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
        annotation = isNullable() ? obj : Objects.requireNonNull(obj, () -> 
            this.getClass().getSimpleName() + " does not allow null return values. In: " + stringFor(this));
        erased = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName() + branchesToString();
    }

    /**
     * Evaluates the program using the passed {@link ExecutionContext}, but within a
     * new scope pushed onto the stack. Returns to the current scope after
     * evaluation.
     * 
     * @param target  the sub-program to evaluate
     * @param context the execution context
     * @param frameId id marker for new frame
     */
    protected static final void evalInNewStackFrame(final AnnotatedTree<?> target, final ExecutionContext context, final Bytecode frameId) {
        evalInNewStackFrame(target, context, frameId.getCode());
    }

    private static void evalInNewStackFrame(final AnnotatedTree<?> target, final ExecutionContext context, final int frameId) {
        context.newCallStackFrame(frameId);
        target.eval(context);
        context.returnFromCallFrame();
    }

    /**
     * A String representation of an {@link AnnotatedTree}. I
     * 
     * @param tree the tree to stringify
     * @return if the tree it is not erased (i.e., contains a value), returns a
     *         stringified version of such value. Otherwise, returns the branch name
     *         via {@link #getName()}
     */
    protected static final String stringFor(final AnnotatedTree<?> tree) {
        return tree.isErased() ? tree.getName() : tree.getAnnotation().toString();
    }
}
