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
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.interpreter.util.Reference;
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

public final class ProtelisLoadingUtilities {

    private static final LoadingCache<Object, Reference> REFERENCES = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build(new CacheLoader<Object, Reference>() {
                @Override
                public Reference load(final Object key) {
                    return new Reference(key);
                }
            });

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
        return qualifiedNameFor(Lambda.class, lambda, ":$anon", 0);
    }

    private static String qualifiedNameFor(final EObject origin, final String suffix, final int counted) {
        return qualifiedNameFor(origin.getClass(), origin, suffix, counted);
    }

    private static String qualifiedNameFor(final Class<? extends EObject> clazz, final EObject origin, final String suffix, final int counted) {
        final EObject container = origin.eContainer();
        if (container instanceof FunctionDef) {
            return qualifiedNameFor((FunctionDef) container) + suffix + counted;
        }
        if (container instanceof ProtelisModule) {
            return qualifiedNameFor((ProtelisModule) container) + suffix + counted;
        }
        if (container instanceof MethodCall) {
            final MethodCall method = (MethodCall) container;
            return qualifiedNameFor(container, ":$" + method.getName(), 0) + suffix + counted;
        }
        if (container instanceof Expression) {
            final Expression expression = (Expression) container;
            if (container instanceof Lambda) {
                return qualifiedNameFor((Lambda) container) + suffix + counted;
            }
            if (expression.getName() != null) {
                final String name = expression.getName().equals(".") ? "method" : expression.getName();
                return qualifiedNameFor(container, ":$" + name, counted) + suffix + counted;
            }
            final List<EObject> elements = expression.getElements();
            if (elements != null && elements.size() == 2 && elements.get(1) instanceof InvocationArguments) {
                return qualifiedNameFor(Expression.class, container, ":$invoke", 0);
            }
            throw new IllegalStateException();
        }
        final Stream<? extends Statement> statements;
        if (container instanceof InvocationArguments) {
            statements = ProtelisLoadingUtilities.invocationArguments((InvocationArguments) container);
        } else if (container instanceof Block) {
            statements = ((Block) container).getStatements().stream();
        } else if (container instanceof IfWithoutElse) {
            statements = ((IfWithoutElse) container).getThen().stream();
        } else {
            statements = Stream.empty();
        }
        int myId = counted;
        final Iterator<? extends Statement> iterator = statements.iterator();
        while (iterator.hasNext()) {
            final Statement statement = iterator.next();
            if (statement.equals(origin)) {
                return qualifiedNameFor(clazz, container, suffix, myId);
            }
            if (clazz.isAssignableFrom(statement.getClass())) {
                myId++;
            }
        }
        return qualifiedNameFor(container.getClass(), container, suffix, counted);
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
