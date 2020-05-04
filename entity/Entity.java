/**
 * Entity: Description
 *
 * @author Samuil Orlioglu
 * @version 4/30/2020
 */

package entity;


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

    /**
     * Get Entity's health.
     *
     * @return Entity's health.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Set Entity's health.
     *
     * @param health New entity's health.
     */
    public void setHealth(int health) {
        this.health = health;
    }


    public Entity(World world, Point2D.Double position) {
        this.world = world;
        facing = EntityFacing.Front;
        action = EntityAction.Standing;
        this.position = position;
    }

    /**
     * Get Entity's position.
     *
     * @return Entity's Position.
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * Set Entity's position.
     *
     * @param position New  entity's Position.
     */
    public void setPosition(Point2D.Double position) {
        this.position = position;
    }

    /**
     * Get Entity's facing
     *
     * @return Entity's facing
     */
    public EntityFacing getFacing() {
        return facing;
    }

    /**
     * Set Entity's facing
     *
     * @param facing New entity's facing
     */
    public void setFacing(EntityFacing facing) {
        this.facing = facing;
    }

    /**
     * Get Entity's action
     *
     * @return Entity's action.
     */
    public EntityAction getAction() {
        return action;
    }

    /**
     * Set Entity's action
     *
     * @param action New entity's action.
     */
    public void setAction(EntityAction action) {
        this.action = action;
    }

    /**
     * Check if this Entity is colliding with another Entity.
     *
     * @param obj Entity to check collision against.
     * @return True if colliding, false otherwise.
     */
    public boolean isCollision(Entity obj) {
        if (position.distance(obj.position) < size * 3) {
            return true;
        }
        return false;
    }

    /**
     * Check if this Entity is colliding with another Entity.
     *
     * @param obj Entity to check collision against.
     * @return True if colliding, false otherwise.
     */
    public boolean isCollision(ITrackableEntity obj) {
        if (position.distance(obj.getPosition()) < size * 3) {
            return true;
        }
        return false;
    }

    /**
     * Draw the Entity
     *
     * @param g         Graphics object to draw to
     * @param component JPanel to draw to.
     */
    public abstract void draw(Graphics g, JPanel component);

    /**
     * Draw the Entity
     *
     * @param g         Graphics object to draw to
     * @param component JPanel to draw to.
     * @param drawDebug boolean to draw debug info.
     */
    public abstract void draw(Graphics g, JPanel component, boolean drawDebug);

    /**
     * Get a facing from a direction Vector
     *
     * @param vec Direction Vector, must consist of 1, -1, or 0. ex. (0, -1), (1, 0) etc.
     * @return Entity facing
     */
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

    /**
     * Get the Entity's World.
     *
     * @return Entity's World
     */
    public World getWorld() {
        return world;
    }

    /**
     * Get Entity's Damage amount.
     *
     * @return Entity's Damage amount.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Get Entity's Size.
     *
     * @return Entity's.
     */
    public double getSize() {
        return size;
    }

    /**
     * Set the Entity's damage amount.
     *
     * @param damage New damage amount.
     **/
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Set the Entity's size.
     *
     * @param size New size.
     **/
    public void setSize(double size) {
        this.size = size;
    }

    /**
     * Check if entity is facing another entity.
     *
     * @param entity Entity to check against.
     * @return True if facing, false otherwise.
     */
    public boolean isFacingEntity(Entity entity) {
        if (facing == EntityFacing.Left && Math.abs(entity.getPosition().getY() - position.getY()) <= MathHelper.EPSILON && entity.getPosition().getX() <= position.getX()) {
            return true;
        } else if (facing == EntityFacing.Right && Math.abs(entity.getPosition().getY() - position.getY()) <= MathHelper.EPSILON && entity.getPosition().getX() >= position.getX()) {
            return true;
        } else if (facing == EntityFacing.Back && entity.getPosition().getY() >= position.getY() && Math.abs(entity.getPosition().getX() - position.getX()) <= MathHelper.EPSILON) {
            return true;
        } else if (facing == EntityFacing.Front && entity.getPosition().getY() <= position.getY() && Math.abs(entity.getPosition().getX() - position.getX()) <= MathHelper.EPSILON) {
            return true;
        }
        return false;
    }

    /**
     * Check if entity is facing another entity.
     *
     * @param entity Entity to check against.
     * @return True if facing, false otherwise.
     */
    public boolean isFacingEntity(ITrackableEntity entity) {
        if (facing == EntityFacing.Left && Math.abs(entity.getPosition().getY() - position.getY()) <= MathHelper.EPSILON && entity.getPosition().getX() <= position.getX()) {
            return true;
        } else if (facing == EntityFacing.Right && Math.abs(entity.getPosition().getY() - position.getY()) <= MathHelper.EPSILON && entity.getPosition().getX() >= position.getX()) {
            return true;
        } else if (facing == EntityFacing.Back && entity.getPosition().getY() >= position.getY() && Math.abs(entity.getPosition().getX() - position.getX()) <= MathHelper.EPSILON) {
            return true;
        } else if (facing == EntityFacing.Front && entity.getPosition().getY() <= position.getY() && Math.abs(entity.getPosition().getX() - position.getX()) <= MathHelper.EPSILON) {

            return true;
        }
        return false;
    }
}
