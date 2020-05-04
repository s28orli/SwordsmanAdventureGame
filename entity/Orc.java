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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Orc extends Entity implements ITrackerEntity {
    private enum TrackingType {
        LeftRight, FrontBack
    }

    public static final double MOVEMENT_SPEED = 0.2;
    protected Image walkingImage;
    protected Image attackingImage;

    int animationIndex;
    protected ArrayList<ITrackableEntity> trackedEntities;
    protected ScentPoint currentScent;
    protected ITrackableEntity currentTrackedEntity;
    protected TrackingType trackingType;

    public Orc(World world) {
        this(world, new Point2D.Double(0, 0));
    }

    public Orc(World world, Point2D.Double position) {
        super(world, position);
        trackedEntities = new ArrayList<>();
        size = 1.5;
        Random random = new Random();
        switch (random.nextInt(2)) {
            case 0:
                trackingType = TrackingType.FrontBack;
                break;
            case 1:
                trackingType = TrackingType.LeftRight;
                break;
        }
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


    }

    @Override
    public void draw(Graphics g, JPanel component) {
        draw(g, component, false);
    }

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

        if (action == EntityAction.Standing) {
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

        g.drawImage(img, x, y, dx, dy, sx, sy, sdx, sdy, component);
        if (drawDebug) {
            if (currentScent != null) {
                g.setColor(Color.CYAN);
                g.drawLine((int) (position.getX() * AbstractTile.TILE_SIZE), (int) (position.getY() * AbstractTile.TILE_SIZE), (int) (currentScent.getPosition().getX() * AbstractTile.TILE_SIZE), (int) (currentScent.getPosition().getY() * AbstractTile.TILE_SIZE));
            }
        }
    }

    @Override
    public void run() {
        int time = 0;
        while (true) {
            try {
                sleep(TIME_DELAY);
                time += TIME_DELAY;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            updateTracking();
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

            }


        }
    }

    private void updateTracking() {

        if (currentScent == null) {

            if (trackedEntities.size() > 0) {
                currentTrackedEntity = trackedEntities.get(0);
            } else {
                return;
            }
            double minimumDistance = Double.MAX_VALUE;
            for (ScentPoint scent : currentTrackedEntity.getScentPoints()) {
                double dist = position.distance(scent.getPosition());
                if (dist < minimumDistance) {
                    minimumDistance = dist;
                    currentScent = scent;
                }
            }
        } else {
            loop1:
            for (int i = -TRACKING_SEARCH_DISTANCE; i <= TRACKING_SEARCH_DISTANCE; i++) {
                loop2:
                for (Point2D cons : MathHelper.consecutiveCoords) {
                    Point2D p = MathHelper.mult(cons, i);
                    Point2D pos = new Point2D.Double(p.getX() + currentScent.getPosition().getX(), p.getY() + currentScent.getPosition().getY());
                    ScentPoint scentPoint = currentTrackedEntity.getScentPoint(pos);
                    if (scentPoint != null) {
                        if (scentPoint.getLife() >= currentScent.getLife()) {
                            currentScent = scentPoint;
                            break loop1;
                        }
                    }
                }
            }
        }

        if (currentScent != null) {
            Vector v = MathHelper.getDirection(position, currentScent.getPosition());
            v.normalize();

            if (trackingType == TrackingType.LeftRight) {
                if (Math.abs(v.getX()) < 0.01) {
                    if (v.getY() < 0) {
                        position = new Point2D.Double(position.getX(), position.getY() - MOVEMENT_SPEED);
                        setFacing(EntityFacing.Back);
                    } else if (v.getY() > 0) {
                        position = new Point2D.Double(position.getX(), position.getY() + MOVEMENT_SPEED);
                        setFacing(EntityFacing.Front);

                    }
                } else if (v.getX() < 0) {
                    position = new Point2D.Double(position.getX() - MOVEMENT_SPEED, position.getY());
                    setFacing(EntityFacing.Left);
                } else if (v.getX() > 0) {
                    position = new Point2D.Double(position.getX() + MOVEMENT_SPEED, position.getY());
                    setFacing(EntityFacing.Right);
                }
            } else if (trackingType == TrackingType.FrontBack) {
                if (Math.abs(v.getY()) < 0.01) {
                    if (v.getX() < 0) {
                        position = new Point2D.Double(position.getX() - MOVEMENT_SPEED, position.getY());
                        setFacing(EntityFacing.Left);
                    } else if (v.getX() > 0) {
                        position = new Point2D.Double(position.getX() + MOVEMENT_SPEED, position.getY());
                        setFacing(EntityFacing.Right);

                    }
                } else if (v.getY() < 0) {
                    position = new Point2D.Double(position.getX(), position.getY() - MOVEMENT_SPEED);
                    setFacing(EntityFacing.Back);
                } else if (v.getY() > 0) {
                    position = new Point2D.Double(position.getX(), position.getY() + MOVEMENT_SPEED);
                    setFacing(EntityFacing.Front);
                }
            }

            setAction(EntityAction.Walking);
        }
    }



    @Override
    public void setAction(EntityAction action) {
        this.action = action;
        if (action == EntityAction.Attacking) {
            animationIndex = 0;
        }

    }

    @Override
    public void addEntityToTrack(ITrackableEntity entity) {
        trackedEntities.add(entity);
    }
}
