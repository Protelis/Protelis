package org.protelis.test.observer;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Simple exception observer.
 */
public class SimpleExceptionObserver implements ExceptionObserver {
    private final List<Exception> exceptions = new LinkedList<Exception>();

    @Override
    public Exception exceptionThrown(final Exception ex) {
        exceptions.add(ex);
        return ex;
    }

    @Override
    public List<Exception> getExceptionList() {
        return exceptions;
    }

    @Override
    public Optional<Exception> getLastException() {
        try {
            return Optional.of(((LinkedList<Exception>) exceptions).getLast());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Exception> getFirstException() {
        try {
            return Optional.of(((LinkedList<Exception>) exceptions).getFirst());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }
}
