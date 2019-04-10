/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang.interpreter.impl;

import java.util.Locale;

import org.danilopianini.lang.LangUtils;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.loading.Metadata;
import org.protelis.lang.util.HoodOp;
import org.protelis.vm.ExecutionContext;

/**
 * Reduce a field into a local value by reduction using a {@link HoodOp}.
 */
public final class HoodCall extends AbstractAnnotatedTree<Object> {

    private static final long serialVersionUID = -4925767634715581329L;
    private final AnnotatedTree<Field> body;
    private final HoodOp function;
    private final boolean inclusive;

    /**
     * @param metadata
     *            A {@link Metadata} object containing information about the code that generated this AST node.
     * @param arg
     *            the argument to evaluate (must return a {@link Field}).
     * @param func
     *            the {@link HoodOp} to apply
     * @param includeSelf
     *            if true, sigma won't be excluded
     */
    public HoodCall(final Metadata metadata, final AnnotatedTree<Field> arg, final HoodOp func, final boolean includeSelf) {
        super(metadata, arg);
        LangUtils.requireNonNull(func);
        body = arg;
        function = func;
        inclusive = includeSelf;
    }

    @Override
    public AnnotatedTree<Object> copy() {
        return new HoodCall(getMetadata(), body.copy(), function, inclusive);
    }

    @Override
    public void evaluate(final ExecutionContext context) {
        projectAndEval(context);
        setAnnotation(function.run(body.getAnnotation(), inclusive ? null : context.getDeviceUID()));
    }

    @Override
    public String getName() {
        return function.name().toLowerCase(Locale.ENGLISH) + "Hood" + (inclusive ? "PlusSelf" : "");
    }

}
