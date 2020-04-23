/**
 * World: Description
 *
 * @author Samuil Orlioglu
 * @version 4/23/2020
 */

package world;

import tiles.AbstractTile;
import tiles.GrassTile;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;
import java.util.HashMap;


public class World {
    private HashMap<Point, Chunk> chunks;

    public World() {
        chunks = new HashMap<>();
    }

    public void generateChunk(Point offset) {
        if (!chunks.containsKey(offset)) {
            Chunk chunk = new Chunk(offset);
            for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                    chunk.setTileId(new Point(x, y), new GrassTile());
                }
            }
            chunks.put(offset, chunk);
        }
    }

    public void draw(Graphics g, ImageObserver observer) {
        for (Point offset : chunks.keySet()) {
            for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                    AbstractTile t = chunks.get(offset).getTileId(new Point(x, y));
                    g.drawImage(t.getTexture(), (offset.x * Chunk.CHUNK_SIZE + x) * AbstractTile.TILE_SIZE, (offset.y * Chunk.CHUNK_SIZE + y) * AbstractTile.TILE_SIZE, AbstractTile.TILE_SIZE, AbstractTile.TILE_SIZE, observer);
                }
            }
        }
    }
}
