package org.protelis.vm;

import java.io.Serializable;

/**
 * A CodePath is a trace from the root to some node in a VM execution tree. Its
 * use is to allow particular execution locations to be serialized and compared
 * between different VMs, thereby enabling code alignment.
 * 
 * It MUST be immutable.
 * 
 * {@link #equals(Object)} and {@link #hashCode()} must be correctly
 * implemented.
 * 
 * The specific implementation of this class is critical for the Protelis
 * generated packet size. Prefer quick implementations for simulated
 * environment, and space efficient implementations for networked systems.
 */
public interface CodePath extends Serializable { }
