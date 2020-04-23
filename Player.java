import tiles.AbstractTile;
import world.Chunk;
import world.World;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.concurrent.TimeUnit;
/**
 * Class that represents the character the user will be controlling
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Player extends KeyAdapter implements Runnable 
{
    private static BufferedImage hero;

    private JPanel panel;

    private final int WINDOW_HEIGHT = 500;

    private final int WINDOW_LENGTH = 500;

    private final int MOVEMENT = 2;
    private Point velocity;

    private int pointX = WINDOW_HEIGHT/2;

    private int pointY = WINDOW_LENGTH/2;

    private String position = "back";
    private World world;
    Rectangle panelBounds;

    @Override
    public void run() {

        world = new World();
        fillWindowWithChunks();
        velocity = new Point(0, 0);
        // set up the GUI "look and feel" which should match
        // the OS on which we are running
        JFrame.setDefaultLookAndFeelDecorated(true);

        // create a JFrame in which we will build our very
        // tiny GUI, and give the window a name
        JFrame frame = new JFrame("Game");
        frame.setPreferredSize(new Dimension(WINDOW_HEIGHT, WINDOW_LENGTH));

        // tell the JFrame that when someone closes the
        // window, the application should terminate
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // JPanel with a paintComponent method
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {

                super.paintComponent(g);
                panelBounds = g.getClipBounds();

                world.draw(g, this);

                g.drawImage(hero, pointX, pointY, this);

            }
        };
        frame.add(panel);
        frame.addKeyListener(this);

        frame.pack();
        frame.setVisible(true);

    }

    @Override
    public void keyTyped(KeyEvent e) { 
        BufferedImage copy = hero;
        if (e.getKeyCode() == KeyEvent.VK_Z) {
            if (position.equals("back")) {
                try {
                    hero = ImageIO.read(new File("Assets/Player/AttackBack.png"));
                }
                catch(IOException a) {
                    System.out.print(a);
                }
            }
            else if (position.equals("front")) {
                try {
                    hero = ImageIO.read(new File("Assets/Player/AttackFront.png"));
                }
                catch(IOException a) {
                    System.out.print(a);
                }
            }
            else if (position.equals("left")) {
                try {
                    hero = ImageIO.read(new File("Assets/Player/AttackLeft.png"));
                }
                catch(IOException a) {
                    System.out.print(a);
                }
            }
            else {
                try {
                    hero = ImageIO.read(new File("Assets/Player/AttackRight.png"));
                }
                catch(IOException a) {
                    System.out.print(a);
                }
            }
        }
        // panel.repaint();
        // try {
            // TimeUnit.SECONDS.sleep((long)0.1);
        // }
        // catch (Exception er){
            // System.out.print(er);
        // }
        // hero = copy;
        // panel.repaint();

        fillWindowWithChunks();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            try {
                hero = ImageIO.read(new File("Assets/Player/PlayerBack.png"));
                pointY = pointY - MOVEMENT;
            }
            catch(IOException a) {
                System.out.print(a);
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            try {
                hero = ImageIO.read(new File("Assets/Player/PlayerFront.png"));
                pointY = pointY + MOVEMENT;
            }
            catch(IOException a) {
                System.out.print(a);
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_D) {
            try {
                hero = ImageIO.read(new File("Assets/Player/PlayerLeft.png"));
                pointX = pointX - MOVEMENT;
            }
            catch(IOException a) {
                System.out.print(a);
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_A) {
            try {
                hero = ImageIO.read(new File("Assets/Player/PlayerRight.png"));
                pointX = pointX + MOVEMENT;
            }
            catch(IOException a) {
                System.out.print(a);
            }
        }
//        fillWindowWithChunks();
        panel.repaint();

    }

    private void fillWindowWithChunks(){
        if(panelBounds != null)
        for (int y = panelBounds.y; y < panelBounds.height + panelBounds.y; y += Chunk.CHUNK_SIZE * AbstractTile.TILE_SIZE) {
            for (int x = panelBounds.x; x < panelBounds.width + panelBounds.x; x += Chunk.CHUNK_SIZE * AbstractTile.TILE_SIZE) {
                Point offset = new Point(x / Chunk.CHUNK_SIZE / AbstractTile.TILE_SIZE, y / Chunk.CHUNK_SIZE / AbstractTile.TILE_SIZE);
//                System.out.println(offset);
                world.generateChunk(offset);

            }
        }
    }

    public static void main(String args[]) throws IOException{
        hero = ImageIO.read(new File("Assets/Player/PlayerBack.png"));

        javax.swing.SwingUtilities.invokeLater(new Player());
    }
}
