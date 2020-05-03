package entity;

import java.awt.geom.Point2D;
import java.util.List;

public interface ITrackableEntity {

    List<ScentPoint> getScentPoints();

    ScentPoint getMostRecentScentPoint();

    Point2D getPosition();

    ScentPoint getScentPoint(Point2D position);

}
