import entity.*;
import tiles.AbstractTile;
import world.Chunk;
import world.World;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class that represents the character the user will be controlling
 *
 * @author Manny Lo
 * @version Spring 2020
 */
public class Game extends InputAdapter implements Runnable {

    private JPanel panel;
    private final int WINDOW_HEIGHT = 800;
    private final int WINDOW_LENGTH = 900;
    private final double MOVEMENT = 0.2;
    private Player player;
    private GameLoop mainLoop;
    private boolean drawDebug = true;
    private World world;
    private Rectangle panelBounds;
    private double zoom = 1;

    private List<Entity> enemies;

    @Override
    public void run() {
        enemies = new ArrayList<>(20);
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
                if (g != null)
                    redraw(g);
            }
        };
        mainLoop = new GameLoop(panel);
        mainLoop.start();

        player = new Player(world);
        player.start();

        Random rand = new Random(0);

        for (int i = 0; i < 10; i++) {
            Orc ent;
            if (i % 5 == 0) {
                ent = new OrcBoss(world, new Point2D.Double(rand.nextInt(5 * Chunk.CHUNK_SIZE) - 2 * Chunk.CHUNK_SIZE, rand.nextInt(5 * Chunk.CHUNK_SIZE) - 2 * Chunk.CHUNK_SIZE));
                ent.addEntityToTrack(player);

            } else {
                ent = new Orc(world, new Point2D.Double(rand.nextInt(5 * Chunk.CHUNK_SIZE) - 2 * Chunk.CHUNK_SIZE, rand.nextInt(5 * Chunk.CHUNK_SIZE) - 2 * Chunk.CHUNK_SIZE));
                ent.addEntityToTrack(player);
            }
            ent.start();
            enemies.add(ent);
        }

        frame.add(panel);
        frame.addKeyListener(this);
        frame.addMouseListener(this);

        frame.pack();
        frame.setVisible(true);
    }

    private void redraw(Graphics g) {
        int width = (int) ((g.getClipBounds().width / 2) * zoom);
        int height = (int) ((g.getClipBounds().height / 2) * zoom);

        g.translate(-(int) (((player.getPosition().getX() * AbstractTile.TILE_SIZE) - width) * zoom), -(int) (((player.getPosition().getY() * AbstractTile.TILE_SIZE) - height) * zoom));
        panelBounds = g.getClipBounds();
        fillWindowWithChunks();
        world.draw(g, panel, drawDebug);
        for (Entity enemy : enemies) {
            enemy.draw(g, panel, drawDebug);
        }
        player.draw(g, panel, drawDebug);


    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoom += e.getPreciseWheelRotation();
        panel.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.setAction(EntityAction.Standing);
        if (e.getKeyCode() == KeyEvent.VK_I) {
            for (Entity enemy : enemies) {

                enemy.setFacing(EntityFacing.Back);
                enemy.setAction(EntityAction.Standing);
            }

        } else if (e.getKeyCode() == KeyEvent.VK_K) {
            for (Entity enemy : enemies) {

                enemy.setAction(EntityAction.Standing);
                enemy.setFacing(EntityFacing.Front);
            }

        } else if (e.getKeyCode() == KeyEvent.VK_J) {
            for (Entity enemy : enemies) {

                enemy.setAction(EntityAction.Standing);
                enemy.setFacing(EntityFacing.Left);
            }

        } else if (e.getKeyCode() == KeyEvent.VK_L) {
            for (Entity enemy : enemies) {
                enemy.setAction(EntityAction.Standing);
                enemy.setFacing(EntityFacing.Right);
            }


        } else if (e.getKeyCode() == KeyEvent.VK_M) {
            for (Entity enemy : enemies) {
                enemy.setAction(EntityAction.Attacking);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            player.setAction(EntityAction.Attacking);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            player.setFacing(EntityFacing.Back);
            player.setAction(EntityAction.Walking);
            player.setPosition(new Point2D.Double(player.getPosition().getX(), (player.getPosition().getY() - MOVEMENT)));

        } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            player.setAction(EntityAction.Walking);
            player.setFacing(EntityFacing.Front);
            player.setPosition(new Point2D.Double(player.getPosition().getX(), (player.getPosition().getY() + MOVEMENT)));

        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            player.setAction(EntityAction.Walking);
            player.setFacing(EntityFacing.Left);
            player.setPosition(new Point2D.Double((player.getPosition().getX() - MOVEMENT), player.getPosition().getY()));

        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            player.setAction(EntityAction.Walking);
            player.setFacing(EntityFacing.Right);
            player.setPosition(new Point2D.Double((player.getPosition().getX() + MOVEMENT), player.getPosition().getY()));

        }

        if (e.getKeyCode() == KeyEvent.VK_I) {
            for (Entity enemy : enemies) {

                enemy.setFacing(EntityFacing.Back);
                enemy.setAction(EntityAction.Walking);
                enemy.setPosition(new Point2D.Double(enemy.getPosition().getX(), (enemy.getPosition().getY() - MOVEMENT)));
            }

        } else if (e.getKeyCode() == KeyEvent.VK_K) {
            for (Entity enemy : enemies) {

                enemy.setAction(EntityAction.Walking);
                enemy.setFacing(EntityFacing.Front);
                enemy.setPosition(new Point2D.Double(enemy.getPosition().getX(), (enemy.getPosition().getY() + MOVEMENT)));
            }

        } else if (e.getKeyCode() == KeyEvent.VK_J) {
            for (Entity enemy : enemies) {

                enemy.setAction(EntityAction.Walking);
                enemy.setFacing(EntityFacing.Left);
                enemy.setPosition(new Point2D.Double((enemy.getPosition().getX() - MOVEMENT), enemy.getPosition().getY()));
            }

        } else if (e.getKeyCode() == KeyEvent.VK_L) {
            for (Entity enemy : enemies) {
                enemy.setAction(EntityAction.Walking);
                enemy.setFacing(EntityFacing.Right);
                enemy.setPosition(new Point2D.Double((enemy.getPosition().getX() + MOVEMENT), enemy.getPosition().getY()));
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
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

        javax.swing.SwingUtilities.invokeLater(new Game());
    }
}

/**
 * GameLoop class: The purpose is to unify calls to redraw the graphics.
 */
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
