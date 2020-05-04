/**
 * ScentPoint: Description
 *
 * @author Samuil Orlioglu
 * @version 5/3/2020
 */

package entity;

import java.awt.geom.Point2D;

public final class ScentPoint {
    private Point2D position;
    private int life;
    private ITrackableEntity source;

    public ScentPoint(Point2D position, int life, ITrackableEntity source){
        this.position = position;
        this.life = life;
        this.source = source;
    }

    /**
     * Get the Scent Point's life.
     * @return Scent Point's life.
     */
    public int getLife(){
        return life;
    }

    /**
     * Get the Scent Point's position.
     * @return Scent Point's position
     */
    public Point2D getPosition(){
        return position;
    }

    /**
     * Update the Scent Point.
     */
    public void tick(){
        life--;
    }

    /**
     * Update the Scent Point.
     * @param amount Amount to subtract life by.
     */
    public void tick(int amount){
        life -= amount;
    }

    /**
     * Get the Entity that this Scent Point belongs to.
     * @return Entity that this Scent Point belongs to.
     */
    public ITrackableEntity getSource() {
        return source;
    }
}
