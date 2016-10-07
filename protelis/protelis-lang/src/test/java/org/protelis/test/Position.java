package org.protelis.test;

/**
 * 2D position.
 */
public class Position {
    private final Double x, y, z;

    /**
     * Create a new position in space.
     * 
     * @param x
     *            x
     * @param y
     *            y
     * @param z
     *            z
     */
    public Position(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Add a vector to this position.
     * 
     * @param x
     *            x
     * @param y
     *            y
     * @param z
     *            z
     * @return new position
     */
    public Position addVector(final Double x, final Double y, final Double z) {
        return new Position(this.x + x, this.y + y, this.z + z);
    }

    /**
     * 
     * @param x
     *            x
     * @param y
     *            y
     * @param z
     *            z
     * @return a new position
     */
    public static Position fromVector(final Double x, final Double y, final Double z) {
        return new Position(x, y, z);
    }

    /**
     * Euclidean distance between two positions.
     * 
     * @param position
     *            other position
     * @return Euclidean distance between two positions
     */
    public double distanceTo(final Position position) {
        return Math.sqrt(Math.pow(x - position.x, 2) + Math.pow(y - position.y, 2) + Math.pow(z - position.z, 2));
    }
}
