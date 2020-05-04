import entity.*;
import tiles.AbstractTile;
import world.Chunk;
import world.World;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Class that represents the game. It uses Java AWT.
 *
 * @author Manny Lo and Samuil Orlioglu
 * @version Spring 2020
 */
public class Game extends InputAdapter implements Runnable {

    JPanel panel;
    private final int WINDOW_HEIGHT = 800;
    private final int WINDOW_LENGTH = 900;
    private final double MOVEMENT = 0.1;
    private final int ENEMY_MARKER_WIDTH = 15;
    private final int ENEMY_MARKER_HEIGHT = 8;

    public static final double DIFFICULTY_INCREASE_MULTIPLIER = 1.03;
    Player player;
    private GameLoop mainLoop;
    private JLabel scoreLabel;
    private JLabel healthLabel;
    private JLabel numEnemiesLabel;
    private JLabel roundLabel;
    int round = 0;
    int score = 0;

    private boolean drawDebug = false;
    private World world;
    private Rectangle panelBounds;
    private double zoom = 1;
    List<Entity> enemies;

    /**
     * Main run method.
     */
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

        JPanel mainPanel = new JPanel(new BorderLayout());
        // JPanel with a paintComponent method
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                if (g != null)
                    redraw(g);
            }
        };

        player = new Player(world);
        player.start();

        mainLoop = new GameLoop(this);
        mainLoop.start();
        frame.add(mainPanel);
        mainPanel.add(panel, BorderLayout.CENTER);

        scoreLabel = new JLabel("Score: " + score);
        healthLabel = new JLabel("Health: " + player.getHealth());
        numEnemiesLabel = new JLabel("Number of Enemies: " + enemies.size());
        roundLabel = new JLabel("Round: 1");
        JPanel panel2 = new JPanel(new GridLayout(1, 4));
        mainPanel.add(panel2, BorderLayout.NORTH);
        panel2.add(scoreLabel);
        panel2.add(healthLabel);
        panel2.add(numEnemiesLabel);
        panel2.add(roundLabel);

        frame.addKeyListener(this);
        frame.addMouseListener(this);

        frame.pack();
        frame.setVisible(true);

    }

    /**
     * Method to redraw the screen.
     * @param g Graphics object to redraw to.
     */
    private void redraw(Graphics g) {

        if (player != null) {
            int width = (int) ((g.getClipBounds().width / 2) * zoom);
            int height = (int) ((g.getClipBounds().height / 2) * zoom);

            g.translate(-(int) (((player.getPosition().getX() * AbstractTile.TILE_SIZE) - width) * zoom), -(int) (((player.getPosition().getY() * AbstractTile.TILE_SIZE) - height) * zoom));

            panelBounds = g.getClipBounds();
            fillWindowWithChunks();
            world.draw(g, panel, drawDebug);
            Object[] arr = enemies.toArray();
            for (Object obj : arr) {
                Entity enemy = (Entity) obj;
                if (isEnemyOnscreen(enemy)) {
                    enemy.draw(g, panel, drawDebug);

                } else {
                    int[] points = getEnemyMarkerPosition(enemy);
                    if (points != null) {
                        g.setColor(Color.RED);
                        g.fillOval(points[0], points[1], points[2], points[3]);

                    }
                }
            }
            player.draw(g, panel, drawDebug);
            scoreLabel.setText("Score: " + score);
            healthLabel.setText("Health: " + player.getHealth());
            numEnemiesLabel.setText("Number of Enemies: " + enemies.size());
        }

    }

    /**
     * Checks to see if specific enemy is within window bounds.
     * @param enemy Enemy t check
     * @return True if enemy is within the window, false otherwise.
     */
    private boolean isEnemyOnscreen(Entity enemy) {
        Point rawPos = new Point((int) enemy.getPosition().getX() * AbstractTile.TILE_SIZE, (int) enemy.getPosition().getY() * AbstractTile.TILE_SIZE);
        if (rawPos.x >= panelBounds.x && rawPos.x <= panelBounds.x + panelBounds.width) {
            return rawPos.y >= panelBounds.y && rawPos.y <= panelBounds.y + panelBounds.height;
        }
        return false;
    }

    /**
     * Get the coordinates of enemy marker for specific enemy.
     * @param enemy Enemy to get coordinates for.
     * @return int[], [0] - x position, [1] - y position, [2] - width, [3] - height
     */
    private int[] getEnemyMarkerPosition(Entity enemy) {
        if (!isEnemyOnscreen(enemy)) {
            Point rawPos = new Point((int) enemy.getPosition().getX() * AbstractTile.TILE_SIZE, (int) enemy.getPosition().getY() * AbstractTile.TILE_SIZE);

            if (rawPos.x >= panelBounds.x && rawPos.x <= panelBounds.x + panelBounds.width) {
                if (rawPos.y < panelBounds.y) {
                    return new int[]{rawPos.x, panelBounds.y, ENEMY_MARKER_HEIGHT, ENEMY_MARKER_WIDTH};
                }
                if (rawPos.y > panelBounds.y + panelBounds.height) {
                    return new int[]{rawPos.x, panelBounds.y + panelBounds.height - ENEMY_MARKER_WIDTH, ENEMY_MARKER_HEIGHT, ENEMY_MARKER_WIDTH};
                }
            }
            if (rawPos.y >= panelBounds.y && rawPos.y <= panelBounds.y + panelBounds.height) {
                if (rawPos.x < panelBounds.x) {
                    return new int[]{panelBounds.x, rawPos.y, ENEMY_MARKER_WIDTH, ENEMY_MARKER_HEIGHT};
                }
                if (rawPos.x > panelBounds.x + panelBounds.width) {
                    return new int[]{panelBounds.x + panelBounds.width - ENEMY_MARKER_WIDTH, rawPos.y, ENEMY_MARKER_WIDTH, ENEMY_MARKER_HEIGHT};
                }
            }
        }
        return null;
    }

    /**
     * What happens on the mouse wheel. Not used right now
     * @param e Event
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoom += e.getPreciseWheelRotation();
        panel.repaint();
    }

    /**
     * What happens on the Key released, mainly stop player from walking.
     * @param e Event
     */
    @Override
    public void keyReleased(KeyEvent e) {
        player.setAction(EntityAction.Standing);
    }

    /**
     * What happens on mouse click, mainly attack
     * @param e Event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            player.setAction(EntityAction.Attacking);
        }
    }

    /**
     * What happens on key press, mainly walk
     * @param e Event
     */
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


        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    /**
     * Generate all chunks within window bounds.
     */
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

    /**
     * Setup game for next level.
     * @param level the next level.
     */
    public void setupNextLevel(int level) {
        /*
         * Number of enemies per level = (level - 1)^ DIFFICULTY_INCREASE_MULTIPLIER + 10
         */
        int numEnemies = (int) (Math.pow(level - 1, Game.DIFFICULTY_INCREASE_MULTIPLIER) + 10);
        Random rand = new Random();
        for (int i = 0; i < numEnemies; i++) {
            Orc ent;
            if (i % 5 == 0) {
                ent = new OrcBoss(world, new Point2D.Double(rand.nextInt(10 * Chunk.CHUNK_SIZE) - 5 * Chunk.CHUNK_SIZE, rand.nextInt(10 * Chunk.CHUNK_SIZE) - 5 * Chunk.CHUNK_SIZE), panel);
            } else
                ent = new Orc(world, new Point2D.Double(rand.nextInt(10 * Chunk.CHUNK_SIZE) - 5 * Chunk.CHUNK_SIZE, rand.nextInt(10 * Chunk.CHUNK_SIZE) - 5 * Chunk.CHUNK_SIZE), panel);
            ent.addEntityToTrack(player);

            ent.start();
            enemies.add(ent);
        }
        player.setHealth(100);
        numEnemiesLabel.setText("Number of Enemies: " + numEnemies);
        roundLabel.setText("Round: " + level);
    }

    public void setupPlayerDiedMessage() {
        player = null;
    }

    /**
     * Main entry point.
     * @param args Not needed
     */
    public static void main(String args[]) {
        javax.swing.SwingUtilities.invokeLater(new Game());
    }
}

/**
 * GameLoop class: The purpose is to unify calls to redraw the graphics and for some game logic.
 */
class GameLoop extends Thread {
    private static final int WAIT_TIME = 100;
    Game game;

    public GameLoop(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        Stack<Entity> toRemove = new Stack<>();
        HashMap<Entity, EntityAction> previousActions = new HashMap<>();
        for (Entity i : game.enemies) {
            previousActions.put(i, i.getAction());
        }
        while (game.player != null) {
            try {
                sleep(WAIT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Entity i : game.enemies) {
                if (game.player.isCollision(i) && i.getAction() == EntityAction.Attacking && previousActions.get(i) != EntityAction.Attacking) {
                    game.player.setHealth(game.player.getHealth() - i.getDamage());
                } else if (game.player.isCollision(i) && game.player.getAction() == EntityAction.Attacking) {
                    i.setHealth(i.getHealth() - game.player.getDamage());
                    i.setAction(EntityAction.Hurting);
                }
                if (i.getHealth() <= 0) {
                    if (i instanceof OrcBoss) {
                        game.score += 2;
                    } else
                        game.score++;
                    toRemove.push(i);
                }
                previousActions.put(i, i.getAction());
            }
            while (!toRemove.empty()) {
                Entity removeEntity = toRemove.pop();
                game.enemies.remove(removeEntity);
                previousActions.remove(removeEntity);
            }

            if (game.player.getHealth() <= 0) {
                System.out.println("You died");
                game.setupPlayerDiedMessage();
            }

            if (game.enemies.size() == 0) {
                game.round++;
                game.setupNextLevel(game.round);
            }

            game.panel.repaint();

        }

        JOptionPane.showMessageDialog(game.panel, "You Died");
        System.exit(0xDEAD);

    }


}
