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
    public static final int CHUNK_SIZE = 20;
    private AbstractTile[] tiles;
    private Point offset;
    public Chunk(Point offset){
        this.offset = offset;
        tiles = new AbstractTile[CHUNK_SIZE * CHUNK_SIZE];
    }

    public AbstractTile getTile(Point position){
        return tiles[position.y / CHUNK_SIZE + position.x % CHUNK_SIZE];
    }

    public void setTile(Point position, AbstractTile tile){
        tiles[position.y / CHUNK_SIZE + position.x % CHUNK_SIZE] = tile;
    }

}
