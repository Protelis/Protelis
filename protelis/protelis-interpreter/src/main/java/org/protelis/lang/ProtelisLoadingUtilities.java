package org.protelis.lang;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend.lib.macro.declaration.Declaration;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.parser.protelis.Assignment;
import org.protelis.parser.protelis.Block;
import org.protelis.parser.protelis.Expression;
import org.protelis.parser.protelis.ExpressionList;
import org.protelis.parser.protelis.FunctionDef;
import org.protelis.parser.protelis.IfWithoutElse;
import org.protelis.parser.protelis.InvocationArguments;
import org.protelis.parser.protelis.KotlinStyleLambda;
import org.protelis.parser.protelis.Lambda;
import org.protelis.parser.protelis.MethodCall;
import org.protelis.parser.protelis.ProtelisModule;
import org.protelis.parser.protelis.Statement;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Static utilities for parsing Protelis
 */
public final class ProtelisLoadingUtilities {

    private static final LoadingCache<Object, Reference> REFERENCES = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build(new CacheLoader<Object, Reference>() {
                @Override
                public Reference load(final Object key) {
                    return new Reference(key);
                }
            });

    /**
     * A reference to the 'it' variable to be used in lambdas.
     */
    public static Reference IT = new Reference("it");

    private ProtelisLoadingUtilities() { }

    public static Stream<Expression> invocationArguments(@Nonnull final InvocationArguments args) {
        final Stream<KotlinStyleLambda> lastArgument = Optional.ofNullable(args.getLastArg())
            .map(Stream::of)
            .orElseGet(Stream::empty);
        final Stream<Expression> inParenthesis = Optional.ofNullable(args.getArgs())
                .<List<Expression>>map(ExpressionList::getArgs)
                .orElseGet(Collections::emptyList)
                .stream();
       return Stream.concat(inParenthesis, lastArgument);
    }

    public static String qualifiedNameFor(final FunctionDef functionDefinition) {
        return qualifiedNameFor((ProtelisModule) functionDefinition.eContainer())
                + ':' + functionDefinition.getName();
    }

    public static String qualifiedNameFor(final ProtelisModule module) {
        return Optional.ofNullable(module)
                .map(ProtelisModule::getName)
                .orElse("anonymous-module");
    }

    public static String qualifiedNameFor(final Lambda lambda) {
        return qualifiedNameFor(Lambda.class, lambda, ":$anon");
    }

    private static String qualifiedNameFor(final EObject origin, final String suffix) {
        return qualifiedNameFor(origin.getClass(), origin, suffix);
    }

    private static String qualifiedNameFor(final Class<? extends EObject> clazz, final EObject origin, final String suffix) {
        final EObject container = origin.eContainer();
        if (container instanceof FunctionDef) {
            return qualifiedNameFor((FunctionDef) container) + suffix;
        }
        if (container instanceof ProtelisModule) {
            return qualifiedNameFor((ProtelisModule) container) + suffix;
        }
        int myId = 0;
        final Iterator<EObject> iterator = container.eContents().iterator();
        while (iterator.hasNext()) {
            final Object statement = iterator.next();
            if (statement.equals(origin)) {
                return qualifiedNameFor(container, ":$" + nameFor(container) + myId + suffix);
            }
            myId++;
        }
        throw new IllegalStateException();
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
        return container instanceof Expression
                ? ((Expression) container).getName()
                : container.getClass().getSimpleName();
    }

    public static List<Reference> referenceListFor(final List<?> l) {
        return l.stream().map(ProtelisLoadingUtilities::referenceFor).collect(Collectors.toList());
    }

    public static Reference referenceFor(final Object o) {
        try {
            return REFERENCES.get(o);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Unable to create a reference for " + o, e);
        }
    }


}
