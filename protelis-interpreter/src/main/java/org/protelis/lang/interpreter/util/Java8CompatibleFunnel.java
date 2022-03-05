package org.protelis.lang.interpreter.util;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Longs;
import org.apache.commons.lang3.ArrayUtils;
import org.protelis.lang.datatype.Either;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Implementation of a Java8-compatible serializable hash function.
 */
public final class Java8CompatibleFunnel implements HashingFunnel {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Override
    public Either<Integer, byte[]> apply(final Object key) {
        if (key instanceof Integer || key instanceof Short || key instanceof Byte) {
            return Either.left(((Number) key).intValue());
        } else if (key instanceof Double) {
            return Either.right(Longs.toByteArray(Double.doubleToRawLongBits((double) key)));
        } else if (key instanceof Float) {
            return Either.left(Float.floatToRawIntBits((float) key));
        } else if (key instanceof Boolean) {
            return Either.right(new byte[] { (byte) ((Boolean) key ? -1 : 0) });
        } else if (key instanceof String) {
            return Either.right(((String) key).getBytes(StandardCharsets.UTF_8));
        } else if (key instanceof BigInteger) {
            return Either.right(((BigInteger) key).toByteArray());
        } else if (key instanceof Character) {
            return apply(key.toString());
        } else if (key instanceof Class) {
            return apply(((Class<?>) key).getName());
        } else if (key instanceof Stream<?>) {
            return Either.right(
                ArrayUtils.toPrimitive(
                    ((Stream<?>) key).map(this)
                        .map(it -> it.isLeft() ? ByteBuffer.allocate(4).putInt(it.getLeft()).array() : it.getRight())
                        .map(ArrayUtils::toObject)
                        .flatMap(Arrays::stream)
                        .toArray(Byte[]::new)
                )
            );
        } else if (key instanceof Iterable<?>) {
            return apply(StreamSupport.stream(((Iterable<?>) key).spliterator(), false));
        } else if (key instanceof Iterator<?>) {
            final Iterator<Object> iterator = (Iterator<Object>) key;
            final Iterable<?> iterable = () -> iterator;
            return apply(iterable);
        }
        return apply(ImmutableList.of(key.getClass().getName(), key.hashCode()));
    }
}
