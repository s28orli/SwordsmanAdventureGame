/**
 * MathHelper: Description
 *
 * @author Samuil Orlioglu
 * @version 5/3/2020
 */

package util;

import java.awt.*;
import java.awt.geom.Point2D;

public class MathHelper {
    /**
     * Neighbor Coordinates. Left, Right, Down, Up
     */
    public static final Point2D[] consecutiveCoords = new Point2D[]{
            new Point(-1, 0), new Point(1, 0), new Point(0, -1), new Point(0, 1)
    };

    /**
     * Used for comparing doubles.
     */
    public static final double EPSILON = 0.01;

    /**
     * Get Direction Vector from point to another point.
     *
     * @param pos1 Point 1
     * @param pos2 Point 2
     * @return A Direction vector NOT normalized.
     */
    public static Vector getDirection(Point2D pos1, Point2D pos2) {
        return new Vector(pos2.getX() - pos1.getX(), pos2.getY() - pos1.getY());
    }


}
