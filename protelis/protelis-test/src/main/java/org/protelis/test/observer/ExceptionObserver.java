package org.protelis.test.observer;

import java.util.List;
import java.util.Optional;

/**
 * This observer intercepts exception thrown during the tests.
 */
public interface ExceptionObserver {
    /**
     * An exception has been thrown.
     * 
     * @param ex
     *            exception
     * @return the same exception
     */
    Exception exceptionThrown(Exception ex);

    /**
     * @return list of all the thrown exception
     */
    List<Exception> getExceptionList();

    /**
     * @return last thrown exception
     */
    Optional<Exception> getLastException();

    /**
     * @return first thrown exception
     */
    Optional<Exception> getFirstException();
}
