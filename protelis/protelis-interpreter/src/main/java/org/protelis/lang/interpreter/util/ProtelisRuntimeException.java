/**
 * 
 */
package org.protelis.lang.interpreter.util;

import java.io.PrintStream;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.impl.All;
import org.protelis.lang.interpreter.impl.FunctionCall;
import org.protelis.lang.loading.Metadata;

/**
 * This class represents a runtime error in the Protelis interpreter.
 */
public final class ProtelisRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final Deque<AnnotatedTree<?>> protelisStackTrace = new LinkedList<>();

    /**
     * Builds a new {@link ProtelisRuntimeException}, happening due to the specified
     * Java exception, into the origin node of the Protelis AST.
     * 
     * @param javaCause the java {@link Throwable} generating the exception
     * @param origin    the point in the Protelis program in which the Java
     *                  exception was thrown
     */
    public ProtelisRuntimeException(@Nonnull final Throwable javaCause, final AnnotatedTree<?> origin) {
        super(computeMessage(javaCause), javaCause);
        protelisStackTrace.add(origin);
    }

    /**
     * Populates the exception stack with a new caller.
     * 
     * @param element the Protelis node which called the failing one
     */
    public void fillInStackFrame(final AnnotatedTree<?> element) {
        protelisStackTrace.add(element);
    }

    private StringBuilder header() {
        return new StringBuilder(1000)
            .append(getClass().getName())
            .append(": ")
            .append(getCause().getMessage());
    }

    @Override
    public void printStackTrace(final PrintStream s) {
        s.println(toString());
        getCause().printStackTrace(s);
    }

    private Stream<AnnotatedTree<?>> stream() {
        return protelisStackTrace.stream();
    }

    @Override
    public String toString() {
        final StringBuilder trace = header();
        if (stream().noneMatch(it -> it instanceof FunctionCall)) {
            trace.append("\n\tin main script ")
                .append(extractLines(protelisStackTrace.getFirst()));
        }
        final StringBuilder longTrace = header();
        AnnotatedTree<?> origin = protelisStackTrace.getFirst();
        boolean wasFunction = true;
        for (final AnnotatedTree<?> current: protelisStackTrace) {
            if (!(current instanceof All)) {
                longTrace.append("\n\tat: ")
                    .append(current + extractLines(current));
            }
            if (wasFunction) {
                origin = current;
                wasFunction = false;
            }
            if (current instanceof FunctionCall) {
                wasFunction = true;
                final FunctionDefinition fun = ((FunctionCall) current).getFunctionDefinition();
                trace.append("\n\tat ")
                    .append(fun.getName())
                    .append(extractLines(origin));
            }
        }
        trace.append("\nFully detailed interpreter trace:\n")
            .append(longTrace);
        return trace.toString();
    }

    private static String computeMessage(final Throwable cause) {
        if (cause.getMessage() == null) {
            return "The cause exception did not provide any useful message.";
        }
        final String original = Objects.requireNonNull(cause.getMessage());
        final String localized = Objects.requireNonNull(cause.getLocalizedMessage());
        if (original.equals(localized)) {
            return original;
        }
        return localized + "(non-localized: " + original + ")";
    }

    private static String extractLines(@Nonnull final AnnotatedTree<?> origin) {
        final Metadata meta = origin.getMetadata();
        final int start = meta.getStartLine();
        final int end = meta.getEndLine();
        return start == end ? " at line number " + start : " between lines " + start + " and " + end;
    }

}
