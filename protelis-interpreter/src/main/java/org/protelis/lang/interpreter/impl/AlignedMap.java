/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
package org.protelis.lang.interpreter.impl;

import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Either;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.datatype.Tuple;
import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.interpreter.util.HashingFunnel;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import static org.protelis.lang.interpreter.util.Bytecode.ALIGNED_MAP;
import static org.protelis.lang.interpreter.util.Bytecode.ALIGNED_MAP_DEFAULT;
import static org.protelis.lang.interpreter.util.Bytecode.ALIGNED_MAP_EXECUTE;
import static org.protelis.lang.interpreter.util.Bytecode.ALIGNED_MAP_FILTER;
import static org.protelis.lang.interpreter.util.Bytecode.ALIGNED_MAP_GENERATOR;

/**
 * Operation evaluating a collection of expressions associated with keys, such
 * as a set of publish-subscribe streams. This allows devices with different
 * sets of keys to align the expressions that share keys together.
 */
public final class AlignedMap extends AbstractProtelisAST<Tuple> {

    private static final Reference CURFIELD = new Reference(new Serializable() {
        private static final long serialVersionUID = 1L;
    });
    private static final long serialVersionUID = 2L;

    private final HashingFunnel hasher;
    private final ProtelisAST<?> defaultValue;
    private final ProtelisAST<Field<?>> fieldGenerator;
    private final ProtelisAST<FunctionDefinition> filter;
    private final ProtelisAST<FunctionDefinition> execute;

    /**
     * @param hasher a function producing a valid, reproducible, and unambiguous hash for arbitrary objects
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param argument
     *            the field on which {@link AlignedMap} should be applied
     * @param filter
     *            filtering function
     * @param operation
     *            function to run
     * @param defaultValue
     *            default value
     */
    public AlignedMap(
            final HashingFunnel hasher,
            final Metadata metadata,
            final ProtelisAST<Field<?>> argument,
            final ProtelisAST<FunctionDefinition> filter,
            final ProtelisAST<FunctionDefinition> operation,
            final ProtelisAST<?> defaultValue
    ) {
        super(metadata, argument, filter, operation, defaultValue);
        this.hasher = hasher;
        fieldGenerator = argument;
        this.filter = filter;
        execute = operation;
        this.defaultValue = defaultValue;
    }

    @Override
    public Tuple evaluate(final ExecutionContext context) {
        final Field<?> origin = context.runInNewStackFrame(ALIGNED_MAP_GENERATOR.getCode(), fieldGenerator::eval);
        /*
         * Extract one field for each key.
         * 
         * This operation translates a field of tuples of tuples of the form:
         * 
         * {ID0 : [[key1, val1], [key2, val2]], ID2 : [[key3, val3], [key2, val4]]}
         * 
         * into a collection such as:
         * 
         * key1 : {ID0 : val1}
         * key2 : {ID0 : val2, ID2 : val4}
         * key3 : {ID2: val3}
         */
        final Map<Object, Map<DeviceUID, Object>> keyToField = new LinkedHashMap<>();
        for (final Map.Entry<DeviceUID, ?> pair : origin.iterable()) {
            final DeviceUID device = pair.getKey();
            final Object originalTupleObject = pair.getValue();
            /*
             * Mappings are of the form: [[key1, value1][key2, value2]...]
             */
            if (originalTupleObject instanceof Tuple) {
                final Tuple originalTuple = (Tuple) originalTupleObject;
                for (final Object keyToValueObject : originalTuple) {
                    if (keyToValueObject instanceof Tuple) {
                        final Tuple keyToValue = (Tuple) keyToValueObject;
                        if (keyToValue.size() == 2) {
                            final Object key = keyToValue.get(0);
                            final Object value = keyToValue.get(1);
                            final Map<DeviceUID, Object> targetField = keyToField.computeIfAbsent(key, k -> new LinkedHashMap<>());
                            targetField.put(device, value);
                        } else {
                            throw new IllegalStateException(
                                    "The tuple must have length 2, " + keyToValue + " has length " + keyToValue.size());
                        }
                    } else {
                        throw new IllegalStateException("Expected " + Tuple.class + ", got " + keyToValueObject.getClass());
                    }
                }
            } else {
                throw new IllegalStateException("Expected " + Tuple.class + ", got " + originalTupleObject.getClass() + ": " + originalTupleObject);
            }
        }
        /*
         * Get or initialize the mapping between keys and functions
         */
        final List<Tuple> resultList = new ArrayList<>(keyToField.size());
        final Object defaultValue = context.runInNewStackFrame(ALIGNED_MAP_DEFAULT.getCode(), this.defaultValue::eval);
        for (final Entry<Object, Map<DeviceUID, Object>> keyFieldPair : keyToField.entrySet()) {
            final Object key = keyFieldPair.getKey();
            final Map<DeviceUID, Object> preField = keyFieldPair.getValue();
            final DeviceUID localDeviceUID = context.getDeviceUID();
            final Object localValue = Optional.ofNullable(preField.remove(localDeviceUID)).orElse(defaultValue);
            final Field.Builder<Object> builder = DatatypeFactory.createFieldBuilder();
            for (final Entry<DeviceUID, Object> entry: preField.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            final Field<Object> reifiedField = builder.build(localDeviceUID, localValue);
            final ExecutionContext restricted = context.restrictDomain(reifiedField);
            /*
             * Compute arguments
             */
            final List<ProtelisAST<?>> args = new ArrayList<>(2);
            args.add(new Constant<>(getMetadata(), key));
            args.add(new Variable(getMetadata(), CURFIELD));
            restricted.putVariable(CURFIELD, reifiedField);
            /*
             * Compute the code path: align on keys
             */
            final Either<Integer, byte[]> hashed = hasher.apply(key);
            if (hashed.isLeft()) {
                restricted.newCallStackFrame(hashed.getLeft());
            } else {
                restricted.newCallStackFrame(hashed.getRight());
            }
            /*
             * Run the actual filtering and operation
             */
            final Object condition = callFunctionInSubContext(ALIGNED_MAP_FILTER.getCode(), restricted, filter, args);
            if (condition instanceof Boolean) {
                if ((Boolean) condition) {
                    /*
                     * Filter passed, run operation.
                     */
                    resultList.add(DatatypeFactory.createTuple(
                            key,
                            callFunctionInSubContext(ALIGNED_MAP_EXECUTE.getCode(), restricted, execute, args)));
                }
            } else {
                throw new IllegalStateException("Filter must return a Boolean, got " + condition.getClass());
            }
            restricted.returnFromCallFrame();
        }
        // return type: [[key0, compval0], [key1, compval1], [key2, compval2]]
        return DatatypeFactory.createTuple(resultList);
    }

    private Object callFunctionInSubContext(
            final int code,
            final ExecutionContext context,
            final ProtelisAST<FunctionDefinition> def,
            final List<ProtelisAST<?>> parameters
    ) {
        return context.runInNewStackFrame(code, ctx -> {
                final FunctionDefinition function = def.eval(ctx);
                return new FunctionCall(getMetadata(), function, parameters).eval(ctx);
            });
    }

    @Override
    public Bytecode getBytecode() {
        return ALIGNED_MAP;
    }

    @Override
    public String getName() {
        return "alignedMap";
    }

    @Override
    public String toString() {
        return getName() + branchesToString();
    }
}
