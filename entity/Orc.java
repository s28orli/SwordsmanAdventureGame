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

public class Orc extends Entity implements ITrackerEntity {
    public static final double MOVEMENT_SPEED = 0.1;
    protected Image walkingImage;
    protected Image attackingImage;
    public static final Point2D[] consecutiveCoords = new Point2D[]{
            new Point(-3, 0), new Point(-2, 0), new Point(-1, 0),
            new Point(3, 0), new Point(2, 0), new Point(1, 0),

            new Point(0, -3), new Point(0, -2), new Point(0, -1),
            new Point(0, 3), new Point(0, 2), new Point(0, 1)
    };
    int animationIndex;
    protected ArrayList<ITrackableEntity> trackedEntities;
    protected ScentPoint currentScent;
    protected ITrackableEntity currentTrackedEntity;

    public Orc(World world) {
        this(world, new Point(0, 0));
    }

    public Orc(World world, Point position) {
        super(world, position);
        trackedEntities = new ArrayList<>();
        size = 1.5;
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
            searchForTarget();
        }
        if (currentScent != null && currentTrackedEntity != null) {
            for (Point2D point : consecutiveCoords) {
                Point2D pos = new Point((int) (point.getX() + position.getX()), (int) (point.getY() + position.getY()));
                ScentPoint scent = currentTrackedEntity.getScentPoint(pos);
                if (scent != null) {
                    if (scent.getLife() > currentScent.getLife()) {
                        currentScent = scent;
                        setAction(EntityAction.Walking);
                        Vector direction = MathHelper.getDirection(position, currentScent.getPosition());
                        direction.normalize();

                        position.setLocation(position.getX() + direction.getX(), position.getY() + direction.getY());
                        return;
                    }
                }
            }
        }


    }

    private void searchForTarget() {
//        double minimumDistance = Double.MAX_VALUE;
//        currentTrackedEntity = null;
//        for (ITrackableEntity entity : trackedEntities) {
//            Point2D pos = entity.getPosition();
//            double dist = pos.distance(position);
//            if (dist < minimumDistance) {
//                minimumDistance = dist;
//                currentTrackedEntity = entity;
//            }
//        }

//        if (currentTrackedEntity != null) {
        for (int y = -Entity.TRACKING_SEARCH_DISTANCE; y <= Entity.TRACKING_SEARCH_DISTANCE; y++) {
            for (int x = -Entity.TRACKING_SEARCH_DISTANCE; x <= Entity.TRACKING_SEARCH_DISTANCE; x++) {
                currentScent = currentTrackedEntity.getScentPoint(new Point((int) (position.getX() + x), (int) (position.getY() + y)));
                if (currentScent != null) {
                    setAction(EntityAction.Walking);
                    Vector direction = MathHelper.getDirection(position, currentScent.getPosition());
                    direction.normalize();

                    position.setLocation(position.getX() + direction.getX(), position.getY() + direction.getY());
                    return;
                }
            }
        }
//        }
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
