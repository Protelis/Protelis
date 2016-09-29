package org.protelis.vm.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.protelis.lang.datatype.DeviceUID;
import org.protelis.vm.ExecutionContext;
import org.protelis.vm.ExecutionEnvironment;
import org.protelis.vm.impl.linking.AbstractLinkingStrategy;
import org.protelis.vm.impl.linking.LinkingLine;
import org.protelis.vm.impl.linking.LinkingStrategy;
import org.protelis.vm.util.CodePath;

import com.google.common.collect.MapMaker;

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
  private final TestContext[] executionContexts;
  private final Map<DeviceUID, Map<CodePath, Object>> contents;

  /**
   * 
   * @param deviceNumber
   *          number of devices
   * @param rule
   *          rule to link the devices together
   */
  protected TestEnvironment(final int deviceNumber, final AbstractLinkingStrategy rule) {
    executionContexts = new TestContext[deviceNumber];
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
   *          number of devices
   * @param rule
   *          linking rule
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
      executionContexts[i] = new TestContext(id, this, new TestNetworkManager(id, this), rule.findPosition(id, deviceNumber));
    }
  }

  /**
   * @param deviceId
   *          device id
   * @return the device with the given id
   */
  public ExecutionContext getExecutionContext(final Integer deviceId) {
    return executionContexts[deviceId];
  }

  /**
   * @param deviceId
   *          device id
   * @return the environment of the device with the given id
   */
  public ExecutionEnvironment getExecutionEnvironment(final Integer deviceId) {
    return executionContexts[deviceId].getExecutionEnvironment();
  }

  /**
   * @return all the execution contexts
   */
  public SpatiallyEmbeddedContext[] getExecutionContexts() {
    return executionContexts;
  }

  /**
   * @param deviceId
   *          device id
   * @return neighborhood of the device with the given id
   */
  public Map<DeviceUID, Map<CodePath, Object>> getNeighborhood(final DeviceUID deviceId) {
    return rule.getNeighborsContent(deviceId, contents);
  }

  /**
   * @param id
   *          device which wants to add new information
   * @param toSend
   *          information to be added
   */
  public void putContent(final DeviceUID id, final Map<CodePath, Object> toSend) {
    contents.put(id, toSend);
  }

  /**
   * @return linking strategy
   */
  public LinkingStrategy getLinkingStrategy() {
    return rule;
  }
}
