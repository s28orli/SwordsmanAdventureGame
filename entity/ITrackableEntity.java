package entity;

import java.awt.geom.Point2D;
import java.util.Collection;

public interface ITrackableEntity {

    Collection<ScentPoint> getScentPoints();

    ScentPoint getScentPoint(Point2D position);

    Point2D getPosition();

}
