/*******************************************************************************
 * Copyright (C) 2010, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import static org.protelis.lang.interpreter.util.Bytecode.ALIGNED_MAP;
import static org.protelis.lang.interpreter.util.Bytecode.ALIGNED_MAP_EXECUTE;
import static org.protelis.lang.interpreter.util.Bytecode.ALIGNED_MAP_FILTER;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.apache.commons.math3.util.Pair;
import org.nustaq.serialization.FSTConfiguration;
import org.protelis.lang.datatype.DatatypeFactory;
import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.datatype.JVMEntity;
import org.protelis.lang.datatype.Tuple;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.util.Bytecode;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.lang.loading.Metadata;
import org.protelis.vm.ExecutionContext;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.primitives.Longs;

/**
 * Operation evaluating a collection of expressions associated with keys, such
 * as a set of publish-subscribe streams. This allows devices with different
 * sets of keys to align the expressions that share keys together.
 */
public final class AlignedMap extends AbstractSATree<Map<Object, Pair<DotOperator, DotOperator>>, Tuple> {

    private static final String APPLY = "apply";
    private static final Reference CURFIELD = new Reference(new Serializable() {
        private static final long serialVersionUID = 1L;
    });
    private static final FSTConfiguration SERIALIZER = FSTConfiguration.createDefaultConfiguration();
    private static final long serialVersionUID = 1L;
    private static final LoadingCache<Object, byte[]> STACK_IDENTIFIERS = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build(new CacheLoader<Object, byte[]>() {
                @Override
                public byte[] load(@Nonnull final Object key) {
                    return SERIALIZER.asByteArray(key);
                }
            });
    static {
        SERIALIZER.registerClass(String.class, Double.class, Integer.class, Tuple.class, FunctionDefinition.class, JVMEntity.class);
    }
    private final AnnotatedTree<?> defVal;
    private final AnnotatedTree<Field<?>> fieldGenerator;
    private final AnnotatedTree<FunctionDefinition> filterOp;

    private final AnnotatedTree<FunctionDefinition> runOp;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param arg
     *            the field on which {@link AlignedMap} should be applied
     * @param filter
     *            filtering function
     * @param op
     *            function to run
     * @param def
     *            default value
     */
    public AlignedMap(final Metadata metadata, final AnnotatedTree<Field<?>> arg, final AnnotatedTree<FunctionDefinition> filter,
            final AnnotatedTree<FunctionDefinition> op, final AnnotatedTree<?> def) {
        super(metadata, arg, filter, op, def);
        fieldGenerator = arg;
        filterOp = filter;
        runOp = op;
        defVal = def;
    }

    @Override
    public AnnotatedTree<Tuple> copy() {
        return new AlignedMap(getMetadata(), fieldGenerator.copy(), filterOp.copy(), runOp.copy(), defVal.copy());
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        projectAndEval(context);
        final Field<?> origin = fieldGenerator.getAnnotation();
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
                            Map<DeviceUID, Object> targetField = keyToField.computeIfAbsent(key, k -> new LinkedHashMap<>());
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
        Map<Object, Pair<DotOperator, DotOperator>> funmap = getSuperscript();
        if (funmap == null) {
            funmap = new LinkedHashMap<>();
        }
        final Map<Object, Pair<DotOperator, DotOperator>> newFunmap = new LinkedHashMap<>(funmap.size());
        setSuperscript(newFunmap);
        final List<Tuple> resl = new ArrayList<>(keyToField.size());
        for (final Entry<Object, Map<DeviceUID, Object>> keyFieldPair : keyToField.entrySet()) {
            final Object key = keyFieldPair.getKey();
            final Map<DeviceUID, Object> preField = keyFieldPair.getValue();
            final DeviceUID localDeviceUID = context.getDeviceUID();
            final Object localValue = Optional.ofNullable(preField.remove(localDeviceUID)).orElseGet(defVal::getAnnotation);
            final Field.Builder<Object> builder = DatatypeFactory.createFieldBuilder();
            for (final Entry<DeviceUID, Object> entry: preField.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            final Field<Object> reifiedField = builder.build(localDeviceUID, localValue);
            final ExecutionContext restricted = context.restrictDomain(reifiedField);
            /*
             * Compute arguments
             */
            final List<AnnotatedTree<?>> args = new ArrayList<>(2);
            args.add(new Constant<>(getMetadata(), key));
            args.add(new Variable(getMetadata(), CURFIELD));
            restricted.putVariable(CURFIELD, reifiedField);
            /*
             * Compute the code path: align on keys
             */
            if (key instanceof Integer || key instanceof Short || key instanceof Byte) {
                restricted.newCallStackFrame(((Number) key).intValue());
            } else if (key instanceof Double) {
                restricted.newCallStackFrame(Longs.toByteArray(Double.doubleToRawLongBits((double) key)));
            } else if (key instanceof Serializable) {
                final byte[] hash = STACK_IDENTIFIERS.getUnchecked(key);
                restricted.newCallStackFrame(hash);
            } else {
                throw new IllegalStateException("alignedMap cannot aligned on non-Serializable objects of type " + key.getClass().getName());
            }
            /*
             * Compute functions if needed
             */
            Pair<DotOperator, DotOperator> funs = funmap.get(key);
            if (funs == null) {
                funs = new Pair<>(new DotOperator(getMetadata(), APPLY, filterOp, args), new DotOperator(getMetadata(), APPLY, runOp, args));
            }
            /*
             * Run the actual filtering and operation
             */
            final DotOperator fop = funs.getFirst();
            restricted.newCallStackFrame(ALIGNED_MAP_FILTER.getCode());
            fop.eval(restricted);
            restricted.returnFromCallFrame();
            final Object cond = fop.getAnnotation();
            if (cond instanceof Boolean) {
                if ((Boolean) cond) {
                    /*
                     * Filter passed, run operation.
                     */
                    restricted.newCallStackFrame(ALIGNED_MAP_EXECUTE.getCode());
                    final DotOperator rop = funs.getSecond();
                    rop.eval(restricted);
                    restricted.returnFromCallFrame();
                    resl.add(DatatypeFactory.createTuple(key, rop.getAnnotation()));
                    /*
                     * If both the key exists and the filter passes, save the
                     * state.
                     */
                    newFunmap.put(key, funs);
                }
            } else {
                throw new IllegalStateException("Filter must return a Boolean, got " + cond.getClass());
            }
            restricted.returnFromCallFrame();
        }
        // return type: [[key0, compval0], [key1, compval1], [key2, compval2]]
        setAnnotation(DatatypeFactory.createTuple(resl));
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
