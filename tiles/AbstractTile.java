/**
 * AbstractTile: Base implementation for a Tile
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
    public static final int TILE_SIZE = 30;
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

    /**
     * Get the Tile's Id
     * @return  Tile's Id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the Tile's name.
     * @return Tile's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the Tile's Texture.
     * @return Tile's Texture.
     */
    public Image getTexture() {
        return texture;
    }

    /**
     * Get a string representation of the tile.
     * @return String representation of the tile.
     */
    @Override
    public String toString() {
        return name;
    }
}
