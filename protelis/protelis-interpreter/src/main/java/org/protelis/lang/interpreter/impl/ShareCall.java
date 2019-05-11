package org.protelis.lang.interpreter.impl;

import static org.protelis.lang.interpreter.util.Bytecode.REP;
import static org.protelis.lang.interpreter.util.Bytecode.SHARE;
import static org.protelis.lang.interpreter.util.Bytecode.SHARE_BODY;
import static org.protelis.lang.interpreter.util.Bytecode.SHARE_INIT;
import static org.protelis.lang.interpreter.util.Bytecode.SHARE_YIELD;

import org.protelis.lang.datatype.Field;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

import com.google.common.base.Optional;

import java8.util.Objects;
import java8.util.function.Consumer;

/**
 * Share construct. Supersedes the previous rep implementation. Paper to be
 * published. TODO: update the documentation as soon as it gets published.
 *
 * @param <S> superscript / export type
 * @param <T> returned type
 */
public final class ShareCall<S, T> extends AbstractSATree<S, T> {
    private static final long serialVersionUID = 8643287734245198408L;
    private final Optional<Reference> fieldName;
    private final Optional<Reference> localName;
    private final Optional<AbstractAnnotatedTree<T>> yield;
    private final AnnotatedTree<?> init;
    private final AnnotatedTree<S> body;

    /**
     * Convenience constructor with {@link java8.util.Optional}.
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
            final Metadata metadata,
            final java8.util.Optional<Reference> localName,
            final java8.util.Optional<Reference> fieldName,
            final AnnotatedTree<?> init,
            final AnnotatedTree<S> body,
            final java8.util.Optional<AnnotatedTree<T>> yield) {
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
            final Metadata metadata,
            final Optional<Reference> localName,
            final Optional<Reference> fieldName,
            final AnnotatedTree<?> init,
            final AnnotatedTree<S> body,
            final Optional<AnnotatedTree<T>> yield) {
        super(metadata, init, body);
        if (!(localName.isPresent() || fieldName.isPresent())) {
            throw new IllegalArgumentException("Share cannot get initialized without at least a variable bind.");
        }
        this.localName = localName;
        this.fieldName = fieldName;
        this.init = init;
        this.body = body;
        this.yield = yield.transform(it ->  {
            if (it instanceof AbstractAnnotatedTree) {
                return (AbstractAnnotatedTree<T>) it;
            }
            throw new IllegalStateException("class type " + it.getClass().getName() + " unkown and unsupported");
        });
    }

    @Override
    public ShareCall<S, T> copy() {
        final ShareCall<S, T> res = new ShareCall<>(
                getMetadata(),
                localName,
                fieldName,
                init.copy(),
                body.copy(),
                yield.transform(AbstractAnnotatedTree::copy));
        return res;
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
    public void evaluate(final ExecutionContext context) {
        evalInNewStackFrame(init, context, SHARE_INIT);
        final S localValue = ensureType(isErased() ? init.getAnnotation() : getSuperscript());
        ifPresent(localName, it -> context.putVariable(it, localValue));
        ifPresent(fieldName, it -> context.putVariable(it, context.buildFieldDeferred(i -> i, localValue, body::getAnnotation)));
        context.newCallStackFrame(SHARE_BODY.getCode());
        final Runnable yieldEvaluation = () -> ifPresent(yield, it -> evalInNewStackFrame(it, context, SHARE_YIELD)); // NOPMD: this is not a thread.
        if (body instanceof All) {
            final All multilineBody = (All) body;
            multilineBody.forEachWithIndex((i, b) -> {
                context.newCallStackFrame(i);
                b.eval(context);
                multilineBody.setAnnotation(b.getAnnotation());
            });
            yieldEvaluation.run();
            multilineBody.forEach(it -> context.returnFromCallFrame());
        } else {
            body.eval(context);
            yieldEvaluation.run();
        }
        context.returnFromCallFrame();
        final S result = ensureType(body.getAnnotation());
        setSuperscript(result);
        setAnnotation(yield.isPresent() ? yield.get().getAnnotation() : (T) result);
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

    private static <T> Optional<T> toGuava(final java8.util.Optional<T> origin) {
        return Optional.fromNullable(origin.orElse(null));
    }

}
