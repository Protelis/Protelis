/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis.util;

import it.unibo.alchemist.utils.FasterString;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Danilo Pianini
 *
 */
public class StackImpl implements Stack {
	
	private static final long serialVersionUID = -7123279550264674313L;
	private final Deque<Map<FasterString, Object>> stack = new LinkedList<>();

	public StackImpl(final Map<FasterString, Object> gamma) {
		stack.push(gamma);
	}

	@Override
	public void push() {
		stack.push(null);
	}

	@Override
	public void pop() {
		stack.pop();
	}

	@Override
	public Object put(final FasterString var, final Object val, final boolean canCreateNew) {
		if (canCreateNew) {
			/*
			 * Overrides the previous value only if it is at this depth in the stack
			 * 
			 * let c = 0;
			 * if(true) {
			 *   let c = 1
			 * } else { 
			 *   1
			 * } // c = 0
			 *
			 * 
			 */
			Map<FasterString, Object> cur = stack.pop();
			if (cur == null) {
				cur = new HashMap<>();
			}
			final Object res = cur.put(var, val);
			stack.push(cur);
			return res;
		}
		/*
		 * Overrides the previous value, regardless its position in the stack
		 * e.g.
		 * 
		 * let c = 0;
		 * if(true) {
		 *   c = 1
		 * } else { 
		 *   1
		 * } // c = 1
		 *
		 * 
		 */
		return stackOperation(varMap -> varMap.computeIfPresent(var, (k, v) -> val));
	}

	private Object stackOperation(final Function<Map<FasterString, Object>, Object> op) {
		for (final Map<FasterString, Object> varMap : stack) {
			if (varMap != null) {
				final Object res = op.apply(varMap);
				if (res != null) {
					return res;
				}
			}
		}
		return null;
	}
	
	@Override
	public Object get(final FasterString var) {
		return stackOperation(varMap -> varMap.get(var));
	}
	
	@Override
	public String toString() {
		return stack.toString();
	}

	@Override
	public void putAll(final Map<FasterString, ? extends Object> map) {
		Map<FasterString, Object> cur = stack.pop();
		if (cur == null) {
			cur = new HashMap<>();
		}
		cur.putAll(map);
		stack.push(cur);
	}

}
