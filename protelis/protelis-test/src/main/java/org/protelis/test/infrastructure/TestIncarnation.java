package org.protelis.test.infrastructure;

import java.util.Objects;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.math3.random.RandomGenerator;
import org.danilopianini.lang.LangUtils;
import org.protelis.lang.ProtelisLoader;
import org.protelis.vm.ProtelisProgram;
import org.protelis.vm.ProtelisVM;

import com.google.common.collect.ImmutableList;

import it.unibo.alchemist.model.implementations.molecules.SimpleMolecule;
import it.unibo.alchemist.model.implementations.reactions.Event;
import it.unibo.alchemist.model.implementations.timedistributions.DiracComb;
import it.unibo.alchemist.model.implementations.timedistributions.ExponentialTime;
import it.unibo.alchemist.model.implementations.times.DoubleTime;
import it.unibo.alchemist.model.interfaces.Action;
import it.unibo.alchemist.model.interfaces.Condition;
import it.unibo.alchemist.model.interfaces.Environment;
import it.unibo.alchemist.model.interfaces.Incarnation;
import it.unibo.alchemist.model.interfaces.Molecule;
import it.unibo.alchemist.model.interfaces.Node;
import it.unibo.alchemist.model.interfaces.Reaction;
import it.unibo.alchemist.model.interfaces.TimeDistribution;

/**
 */
public final class TestIncarnation implements Incarnation<Object> {

    private static final TestIncarnation SINGLETON = new TestIncarnation();

    @Override
    public double getProperty(final Node<Object> node, final Molecule mol, final String prop) {
        final Object val = node.getConcentration(mol);
        if (val instanceof Number) {
            return ((Number) val).doubleValue();
        } else if (val instanceof String) {
            try {
                return Double.parseDouble(val.toString());
            } catch (final NumberFormatException e) {
                if (val.equals(prop)) {
                    return 1;
                }
                return 0;
            }
        } else if (val instanceof Boolean) {
            final Boolean cond = (Boolean) val;
            if (cond) {
                return 1d;
            } else {
                return 0d;
            }
        }
        return Double.NaN;
    }

    @Override
    public Molecule createMolecule(final String s) {
        return new SimpleMolecule(s);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    /**
     * @return an instance of a {@link TestIncarnation}
     */
    public static TestIncarnation instance() {
        return SINGLETON;
    }

    @Override
    public Node<Object> createNode(final RandomGenerator rand, final Environment<Object> env, final String param) {
        return new ProtelisNode(env);
    }

    @Override
    public TimeDistribution<Object> createTimeDistribution(final RandomGenerator rand, final Environment<Object> env,
                    final Node<Object> node, final String param) {
        if (param == null) {
            return new ExponentialTime<>(Double.POSITIVE_INFINITY, rand);
        }
        double frequency;
        try {
            frequency = Double.parseDouble(param);
        } catch (final NumberFormatException e) {
            frequency = 1;
        }
        return new DiracComb<>(new DoubleTime(rand.nextDouble() / frequency), frequency);
    }

    @Override
    public Reaction<Object> createReaction(final RandomGenerator rand, final Environment<Object> env,
                    final Node<Object> node, final TimeDistribution<Object> time, final String param) {
        LangUtils.requireNonNull(node, time);
        final Reaction<Object> result = new Event<>(node, time);
        if (param != null) {
            result.setActions(ImmutableList.of(createAction(rand, env, node, time, result, param)));
        }
        return result;
    }

    @Override
    public Condition<Object> createCondition(final RandomGenerator rand, final Environment<Object> env,
                    final Node<Object> node, final TimeDistribution<Object> time, final Reaction<Object> reaction,
                    final String param) {
        throw new NotImplementedException("createCondition");
    }

    @Override
    public Action<Object> createAction(final RandomGenerator rand, final Environment<Object> env,
                    final Node<Object> node, final TimeDistribution<Object> time, final Reaction<Object> reaction,
                    final String param) {
        Objects.requireNonNull(param);
        if (node instanceof ProtelisNode) {
            final ProtelisNode pNode = (ProtelisNode) node;
            try {
                return new RunProtelisProgram(env, pNode, reaction, rand, param);
            } catch (RuntimeException e) { // NOPMD: we want to catch any runtime exception
                throw new IllegalArgumentException("Could not create the requested Protelis program: " + param, e);
            }
        }
        throw new IllegalArgumentException("The node must be an instance of " + ProtelisNode.class.getSimpleName()
                        + ", it is a " + node.getClass().getName() + " instead");
    }

    @Override
    public Object createConcentration(final String s) {
        try {
            final ProtelisProgram program = ProtelisLoader
                            .parse(Objects.requireNonNull(s, "The concentration can not be null."));
            final ProtelisVM vm = new ProtelisVM(program, new DummyContext());
            vm.runCycle();
            return vm.getCurrentValue();
        } catch (IllegalArgumentException e) {
            /*
             * Not a valid program: inject the String itself
             */
            return s;
        }
    }

}
