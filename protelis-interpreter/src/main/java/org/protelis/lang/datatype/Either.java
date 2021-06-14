package org.protelis.lang.datatype;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public final class Either<L, R> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final L left;
    private final R right;
    @SuppressFBWarnings(
        value = "SE_TRANSIENT_FIELD_NOT_RESTORED",
        justification = "The value is re-computed as needed upon deserialization"
    )
    private transient int hash;

    private Either(final L left, final R right) {
        if (left == null && right == null || left != null && right != null) {
            throw new IllegalArgumentException(
                "Either left or right must be null, but not both (left: '" + left + "', right: '" + right + "')"
            );
        }
        this.left = left;
        this.right = right;
    }

    public boolean isLeft() {
        return right == null;
    }

    public boolean isRight() {
        return left == null;
    }

    public R getRight() {
        if (isRight()) {
            return right;
        }
        throw new IllegalStateException(this + " is a Left either and does not store any value on its right");
    }

    public L getLeft() {
        if (isLeft()) {
            return left;
        }
        throw new IllegalStateException(this + " is a Right either and does not store any value on its left");
    }

    @Override
    public String toString() {
        if (isLeft()) {
            return "Left(" + left + ")";
        }
        return "Right(" + right + ")";
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final Either<?, ?> either = (Either<?, ?>) other;
        return new EqualsBuilder().append(left, either.left).append(right, either.right).isEquals();
    }

    @Override
    public int hashCode() {
        if (hash == 0) {
            hash = new HashCodeBuilder().append(left).append(right).toHashCode();
        }
        return hash;
    }

    public static <L, R> Either<L, R> left(final L value) {
        return new Either<>(value, null);
    }

    public static <L, R> Either<L, R> right(final R value) {
        return new Either<>(null, value);
    }

}
