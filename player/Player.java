package player;

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


public class Player extends Thread {
    private PlayerFacing facing;
    private PlayerAction action;
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
    Point2D position; // Player's position in Tiles


    Image walkingFront;
    Image walkingBack;
    Image walkingLeft;
    Image walkingRight;

    Image attackingFront;
    Image attackingBack;
    Image attackingLeft;
    Image attackingRight;

    public Player() {
        facing = PlayerFacing.Front;
        action = PlayerAction.Standing;
        position = new Point(0, 0);
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

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D.Double position) {
        if (!IsPlayerSwingingSword)
            this.position = position;
    }

    public PlayerFacing getFacing() {
        return facing;
    }

    public void setFacing(PlayerFacing facing) {
        this.facing = facing;
    }

    public PlayerAction getAction() {
        return action;
    }

    public void setAction(PlayerAction action) {
        if (!IsPlayerSwingingSword) {
            this.action = action;
            if (action == PlayerAction.Attacking) {
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
                if (action == PlayerAction.Walking || action == PlayerAction.Standing)
                    img = walkingFront;
                else
                    img = attackingFront;
                break;

            case Back:
                if (action == PlayerAction.Walking || action == PlayerAction.Standing)
                    img = walkingBack;
                else
                    img = attackingBack;
                break;

            case Left:
                if (action == PlayerAction.Walking || action == PlayerAction.Standing)
                    img = walkingLeft;
                else
                    img = attackingLeft;
                break;

            case Right:
                if (action == PlayerAction.Walking || action == PlayerAction.Standing)
                    img = walkingRight;
                else
                    img = attackingRight;
                break;
        }

        if (action == PlayerAction.Standing) {
            index = 0;

        }
        // Set proper width
        if (action == PlayerAction.Attacking) {
            if ((facing == PlayerFacing.Left || facing == PlayerFacing.Right))
                width = ATTACKING_HORIZONTAL_IMAGE_WIDTH;
            if ((facing == PlayerFacing.Front || facing == PlayerFacing.Back))
                width = ATTACKING_VERTICAL_IMAGE_WIDTH;
        }


        // Panel location coordinates
        int x = (int) (position.getX() * AbstractTile.TILE_SIZE);
        int y = (int) (position.getY() * AbstractTile.TILE_SIZE);
        int dx = (x + width);
        int dy = (y + width);

        // If Attacking and facing left, adjust image bounds for offset left attack image
        if (action == PlayerAction.Attacking && facing == PlayerFacing.Left) {
            x = (int) (position.getX() * AbstractTile.TILE_SIZE) - WALKING_IMAGE_WIDTH + HORIZONTAL_TEXTURE_OFFSET;
            dx = (x + width);
        }
        // If Attacking and facing back, adjust image bounds for offset back attack image
        if (action == PlayerAction.Attacking && facing == PlayerFacing.Back) {
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

                if (animationIndex > 8 && action == PlayerAction.Walking) {
                    animationIndex = 1;
                    IsPlayerSwingingSword = false;

                }
                if (animationIndex > 5 && action == PlayerAction.Attacking) {
                    animationIndex = 0;
                    action = PlayerAction.Standing;
                    IsPlayerSwingingSword = false;
                }
            }


        }
    }
}
