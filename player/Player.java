package player;

import tiles.AbstractTile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * player.Player: Description
 *
 * @author Samuil Orlioglu
 * @version 4/27/2020
 */


public class Player extends Thread {
    private PlayerFacing facing;
    private PlayerAction action;
    private static final int imageWidth = 64;
    private static final int animationTimeLength = 100;
    private int animationIndex;
    Point2D position;

    Image walkingFront;
    Image walkingBack;
    Image walkingLeft;
    Image walkingRight;

    Image attackingFront;
    Image attackingBack;
    Image attackingLeft;
    Image attackingRight;


    LocalDateTime attackStarted;

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
                e.printStackTrace();
            }
        }

    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D.Double position) {
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
        this.action = action;
        if(action == PlayerAction.Attacking){
            attackStarted = LocalDateTime.now();
        }
    }

    public void draw(Graphics g, JPanel component) {
        int index = animationIndex;
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

        if (action == PlayerAction.Standing || action == PlayerAction.Attacking) {
            index = 0;
        }
        int x = (int) (position.getX() * AbstractTile.TILE_SIZE);
        int y = (int) (position.getY() * AbstractTile.TILE_SIZE);
        int dx = x + AbstractTile.TILE_SIZE * 5;
        int dy = y + AbstractTile.TILE_SIZE * 5;

        int sx = index * imageWidth;
        int sy = 0;

        int sdx = sx + imageWidth;
        int sdy = sy + imageWidth;

        g.drawImage(img, x, y, dx, dy, sx, sy, sdx, sdy, component);

    }

    @Override
    public void run() {
        int time = 0;
        while (true) {
            try {
                sleep(100);
                time += 100;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if ((double) (time) / animationTimeLength > 1) {
                time = 0;
                animationIndex += 1;
                if (animationIndex > 8) {
                    animationIndex = 1;
                }
            }
            if(attackStarted != null){
                System.out.println(LocalDateTime.now().minusSeconds(attackStarted.getSecond()).getSecond());
                if(LocalDateTime.now().minusSeconds(attackStarted.getSecond()).getSecond() >= 1){
                    action = PlayerAction.Standing;
                    attackStarted = null;
                }
            }


        }
    }
}
