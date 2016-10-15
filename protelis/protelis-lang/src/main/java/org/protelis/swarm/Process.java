package org.protelis.swarm;

import java.io.Serializable;

import org.protelis.lang.datatype.FunctionDefinition;


public class Process<T> implements Serializable, Comparable<Process<T>> {
    
    private final FunctionDefinition function;
    private final T result;
    private final int pid;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Process(int clock, FunctionDefinition fun, T result) {
        function = fun;
        this.result = result;
        this.pid = clock;
    }
    
    public static <T> Process<T> newProcess(Number pid, FunctionDefinition fun, T result) {
        return new Process<>(pid.intValue(), fun, result);
    }
    
    public <R> Process<R> update(R result) {
        return new Process<>(pid(), function(), result);
    }
    
    public FunctionDefinition function() {
        return function;
    }
    
    public T result() {
        return result;
    }
    
    public int pid() {
        return pid;
    }

    @Override
    public int compareTo(Process<T> p) {
        return Integer.compare(pid, p.pid);
    }
    
    @Override
    public String toString() {
        return "{" + pid() + ": " + result() + "}";
    }

}

