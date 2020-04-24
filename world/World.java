/**
 * World: Description
 *
 * @author Samuil Orlioglu
 * @version 4/23/2020
 */

package world;

import generation.IGenerator;
import generation.RandomGenerator;
import generation.RandomSeedGenerator;
import tiles.AbstractTile;
import tiles.GrassTile;
import tiles.StoneTile;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.HashMap;


public class World {
    private HashMap<Point, Chunk> chunks;
    private IGenerator generator;

    public World() {
        chunks = new HashMap<>();
        generator = new RandomSeedGenerator();
    }

    public void generateChunk(Point offset) {
        if (!chunks.containsKey(offset)) {
            Chunk chunk = new Chunk(offset);
            for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                    double val = generator.getValue(offset.x * Chunk.CHUNK_SIZE + x, offset.x * Chunk.CHUNK_SIZE + x);
                    if (val > 0.5)
                        chunk.setTile(new Point(x, y), new StoneTile());
                    else if (val <= 0.5) {
                        chunk.setTile(new Point(x, y), new GrassTile());

                    }
                }
            }
            chunks.put(offset, chunk);
        }
    }

    public void draw(Graphics g, ImageObserver observer) {
        for (Point offset : chunks.keySet()) {
            for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                    AbstractTile t = chunks.get(offset).getTile(new Point(x, y));
                    g.drawImage(t.getTexture(), (offset.x * Chunk.CHUNK_SIZE + x) * AbstractTile.TILE_SIZE, (offset.y * Chunk.CHUNK_SIZE + y) * AbstractTile.TILE_SIZE, AbstractTile.TILE_SIZE, AbstractTile.TILE_SIZE, observer);
                }
            }
        }
    }

    public AbstractTile getTileAtPosition(int x, int y) {
        int offsetX = x / Chunk.CHUNK_SIZE;
        int offsetY = y / Chunk.CHUNK_SIZE;
        Point offset = new Point(offsetX, offsetY);
        if (chunks.containsKey(offset)) {
            Chunk c = chunks.get(offset);
            Point pos = new Point(x % Chunk.CHUNK_SIZE, y % Chunk.CHUNK_SIZE);
            return c.getTile(pos);
        }
        return null;
    }

    public AbstractTile getTileAtPosition(Point point) {
        int x = point.x;
        int y = point.y;
        int offsetX = x / Chunk.CHUNK_SIZE;
        int offsetY = y / Chunk.CHUNK_SIZE;
        Point offset = new Point(offsetX, offsetY);
        if (chunks.containsKey(offset)) {
            Chunk c = chunks.get(offset);
            Point pos = new Point(x % Chunk.CHUNK_SIZE, y % Chunk.CHUNK_SIZE);
            return c.getTile(pos);
        }
        return null;
    }
}
