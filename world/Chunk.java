/**
 * Chunk: A square chunk of of the world with tiles.
 *
 * @author Samuil Orlioglu
 * @version 4/23/2020
 */

package world;

import tiles.AbstractTile;

import java.awt.*;

public class Chunk {
    public static final int CHUNK_SIZE = 5;
    private AbstractTile[] tiles;
    private Point offset;



    public Chunk(Point offset){
        this.offset = offset;
        tiles = new AbstractTile[CHUNK_SIZE * CHUNK_SIZE];
    }

    /**
     *
     * @param position Position of new Tile in chunk. X and Y must be [0, CHUNK_SIZE - 1]
     * @return Tile at position.
     */
    public AbstractTile getTile(Point position){
        int index = position.y * CHUNK_SIZE + position.x;
        return tiles[index];
    }

    /**
     * Set a tile at position
     * @param position Position of new Tile in chunk. X and Y must be [0, CHUNK_SIZE - 1]
     * @param tile New Tile
     */
    public void setTile(Point position, AbstractTile tile){
        int index = position.y * CHUNK_SIZE + position.x;
        tiles[index] = tile;
    }
    /**
     * Get offset of this Chunk.
     * @return  Offset of this Chunk.
     */
    public Point getOffset() {
        return offset;
    }

}
