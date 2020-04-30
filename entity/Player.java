package entity;

import tiles.AbstractTile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

/**
 * player.Player: Player class that manages animations and player actions
 *
 * @author Samuil Orlioglu
 * @version 4/27/2020
 */


public class Player extends Entity {

    private static final int WALKING_IMAGE_WIDTH = 64;
    private static final int ATTACKING_HORIZONTAL_IMAGE_WIDTH = 115;
    private static final int ATTACKING_VERTICAL_IMAGE_WIDTH = 95;

    private static final int ANIMATION_TIME_LENGTH = 100;
    private static final int HORIZONTAL_TEXTURE_OFFSET = 17;
    private static final int VERTICAL_TEXTURE_OFFSET = 50;
    private static final int WALKING_IMAGE_HEIGHT = 72;
    private static final int TIME_DELAY = 50;
    private int animationIndex;
    private boolean IsPlayerSwingingSword;


    Image walkingFront;
    Image walkingBack;
    Image walkingLeft;
    Image walkingRight;

    Image attackingFront;
    Image attackingBack;
    Image attackingLeft;
    Image attackingRight;

    public Player() {
        super();
        animationIndex = 0;
        File walkingFrontFile = new File("Assets/Player/WalkingFront.png");
        File walkingBackFile = new File("Assets/Player/WalkingBack.png");
        File walkingLeftFile = new File("Assets/Player/WalkingLeft.png");
        File walkingRightFile = new File("Assets/Player/WalkingRight.png");

        File attackingFrontFile = new File("Assets/Player/AttackFront.png");
        File attackingBackFile = new File("Assets/Player/AttackBack.png");
        File attackingLeftFile = new File("Assets/Player/AttackLeft.png");
        File attackingRightFile = new File("Assets/Player/AttackRight.png");

        if (walkingBackFile.exists() &&
                walkingFrontFile.exists() &&
                walkingRightFile.exists() &&
                walkingLeftFile.exists() &&
                attackingFrontFile.exists() &&
                attackingBackFile.exists() &&
                attackingLeftFile.exists() &&
                attackingRightFile.exists()) {
            try {
                walkingFront = ImageIO.read(walkingFrontFile);
                walkingBack = ImageIO.read(walkingBackFile);
                walkingLeft = ImageIO.read(walkingLeftFile);
                walkingRight = ImageIO.read(walkingRightFile);

                attackingFront = ImageIO.read(attackingFrontFile);
                attackingBack = ImageIO.read(attackingBackFile);
                attackingLeft = ImageIO.read(attackingLeftFile);
                attackingRight = ImageIO.read(attackingRightFile);

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
        int index = animationIndex;
        int width = WALKING_IMAGE_WIDTH;
        Image img = null;

        switch (facing) {
            case Front:
                if (action == EntityAction.Walking || action == EntityAction.Standing)
                    img = walkingFront;
                else
                    img = attackingFront;
                break;

            case Back:
                if (action == EntityAction.Walking || action == EntityAction.Standing)
                    img = walkingBack;
                else
                    img = attackingBack;
                break;

            case Left:
                if (action == EntityAction.Walking || action == EntityAction.Standing)
                    img = walkingLeft;
                else
                    img = attackingLeft;
                break;

            case Right:
                if (action == EntityAction.Walking || action == EntityAction.Standing)
                    img = walkingRight;
                else
                    img = attackingRight;
                break;
        }

        if (action == EntityAction.Standing) {
            index = 0;

        }
        // Set proper width
        if (action == EntityAction.Attacking) {
            if ((facing == EntityFacing.Left || facing == EntityFacing.Right))
                width = ATTACKING_HORIZONTAL_IMAGE_WIDTH;
            if ((facing == EntityFacing.Front || facing == EntityFacing.Back))
                width = ATTACKING_VERTICAL_IMAGE_WIDTH;
        }


        // Panel location coordinates
        int x = (int) (position.getX() * AbstractTile.TILE_SIZE);
        int y = (int) (position.getY() * AbstractTile.TILE_SIZE);
        int dx = (x + width);
        int dy = (y + width);

        // If Attacking and facing left, adjust image bounds for offset left attack image
        if (action == EntityAction.Attacking && facing == EntityFacing.Left) {
            x = (int) (position.getX() * AbstractTile.TILE_SIZE) - WALKING_IMAGE_WIDTH + HORIZONTAL_TEXTURE_OFFSET;
            dx = (x + width);
        }
        // If Attacking and facing back, adjust image bounds for offset back attack image
        if (action == EntityAction.Attacking && facing == EntityFacing.Back) {
            y = (int) (position.getY() * AbstractTile.TILE_SIZE) - WALKING_IMAGE_HEIGHT + VERTICAL_TEXTURE_OFFSET;
            dy = (y + width);
        }

        // Image location coordinates
        int sx = index * width;
        int sy = 0;
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

                if (animationIndex > 8 && action == EntityAction.Walking) {
                    animationIndex = 1;
                    IsPlayerSwingingSword = false;

                }
                if (animationIndex > 5 && action == EntityAction.Attacking) {
                    animationIndex = 0;
                    action = EntityAction.Standing;
                    IsPlayerSwingingSword = false;
                }
            }


        }
    }
}
