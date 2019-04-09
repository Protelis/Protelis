/**
 * 
 */
package org.protelis.lang.util;

import java.io.PrintStream;
import java.util.Deque;
import java.util.LinkedList;

import javax.annotation.Nonnull;

import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.impl.FunctionCall;
import org.protelis.lang.loading.Metadata;

import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

/**
 * This class represents a runtime error in the Protelis interpreter.
 */
public final class ProtelisRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final Deque<AnnotatedTree<?>> protelisStackTrace = new LinkedList<>();

    public ProtelisRuntimeException(final Throwable javaCause, final AnnotatedTree<?> origin) {
        super(javaCause);
        protelisStackTrace.add(origin);
    }

    public void calledBy(final AnnotatedTree<?> element) {
        protelisStackTrace.add(element);
    }

    @Override
    public void printStackTrace(final PrintStream s) {
        s.println(toString());
        getCause().printStackTrace(s);
    }

    private Stream<AnnotatedTree<?>> stream() {
        return StreamSupport.stream(protelisStackTrace);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(1000)
            .append(getClass().getSimpleName())
            .append(": ")
            .append(getCause().getMessage());
        if (stream().anyMatch(it -> it instanceof FunctionCall)) {
            AnnotatedTree<?> origin = protelisStackTrace.getFirst();
            boolean wasFunction = true;
            for (final AnnotatedTree<?> current: protelisStackTrace) {
                if (wasFunction) {
                    origin = current;
                    wasFunction = false;
                }
                if (current instanceof FunctionCall) {
                    wasFunction = true;
                    final FunctionDefinition fun = ((FunctionCall) current).getFunctionDefinition();
                    sb.append("\n\tat ")
                        .append(fun.getName())
                        .append(extractLines(origin));
                }
            }
        } else {
            sb.append("\n\tin main script ")
                .append(extractLines(protelisStackTrace.getFirst()));
        }
        return sb.toString();
    }

    private static String extractLines(@Nonnull final AnnotatedTree<?> origin) {
        final Metadata meta = origin.getMetadata();
        int start = meta.getStartLine();
        int end = meta.getEndLine();
        return start == end ? " at line number " + start : " between lines " + start + " and " + end;
    }

}
