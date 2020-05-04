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
import java.awt.image.BufferedImage;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

public class OrcBoss extends Orc {


    public OrcBoss(World world, Point2D.Double position, JPanel component) {
        super(world, position, component);
        this.position = position;
        size = 2;
        health = 150;
        damage = 15;
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
        hurtingImage = new BufferedImage(walkingImage.getWidth(), walkingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gbi = hurtingImage.createGraphics();
        gbi.drawImage(walkingImage, 0, 0, hurtingImage.getWidth(), hurtingImage.getHeight(), component);
        gbi.setColor(Color.RED);
        gbi.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
        gbi.fillRect(0, 0, hurtingImage.getWidth(), hurtingImage.getHeight());
    }

    /**
     * Draw the Orc Boss
     * @param g Graphics object to draw to
     * @param component JPanel to draw to.
     * @param drawDebug boolean to draw debug info.
     */
    @Override
    public void draw(Graphics g, JPanel component, boolean drawDebug) {
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
            drawDebugInfo(g);
        }

    }


}
