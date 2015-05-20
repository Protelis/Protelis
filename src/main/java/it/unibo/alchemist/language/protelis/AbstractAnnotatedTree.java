/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis;

import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.vm.ExecutionContext;
import it.unibo.alchemist.utils.L;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

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
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		toString(sb, 0);
		return sb.toString();
	}
	
	/**
	 * @param sb
	 *            {@link StringBuilder} to fill
	 * @param i
	 *            level of indentation
	 */
	@Override
	public final void toString(final StringBuilder sb, final int i) {
		indent(sb, i);
		if (erased) {
			sb.append('|');
			asString(sb, i);
			sb.append('|');
		} else {
			asString(sb, i);
			sb.append('\n');
			indent(sb, i);
			sb.append(':');
			if (annotation instanceof AnnotatedTree<?>) {
				sb.append('\n');
				((AnnotatedTree<?>) annotation).toString(sb, i + 1);
			} else {
				sb.append(annotation);
			}
		}
	}

	/**
	 * @param sb
	 *            {@link StringBuilder} to fill
	 * @param indent
	 *            level of indentation
	 */
	protected abstract void asString(StringBuilder sb, int indent);

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
	 * @return returns a stream containing the branches' annotations
	 */
	protected Stream<?> getBranchesAnnotationStream() {
		/*
		 * TODO: as soon as Javac fixes its terrible bug, switch this to:
		 * 
		 * return branches.stream().map(AnnotatedTree::getAnnotation);
		 */
		final List<Object> res = new ArrayList<>(branches.size());
		for (final AnnotatedTree<?> o : branches) {
			res.add(o.getAnnotation());
		}
		return res.stream();
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
	protected final void evalEveryBranchWithProjection(final ExecutionContext context) {
		for (int i = 0; i < branches.size(); i++) {
			final AnnotatedTree<?> branch = branches.get(i);
			context.newCallStackFrame((byte) i);
			branch.eval(context);
			context.returnFromCallFrame();
		}
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

//	/**
//	 * @param currentPosition
//	 *            go back one position in the passed call stack
//	 */
//	protected static final void removeLast(final TByteList currentPosition) {
//		currentPosition.removeAt(currentPosition.size() - 1);
//	}
	
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
				L.error(e);
			}
		}
	}
	
	/**
	 * Print utility to be used by subclasses. Prints all branches with the
	 * desired separator.
	 * 
	 * @param sb
	 *            the {@link StringBuilder} to use
	 * @param i
	 *            indentation
	 * @param separator
	 *            separator
	 */
	protected void fillBranches(final StringBuilder sb, final int i, final char separator) {
		forEach(b -> {
			sb.append('\n');
			b.toString(sb, i + 1);
			sb.append(separator);
		});
		if (getBranchesNumber() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
	}

}
