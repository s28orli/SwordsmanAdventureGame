/**
 * Vector: A Vector class for represent difference between Points.
 *
 * @author Samuil Orlioglu
 * @version 5/3/2020
 */

package util;

import java.awt.geom.Point2D;


public class Vector extends Point2D {
    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get X component of Vector
     *
     * @return X component of Vector
     */
    @Override
    public double getX() {
        return x;
    }

    /**
     * Get Y component of Vector
     *
     * @return Y component of Vector
     */
    @Override
    public double getY() {
        return y;
    }

    /**
     * Set the x and y components of this Vector.
     *
     * @param x New X value
     * @param y New Y value
     */
    @Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the Magnitude of this Vector
     *
     * @return Magnitude of this Vector
     */
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Normalize the Vector to Range of -1.0 to 1.0.
     */
    public void normalize() {
        x = x / magnitude();
        y = y / magnitude();
    }

    /**
     * Get a String representation of the Vector
     *
     * @return String representation of the Vector
     */
    @Override
    public String toString() {
        return "<" +
                "x=" + x +
                ", y=" + y +
                '>';
    }
}
