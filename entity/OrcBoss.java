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

    public OrcBoss(Point position, JPanel component) {
        super(component);
    public OrcBoss(World world, Point2D.Double position) {
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
        hurtingImage = new BufferedImage(walkingImage.getWidth(), walkingImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gbi = hurtingImage.createGraphics();
        gbi.drawImage(walkingImage, 0, 0, hurtingImage.getWidth(), hurtingImage.getHeight(), component);
        gbi.setColor(Color.RED);

        gbi.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
    public OrcBoss(World world){
        this(world, new Point2D.Double(0, 0));
    }

        gbi.fillRect(0, 0, hurtingImage.getWidth(), hurtingImage.getHeight());
    }

    public OrcBoss(JPanel component) {
        this(new Point(0, 0), component);
    }

}
