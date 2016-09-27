package org.protelis.vm.impl;

import java.util.Map;
import java.util.stream.LongStream;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.util.Reference;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ExecutionEnvironment;

import com.google.common.collect.MapMaker;

import java8.util.function.Function;

/**
 * 
 *
 */
public class MyDummyEnvironment {
    enum LinkingRule {
        NONE, ALL, STAR, LINE
    };

    private static final MapMaker MAPMAKER = new MapMaker();
    private final int nodeNumber;
    private final LinkingRule rule;
    private final Map<Long, AbstractExecutionContext> nodes;

    /**
     * 
     */
    public MyDummyEnvironment() {
        this(10, LinkingRule.LINE);
    }

    /**
     * 
     * @param nodeNumber
     * @param rule
     * @param defaultValue
     */
    protected MyDummyEnvironment(final int nodeNumber, final LinkingRule rule) {
        nodes = MAPMAKER.makeMap();
        this.nodeNumber = nodeNumber;
        this.rule = rule;
    }

    public static MyDummyEnvironment build() {
        return build(100, LinkingRule.LINE);
    }

    public static MyDummyEnvironment build(final int nodeNumber, LinkingRule rule) {
        MyDummyEnvironment env = new MyDummyEnvironment(nodeNumber, rule);
        env.setup();
        return env;
    }

    public void setup() {
        for (long i = 0; i < nodeNumber; i++) {
            nodes.put(i, new MyDummyContext(i, new MyDummyNetworkManager(this, i)));
        }
    }

    /**
     * 
     * @param n
     * @return
     */
    protected Map<Long, AbstractExecutionContext> getNeighborhood(final long n) {
        final Map<Long, AbstractExecutionContext> res = MAPMAKER.makeMap();
        switch (rule) {
        case NONE:
            break;
        case ALL:
            LongStream.rangeClosed(2, nodeNumber).forEach(v -> res.put(v + 1, nodes.get(v + 1)));
            break;
        case STAR:
            if (n == 1) {
                LongStream.rangeClosed(2, nodeNumber).forEach(v -> res.put(v + 1, nodes.get(v + 1)));
            }
            break;
        case LINE:
            if (n < nodeNumber) {
                res.put(n + 1, nodes.get(n + 1));
            }
            break;
        default:
            break;
        }
        return res;
    }

    public ExecutionContext getNode(final long nodeId) {
        return nodes.get(nodeId);
    }

    public ExecutionEnvironment getNodeEnvironment(final long nodeId) {
        return nodes.get(nodeId).getExecutionEnvironment();
    }

    public ExecutionContext[] getExecutionContexts() {
        ExecutionContext[] ctxs = new ExecutionContext[nodeNumber];
        for (Integer i = 0; i < nodeNumber; i++) {
            ctxs[i] = nodes.get(i.longValue());
        }
        return ctxs;
    }
}
