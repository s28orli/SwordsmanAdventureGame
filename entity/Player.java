package entity;

import tiles.AbstractTile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

/**
 * Player: Player class that manages animations and player actions
 *
 * @author Samuil Orlioglu
 * @version 4/27/2020
 */


public class Player extends Entity {


    private int animationIndex;
    private boolean IsPlayerSwingingSword;
    Image walkingImage;
    Image attackingImage;

    public Player() {
        super();
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
    public void setPosition(Point2D position) {
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

    public void draw(Graphics g, JPanel component) {

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
}
