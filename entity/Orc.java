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


    public static final double MOVEMENT_SPEED = 0.2;
    public static final double TARGET_DETECTION_RANGE = 5;
    protected BufferedImage walkingImage;
    protected BufferedImage attackingImage;
    protected BufferedImage hurtingImage;
    protected static final int HURTING_DURATION = 3;
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

    @Override
    public void draw(Graphics g, JPanel component) {
        BufferedImage img;
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
            if (currentScent != null) {
                g.setColor(Color.CYAN);
                g.drawLine((int) (position.getX() * AbstractTile.TILE_SIZE), (int) (position.getY() * AbstractTile.TILE_SIZE), (int) (currentScent.getPosition().getX() * AbstractTile.TILE_SIZE), (int) (currentScent.getPosition().getY() * AbstractTile.TILE_SIZE));
            }
        }
    }


    @Override
    public void run() {
        int time = 0;
        int idleTime = 0;
        while (true) {
            try {
                sleep(TIME_DELAY);
                time += TIME_DELAY;
                idleTime++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            updateTracking();

            // If not tracking anyone
            if (currentTrackedEntity == null) {
                action = idleFunction(idleTime);
                if (action == EntityAction.Walking) {
                    Vector dir = getIdleStateWalkingDirection(idleTime);
                    position = new Point2D.Double(position.getX() + dir.getX(), position.getY() + dir.getY());
                    facing = Entity.getFacingFromVector(dir);
                }
            }
            // If tracker someone and colliding with them, then attack.
            else {
                if (isCollision(currentTrackedEntity)) ;
                setAction(EntityAction.Attacking);
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

    private void updateTracking() {

        if (currentScent == null) {

            for (ITrackableEntity ent : trackedEntities) {
                if (ent.getPosition().distance(position) < TARGET_DETECTION_RANGE) {
                    currentTrackedEntity = ent;
                }
            }
            if (currentTrackedEntity == null) {
                return;
            }


            double minimumDistance = Double.MAX_VALUE;
            for (Object obj : currentTrackedEntity.getScentPoints().toArray()) {
                ScentPoint scent = (ScentPoint) obj;
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
        if (hurtingTime == -1) {
            this.action = action;
            if (action == EntityAction.Attacking) {
                animationIndex = 0;
            }
            if (action == EntityAction.Hurting) {
                hurtingTime = 0;
            }
        }

    }

    @Override
    public void addEntityToTrack(ITrackableEntity entity) {
        trackedEntities.add(entity);
    }

    private EntityAction idleFunction(int time) {
        double val = Math.sin(time / idleActionFreqDenominator);
        if (val > 0) {
            return EntityAction.Walking;
        }
        return EntityAction.Standing;
    }

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
