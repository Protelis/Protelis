package org.protelis.test;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

/**
 * Configure simulation result and properties.
 */
public final class TestConfig {
    /**
     * Id that represent all the devices.
     */
    public static final int ALL = -1;
    private List<Triple<Integer, String, Object>> properties;
    private Object[][] result;
    private int maxRound, distance;
    private String fileName;

    /**
     * @return file to be tested
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return max round
     */
    public int getMaxRound() {
        return maxRound;
    }

    /**
     * @return distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * @return result of the simulation
     */
    public Object[][] getExpectedResult() {
        if (result == null) {
            throw new IllegalArgumentException("Expected result is null");
        }
        return result;
    }

    /**
     * @return environment's properties
     */
    public List<Triple<Integer, String, Object>> getProperties() {
        return properties;
    }

    // /**
    // *
    // * @param properties
    // * environment properties.
    // * @return test configuration
    // */
    // @SuppressWarnings("unchecked")
    // public TestConfig setProperties(final Triple<Integer, String, Object>...
    // properties) {
    // this.properties = Arrays.asList(properties);
    // return this;
    // }

    /**
     * 
     * @param property
     *            property name
     * @param value
     *            property value
     * @param ids
     *            devices to be set
     * @return test configuration
     */
    public TestConfig setProperties(final String property, final Object value, final Integer... ids) {
        for (Integer id : ids) {
            properties.add(Triple.of(id, property, value));
        }
        return this;
    }

    /**
     * 
     * @param property
     *            property name
     * @param value
     *            property value
     * @return test configuration
     */
    public TestConfig setProperties(final String property, final Object value) {
        properties.add(Triple.of(ALL, property, value));
        return this;
    }

    /**
     * 
     * @param result
     *            expected result
     * @return test configuration
     */
    public TestConfig setExpectedResult(final Object[][] result) {
        this.result = result;
        return this;
    }

    @SuppressWarnings("unchecked")
    private Integer addProperty(final Integer index, final Object value) {
        int idx = index;
        if (value instanceof Object[]) {
            Object[] array = (Object[]) value;
            for (int i = 0; i < array.length; i++) {
                idx = addProperty(idx, array[i]);
            }
        } else {
            Pair<String, Object> p = (Pair<String, Object>) value;
            properties.add(Triple.of(index, p.getLeft(), p.getRight()));
        }
        return idx++;
    }

    private void setUp(final String fileName, final int maxRound, final int distance, final Object[][] result) {
        this.result = result;
        this.maxRound = maxRound;
        this.distance = distance;
        this.fileName = fileName;
    }

    /**
     * Test configuration.
     * 
     * @param fileName
     *            file to be tested
     * @param maxRound
     *            number of execution round
     * @param distance
     *            distance under which devices are considered neighbors
     * @param properties
     *            environment's properties
     * @param result
     *            expected simulation result
     */
    public TestConfig(final String fileName, final int maxRound, final int distance, final Object[] properties,
                    final Object[][] result) {
        this.properties = new LinkedList<>();
        addProperty(0, properties);
        setUp(fileName, maxRound, distance, result);
    }

    /**
     * Test configuration.
     * 
     * @param fileName
     *            file to be tested
     * @param maxRound
     *            number of execution round
     * @param distance
     *            distance under which devices are considered neighbors
     * @param properties
     *            environment's properties
     * @param result
     *            expected simulation result
     */
    public TestConfig(final String fileName, final int maxRound, final int distance,
                    final Triple<Integer, String, Object>[] properties, final Object[][] result) {
        this.properties = new LinkedList<>();
        for (Triple<Integer, String, Object> t : properties) {
            this.properties.add(t);
        }
        setUp(fileName, maxRound, distance, result);
    }

    /**
     * Test configuration.
     * 
     * @param fileName
     *            file to be tested
     * @param maxRound
     *            number of execution round
     * @param distance
     *            distance under which devices are considered neighbors
     * @param properties
     *            environment's properties
     * @param result
     *            expected simulation result
     */
    public TestConfig(final String fileName, final int maxRound, final int distance,
                    final List<Triple<Integer, String, Object>> properties, final Object[][] result) {
        this.properties = properties;
        setUp(fileName, maxRound, distance, result);
    }

    /**
     * Test configuration.
     * 
     * @param fileName
     *            file to be tested
     * @param maxRound
     *            number of execution round
     * @param distance
     *            distance under which devices are considered neighbors
     */
    public TestConfig(final String fileName, final int maxRound, final int distance) {
        this.properties = new LinkedList<>();
        setUp(fileName, maxRound, distance, null);
    }

    /**
     * Test configuration.
     * 
     * @param fileName
     *            file to be tested
     * @param maxRound
     *            number of execution round
     * @param distance
     *            distance under which devices are considered neighbors
     * @param properties
     *            environment's properties
     * @param result
     *            expected simulation result
     * @return new test configuration
     */
    public static TestConfig create(final String fileName, final int maxRound, final int distance,
                    final Triple<Integer[], String, Object>[] properties, final Object[][] result) {
        List<Triple<Integer, String, Object>> res = new LinkedList<>();
        for (Triple<Integer[], String, Object> outer : properties) {
            for (Integer id : outer.getLeft()) {
                res.add(Triple.of(id, outer.getMiddle(), outer.getRight()));
            }
        }
        return new TestConfig(fileName, maxRound, distance, res, result);
    }

    /**
     * Test configuration.
     * 
     * @param fileName
     *            file to be tested
     * @param maxRound
     *            number of execution round
     * @param distance
     *            distance under which devices are considered neighbors
     * @param properties
     *            environment's properties
     * @param result
     *            expected simulation result
     * @return new test configuration
     */
    public static TestConfig create(final String fileName, final int maxRound, final int distance,
                    final Triple<Integer[], String, Object> properties, final Object[][] result) {
        List<Triple<Integer, String, Object>> res = new LinkedList<>();
        for (Integer id : properties.getLeft()) {
            res.add(Triple.of(id, properties.getMiddle(), properties.getRight()));
        }
        return new TestConfig(fileName, maxRound, distance, res, result);
    }

    /**
     * Test configuration.
     * 
     * @param fileName
     *            file to be tested
     * @param maxRound
     *            number of execution round
     * @param distance
     *            distance under which devices are considered neighbors
     * @param properties
     *            environment's properties
     * @param result
     *            expected simulation result
     * @return new test configuration
     */
    public static TestConfig create(final String fileName, final int maxRound, final int distance,
                    final Object[] properties, final Object[][] result) {
        return new TestConfig(fileName, maxRound, distance, properties, result);
    }

    /**
     * 
     * Test configuration.
     * 
     * @param fileName
     *            file to be tested
     * @param maxRound
     *            number of execution round
     * @param distance
     *            distance under which devices are considered neighbors
     * @return new test configuration
     */
    public static TestConfig create(final String fileName, final int maxRound, final int distance) {
        return new TestConfig(fileName, maxRound, distance);
    }

    /**
     * 
     * Test configuration with default settings.
     * 
     * maxRound = {@link Results}.EXECUTION_ROUND
     * 
     * distance = {@link Results}.MANHATTAN_BLOCK
     * 
     * @param fileName
     *            file to be tested
     * @return new test configuration
     */
    public static TestConfig create(final String fileName) {
        return new TestConfig(fileName, Results.EXECUTION_ROUND, Results.MANHATTAN_BLOCK);
    }

}