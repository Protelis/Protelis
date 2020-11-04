package org.protelis.lang.interpreter.impl;

import static org.protelis.lang.interpreter.util.Bytecode.REP;
import static org.protelis.lang.interpreter.util.Bytecode.SHARE;
import static org.protelis.lang.interpreter.util.Bytecode.SHARE_BODY;
import static org.protelis.lang.interpreter.util.Bytecode.SHARE_INIT;
import static org.protelis.lang.interpreter.util.Bytecode.SHARE_YIELD;

import java.util.Objects;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.protelis.lang.datatype.Field;
import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

import com.google.common.base.Optional;

/**
 * Share construct. Supersedes the previous rep implementation. Paper to be
 * published. TODO: update the documentation as soon as it gets published.
 *
 * @param <S> superscript / export type
 * @param <T> returned type
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "Guava"})
public final class ShareCall<S, T> extends AbstractPersistedTree<S, T> {
    private static final long serialVersionUID = 8643287734245198408L;
    private final Optional<Reference> fieldName;
    private final Optional<Reference> localName;
    private final Optional<AbstractProtelisAST<T>> yield;
    private final ProtelisAST<S> init;
    private final ProtelisAST<S> body;

    /**
     * Convenience constructor with {@link java.util.Optional}.
     * 
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param localName
     *            variable name
     * @param fieldName
     *            name of the field version
     * @param init
     *            initial value
     * @param body
     *            body
     * @param yield
     *            body
     */
    public ShareCall(
            @Nonnull final Metadata metadata,
            @Nonnull final java.util.Optional<Reference> localName,
            @Nonnull final java.util.Optional<Reference> fieldName,
            @Nonnull final ProtelisAST<S> init,
            @Nonnull final ProtelisAST<S> body,
            @Nonnull final java.util.Optional<ProtelisAST<T>> yield) {
        this(metadata, toGuava(localName), toGuava(fieldName), init, body, toGuava(yield));
    }

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param localName
     *            variable name
     * @param fieldName
     *            name of the field version
     * @param init
     *            initial value
     * @param body
     *            body
     * @param yield
     *            body
     */
    public ShareCall(
            @Nonnull final Metadata metadata,
            @Nonnull final Optional<Reference> localName,
            @Nonnull final Optional<Reference> fieldName,
            @Nonnull final ProtelisAST<S> init,
            @Nonnull final ProtelisAST<S> body,
            @Nonnull final Optional<ProtelisAST<T>> yield) {
        super(metadata, init, body);
        if (!(localName.isPresent() || fieldName.isPresent())) {
            throw new IllegalArgumentException("Share cannot get initialized without at least a variable bind.");
        }
        this.localName = localName;
        this.fieldName = fieldName;
        this.init = init;
        this.body = body;
        this.yield = yield.transform(it ->  {
            if (it instanceof AbstractProtelisAST) {
                return (AbstractProtelisAST<T>) it;
            }
            throw new IllegalStateException("class type " + it.getClass().getName() + " unknown and unsupported");
        });
    }

    @SuppressWarnings("unchecked")
    private S ensureType(final Object o) {
        if (o instanceof Field) {
            throw new IllegalStateException("Share is not allowed to return, store, or get initialized to Field values: " + o);
        }
        return (S) Objects.requireNonNull(o, "Share is not allowed to return, store, or get initialized to null values.");
    }

    @SuppressWarnings("unchecked")
    @Override
    public T evaluate(final ExecutionContext context) {
        final S initValue = context.runInNewStackFrame(SHARE_INIT.getCode(), init::eval);
        final S localValue = ensureType(loadState(context, () -> initValue));
        ifPresent(localName, it -> context.putVariable(it, localValue));
        final BodyResult<S> bodyResult = new BodyResult<>();
        ifPresent(fieldName, it -> context
                .putVariable(it, context.buildFieldDeferred(i -> i, localValue, bodyResult::getResult)));
        context.newCallStackFrame(SHARE_BODY.getCode());
        final Optional<T> yieldResult;
        if (body instanceof All) {
            final All multilineBody = (All) body;
            multilineBody.forEachWithIndex((i, b) -> {
                context.newCallStackFrame(i);
                bodyResult.result = (S) b.eval(context);
            });
            yieldResult = evaluateYield(context);
            multilineBody.forEach(it -> context.returnFromCallFrame());
        } else {
            bodyResult.result = body.eval(context);
            yieldResult = evaluateYield(context);
        }
        context.returnFromCallFrame();
        saveState(context, ensureType(bodyResult.result));
        return yieldResult.or((T) bodyResult.result);
    }

    private Optional<T> evaluateYield(final ExecutionContext context) {
        return yield.transform(it -> context.runInNewStackFrame(SHARE_YIELD.getCode(), it::eval));
    }

    @Override
    public Bytecode getBytecode() {
        return fieldName.isPresent() ? SHARE : REP;
    }

    @Override
    public String getName() {
        return fieldName.isPresent() ? "share" : "rep";
    }

    @Override
    public String toString() {
        final Optional<String> field = fieldName.transform(Reference::toString);
        return getName() + " ("
            + localName.transform(Reference::toString)
                .transform(it -> it + field.transform(f -> ", ").or("")).or("")
            + field.or("")
            + " <- "
            + stringFor(init)
            + ") { "
            + stringFor(body)
            + " }"
            + yield.transform(it -> " yield { " + stringFor(it) + '}').or("");
    }

    private static <T> void ifPresent(final Optional<T> var, final Consumer<T> todo) {
        if (var.isPresent()) {
            todo.accept(var.get());
        }
    }

    private static <T> Optional<T> toGuava(final java.util.Optional<T> origin) {
        return Optional.fromNullable(origin.orElse(null));
    }

    private static class BodyResult<S> {
        private S result;
        private S getResult() {
            return result;
        }
    }

}
