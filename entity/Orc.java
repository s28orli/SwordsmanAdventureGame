/**
 * Orc: Orc class that manages animations and orc actions
 *
 * @author Samuil Orlioglu
 * @version 4/30/2020
 */

package entity;

import tiles.AbstractTile;
import util.MathHelper;
import util.Vector;
import world.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Orc extends Entity implements ITrackerEntity {
    private enum TrackingType {
        LeftRight, FrontBack
    }


    public static final double MOVEMENT_SPEED = 0.1;
    public static final double TARGET_DETECTION_RANGE = 10;
    public static final double ATTACK_RANGE_MIN = 1;
    public static final double ATTACK_RANGE_MAX = 3;
    protected static final int HURTING_DURATION = 3;
    protected static final int TARGET_ESCAPE_RANGE = 30;

    protected BufferedImage walkingImage;
    protected BufferedImage attackingImage;
    protected BufferedImage hurtingImage;
    protected int hurtingTime = -1;
    protected int hurtingIndex = 0;


    int animationIndex;
    protected ArrayList<ITrackableEntity> trackedEntities;
    protected ScentPoint currentScent;
    protected ITrackableEntity currentTrackedEntity;
    protected TrackingType trackingType;
    protected double idleWalkFreqDenominator;
    protected double idleActionFreqDenominator;


    public Orc(World world, JPanel component) {
        this(world, new Point2D.Double(0, 0), component);
    }

    public Orc(World world, Point2D.Double position, JPanel component) {
        super(world, position);
        trackedEntities = new ArrayList<>();
        size = 1.5;
        health = 120;
        damage = 5;
        Random random = new Random();
        switch (random.nextInt(2)) {
            case 0:
                trackingType = TrackingType.FrontBack;
                break;
            case 1:
                trackingType = TrackingType.LeftRight;
                break;
        }

        idleWalkFreqDenominator = random.nextDouble() * random.nextInt(10) + 10;
        idleActionFreqDenominator = random.nextDouble() * random.nextInt(10) + 5;

        File walkingFile = new File("Assets/Orc/OrcWalk.png");
        File attackingFile = new File("Assets/Orc/OrcAttack.png");

        if (walkingFile.exists() && attackingFile.exists()) {
            try {
                walkingImage = ImageIO.read(walkingFile);
                attackingImage = ImageIO.read(attackingFile);
            } catch (IOException e) {
                System.err.println("Did not find all necessary orc images!!! Exiting!!!!");
                System.exit(-1);
            }
        }

        hurtingImage = new BufferedImage(walkingImage.getWidth(), walkingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gbi = hurtingImage.createGraphics();
        gbi.drawImage(walkingImage, 0, 0, hurtingImage.getWidth(), hurtingImage.getHeight(), component);
        gbi.setColor(Color.RED);

        gbi.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));

        gbi.fillRect(0, 0, hurtingImage.getWidth(), hurtingImage.getHeight());


    }

    /**
     * Draw the Orc
     * @param g Graphics object to draw to
     * @param component JPanel to draw to.
     */
    @Override
    public void draw(Graphics g, JPanel component) {
        draw(g, component, false);
    }

    /**
     * Draw the Orc
     * @param g Graphics object to draw to
     * @param component JPanel to draw to.
     * @param drawDebug boolean to draw debug info.
     */
    @Override
    public void draw(Graphics g, JPanel component, boolean drawDebug) {
        Image img;
        int width = WALKING_ANIMATION_SIZE;
        if (action == EntityAction.Attacking) {
            img = attackingImage;
        } else {
            img = walkingImage;
        }

        int index = animationIndex;

        if (action == EntityAction.Standing || action == EntityAction.Hurting) {
            index = 0;
        }


        int halfWidth = width / 2;

        // Panel location coordinates
        int x = (int) (position.getX() * AbstractTile.TILE_SIZE - (halfWidth * size));
        int y = (int) (position.getY() * AbstractTile.TILE_SIZE - (halfWidth * size));
        int dx = (int) (x + (width * size));
        int dy = (int) (y + (width * size));


        // Image location coordinates
        int sx = index * width;
        int sy = facing.value * width;
        int sdx = sx + width;
        int sdy = sy + width;


        if (action == EntityAction.Hurting && hurtingIndex % 2 == 0) {
            g.drawImage(hurtingImage, x, y, dx, dy, sx, sy, sdx, sdy, component);

        } else {

            g.drawImage(img, x, y, dx, dy, sx, sy, sdx, sdy, component);
        }
        if (drawDebug) {
            drawDebugInfo(g);
        }
    }

    protected void drawDebugInfo(Graphics g) {
        if (currentTrackedEntity != null) {
            g.setColor(Color.CYAN);
            g.drawLine((int) (position.getX() * AbstractTile.TILE_SIZE), (int) (position.getY() * AbstractTile.TILE_SIZE), (int) (currentTrackedEntity.getPosition().getX() * AbstractTile.TILE_SIZE), (int) (currentTrackedEntity.getPosition().getY() * AbstractTile.TILE_SIZE));
        }
    }

    /**
     * Run to Orc
     */
    @Override
    public void run() {
        int time = 0;
        int idleTime = 0;
        int trackTimeout = 0;
        while (true) {
            try {
                sleep(TIME_DELAY);
                time += TIME_DELAY;
                idleTime++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            updateTracking(trackTimeout == 0);

            if (trackTimeout > 0) {
                trackTimeout--;
            }
            // If not tracking anyone
            if (currentTrackedEntity == null) {
                action = idleFunction(idleTime);
                if (action == EntityAction.Walking) {
                    Vector dir = getIdleStateWalkingDirection(idleTime);
                    position = new Point2D.Double(position.getX() + dir.getX(), position.getY() + dir.getY());
                    facing = Entity.getFacingFromVector(dir);
                }
            } else {
                // If target is out of range, ignore it
                if (currentTrackedEntity.getPosition().distance(position) > TARGET_ESCAPE_RANGE) {
                    currentTrackedEntity = null;
                } else
                    // If tracking someone and colliding with them, then attack.
                    if (isFacingEntity(currentTrackedEntity)) {
                        double distance = position.distance(currentTrackedEntity.getPosition());
                        if (distance > getAttackRangeMin() && distance <= getAttackRangeMax()) {
                            setAction(EntityAction.Attacking);
                        } else if (distance < getAttackRangeMin()) {
                            setAction(EntityAction.Attacking);
                            trackTimeout = 10; // Give player some time to escape a pile up.

                        }

                    }
            }
            // Make sure animations are not lasting too long
            if ((double) (time) / ANIMATION_TIME_LENGTH > 1) {
                time = 0;
                animationIndex += 1;

                if (animationIndex > walkingImageCycle && action == EntityAction.Walking) {
                    animationIndex = 1;
                }

                if (animationIndex > attackingImageCycle && action == EntityAction.Attacking) {
                    animationIndex = 0;
                    action = EntityAction.Standing;
                }

                if (action == EntityAction.Hurting) {
                    hurtingTime++;
                    hurtingIndex++;
                }
                if (hurtingTime > HURTING_DURATION) {
                    hurtingTime = -1;
                    action = EntityAction.Standing;
                    hurtingIndex = 0;
                }
            }


        }
    }

    /**
     * Update the Orc's targeting actions
     * @param shouldMove should the orc move toward target
     */
    private void updateTracking(boolean shouldMove) {

        if (currentScent == null) {

            for (ITrackableEntity ent : trackedEntities) {
                if (ent.getPosition().distance(position) < TARGET_DETECTION_RANGE) {
                    currentTrackedEntity = ent;
                }
            }
            if (currentTrackedEntity == null) {
                return;
            }

        }
        if (shouldMove)
            if (currentTrackedEntity != null) {
                Vector v = MathHelper.getDirection(position, currentTrackedEntity.getPosition());

                double moveX = MOVEMENT_SPEED;
                double moveY = MOVEMENT_SPEED;

                // Avoid Orc position flopping bug
                if (Math.abs(currentTrackedEntity.getPosition().getX() - position.getX()) < MOVEMENT_SPEED) {
                    moveX = Math.abs(currentTrackedEntity.getPosition().getX() - position.getX());
                }
                if (Math.abs(currentTrackedEntity.getPosition().getY() - position.getY()) < MOVEMENT_SPEED) {
                    moveY = Math.abs(currentTrackedEntity.getPosition().getY() - position.getY());
                }
                v.normalize();

                if (trackingType == TrackingType.LeftRight) {
                    if (Math.abs(v.getX()) < MathHelper.EPSILON) {
                        if (v.getY() < 0) {
                            position = new Point2D.Double(position.getX(), position.getY() - moveY);
                            setFacing(EntityFacing.Back);
                        } else if (v.getY() > 0) {
                            position = new Point2D.Double(position.getX(), position.getY() + moveY);
                            setFacing(EntityFacing.Front);

                        }
                    } else if (v.getX() < 0) {
                        position = new Point2D.Double(position.getX() - moveX, position.getY());
                        setFacing(EntityFacing.Left);
                    } else if (v.getX() > 0) {
                        position = new Point2D.Double(position.getX() + moveX, position.getY());
                        setFacing(EntityFacing.Right);
                    }

                } else if (trackingType == TrackingType.FrontBack) {
                    if (Math.abs(v.getY()) < MathHelper.EPSILON) {
                        if (v.getX() < 0) {
                            position = new Point2D.Double(position.getX() - moveX, position.getY());
                            setFacing(EntityFacing.Left);
                        } else if (v.getX() > 0) {
                            position = new Point2D.Double(position.getX() + moveX, position.getY());
                            setFacing(EntityFacing.Right);

                        }
                    } else if (v.getY() < 0) {
                        position = new Point2D.Double(position.getX(), position.getY() - moveY);
                        setFacing(EntityFacing.Back);
                    } else if (v.getY() > 0) {
                        position = new Point2D.Double(position.getX(), position.getY() + moveY);
                        setFacing(EntityFacing.Front);
                    }
                }

                setAction(EntityAction.Walking);
            }
    }


    /**
     * Set the current action of Orc
     * @param action New action
     */
    @Override
    public void setAction(EntityAction action) {
        if (hurtingTime == -1 && this.action != EntityAction.Attacking) {
            this.action = action;
            if (action == EntityAction.Attacking) {
                animationIndex = 0;
            }
            if (action == EntityAction.Hurting) {
                hurtingTime = 0;
            }
        }

    }

    /**
     * Adds a new Entity that Orc will target
     * @param entity targeted entity.
     */
    @Override
    public void addEntityToTrack(ITrackableEntity entity) {
        trackedEntities.add(entity);
    }

    /**
     * Get the Orc's Largest attack Range
     */
    @Override
    public double getAttackRangeMax() {
        return ATTACK_RANGE_MAX;
    }

    /**
     * Get the Orc's Smallest attack Range
     */
    @Override
    public double getAttackRangeMin() {
        return ATTACK_RANGE_MIN;
    }

    /**
     * Uses a function to generate an action for Orc to do while not targeting entities.
     * @param time Current Time
     * @return Action for Orc to do
     */
    private EntityAction idleFunction(int time) {
        double val = Math.cos(time / idleActionFreqDenominator);
        if (val > 0) {
            return EntityAction.Walking;
        }
        return EntityAction.Standing;
    }

    /**
     * Uses a function to generate an walking direction for Orc to do while not targeting entities.
     * @param time Current Time
     * @return Direction for Orc to walk toward.
     */
    private Vector getIdleStateWalkingDirection(int time) {
        double move = 0;
        double val = Math.sin(time / idleWalkFreqDenominator);
        if (val < 0) {
            move = -MOVEMENT_SPEED;
        } else if (val >= 0) {
            move = MOVEMENT_SPEED;
        }
        if (trackingType == TrackingType.FrontBack) {
            return new Vector(0, move);
        } else return new Vector(move, 0);
    }


}
