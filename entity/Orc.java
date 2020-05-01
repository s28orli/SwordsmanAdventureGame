/**
 * Orc: Orc class that manages animations and orc actions
 *
 * @author Samuil Orlioglu
 * @version 4/30/2020
 */

package entity;

import tiles.AbstractTile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Orc extends Entity {
    private static final int WALKING_IMAGE_WIDTH = 64;

    Image walkingFront;
    Image walkingBack;
    Image walkingLeft;
    Image walkingRight;

    Image attackingFront;
    Image attackingBack;
    Image attackingLeft;
    Image attackingRight;


    int animationIndex;

    public Orc() {
        super();
        size = 1.5;
        File walkingFrontFile = new File("Assets/Orc/WalkingFront.png");
        File walkingBackFile = new File("Assets/Orc/WalkingBack.png");
        File walkingLeftFile = new File("Assets/Orc/WalkingLeft.png");
        File walkingRightFile = new File("Assets/Orc/WalkingRight.png");

        File attackingFrontFile = new File("Assets/Orc/AttackFront.png");
        File attackingBackFile = new File("Assets/Orc/AttackBack.png");
        File attackingLeftFile = new File("Assets/Orc/AttackLeft.png");
        File attackingRightFile = new File("Assets/Orc/AttackRight.png");

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
                System.err.println("Did not find all necessary orc images!!! Exiting!!!!");
                System.exit(-1);
            }
        }
    }

    @Override
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


        // Panel location coordinates
        int x = (int) (position.getX() * AbstractTile.TILE_SIZE);
        int y = (int) (position.getY() * AbstractTile.TILE_SIZE);
        int dx = (int)(x + width * size);
        int dy = (int)(y + width* size);

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

                }

                if(animationIndex > 7 && action == EntityAction.Attacking){
                    animationIndex = 0;
                    action = EntityAction.Standing;
                }

            }


        }
    }
}