/**
 * ModifiedDiamondSquareGenerator: Description
 *
 * @author Samuil Orlioglu
 * @version 4/24/2020
 */

package generation;

import java.util.Random;

public class ModifiedDiamondSquareGenerator implements IGenerator {
    int seed;
    int size;
    Random random;
    double[][] values;

    public ModifiedDiamondSquareGenerator(int seed, int size) {
        this.seed = seed;
        this.size = size;
        random = new Random(seed);
        values = new double[size][size];
    }

    public ModifiedDiamondSquareGenerator(int size) {
        this.seed = 0;
        this.size = size;
        random = new Random(seed);
        values = new double[size][size];
    }

    @Override
    public double getValue(double x, double y) {
        return 0;
    }


}
