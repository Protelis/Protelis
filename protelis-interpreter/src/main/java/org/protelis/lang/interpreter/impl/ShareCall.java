/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.lang.interpreter.impl;

import static org.protelis.lang.interpreter.util.Bytecode.REP;
import static org.protelis.lang.interpreter.util.Bytecode.SHARE;
import static org.protelis.lang.interpreter.util.Bytecode.SHARE_BODY;
import static org.protelis.lang.interpreter.util.Bytecode.SHARE_INIT;
import static org.protelis.lang.interpreter.util.Bytecode.SHARE_YIELD;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.impl.FieldMapImpl.Builder;
import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

import com.google.common.base.Optional;

/**
 * Share construct. Supersedes the previous rep implementation.
 * See publication at: <a href="https://lmcs.episciences.org/6816">https://lmcs.episciences.org/6816</a>
 *
 * @param <S> superscript / export type
 * @param <T> returned type
 */
public final class ShareCall<S, T> extends AbstractPersistedTree<S, T> {
    private static final long serialVersionUID = 1L;
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
            throw new IllegalStateException("class type " + it.getClass().getName() + " unkown and unsupported");
        });
    }

    private Field<S> alignField(final DeviceUID localDevice, final Field<S> init, final Field<S> local, final Field<S> nbr) {
        final Builder<S> builder = new Builder<>();
        for (final Map.Entry<DeviceUID, S> entry : nbr.iterable()) {
            final DeviceUID otherDevice = entry.getKey();
            if (!localDevice.equals(otherDevice)) {
                final S value = local.containsKey(otherDevice) ? local.get(otherDevice) : init.get(otherDevice);
                builder.add(otherDevice, value);
            }
        }
        return builder.build(localDevice, local.get(localDevice));
    }

    @SuppressWarnings("unchecked")
    private java.util.Optional<Field<S>> asFieldOrEmpty(final S value, final boolean condition) {
        return java.util.Optional.of(value)
                .filter(it -> condition && it instanceof Field)
                .map(it -> (Field<S>) it);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T evaluate(final ExecutionContext context) {
        final S initValue = context.runInNewStackFrame(SHARE_INIT.getCode(), init::eval);
        final S localValue = ensureType(loadState(context, () -> initValue));
        final boolean localIsField = localValue instanceof Field;
        if (localIsField && !(initValue instanceof Field)) {
            throw new IllegalStateException("The local value " + localValue
                    + " is a field, but the default one " + initValue + " is not: "
                    + initValue.getClass().getSimpleName() + ". Types must be consistent");
        }
        final BodyResult<S> bodyResult = new BodyResult<>();
        final DeviceUID myId = context.getDeviceUID();
        final java.util.Optional<Field<S>> localAsField = asFieldOrEmpty(localValue, localIsField);
        final java.util.Optional<Field<S>> initAsField = asFieldOrEmpty(initValue, localIsField);
        assert !localIsField || localAsField.isPresent() && initAsField.isPresent();
        /*
         * Three cases:
         * 1. rep. No fieldName, no field should get build
         * 2. share/classic: local is not a field, fieldName is present, build field as usual
         * 3. share/new: local is a field, fieldName is present, build a field with extractValueFromField
         */
        final Field<S> nbr;
        if (fieldName.isPresent()) {
            if (localIsField) {
                nbr = context.buildFieldDeferred(
                        field -> extractValueFromField(initAsField.get(), (Field<S>) field, myId),
                        localAsField.get(),
                        bodyResult::getResult
                );
            } else {
                nbr = context.buildFieldDeferred(Function.identity(), localValue, bodyResult::getResult);
            }
        } else {
            nbr = null;
        }
        ifPresent(
            localName,
            localIsField
                ? it -> context.putVariable(it, alignField(myId, initAsField.get(), localAsField.get(), nbr))
                : it -> context.putVariable(it, localValue)
        );
        ifPresent(fieldName, it -> context.putVariable(it, nbr));
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

    @SuppressWarnings("unchecked")
    private S ensureType(final Object o) {
        return (S) Objects.requireNonNull(o, "Share is not allowed to return, store, or get initialized to null values.");
    }

    private Optional<T> evaluateYield(final ExecutionContext context) {
        return yield.transform(it -> context.runInNewStackFrame(SHARE_YIELD.getCode(), it::eval));
    }

    private S extractValueFromField(final Field<S> init, final Field<S> other, final DeviceUID id) {
        return other.containsKey(id) ? other.get(id) : init.get(other.getLocalDevice());
    }

    @Override
    public Bytecode getBytecode() {
        return fieldName.isPresent() ? SHARE : REP;
    }

    @Override
    public String getName() {
        return fieldName.isPresent() ? "share" : "rep";
    }

    /**
     * @return an {@link Optional} containing the {@link ProtelisAST} representing the yield expression,
     * or an {@link Optional#absent()} otherwise.
     */
    public Optional<AbstractProtelisAST<T>> getYieldExpression() {
        return yield;
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
        private S getResult() { // NOPMD: false positive, method used as function reference
            return result;
        }
    }
}
