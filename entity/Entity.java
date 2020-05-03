/**
 * Entity: Description
 *
 * @author Samuil Orlioglu
 * @version 4/30/2020
 */

package entity;

import world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public abstract class Entity extends Thread {
    public static final int TIME_DELAY = 50;
    public static final int ANIMATION_TIME_LENGTH = 100;
    public static final int WALKING_ANIMATION_SIZE = 64;
    public static final int ATTACKING_ANIMATION_SIZE = 192;
    public static final int TRACKING_SEARCH_DISTANCE = 5;
    protected World world;
    protected Point2D position;
    protected EntityFacing facing;
    protected EntityAction action;
    protected int health;
    protected double size = 1;
    protected int attackingImageCycle = 7;
    protected int walkingImageCycle = 7;







    public Entity(World world){
        this(world, new Point(0, 0));
    }




    public Entity(World world, Point position){
        this.world = world;
        facing = EntityFacing.Front;
        action = EntityAction.Standing;
        this.position = position;
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
