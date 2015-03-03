/*
 * Copyright (C) 2010-2015, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.model;

import it.unibo.alchemist.language.protelis.datatype.Tuple;
import it.unibo.alchemist.model.implementations.actions.ProtelisProgram;
import it.unibo.alchemist.model.implementations.molecules.Molecule;
import it.unibo.alchemist.model.interfaces.IMolecule;
import it.unibo.alchemist.model.interfaces.INode;
import it.unibo.alchemist.model.interfaces.Incarnation;
import it.unibo.alchemist.utils.FasterString;
import it.unibo.alchemist.utils.L;

import java.util.List;
import java.util.Optional;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import alice.tuprolog.Var;

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
	}

	@Override
	public double getProperty(final INode<?> node, final IMolecule mol, final String prop) {
		final Object val = node.getConcentration(mol);
		return getVal(val, prop);
	}

	private static double getVal(final Object val, final String prop) {
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
					@SuppressWarnings("unchecked")
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

	@Override
	public IMolecule createMolecule(final String s) {
		if (s.startsWith(ProtelisProgram.PROGRAM_ID_PREFIX)) {
			return ProtelisProgram.getProgramByID(new FasterString(s));
		}
		return new Molecule(s);
	}

}
