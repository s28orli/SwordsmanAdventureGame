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

    Image walkingImage;
    Image attackingImage;



    int animationIndex;

    public Orc() {
        super();
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

        System.out.println(sx + " " + sy + " " + sdx + " " + sdy);
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
                }

                if (animationIndex > attackingImageCycle && action == EntityAction.Attacking) {
                    animationIndex = 0;
                    action = EntityAction.Standing;
                }

            }


        }
    }

    @Override
    public void setAction(EntityAction action) {
        this.action = action;
        if (action == EntityAction.Attacking) {
            animationIndex = 0;
        }

    }
}
