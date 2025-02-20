/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.test.observer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Simple exception observer.
 */
public final class SimpleExceptionObserver implements ExceptionObserver {
    private final LinkedList<Exception> exceptions = new LinkedList<>(); // NOPMD methods getFirst and getLast

    @Override
    public void exceptionThrown(final Exception ex) {
        exceptions.add(ex);
    }

    @Override
    public List<Exception> getExceptionList() {
        return Collections.unmodifiableList(exceptions);
    }

    @Override
    public Optional<Exception> getLastException() {
        try {
            return Optional.of(exceptions.getLast());
        } catch (final NoSuchElementException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Exception> getFirstException() {
        try {
            return Optional.of(exceptions.getFirst());
        } catch (final NoSuchElementException e) {
            return Optional.empty();
        }
    }
}
