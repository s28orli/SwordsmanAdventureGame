/**
 * ScentPoint: Description
 *
 * @author Samuil Orlioglu
 * @version 5/3/2020
 */

package entity;

import java.awt.*;
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

    public int getLife(){
        return life;
    }

    public Point2D getPosition(){
        return position;
    }

    public void tick(){
        life--;
    }

    public void tick(int amount){
        life -= amount;
    }

    public ITrackableEntity getSource() {
        return source;
    }
}
