import player.Player;
import player.PlayerAction;
import player.PlayerFacing;
import tiles.AbstractTile;
import world.Chunk;
import world.World;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * Class that represents the character the user will be controlling
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Game extends KeyAdapter implements Runnable {
    private static BufferedImage hero;

    private JPanel panel;

    private final int WINDOW_HEIGHT = 800;

    private final int WINDOW_LENGTH = 900;

    private final double MOVEMENT = 0.1;
    Player player;
    GameLoop mainLoop;


    private World world;
    Rectangle panelBounds;

    @Override
    public void run() {

        world = new World();
        panelBounds = new Rectangle(-WINDOW_LENGTH / 2, -WINDOW_HEIGHT / 2, WINDOW_LENGTH, WINDOW_HEIGHT);


        fillWindowWithChunks();
        // set up the GUI "look and feel" which should match
        // the OS on which we are running
        JFrame.setDefaultLookAndFeelDecorated(true);

        // create a JFrame in which we will build our very
        // tiny GUI, and give the window a name
        JFrame frame = new JFrame("Game");
        frame.setPreferredSize(new Dimension(WINDOW_LENGTH, WINDOW_HEIGHT));

        // tell the JFrame that when someone closes the
        // window, the application should terminate
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // JPanel with a paintComponent method
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {

                super.paintComponent(g);
                redraw(g);
            }
        };
        mainLoop = new GameLoop(panel);
        mainLoop.start();

        player = new Player();
        player.start();
        frame.add(panel);
        frame.addKeyListener(this);

        frame.pack();
        frame.setVisible(true);

    }

    private void redraw(Graphics g) {
        int width = g.getClipBounds().width / 2;
        int height = g.getClipBounds().height / 2;
        g.translate(-(int)((player.getPosition().getX() * AbstractTile.TILE_SIZE) - width), -(int)((player.getPosition().getY() * AbstractTile.TILE_SIZE) - height));
        panelBounds = g.getClipBounds();

        world.draw(g, panel);
        player.draw(g, panel);
//        g.drawImage(hero, playerPosition.x * AbstractTile.TILE_SIZE, playerPosition.y * AbstractTile.TILE_SIZE, AbstractTile.TILE_SIZE * 5, AbstractTile.TILE_SIZE * 5, panel);

    }

//    @Override
//    public void keyTyped(KeyEvent e) {
//        if (e.getKeyCode() == KeyEvent.VK_Z) {
//            if (position.equals("back")) {
//                try {
//                    hero = ImageIO.read(new File("Assets/player.Player/AttackBack.png"));
//                } catch (IOException a) {
//                    System.out.print(a);
//                }
//            } else if (position.equals("front")) {
//                try {
//                    hero = ImageIO.read(new File("Assets/player.Player/AttackFront.png"));
//                } catch (IOException a) {
//                    System.out.print(a);
//                }
//            } else if (position.equals("left")) {
//                try {
//                    hero = ImageIO.read(new File("Assets/player.Player/AttackLeft.png"));
//                } catch (IOException a) {
//                    System.out.print(a);
//                }
//            } else {
//                try {
//                    hero = ImageIO.read(new File("Assets/player.Player/AttackRight.png"));
//                } catch (IOException a) {
//                    System.out.print(a);
//                }
//            }
//        }
//        // panel.repaint();
//        // try {
//        // TimeUnit.SECONDS.sleep((long)0.1);
//        // }
//        // catch (Exception er){
//        // System.out.print(er);
//        // }
//        // hero = copy;
//        // panel.repaint();
//
//        fillWindowWithChunks();
//    }


    @Override
    public void keyReleased(KeyEvent e) {
        player.setAction(PlayerAction.Standing);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            player.setFacing(PlayerFacing.Back);
            player.setAction(PlayerAction.Walking);
            player.setPosition(new Point2D.Double(player.getPosition().getX(), (player.getPosition().getY() - MOVEMENT)));

        } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            player.setAction(PlayerAction.Walking);
            player.setFacing(PlayerFacing.Front);
            player.setPosition(new Point2D.Double(player.getPosition().getX(), (player.getPosition().getY() + MOVEMENT)));

        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            player.setAction(PlayerAction.Walking);
            player.setFacing(PlayerFacing.Left);
            player.setPosition(new Point2D.Double((player.getPosition().getX() - MOVEMENT), player.getPosition().getY()));

        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            player.setAction(PlayerAction.Walking);
            player.setFacing(PlayerFacing.Right);
            player.setPosition(new Point2D.Double((player.getPosition().getX() + MOVEMENT), player.getPosition().getY()));

        }
        fillWindowWithChunks();

    }

    private void fillWindowWithChunks() {
        if (panelBounds != null)
            for (int y = panelBounds.y; y < panelBounds.height + panelBounds.y; y += AbstractTile.TILE_SIZE) {
                for (int x = panelBounds.x; x < panelBounds.width + panelBounds.x; x += AbstractTile.TILE_SIZE) {

                    double posX = (double) x / (double) Chunk.CHUNK_SIZE / (double) AbstractTile.TILE_SIZE;
                    double posY = (double) y / (double) Chunk.CHUNK_SIZE / (double) AbstractTile.TILE_SIZE;
                    Point offset = new Point((int) Math.floor(posX), (int) Math.floor(posY));
                    world.generateChunk(offset);

                }
            }
    }

    public static void main(String args[]) throws IOException {
        hero = ImageIO.read(new File("Assets/Player/PlayerBack.png"));

        javax.swing.SwingUtilities.invokeLater(new Game());
    }
}

class GameLoop extends Thread {
    JPanel panel;
    private static final int WAIT_TIME = 100;

    public GameLoop(JPanel panel) {
        this.panel = panel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(WAIT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            panel.repaint();

        }
    }
}
