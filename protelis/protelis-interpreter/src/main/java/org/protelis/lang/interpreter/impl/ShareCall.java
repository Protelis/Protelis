package org.protelis.lang.interpreter.impl;

import java.util.List;

import org.protelis.lang.datatype.Field;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.util.Reference;
import org.protelis.vm.ExecutionContext;

import com.google.common.base.Optional;

import java8.util.Objects;
import java8.util.function.Consumer;

/**
 * Share construct. Paper to be published. TODO: update the documentation as soon as it gets published.
 *
 * @param <S> superscript / export type
 * @param <T> returned type
 */
public final class ShareCall<S, T> extends AbstractSATree<S, T> {
    private static final long serialVersionUID = 8643287734245198408L;
    private static final byte INIT = 0;
    private static final byte BODY = 1;
    private static final byte YIELD = 2;
    private final Optional<AbstractAnnotatedTree<T>> yield;
    private final Optional<Reference> localName;
    private final Optional<Reference> fieldName;

    /**
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
            final Optional<Reference> localName,
            final Optional<Reference> fieldName,
            final AnnotatedTree<?> init,
            final AnnotatedTree<?> body,
            final Optional<AnnotatedTree<T>> yield) {
        super(init, body);
        if (!(localName.isPresent() || fieldName.isPresent())) {
            throw new IllegalArgumentException("Share cannot get initialized without at least a variable bind.");
        }
        this.localName = localName;
        this.fieldName = fieldName;
        this.yield = yield.transform(it ->  {
            if (it instanceof AbstractAnnotatedTree) {
                return (AbstractAnnotatedTree<T>) it;
            }
            throw new IllegalStateException("class type " + it.getClass().getName() + " unkown and unsupported");
        });
    }

    /**
     * Convenience constructor with {@link java8.util.Optional}.
     * 
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
            final java8.util.Optional<Reference> localName,
            final java8.util.Optional<Reference> fieldName,
            final AnnotatedTree<?> init,
            final AnnotatedTree<?> body,
            final java8.util.Optional<AnnotatedTree<T>> yield) {
        this(toGuava(localName), toGuava(fieldName), init, body, toGuava(yield));
    }

    @Override
    public ShareCall<S, T> copy() {
        final List<AnnotatedTree<?>> branches = deepCopyBranches();
        final ShareCall<S, T> res = new ShareCall<>(
                localName,
                fieldName,
                branches.get(INIT),
                branches.get(BODY),
                yield.transform(AbstractAnnotatedTree::copy));
        return res;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void eval(final ExecutionContext context) {
        final AnnotatedTree<?> initBranch = getBranch(INIT);
        initBranch.evalInNewStackFrame(context, INIT);
        final S localValue = ensureType(isErased() ? initBranch.getAnnotation() : getSuperscript());
        final AnnotatedTree<?> body = getBranch(BODY);
        ifPresent(localName, it -> context.putVariable(it, localValue, true));
        ifPresent(fieldName, it -> context.putVariable(it, context.buildFieldDeferred(i -> i, localValue, body::getAnnotation), true));
        context.newCallStackFrame(BODY);
        final Runnable yieldEvaluation = () -> ifPresent(yield, it -> it.evalInNewStackFrame(context, YIELD));
        if (body instanceof All) {
            final All multilineBody = (All) body;
            multilineBody.forEachWithIndex((i, b) -> {
                context.newCallStackFrame(i.byteValue());
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

    @SuppressWarnings("unchecked")
    private S ensureType(final Object o) {
        if (o instanceof Field) {
            throw new IllegalStateException("Share is not allowed to return, store, or get initialized to Field values: " + o);
        }
        return (S) Objects.requireNonNull(o, "Share is not allowed to return, store, or get initialized to null values.");
    }

    @Override
    protected void innerAsString(final StringBuilder sb, final int indent) {
        sb.append(fieldName.isPresent() ? "share" : "rep")
            .append(" (")
            .append(localName.transform(Reference::toString).transform(it -> it + ", ").or(""))
            .append(fieldName.transform(Reference::toString).or(""))
            .append(" <- \n");
        getBranch(INIT).toString(sb, indent + 1);
        sb.append(") {\n");
        getBranch(BODY).toString(sb, indent + 1);
        sb.append('\n');
        indent(sb, indent);
        sb.append('}');
        if (yield.isPresent()) {
            sb.append(" yield {\n");
            getBranch(YIELD).toString(sb, indent + 1);
            indent(sb, indent);
            sb.append('}');
        }
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
