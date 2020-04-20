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
public class Player implements Runnable, ImageObserver
{
    private static BufferedImage hero;

    private JPanel panel;
    
    private final int WINDOW_HEIGHT = 500;
    
    private final int WINDOW_LENGTH = 500;
    
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

		    
		    g.drawImage(hero, WINDOW_HEIGHT/2, WINDOW_LENGTH/2, this);
			
		}
	    };
	frame.add(panel);

	// display the window we've created
	frame.pack();
	frame.setVisible(true);

    }
    
    public boolean imageUpdate(Image img, int infoflags, int x, int y,
			       int width, int height) {

	if ((infoflags & ImageObserver.ALLBITS) > 0) {
	    panel.repaint();
	    return false;
	}
	return true;
    }
    
    public static void main(String args[]) throws IOException{
        hero = ImageIO.read(new File("Assets/Player/PlayerBack.png"));
	
	javax.swing.SwingUtilities.invokeLater(new Player());
    }
}
