/**
 * Entity: Description
 *
 * @author Samuil Orlioglu
 * @version 4/30/2020
 */

package entity;

import tiles.AbstractTile;

import util.MathHelper;
import util.Vector;
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
    protected Point2D.Double position;
    protected EntityFacing facing;
    protected EntityAction action;
    protected int health;
    protected int damage;
    protected double size = 1;
    protected int attackingImageCycle = 7;
    protected int walkingImageCycle = 7;


    public Entity(World world) {
        this(world, new Point2D.Double(0, 0));
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }


    public Entity(World world, Point2D.Double position) {
        this.world = world;
        facing = EntityFacing.Front;
        action = EntityAction.Standing;
        this.position = position;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D.Double position) {
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

    public boolean isCollision(Entity obj) {
        if (position.distance(obj.position) < size * 3) {
            return true;
        }
        return false;
    }

    public boolean isCollision(ITrackableEntity obj) {
        if (position.distance(obj.getPosition()) < size * 3) {
            return true;
        }
        return false;
    }

    public abstract void draw(Graphics g, JPanel component);

    public abstract void draw(Graphics g, JPanel component, boolean drawDebug);

    public static EntityFacing getFacingFromVector(Vector vec) {
        if (vec.getX() < 0 && vec.getY() == 0) {
            return EntityFacing.Left;
        }
        if (vec.getX() > 0 && vec.getY() == 0) {
            return EntityFacing.Right;
        }

        if (vec.getX() == 0 && vec.getY() > 0) {
            return EntityFacing.Front;
        }
        if (vec.getX() == 0 && vec.getY() < 0) {
            return EntityFacing.Back;
        }
        return null;
    }

    public World getWorld() {
        return world;
    }

    public int getDamage() {
        return damage;
    }

    public double getSize() {
        return size;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public boolean isFacingEntity(Entity entity){
        if(facing == EntityFacing.Left && Math.abs(entity.getPosition().getY()- position.getY()) <= MathHelper.EPSILON && entity.getPosition().getX() <= position.getX()){
            return true;
        }
        else if(facing == EntityFacing.Right && Math.abs(entity.getPosition().getY() - position.getY()) <= MathHelper.EPSILON && entity.getPosition().getX() >= position.getX()){
            return true;
        }

        else if(facing == EntityFacing.Back && entity.getPosition().getY() >= position.getY() && Math.abs(entity.getPosition().getX() - position.getX()) <= MathHelper.EPSILON){
            return true;
        }
        else if(facing == EntityFacing.Front && entity.getPosition().getY() <= position.getY() && Math.abs(entity.getPosition().getX() - position.getX()) <= MathHelper.EPSILON){
            return true;
        }
        return false;
    }

    public boolean isFacingEntity(ITrackableEntity entity){
        if(facing == EntityFacing.Left && Math.abs(entity.getPosition().getY()- position.getY()) <= MathHelper.EPSILON && entity.getPosition().getX() <= position.getX()){
            return true;
        }
        else if(facing == EntityFacing.Right && Math.abs(entity.getPosition().getY() - position.getY()) <= MathHelper.EPSILON && entity.getPosition().getX() >= position.getX()){
            return true;
        }

        else if(facing == EntityFacing.Back && entity.getPosition().getY() >= position.getY() && Math.abs(entity.getPosition().getX() - position.getX()) <= MathHelper.EPSILON){
            return true;
        }
        else if(facing == EntityFacing.Front && entity.getPosition().getY() <= position.getY() && Math.abs(entity.getPosition().getX() - position.getX()) <= MathHelper.EPSILON){

            return true;
        }
        return false;
    }
}
