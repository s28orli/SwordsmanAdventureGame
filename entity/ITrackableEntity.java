package entity;

import java.awt.geom.Point2D;
import java.util.Collection;

public interface ITrackableEntity {

    Collection<ScentPoint> getScentPoints();

    ScentPoint getMostRecentScentPoint();

    Point2D getPosition();

    ScentPoint getScentPoint(Point2D position);

}
