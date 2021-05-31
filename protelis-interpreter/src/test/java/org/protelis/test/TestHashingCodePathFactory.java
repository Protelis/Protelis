/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.test;

import static com.google.common.hash.Hashing.crc32;
import static com.google.common.hash.Hashing.murmur3_128;
import static com.google.common.hash.Hashing.sha256;
import static com.google.common.hash.Hashing.sha384;
import static com.google.common.hash.Hashing.sha512;
import static com.google.common.hash.Hashing.sipHash24;
import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static org.apache.commons.lang3.SerializationUtils.serialize;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.protelis.vm.impl.HashingCodePathFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.hash.HashFunction;

/**
 *
 */
public class TestHashingCodePathFactory {

    /**
     * Ensures serializability of {@link HashingCodePathFactory}.
     */
    @Test
    public void testSerialization() {
        for (final HashFunction fun : ImmutableList.of(murmur3_128(), sha256(), sha384(), sha512(), crc32(), sipHash24())) {
            assertNotNull(deserialize(serialize(new HashingCodePathFactory(fun))));
        }
    }
}
