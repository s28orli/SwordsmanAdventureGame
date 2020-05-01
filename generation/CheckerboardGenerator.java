/**
 * CheckerboardGenerator: Description
 *
 * @author Samuil Orlioglu
 * @version 4/30/2020
 */

package generation;

import tiles.AbstractTile;

public class CheckerboardGenerator implements IGenerator {


    @Override
    public double getValue(double x, double y) {
        double val = ((int) x % AbstractTile.TILE_SIZE ^ (int) y % AbstractTile.TILE_SIZE);
        return val;

    }
}
