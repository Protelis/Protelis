/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.lang;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.parser.protelis.Assignment;
import org.protelis.parser.protelis.Block;
import org.protelis.parser.protelis.Declaration;
import org.protelis.parser.protelis.Expression;
import org.protelis.parser.protelis.ExpressionList;
import org.protelis.parser.protelis.FunctionDef;
import org.protelis.parser.protelis.IfWithoutElse;
import org.protelis.parser.protelis.InvocationArguments;
import org.protelis.parser.protelis.KotlinStyleLambda;
import org.protelis.parser.protelis.Lambda;
import org.protelis.parser.protelis.MethodCall;
import org.protelis.parser.protelis.ProtelisModule;
import org.protelis.parser.protelis.VarUse;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Static utilities for parsing Protelis.
 */
public final class ProtelisLoadingUtilities {

    /**
     * A reference to the 'it' variable to be used in lambdas.
     */
    public static final Reference IT = new Reference("it");

    private static final LoadingCache<Object, Reference> REFERENCES = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build(new CacheLoader<Object, Reference>() {
                @Override
                @Nonnull
                public Reference load(@Nonnull final Object key) {
                    return new Reference(key);
                }
            });

    private ProtelisLoadingUtilities() { }

    /**
     * @param args invocation arguments
     * @return a stream of expressions, one element per argument.
     */
    public static Stream<Expression> argumentsToExpressionStream(@Nonnull final InvocationArguments args) {
        final Stream<KotlinStyleLambda> lastArgument = Optional.ofNullable(args.getLastArg())
            .map(Stream::of)
            .orElseGet(Stream::empty);
        final Stream<Expression> inParenthesis = Optional.ofNullable(args.getArgs())
                .<List<Expression>>map(ExpressionList::getArgs)
                .orElseGet(Collections::emptyList)
                .stream();
       return Stream.concat(inParenthesis, lastArgument);
    }

    private static String nameFor(final EObject container) {
        if (container instanceof Block) {
            return "b";
        }
        if (container instanceof Declaration) {
            return "let";
        }
        if (container instanceof Assignment) {
            return "=";
        }
        if (container instanceof IfWithoutElse) {
            return "ifwoe";
        }
        if (container instanceof ExpressionList) {
            return "";
        }
        if (container instanceof InvocationArguments) {
            return "()";
        }
        if (container instanceof MethodCall) {
            return ((MethodCall) container).getName();
        }
        if (container instanceof Expression) {
            final Expression exp = (Expression) container;
            if (exp.getName() == null && exp.getElements().size() == 2) {
                return qualifiedNameFor(((VarUse) exp.getElements().get(0)).getReference(), "");
            }
            return exp.getName();
        }
        return container.getClass().getSimpleName();
    }

    private static String qualifiedNameFor(final EObject origin, final String suffix) {
        if (origin instanceof JvmIdentifiableElement) {
            return ((JvmIdentifiableElement) origin).getQualifiedName();
        }
        final EObject container = origin.eContainer();
        if (container instanceof FunctionDef) {
            return qualifiedNameFor((FunctionDef) container) + suffix;
        }
        if (container instanceof ProtelisModule) {
            return qualifiedNameFor((ProtelisModule) container) + suffix;
        }
        int myId = 0;
        for (final Object statement : container.eContents()) {
            if (statement.equals(origin)) {
                return qualifiedNameFor(container, ":$" + nameFor(container)) + suffix + myId;
            }
            myId++;
        }
        throw new IllegalStateException("Bug in Protelis qualified name computation");
    }

    /**
     * @param functionDefinition the function
     * @return a qualified name
     */
    public static String qualifiedNameFor(final FunctionDef functionDefinition) {
        return qualifiedNameFor((ProtelisModule) functionDefinition.eContainer())
                + ':' + functionDefinition.getName();
    }

    /**
     * @param lambda a lambda produced by the parser
     * @return its qualified name
     */
    public static String qualifiedNameFor(final Lambda lambda) {
        return qualifiedNameFor(lambda, ":$l");
    }

    /**
     * @param module the module
     * @return a qualified name
     */
    public static String qualifiedNameFor(final ProtelisModule module) {
        return Optional.ofNullable(module)
                .map(ProtelisModule::getName)
                .map(it -> it
                        .replace("protelis:state:time", "⏱") // NOPMD: false positive
                        .replace("protelis:state:nonselfstabilizing:time", "⍼⏱") // NOPMD: false positive
                        .replace("protelis:lang:utils", "⚒")
                        .replace("protelis:coord:meta:timereplication", "⎇⏳")
                        .replace("protelis:coord:nonselfstabilizing:accumulation", "⍼⍖")
                        .replace("protelis:coord:accumulation", "⍖")
                        .replace("protelis:coord:graph", "⏧")
                        .replace("protelis:coord:meta", "⎇")
                        .replace("protelis:coord:sharedtimer", "⛖⏱")
                        .replace("protelis:coord:sparsechoice", "⌘")
                        .replace("protelis:coord:spreading", "⍏")
                        .replace("protelis:coord:tree", "⏉")
                )
                .orElse("?");
    }

    /**
     * @param o an object
     * @return the corrisponding {@link Reference} inside Protelis
     */
    public static Reference referenceFor(final Object o) {
        try {
            return REFERENCES.get(o);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Unable to create a reference for " + o, e);
        }
    }

    /**
     * @param l a list of objects
     * @return a list of the corresponding references inside Protelis
     */
    public static List<Reference> referenceListFor(final List<?> l) {
        return l.stream().map(ProtelisLoadingUtilities::referenceFor).collect(Collectors.toList());
    }
}
