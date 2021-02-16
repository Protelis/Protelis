/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.lang.loading;

import java.io.Serializable;

/**
 * This class represents data about each AST node about the original code that generated it.
 *
 */
public interface Metadata extends Serializable {

    /**
     * @return the line number at which the entity is defined
     */
    int getStartLine();

    /**
     * @return the line number at which the entity terminates
     */
    int getEndLine();

}
