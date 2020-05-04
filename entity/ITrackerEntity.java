package entity;

public interface ITrackerEntity {
    void addEntityToTrack(ITrackableEntity entity);
    double getAttackRangeMax();
    double getAttackRangeMin();

}
