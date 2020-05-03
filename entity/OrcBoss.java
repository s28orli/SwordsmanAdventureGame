/**
 * OrcBoss: Description
 *
 * @author Samuil Orlioglu
 * @version 4/30/2020
 */

package entity;

import tiles.AbstractTile;
import world.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class OrcBoss extends Orc {

    public OrcBoss(World world, Point position) {
        super(world, position);
        this.position = position;
        size = 2;
        File walkingFile = new File("Assets/Orc/Boss/OrcBossWalk.png");
        File attackingFile = new File("Assets/Orc/Boss/OrcBossAttack.png");
        walkingImageCycle = 7;
        attackingImageCycle = 5;
        if (walkingFile.exists() && attackingFile.exists()) {
            try {
                walkingImage = ImageIO.read(walkingFile);
                attackingImage = ImageIO.read(attackingFile);
            } catch (IOException e) {
                System.err.println("Did not find all necessary orc boss images!!! Exiting!!!!");
                System.exit(-1);
            }
        }
    }

    public OrcBoss(World world){
        this(world, new Point(0, 0));
    }

    @Override
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
}
