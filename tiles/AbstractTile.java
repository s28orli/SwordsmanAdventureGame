/**
 * AbstractTile: Description
 *
 * @author Samuil Orlioglu
 * @version 4/22/2020
 */

package tiles;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class AbstractTile {
    public static final int TILE_SIZE = 100;
    private int id;
    private String name;
    Image texture;


    public AbstractTile(int id, String name, String pathToImage) {
        this.id = id;
        this.name = name;
        try {
            texture = ImageIO.read(new File(pathToImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Image getTexture() {
        return texture;
    }

    @Override
    public String toString() {
        return name;
    }
}
