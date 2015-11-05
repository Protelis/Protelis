/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.vm.util;

import gnu.trove.list.TByteList;

import java.io.Serializable;
import java.util.Arrays;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

/**
 * A CodePath is a trace from the root to some node in a VM execution tree. Its
 * use is to allow particular execution locations to be serialized and compared
 * between different VMs, thereby enabling code alignment. Importantly, the
 * hashCode can be used to uniquely identify CodePath objects, allowing
 * lightweight transmission and comparison.
 */
public class CodePath implements Serializable {

    private static final long serialVersionUID = 5914261026069038877L;
    private static final HashFunction HASH_FUNCTION = Hashing.murmur3_32();
    private static final int INT_MASK = 0xFF;
    private static final long LONG_MASK = 0xFF;
    private static final int ENCODING_BASE = 36;
    private static final int BITS_PER_BYTE = 8;
    /**
     * This sequence of longs will be filled with the bytes representing the
     * execution trace. The choice of a long[] is targeted at obtaining the best
     * possible performance. Java can not store bytes (they are converted to
     * int), but is pretty efficient with int[] and long[].
     */
    private final long[] path;
    private final int size;
    /**
     * This flag switches to true when there are four or less numerical markers.
     * In this case, in fact, an int is big enough to include them all, without
     * losing any data.
     */
    private final boolean safe;
    private final int hash;
    private String string;

    /**
     * @param stack
     *            The numerical markers forming an execution trace to be
     *            represented
     */
    public CodePath(final TByteList stack) {
        size = stack.size();
        safe = size < 4;
        if (safe) {
            /*
             * Very short stack, an int suffices.
             */
            path = null;
            int tempHash = 0;
            for (int i = 0; i < stack.size(); i++) {
                /*
                 * Suppose we have bytes [1, 2, 3].
                 * 
                 * First, we map the byte to an int, using the operation
                 * described in the comment above. Our first byte, which is the
                 * hex number 0x01, becomes 0x00000001. Then, we shift the byte
                 * towards left of 0 positions (so it remains exactly equal) and
                 * then join it with the current 0x00000000 result through a or
                 * operator, so that the current result is 0x00000001.
                 * 
                 * Our second byte is 0x02. It becomes 0x00000002 first, then
                 * gets shifted of 8 bits, namely of two positions in
                 * hexadecimal notation. It becomes 0x00000200, and gets
                 * combined with the current result (0x00000001) with an or, so
                 * the current result becomes 0x00000201.
                 * 
                 * At the end of our procedure, when the third value has been
                 * processed, the resulting value is 0x00030201
                 * 
                 */
                tempHash |= (stack.get(i) & INT_MASK) << (BITS_PER_BYTE * i);
            }
            hash = tempHash;
        } else {
            final Hasher hashGen = HASH_FUNCTION.newHasher(size);
            path = new long[(stack.size() - 1) / Long.BYTES + 1];
            /*
             * Here, we run across all the bytes, and we do two operations at
             * the same time:
             * 
             * 1) We feed our hasher
             * 
             * 2) We fill the long[]. This long[] is filled using a strategy
             * very similar to the one used above for the int hash in case of
             * size < 4.
             */
            for (int i = 0; i < stack.size(); i++) {
                final byte b = stack.get(i);
                hashGen.putByte(b);
                path[i / Long.BYTES] |= (b & LONG_MASK) << (BITS_PER_BYTE * (i % Long.BYTES));
            }
            hash = hashGen.hash().asInt();
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

    /**
     * @return a representation of this path as a long array. The returned array
     *         is a defensive copy, i.e. changes to the returned array will NOT
     *         modify this object
     */
    public long[] asLongArray() {
        if (safe) {
            return new long[] { hash };
        }
        return Arrays.copyOf(path, path.length);
    }

}
