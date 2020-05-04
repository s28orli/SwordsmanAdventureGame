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
    public static final Point2D[] consecutiveCoords = new Point2D[]{
            new Point(-1, 0), new Point(1, 0), new Point(0, -1), new Point(0, 1)
    };

    public static final double EPSILON = 0.01;

    public static Vector getDirection(Point2D pos1, Point2D pos2) {
        return new Vector(pos2.getX() - pos1.getX(), pos2.getY() - pos1.getY());
    }

    public static Point2D mult(Point2D point, double val){
        return new Point2D.Double(point.getX() * val, point.getY() * val);
    }
}
