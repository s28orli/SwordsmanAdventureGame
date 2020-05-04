package entity;

import tiles.AbstractTile;
import world.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Player: Player class that manages animations and player actions
 *
 * @author Samuil Orlioglu
 * @version 4/27/2020
 */


public class Player extends Entity implements ITrackableEntity {


    private int animationIndex;
    private boolean IsPlayerSwingingSword;
    Image walkingImage;
    Image attackingImage;
    Font healthFont;
    HashMap<Point2D, ScentPoint> scentTrail;

    public Player(World world) {
        super(world);
        healthFont = new Font("Comic Sans", Font.BOLD, 12);

        health = 100;
        damage = 8;
        scentTrail = new HashMap<Point2D, ScentPoint>();
        animationIndex = 0;
        size = 1;
        walkingImageCycle = 7;
        attackingImageCycle = 5;
        File walkingFile = new File("Assets/Player/PlayerWalk.png");
        File attackingFile = new File("Assets/Player/PlayerAttack.png");


        if (walkingFile.exists() && attackingFile.exists()) {
            try {
                walkingImage = ImageIO.read(walkingFile);
                attackingImage = ImageIO.read(attackingFile);

            } catch (IOException e) {
                System.err.println("Did not find all necessary player images!!! Exiting!!!!");
                System.exit(-1);
            }
        }
    }

    @Override
    public void setPosition(Point2D.Double position) {
        if (!IsPlayerSwingingSword)
            this.position = position;
    }


    @Override
    public void setFacing(EntityFacing facing) {
        this.facing = facing;
    }


    @Override
    public void setAction(EntityAction action) {
        if (!IsPlayerSwingingSword) {
            this.action = action;
            if (action == EntityAction.Attacking) {
                animationIndex = 0;
                IsPlayerSwingingSword = true;
            }
        }
    }

    public void draw(Graphics g, JPanel component, boolean drawDebug) {
        if (drawDebug) {
            int rad = 5;
            g.setColor(Color.RED);
            Object[] t = scentTrail.values().toArray();
            for (Object obj : t) {
                ScentPoint point = (ScentPoint) obj;
                g.fillOval((int) (point.getPosition().getX() * AbstractTile.TILE_SIZE) - rad, (int) (point.getPosition().getY() * AbstractTile.TILE_SIZE) - rad, rad * 2, rad * 2);
            }
        }


        Image img;
        int width;
        if (action == EntityAction.Attacking) {
            img = attackingImage;
            width = ATTACKING_ANIMATION_SIZE;
        } else {
            img = walkingImage;
            width = WALKING_ANIMATION_SIZE;
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
        g.setColor(Color.RED);

        String healthString = health + "";
        g.setFont(healthFont);
        g.getFontMetrics().stringWidth(healthString);
        int halfHealthString = g.getFontMetrics().stringWidth(healthString) / 2;
        int halfXPos = (x + dx) / 2;
        int healthStringXPos = halfXPos - halfHealthString;
        int healthStringYPos = y;
        if (action == EntityAction.Attacking) {
            healthStringYPos = y + (int) ((ATTACKING_ANIMATION_SIZE / 3) * size);
        }
        g.drawImage(img, x, y, dx, dy, sx, sy, sdx, sdy, component);
        g.drawString(healthString, healthStringXPos, healthStringYPos);


    }

    @Override
    public void draw(Graphics g, JPanel component) {
        draw(g, component, false);
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

            updateScentTrail();
            if ((double) (time) / ANIMATION_TIME_LENGTH > 1) {
                time = 0;
                animationIndex += 1;

                if (animationIndex > walkingImageCycle && action == EntityAction.Walking) {
                    animationIndex = 1;
                    IsPlayerSwingingSword = false;

                }
                if (animationIndex > attackingImageCycle && action == EntityAction.Attacking) {
                    animationIndex = 0;
                    action = EntityAction.Standing;
                    IsPlayerSwingingSword = false;
                }
            }


        }
    }

    private synchronized void updateScentTrail() {
        Stack<ScentPoint> toRemove = new Stack<>();
        for (Point2D point : scentTrail.keySet()) {
            scentTrail.get(point).tick();
            if (scentTrail.get(point).getLife() <= 0) {
                toRemove.push(scentTrail.get(point));
            }
        }

        while (!toRemove.empty()) {
            ScentPoint p = toRemove.pop();
            scentTrail.remove(p.getPosition());
            world.removeScent(p.getPosition());

        }

        Point2D pos = new Point((int) position.getX(), (int) position.getY());
        ScentPoint s = new ScentPoint(pos, 200, this);
        scentTrail.put(pos, s);
        world.addScent(pos, s);
    }

    @Override
    public Collection<ScentPoint> getScentPoints() {
        return scentTrail.values();
    }



    @Override
    public synchronized ScentPoint getScentPoint(Point2D position) {
        Object[] t = scentTrail.values().toArray();
        for (Object obj : t) {
            ScentPoint point = (ScentPoint) obj;
            if (point.getPosition().equals(position)) {
                return point;
            }
        }
        return null;
    }
}
