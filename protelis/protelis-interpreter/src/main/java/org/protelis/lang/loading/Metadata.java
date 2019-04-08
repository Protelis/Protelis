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
