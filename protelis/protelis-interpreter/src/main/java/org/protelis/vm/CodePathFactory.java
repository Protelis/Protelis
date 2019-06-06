package org.protelis.vm;

import java.io.Serializable;

import gnu.trove.list.TIntList;
import gnu.trove.stack.TIntStack;

/**
 * A function which is able to build a {@link CodePath} given the current
 * Protelis stack status.
 */
@FunctionalInterface
public interface CodePathFactory extends Serializable {

    /**
     * Creates a {@link CodePath} for the current stack status.
     * 
     * @param callStackIdentifiers the stack frames identifiers
     * @param callStackSizes       the stack frame sizes, by frame
     * @return a new {@link CodePath}
     */
    CodePath createCodePath(TIntList callStackIdentifiers, TIntStack callStackSizes);
}
