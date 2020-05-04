/**
 * World: A world with a dictionary of chunks.
 *
 * @author Samuil Orlioglu
 * @version 4/23/2020
 */

package world;

import entity.ScentPoint;
import generation.*;
import tiles.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;
import java.util.HashMap;


public class World {
    private HashMap<Point, Chunk> chunks;
    private IGenerator generator;
    private HashMap<Point2D, ScentPoint> scents;

    public World() {
        chunks = new HashMap<>();
        generator = new PerlinGenerator(3, 40, 70, .01);
        scents = new HashMap<>();
    }

    public void generateChunk(Point offset) {
        if (!chunks.containsKey(offset)) {
            Chunk chunk = new Chunk(offset);
            for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                    double val = generator.getValue((offset.x * Chunk.CHUNK_SIZE + x + 0.1), (offset.y * Chunk.CHUNK_SIZE + y + 0.1));
                    chunk.setTile(new Point(x, y), getTileFromTerrainValue(val));
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
                    if (t != null)
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

    private AbstractTile getTileFromTerrainValue(double val) {

        int t = (int) (val * 5);

        if(t <= 1){
            return new WaterTile();
        }
        if(t == 2){
            return new GrassTile();
        }
        if (t == 3){
            return new DirtTile();
        }
        else return new StoneTile();
    }

    public void addScent(Point2D point, ScentPoint scentPoint){
        scents.put(point, scentPoint);
    }

    public void removeScent(Point2D point){
        if(scents.containsKey(point))
            scents.remove(point);
    }

    public ScentPoint getScent(Point2D point){
        if(scents.containsKey(point))
            return scents.get(point);
        return null;
    }
}
