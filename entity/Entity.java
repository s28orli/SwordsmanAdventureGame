/**
 * Entity: Description
 *
 * @author Samuil Orlioglu
 * @version 4/30/2020
 */

package entity;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public abstract class Entity extends Thread {
    protected Point2D position;
    protected EntityFacing facing;
    protected EntityAction action;

    public Entity(){
        facing = EntityFacing.Front;
        action = EntityAction.Standing;
        position = new Point(0, 0);
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public EntityFacing getFacing() {
        return facing;
    }

    public void setFacing(EntityFacing facing) {
        this.facing = facing;
    }

    public EntityAction getAction() {
        return action;
    }

    public void setAction(EntityAction action) {
        this.action = action;
    }

    public abstract void draw(Graphics g, JPanel component);
}
