import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import javax.imageio.ImageIO;
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

    private int pointX = WINDOW_HEIGHT/2;

    private int pointY = WINDOW_LENGTH/2;

    @Override
    public void run() {

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

                g.drawImage(hero, pointX, pointY, this);
            }
        };
        frame.add(panel);
        frame.addKeyListener(this);

        frame.pack();
        frame.setVisible(true);

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            try {
                hero = ImageIO.read(new File("Assets/Player/PlayerBack.png"));
                pointY = pointY - MOVEMENT;
            }
            catch(IOException a) {
                System.out.print(a);
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            try {
                hero = ImageIO.read(new File("Assets/Player/PlayerFront.png"));
                pointY = pointY + MOVEMENT;
            }
            catch(IOException a) {
                System.out.print(a);
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT ) {
            try {
                hero = ImageIO.read(new File("Assets/Player/PlayerLeft.png"));
                pointX = pointX - MOVEMENT;
            }
            catch(IOException a) {
                System.out.print(a);
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT ) {
            try {
                hero = ImageIO.read(new File("Assets/Player/PlayerRight.png"));
                pointX = pointX + MOVEMENT;
            }
            catch(IOException a) {
                System.out.print(a);
            }
        }
        panel.repaint();
    }

    public static void main(String args[]) throws IOException{
        hero = ImageIO.read(new File("Assets/Player/PlayerBack.png"));

        javax.swing.SwingUtilities.invokeLater(new Player());
    }
}
