/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.vm.util;

import static org.danilopianini.lang.Constants.DJB2_MAGIC;
import gnu.trove.list.TByteList;

import java.io.Serializable;
import java.util.Arrays;

import org.danilopianini.lang.Constants;

/**
 * @author Danilo Pianini
 * A CodePath is a trace from the root to some node in a VM execution tree.
 * Its use is to allow particular execution locations to be serialized and compared between
 * different VMs, thereby enabling code alignment.  Importantly, the hashCode can be
 * used to uniquely identify CodePath objects, allowing lightweight transmission and comparison.
 */
public class CodePath implements Serializable {

	private static final long serialVersionUID = 5914261026069038877L;
	private static final int ENCODING_BASE = 36;
	private final long[] path;
	private final int size;
	private final boolean safe;
	private int hash;
	private String string;

	/**
	 * @param stack 
	 * 		The numerical markers forming an execution trace to be represented
	 */
	public CodePath(final TByteList stack) {
		size = stack.size();
		safe = size < 4;
		if (safe) {
			path = null;
			for (int i = 0; i < stack.size(); i++) {
				hash |= (-1 & stack.get(i)) << 8 * i;
			}
		} else {
			path = new long[(stack.size() + 7) / 8];
			hash = Constants.DJB2_START;
			for (int i = 0; i < stack.size(); i++) {
				final byte b = stack.get(i);
				hash = hash * DJB2_MAGIC ^ b;
				path[i / 8] |= ((0L | b) << 56 >>> 56) << 8 * (i % 8);
			}
		}
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof CodePath) {
			final CodePath pc = (CodePath) o;
			if (safe) {
				if (pc.safe) {
					return hash == pc.hash;
				}
				return false;
			}
			return size == pc.size && Arrays.equals(path, pc.path);
		}
		return false;
	}

	@Override
	public String toString() {
		if (string == null) {
			final StringBuilder sb = new StringBuilder();
			sb.append(Integer.toString(hash, ENCODING_BASE));
			if (!safe) {
				for (final long l : path) {
					sb.append(Long.toString(l, ENCODING_BASE));
				}
			}
			string = sb.toString();
		}
		return string;
	}

}
