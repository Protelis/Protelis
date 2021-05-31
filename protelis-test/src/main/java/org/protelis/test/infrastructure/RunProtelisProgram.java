/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.test.infrastructure;

import java.util.List;

import org.apache.commons.math3.random.RandomGenerator;
import org.danilopianini.lang.LangUtils;
import org.protelis.lang.ProtelisLoader;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ProtelisVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.alchemist.model.implementations.molecules.SimpleMolecule;
import it.unibo.alchemist.model.interfaces.Action;
import it.unibo.alchemist.model.interfaces.Context;
import it.unibo.alchemist.model.interfaces.Environment;
import it.unibo.alchemist.model.interfaces.Molecule;
import it.unibo.alchemist.model.interfaces.Node;
import it.unibo.alchemist.model.interfaces.Reaction;

/**
 */
@SuppressFBWarnings(
        value = { "EQ_DOESNT_OVERRIDE_EQUALS", "SE_BAD_FIELD" },
        justification = "This class is not meant to be serialized, and not overriding equals() is desired.")
public final class RunProtelisProgram extends SimpleMolecule implements Action<Object> {
    /**
     * RESULT.
     */
    public static final String RESULT = "$$result";
    private static final long serialVersionUID = 2207914086772704332L;
    private final Environment<Object> environment;
    private final ProtelisNode node;
    private final org.protelis.vm.ProtelisProgram program;
    private final RandomGenerator random;
    private final ProtelisVM vm;
    private final CachingNetworkManager netmgr;
    private int round;
    private static final Logger L = LoggerFactory.getLogger(RunProtelisProgram.class);

    /**
     * @param env
     *            the environment
     * @param n
     *            the node
     * @param r
     *            the reaction
     * @param rand
     *            the random engine
     * @param prog
     *            the Protelis program
     * @throws SecurityException
     *             if you are not authorized to load required classes
     * @throws ClassNotFoundException
     *             if required classes can not be found
     */
    public RunProtelisProgram(final Environment<Object> env, final ProtelisNode n, final Reaction<Object> r,
                    final RandomGenerator rand, final String prog) {
        this(env, n, r, rand, ProtelisLoader.parse(prog));
    }

    private RunProtelisProgram(final Environment<Object> env, final ProtelisNode n, final Reaction<Object> r,
                    final RandomGenerator rand, final org.protelis.vm.ProtelisProgram prog) {
        super(prog.getName());
        LangUtils.requireNonNull(env, r, n, prog, rand);
        program = prog;
        environment = env;
        node = n;
        random = rand;
        this.netmgr = new CachingNetworkManager();
        node.setNetworkManger(netmgr);
        final ExecutionContext ctx = new DummyDevice(env, n, r, rand, netmgr);
        vm = new ProtelisVM(prog, ctx);
        round = 0;
    }

    @Override
    public void execute() {
        vm.runCycle();
        node.setConcentration(this, vm.getCurrentValue());
        L.debug(" [node{}-rnd:{}]: {}", node.toString(), round, vm.getCurrentValue());
        round++;
        environment.getNeighborhood(node).getNeighbors().forEach(n -> {
            final ProtelisNode pNode = (ProtelisNode) n;
            final CachingNetworkManager cnm = (CachingNetworkManager) pNode.getNetworkManager();
            cnm.receiveFromNeighbor(node, netmgr.getSendCache());
        });
        node.put(RESULT, vm.getCurrentValue());
    }

    /**
     * @return the environment
     */
    protected Environment<Object> getEnvironment() {
        return environment;
    }

    /**
     * @return the node
     */
    protected ProtelisNode getNode() {
        return node;
    }

    @Override
    public List<? extends Molecule> getModifiedMolecules() {
        /*
         * A Protelis program may modify any molecule (global variable)
         */
        return null;
    }

    @Override
    public Context getContext() {
        /*
         * A Protelis program never writes in other nodes
         */
        return Context.LOCAL;
    }

    @Override
    public Action<Object> cloneAction(final Node<Object> n, final Reaction<Object> r) {
        if (n instanceof ProtelisNode) {
            return new RunProtelisProgram(environment, (ProtelisNode) n, r, random, program);
        }
        throw new IllegalStateException("Can not load a Protelis program on a " + n.getClass() + ". A "
                        + ProtelisNode.class + " is required.");
    }
}
