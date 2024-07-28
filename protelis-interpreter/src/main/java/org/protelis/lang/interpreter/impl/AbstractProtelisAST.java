/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.util.ProtelisRuntimeException;
import org.protelis.lang.interpreter.util.WithBytecode;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Basic implementation of an {@link ProtelisAST}.
 *
 * @param <T>
 *            annotation type
 */
public abstract class AbstractProtelisAST<T> implements ProtelisAST<T>, WithBytecode {

    private static final long serialVersionUID = 1L;
    private final List<ProtelisAST<?>> branches;
    private final Metadata metadata;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param branch
     *            the branches for this tree
     */
    protected AbstractProtelisAST(final Metadata metadata, final ProtelisAST<?>... branch) {
        this(metadata, branch.length == 0 ? Collections.emptyList() : Arrays.asList(branch));
    }

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param branch
     *            the branches for this tree
     */
    protected AbstractProtelisAST(final Metadata metadata, final List<ProtelisAST<?>> branch) {
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

    @Override
    public final T eval(final ExecutionContext context) {
        try {
            final T result = context.runInNewStackFrame(getBytecode().getCode(), this::evaluate);
            if (isNullable() || result != null) {
                return result;
            } else {
                throw new ProtelisRuntimeException(
                    new IllegalStateException(
                        "Evaluation returned null, but null values are not allowed but when interacting with Java methods."
                    ),
                    this
                );
            }
        } catch (ProtelisRuntimeException e) { // NOPMD: this is wanted.
            e.fillInStackFrame(this); // Annotate this frame
            throw e; // Go up the AST
        } catch (Exception e) { // NOPMD: this is wanted.
            throw new ProtelisRuntimeException(e, this);
        }
    }

    /**
     * Evaluates this AST node. This method can throw any exception,
     * {@link AbstractProtelisAST} takes care of storing the necessary metadata.
     * 
     * @param context the execution context
     * @return the result of the evaluation
     */
    protected abstract T evaluate(ExecutionContext context);

    /**
     * Facility to run lambdas across all the branches.
     * 
     * @param action
     *            the Consumer to execute
     */
    protected final void forEach(final Consumer<? super ProtelisAST<?>> action) {
        for (final ProtelisAST<?> subProgram: branches) {
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
    protected final void forEachWithIndex(final BiConsumer<Integer, ? super ProtelisAST<?>> action) {
        for (int i = 0; i < getBranchesNumber(); i++) {
            action.accept(i, getBranch(i));
        }
    }

    @Override
    public final ProtelisAST<?> getBranch(final int i) {
        return branches.get(i);
    }

    @Override
    public final List<ProtelisAST<?>> getBranches() {
        return Collections.unmodifiableList(branches);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName() + branchesToString();
    }

    /**
     * A String representation of an {@link ProtelisAST}. I
     * 
     * @param tree the tree to stringify
     * @return if the tree it is not erased (i.e., contains a value), returns a
     *         stringified version of such value. Otherwise, returns the branch name
     *         via {@link #getName()}
     */
    protected static final String stringFor(final ProtelisAST<?> tree) {
        return Objects.requireNonNull(tree, "Impossible to convert a null ProtelisAST to a String").getName();
    }
}
