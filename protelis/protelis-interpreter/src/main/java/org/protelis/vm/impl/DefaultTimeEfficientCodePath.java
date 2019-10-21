/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.vm.impl;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.vm.CodePath;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import gnu.trove.list.TIntList;

/**
 * A CodePath whose focus is on time performance. Not space efficient, not meant
 * to be used for real world networking.
 */
public final class DefaultTimeEfficientCodePath implements CodePath {

    private static final long serialVersionUID = 2L;
    private static final Map<Integer, Bytecode> REVERSE_LOOKUP_BYTECODE = Arrays.stream(Bytecode.values())
          .collect(Collectors.toMap(Bytecode::getCode, Function.identity()));

    private final int[] repr;
    private transient int lazyHash;
    private transient String lazyString;

    /**
     * @param source the current stack frames identifiers
     */
    public DefaultTimeEfficientCodePath(final TIntList source) {
        repr = source.toArray();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof DefaultTimeEfficientCodePath
            && Arrays.equals(repr, ((DefaultTimeEfficientCodePath) obj).repr);
    }

    @Override
    public int hashCode() {
        if (lazyHash == 0) {
            final Hasher murmur = Hashing.murmur3_32().newHasher();
            for (final int x: repr) {
                murmur.putInt(x);
            }
            lazyHash = murmur.hash().asInt();
        }
        return lazyHash;
    }

    @Override
    public String toString() {
        if (lazyString == null) {
            lazyString = "CodePath" + Arrays.stream(repr)
                .mapToObj(it -> Optional.ofNullable(REVERSE_LOOKUP_BYTECODE.get(it))
                        .map(Object::toString)
                        .orElse(Integer.toString(it)))
                .collect(Collectors.joining("->", "[", "]"));
        }
        return lazyString;
    }

}
