package org.protelis.lang.interpreter.util;

import org.protelis.lang.datatype.Either;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Serializable function that can return either an {@link Integer} or a byte[].
 */
@FunctionalInterface
public interface HashingFunnel extends Function<Object, Either<Integer, byte[]>>, Serializable {
}
