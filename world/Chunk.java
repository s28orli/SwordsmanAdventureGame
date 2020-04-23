/**
 * Chunk: Description
 *
 * @author Samuil Orlioglu
 * @version 4/23/2020
 */

package world;

import tiles.AbstractTile;

import java.awt.*;
import java.awt.geom.Point2D;

public class Chunk {
    public static final int CHUNK_SIZE = 20;
    private AbstractTile[] tiles;
    private Point offset;
    public Chunk(Point offset){
        this.offset = offset;
        tiles = new AbstractTile[CHUNK_SIZE * CHUNK_SIZE];
    }

    public AbstractTile getTileId(Point position){
        return tiles[position.y / CHUNK_SIZE + position.x % CHUNK_SIZE];
    }

    public void setTileId(Point position, AbstractTile tile){
        tiles[position.y / CHUNK_SIZE + position.x % CHUNK_SIZE] = tile;
    }

}
