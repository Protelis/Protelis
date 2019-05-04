package org.protelis.vm;

import java.io.Serializable;

import gnu.trove.list.TIntList;
import gnu.trove.stack.TIntStack;
import java8.lang.FunctionalInterface;

@FunctionalInterface
public interface CodePathFactory extends Serializable {
    CodePath createCodePath(TIntList callStackIdentifiers, TIntStack callStackSizes);
}
