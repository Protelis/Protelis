package org.protelis.lang.interpreter.util;

import org.protelis.lang.datatype.Either;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface HashingFunnel extends Function<Object, Either<Integer, byte[]>>, Serializable {
}
