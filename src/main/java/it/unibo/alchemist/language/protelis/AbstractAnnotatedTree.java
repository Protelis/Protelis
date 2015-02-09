/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import gnu.trove.list.TByteList;
import gnu.trove.map.TIntObjectMap;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.model.interfaces.INode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.danilopianini.lang.CollectionUtils;

/**
 * @author Danilo Pianini
 *
 * @param <T>
 *            annotation type
 */
public abstract class AbstractAnnotatedTree<T> implements AnnotatedTree<T> {

	private static final long serialVersionUID = -8156985119843359212L;
	private final List<AnnotatedTree<?>> branches;
	private T annotation;
	private boolean erased = true;

	/**
	 * @param branch
	 *            the branches for this tree
	 */
	protected AbstractAnnotatedTree(final AnnotatedTree<?>... branch) {
		this(Arrays.asList(branch));
	}

	/**
	 * @param branch
	 *            the branches for this tree
	 */
	protected AbstractAnnotatedTree(final List<AnnotatedTree<?>> branch) {
		Objects.requireNonNull(branch);
		branches = branch;
	}

	@Override
	public String toString() {
		if (erased) {
			return "|" + asString() + "|";
		}
		return asString() + "·" + annotation;
	}

	/**
	 * @return a {@link String} representation of this tree node
	 */
	protected abstract String asString();

	@Override
	public final T getAnnotation() {
		return annotation;
	}

	@Override
	public void reset() {
		for (final AnnotatedTree<?> b : branches) {
			b.reset();
		}
		annotation = null;
	}

	@Override
	public void erase() {
		for (final AnnotatedTree<?> b : branches) {
			b.erase();
		}
		annotation = null;
		erased = true;
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
	public boolean isErased() {
		return erased;
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
	 * @return the number of branches
	 */
	protected int getBranchesNumber() {
		return branches.size();
	}

	/**
	 * Facility to run lambdas across all the branches.
	 * 
	 * @param action
	 *            the Consumer to execute
	 */
	protected final void forEach(final Consumer<? super AnnotatedTree<?>> action) {
		branches.stream().forEach(action);
	}

	/**
	 * Facility to run lambdas across all the branches.
	 * 
	 * @param action
	 *            the Consumer to execute
	 */
	protected final void forEachWithIndex(final BiConsumer<Integer, ? super AnnotatedTree<?>> action) {
		CollectionUtils.forEach(branches, action);
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
		branches.parallelStream().forEach(action);
	}

	/**
	 * Runs eval() on every branch, passing sigma and gamma as they are and
	 * projecting theta.
	 * 
	 * @param sigma
	 *            local node
	 * @param theta
	 *            AST of aligned nodes
	 * @param gamma
	 *            variables
	 * @param lastExec
	 *            last AST received (to be used in static-case optimization)
	 * @param newMap
	 *            the AST that will be sent at the end of this computation
	 * @param currentPosition
	 *            current position in the stack
	 */
	protected final void evalEveryBranchWithProjection(final INode<Object> sigma,
			final TIntObjectMap<Map<CodePath, Object>> theta, final Stack gamma, final Map<CodePath, Object> lastExec,
			final Map<CodePath, Object> newMap, final TByteList currentPosition) {
		forEachWithIndex((i, branch) -> {
			currentPosition.add(i.byteValue());
			branch.eval(sigma, theta, gamma, lastExec, newMap, currentPosition);
			removeLast(currentPosition);
		});
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

	@Override
	public final AnnotatedTree<?> getBranch(final int i) {
		return branches.get(i);
	}

	/**
	 * @param currentPosition
	 *            go back one position in the passed call stack
	 */
	protected static final void removeLast(final TByteList currentPosition) {
		currentPosition.removeAt(currentPosition.size() - 1);
	}

}
