package org.protelis.vm.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ExecutionEnvironment;
import org.protelis.vm.util.CodePath;

import com.google.common.collect.MapMaker;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Dummy environment for testing purpose.
 */
public class TestEnvironment {
    /**
     * A builder of {@link ConcurrentMap}.
     */
    private static final MapMaker MAPMAKER = new MapMaker();
    private final int deviceNumber;
    private final AbstractLinkingStrategy rule;
    private final TIntObjectMap<AbstractExecutionContext> nodes;
    private final Map<DeviceUID, Map<CodePath, Object>> contents;

    /**
     * 
     * @param deviceNumber
     *            number of devices
     * @param rule
     *            rule to link the devices together
     */
    protected TestEnvironment(final int deviceNumber, final AbstractLinkingStrategy rule) {
        nodes = new TIntObjectHashMap<AbstractExecutionContext>();
        contents = MAPMAKER.makeMap();
        this.deviceNumber = deviceNumber;
        this.rule = rule;
    }

    /**
     * Build a default environment with 10 devices in a line.
     * 
     * @return environment with 10 devices in a line
     */
    public static TestEnvironment build() {
        return build(10, new LinkingLine());
    }

    /**
     * Build the given environment.
     * 
     * @param deviceNumber
     *            number of devices
     * @param rule
     *            linking rule
     * @return new environment
     */
    public static TestEnvironment build(final int deviceNumber, final AbstractLinkingStrategy rule) {
        TestEnvironment env = new TestEnvironment(deviceNumber, rule);
        env.setup();
        return env;
    }

    private void setup() {
        for (int i = 0; i < deviceNumber; i++) {
            MyDeviceUID id = new MyDeviceUID(i);
            nodes.put(i, new TestContext(id, new TestNetworkManager(id, this)));
        }
    }

    /**
     * @param deviceId
     *            device id
     * @return the device with the given id
     */
    public ExecutionContext getNode(final Integer deviceId) {
        return nodes.get(deviceId);
    }

    /**
     * @param deviceId
     *            device id
     * @return the environment of the device with the given id
     */
    public ExecutionEnvironment getNodeEnvironment(final Integer deviceId) {
        return nodes.get(deviceId).getExecutionEnvironment();
    }

    /**
     * @return all the execution contexts
     */
    public ExecutionContext[] getExecutionContexts() {
        ExecutionContext[] ctxs = new ExecutionContext[deviceNumber];
        for (int i = 0; i < deviceNumber; i++) {
            ctxs[i] = nodes.get(i);
        }
        return ctxs;
    }

    /**
     * @param deviceId
     *            device id
     * @return neighborhood of the device with the given id
     */
    public Map<DeviceUID, Map<CodePath, Object>> getNeighborhood(final DeviceUID deviceId) {
        return rule.getNeighbors(deviceId, contents);
    }

    /**
     * @param id
     *            device which wants to add new information
     * @param toSend
     *            information to be added
     */
    public void putContent(final DeviceUID id, final Map<CodePath, Object> toSend) {
        contents.put(id, toSend);
    }
}
