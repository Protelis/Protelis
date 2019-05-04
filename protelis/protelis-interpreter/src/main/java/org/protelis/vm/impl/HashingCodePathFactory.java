package org.protelis.vm.impl;

import java.util.Arrays;

import org.protelis.vm.CodePath;
import org.protelis.vm.CodePathFactory;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;

import gnu.trove.list.TIntList;
import gnu.trove.stack.TIntStack;

public class HashingCodePathFactory implements CodePathFactory {

    private final HashFunction algorithm;
    
    public HashingCodePathFactory(final HashFunction hashAlgorithm) {
        algorithm = hashAlgorithm;
    }

    @Override
    public final CodePath createCodePath(final TIntList callStackIdentifiers, final TIntStack callStackSizes) {
        Hasher hasher = algorithm.newHasher(callStackIdentifiers.size() * 4);
        callStackIdentifiers.forEach(it -> {
            hasher.putInt(it);
            return true;
        });
        return new HashingCodePath(hasher.hash().asBytes());
    }

    public static final class HashingCodePath implements CodePath {
        private final byte[] hash;
        public HashingCodePath(final byte[] hash) {
            if (hash.length < 4) {
                throw new IllegalArgumentException("Hashes shorter than four bytes are unacceptable: " + Arrays.toString(hash));
            }
            this.hash = hash;
        }
        @Override
        public boolean equals(final Object obj) {
            return obj instanceof HashingCodePath
                    && Arrays.equals(hash, ((HashingCodePath) obj).hash);
        }
        @Override
        public int hashCode() {
            // CHECKSTYLE: MagicNumber OFF
            return (hash[0] & 0xFF)
                | ((hash[1] & 0xFF) << 8)
                | ((hash[2] & 0xFF) << 16)
                | ((hash[3] & 0xFF) << 24);
            // CHECKSTYLE: MagicNumber ON
        }
    }

}
