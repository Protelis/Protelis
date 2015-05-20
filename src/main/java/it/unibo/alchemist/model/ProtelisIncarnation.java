/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.model;

import gnu.trove.list.array.TByteArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import it.unibo.alchemist.external.cern.jet.random.engine.MersenneTwister;
import it.unibo.alchemist.external.cern.jet.random.engine.RandomEngine;
import it.unibo.alchemist.language.protelis.FunctionDefinition;
import it.unibo.alchemist.language.protelis.datatype.Tuple;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.util.CodePath;
import it.unibo.alchemist.language.protelis.util.Stack;
import it.unibo.alchemist.language.protelis.util.StackImpl;
import it.unibo.alchemist.model.implementations.actions.ProtelisProgram;
import it.unibo.alchemist.model.implementations.molecules.Molecule;
import it.unibo.alchemist.model.implementations.nodes.ProtelisNode;
import it.unibo.alchemist.model.interfaces.IMolecule;
import it.unibo.alchemist.model.interfaces.INode;
import it.unibo.alchemist.model.interfaces.Incarnation;
import it.unibo.alchemist.utils.FasterString;
import it.unibo.alchemist.utils.L;
import it.unibo.alchemist.utils.ParseUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.Pair;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import alice.tuprolog.Var;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author Danilo Pianini
 *
 */
public class ProtelisIncarnation implements Incarnation {

	/*
	 * search(A, [A|_]).
	 * 
	 * search(A, [B|T]) :- search(A, B).
	 * 
	 * search(A, [B|T]) :- search(A, T).
	 * 
	 * ts(A) :- search(A, [[[['service 1', 1.3]], ["service 2",0]]]).
	 */
	private static final Prolog ENGINE = new Prolog();
	private static final String GOALP1 = "search(";
	private static final String GOALP2 = ", ";
	private static final String GOALP3 = ").";
	private static final String[] ANS_NAMES = {"ans", "res", "result", "answer", "val", "value"};
	private static final Set<FasterString> NAMES;
	private static final RandomEngine RAND = new MersenneTwister();
	
	private final Cache<String, Optional<Pair<AnnotatedTree<?>, Map<FasterString, FunctionDefinition>>>> cache = CacheBuilder.newBuilder()
		.maximumSize(100)
		.expireAfterAccess(1, TimeUnit.HOURS)
		.expireAfterWrite(1, TimeUnit.HOURS)
		.build();

	static {
		final String th0 = "search(A, [A|_]).\n";
		final String th1 = "search(A, [B|T]) :- search(A, B).\n";
		final String th2 = "search(A, [B|T]) :- search(A, T).";
		Theory t;
		try {
			t = new Theory(th0 + th1 + th2);
			ENGINE.addTheory(t);
		} catch (InvalidTheoryException e) {
			L.error("Unable to initiate tuProlog.");
		}
		NAMES = Collections.unmodifiableSet(Arrays.stream(ANS_NAMES)
				.flatMap(n -> Arrays.stream(new String[]{n.toLowerCase(Locale.US), n.toUpperCase(Locale.US)}))
				.map(FasterString::new)
				.collect(Collectors.toSet()));
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public double getProperty(final INode<?> node, final IMolecule mol, final String prop) {
		Object val = node.getConcentration(mol);
		Optional<Pair<AnnotatedTree<?>, Map<FasterString, FunctionDefinition>>> prog = cache.getIfPresent(prop);
		if (prog == null) {
			try {
				prog = Optional.of(ParseUtils.parse(prop));
				cache.put(prop, prog);
			} catch (final RuntimeException e) {
				/*
				 * all fine, there is no program to evaluate.
				 */
				prog = Optional.empty();
				cache.put(prop, prog);
			}
		}
		val = preprocess(prog, node, val);
		if (val instanceof Number) {
			return ((Number) val).doubleValue();
		} else if (val instanceof String) {
			if (val.equals(prop)) {
				return 1;
			}
			return 0;
		} else if (val instanceof Boolean) {
			final Boolean cond = (Boolean) val;
			if (cond) {
				return 1d;
			} else {
				return 0d;
			}
		} else if (val instanceof Tuple) {
			/*
			 * [[service 1, THIS]*]
			 * 
			 * . -> subtuble = -> double / string match * -> ignore level
			 */
			final Tuple t = (Tuple) val;
			try {
				final SolveInfo s = ENGINE.solve(GOALP1 + prop + GOALP2 + t.toString() + GOALP3);
				if (s.isSuccess()) {
					final List<Var> binds = s.getBindingVars();
					if (binds.isEmpty()) {
						return 1;
					} else {
						final Optional<Var> vv = binds.stream()
								.filter(v -> v.getTerm() instanceof alice.tuprolog.Number)
								.findFirst();
						if (vv.isPresent()) {
							return ((alice.tuprolog.Number) vv.get().getTerm()).doubleValue();
						}
					}
				}
			} catch (MalformedGoalException e) {
				L.warn(e);
			} catch (NoSolutionException e) {
				L.error(e);
			}
		}
		return Double.NaN;
	}

	@SuppressWarnings("unchecked")
	private static Object preprocess(final Optional<Pair<AnnotatedTree<?>, Map<FasterString, FunctionDefinition>>> prog, final INode<?> node, final Object val) {
		try {
			if (prog.isPresent()) {
				final Pair<AnnotatedTree<?>, Map<FasterString, FunctionDefinition>> curProg = prog.get();
				final Map<CodePath, Object> newMap = new HashMap<>();
				final Map<CodePath, Object> lastExec = new HashMap<>();
				final Stack stack = new StackImpl(new HashMap<>(curProg.getSecond()));
				NAMES.stream().forEach(n -> stack.put(n, val, true));
				curProg.getFirst().eval((INode<Object>) node, new TIntObjectHashMap<>(), stack, lastExec, newMap, new TByteArrayList());
				return curProg.getFirst().getAnnotation();
			}
		} catch (final RuntimeException e) {
			/*
			 * Something went wrong, fallback.
			 */
			return val;
		}
		return val;
	}
	
	@Override
	public IMolecule createMolecule(final String s) {
		if (s.startsWith(ProtelisProgram.PROGRAM_ID_PREFIX)) {
			return ProtelisProgram.getProgramByID(new FasterString(s));
		}
		return new Molecule(s);
	}

}
