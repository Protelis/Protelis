package org.protelis.test;

import java.util.Arrays;
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
    /**
     * Use this constant when you want to skip a value from testing (as it might
     * be platform dependent).
     */
    public static final Double DC = 1.0 * Integer.MAX_VALUE;
    private List<Triple<Integer, String, Object>> properties;
    private List<Pair<Position, Object[][]>> r;
    private int maxRound;
    private Double distance;
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
    public Double getDistance() {
        return distance;
    }

    /**
     * @return result of the simulation
     */
    public Object[] getExpectedResult() {
        if (r.size() == 0) {
            throw new IllegalArgumentException("Expected result is null");
        }
        List<Object> res = new LinkedList<>();
        for (Pair<Position, Object[][]> group : r) {
            for (Object object : matrixToArray(group.getRight())) {
                res.add(object);
            }
        }
        return res.toArray();
    }

    /**
     * @return result of the simulation
     */
    public List<Pair<Position, Object[][]>> getExpectedResultGroups() {
        if (r.size() == 0) {
            throw new IllegalArgumentException("Expected result is null");
        }
        return r;
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

    private Object[] matrixToArray(final Object[][] result) {
        List<Object> res = new LinkedList<>();

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                res.add(result[i][j]);
            }
        }
        return res.toArray();
    }

    /**
     * 
     * @param result
     *            expected result as grid
     * @return test configuration
     */
    public TestConfig setExpectedResult(final Object[][] result) {
        r = new LinkedList<>();
        return addExpectedResult(Position.fromVector(0.0, 0.0, 0.0), result);
    }

    /**
     * @param groupStartingPosition
     *            starting position
     * @param result
     *            expected result as grid
     * @return test configuration
     */
    public TestConfig addExpectedResult(final Position groupStartingPosition, final Object[][] result) {
        r.add(Pair.of(groupStartingPosition, result));
        return this;
    }

    // @SuppressWarnings("unchecked")
    // private Integer addProperty(final Integer index, final Object value) {
    // int idx = index;
    // if (value instanceof Object[]) {
    // Object[] array = (Object[]) value;
    // for (int i = 0; i < array.length; i++) {
    // idx = addProperty(idx, array[i]);
    // }
    // } else {
    // Pair<String, Object> p = (Pair<String, Object>) value;
    // properties.add(Triple.of(index, p.getLeft(), p.getRight()));
    // }
    // return idx++;
    // }

    private void setUp(final String fileName, final int maxRound, final Double distance, final Object[][] result) {
        if (result != null) {
            setExpectedResult(result);
        }
        this.maxRound = maxRound;
        this.distance = distance;
        this.fileName = fileName;
    }

    // /**
    // * Test configuration.
    // *
    // * @param fileName
    // * file to be tested
    // * @param maxRound
    // * number of execution round
    // * @param distance
    // * distance under which devices are considered neighbors
    // * @param properties
    // * environment's properties
    // * @param result
    // * expected simulation result
    // */
    // public TestConfig(final String fileName, final int maxRound, final int
    // distance, final Object[] properties,
    // final Object[][] result) {
    // this.properties = new LinkedList<>();
    // r = new LinkedList<>();
    // addProperty(0, properties);
    // setUp(fileName, maxRound, distance, result);
    // }

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
    public TestConfig(final String fileName, final int maxRound, final Double distance,
                    final Triple<Integer, String, Object>[] properties, final Object[][] result) {
        this(fileName, maxRound, distance, Arrays.asList(properties), result);
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
    public TestConfig(final String fileName, final int maxRound, final Double distance,
                    final List<Triple<Integer, String, Object>> properties, final Object[][] result) {
        this.properties = properties;
        r = new LinkedList<>();
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
    public TestConfig(final String fileName, final int maxRound, final Double distance) {
        this(fileName, maxRound, distance, new LinkedList<>(), null);
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
    public static TestConfig create(final String fileName, final int maxRound, final Double distance,
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
    public static TestConfig create(final String fileName, final int maxRound, final Double distance,
                    final Triple<Integer[], String, Object> properties, final Object[][] result) {
        List<Triple<Integer, String, Object>> res = new LinkedList<>();
        for (Integer id : properties.getLeft()) {
            res.add(Triple.of(id, properties.getMiddle(), properties.getRight()));
        }
        return new TestConfig(fileName, maxRound, distance, res, result);
    }

    // /**
    // * Test configuration.
    // *
    // * @param fileName
    // * file to be tested
    // * @param maxRound
    // * number of execution round
    // * @param distance
    // * distance under which devices are considered neighbors
    // * @param properties
    // * environment's properties
    // * @param result
    // * expected simulation result
    // * @return new test configuration
    // */
    // public static TestConfig create(final String fileName, final int
    // maxRound, final Double distance,
    // final Object[] properties, final Object[][] result) {
    // return new TestConfig(fileName, maxRound, distance, properties, result);
    // }

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
    public static TestConfig create(final String fileName, final int maxRound, final double distance) {
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

    /**
     * 
     * Test configuration with default settings.
     * 
     * distance = {@link Results}.MANHATTAN_BLOCK
     * 
     * @param maxRound
     *            number of execution round
     * 
     * @param fileName
     *            file to be tested
     * @return new test configuration
     */
    public static TestConfig create(final String fileName, final int maxRound) {
        return new TestConfig(fileName, maxRound, Results.MANHATTAN_BLOCK);
    }

}