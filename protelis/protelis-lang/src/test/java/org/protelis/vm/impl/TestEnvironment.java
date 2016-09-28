package org.protelis.vm.impl;

import java.util.Map;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ExecutionEnvironment;
import org.protelis.vm.util.CodePath;

import com.google.common.collect.MapMaker;

/**
 * 
 *
 */
public class MyDummyEnvironment {
    public enum LinkingRule {
        NONE, ALL, STAR, LINE
    };

    private static final MapMaker MAPMAKER = new MapMaker();
    private final int nodeNumber;
    private final LinkingRule rule;
    private Map<Long, MyDeviceUID> ids;
    private final Map<Long, AbstractExecutionContext> nodes;
    public Map<DeviceUID, Map<CodePath, Object>> contents;

    /**
     * 
     * @param nodeNumber
     * @param rule
     * @param defaultValue
     */
    protected MyDummyEnvironment(final int nodeNumber, final LinkingRule rule) {
        nodes = MAPMAKER.makeMap();
        contents = MAPMAKER.makeMap();
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
        ids = MAPMAKER.makeMap();
        for (long i = 0; i < nodeNumber; i++) {
            MyDeviceUID id = new MyDeviceUID(i);
            ids.put(i, id);
            nodes.put(i, new MyDummyContext(id, new MyDummyNetworkManager(id, this)));
        }
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

    public Map<DeviceUID, Map<CodePath, Object>> getNeighborhood(DeviceUID id) {
        long n = ((MyDeviceUID) id).getId();

        final Map<DeviceUID, Map<CodePath, Object>> res = MAPMAKER.makeMap();
        switch (rule) {
        case NONE:
            break;
        // case ALL:
        // LongStream.rangeClosed(2, nodeNumber).forEach(v -> res.put(v + 1,
        // contents.get(v + 1)));
        // break;
        // case STAR:
        // if (n == 1) {
        // LongStream.rangeClosed(2, nodeNumber).forEach(v -> res.put(v + 1,
        // contents.get(v + 1)));
        // }
        // break;
        case LINE:
            if (n < nodeNumber - 1) {
                DeviceUID i = ids.get(n + 1);
                if (contents.get(i) != null)
                    res.put(i, contents.get(i));
            }
            if (n > 0) {
                DeviceUID i = ids.get(n - 1);
                if (contents.get(i) != null)
                    res.put(i, contents.get(i));
            }
            break;
        default:
            break;
        }
        return res;
    }

    public void putContent(DeviceUID id, Map<CodePath, Object> toSend) {
        contents.put(id, toSend);
    }
}
