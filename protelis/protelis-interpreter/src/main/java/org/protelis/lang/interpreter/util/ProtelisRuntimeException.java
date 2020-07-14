/**
 * 
 */
package org.protelis.lang.interpreter.util;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.interpreter.ProtelisAST;
import org.protelis.lang.interpreter.impl.All;
import org.protelis.lang.interpreter.impl.FunctionCall;
import org.protelis.lang.loading.Metadata;

/**
 * This class represents a runtime error in the Protelis interpreter.
 */
public final class ProtelisRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final Deque<ProtelisAST<?>> protelisStackTrace = new LinkedList<>();

    /**
     * Builds a new {@link ProtelisRuntimeException}, happening due to the specified
     * Java exception, into the origin node of the Protelis AST.
     * 
     * @param javaCause the java {@link Throwable} generating the exception
     * @param origin    the point in the Protelis program in which the Java
     *                  exception was thrown
     */
    public ProtelisRuntimeException(@Nonnull final Throwable javaCause, final ProtelisAST<?> origin) {
        super(javaCause);
        protelisStackTrace.add(origin);
    }

    /**
     * Populates the exception stack with a new caller.
     * 
     * @param element the Protelis node which called the failing one
     */
    public void fillInStackFrame(final ProtelisAST<?> element) {
        protelisStackTrace.add(element);
    }

    @Override
    public String getMessage() {
        return Optional.ofNullable(getCause().getMessage())
                .orElse("The cause exception did not provide any useful message")
            + '\n' + getProtelisStacktrace();
    }

    /**
     * @return A stringyfied version of the Protelis stack trace that caused the issue
     */
    public String getProtelisStacktrace() {
        final StringBuilder trace = header();
        if (stream().noneMatch(it -> it instanceof FunctionCall)) {
            trace.append("\n\tin main script ")
                .append(extractLines(protelisStackTrace.getFirst()));
        }
        final StringBuilder longTrace = header();
        ProtelisAST<?> origin = protelisStackTrace.getFirst();
        boolean wasFunction = true;
        for (final ProtelisAST<?> current: protelisStackTrace) {
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

    private StringBuilder header() {
        return new StringBuilder(1000)
            .append(getClass().getName())
            .append(": ")
            .append(getCause().getMessage());
    }

    private Stream<ProtelisAST<?>> stream() {
        return protelisStackTrace.stream();
    }

    private static String extractLines(@Nonnull final ProtelisAST<?> origin) {
        final Metadata meta = origin.getMetadata();
        final int start = meta.getStartLine();
        final int end = meta.getEndLine();
        return start == end ? " at line number " + start : " between lines " + start + " and " + end;
    }

}
