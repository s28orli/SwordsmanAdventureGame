/**
 * RandomGenerator: Random terrain generator that returns random values between 0.0 and 1.0
 *
 * @author Samuil Orlioglu
 * @version 4/23/2020
 */

package generation;

import java.util.Random;

public class RandomGenerator implements IGenerator {
    Random random;
    int seed;

    public RandomGenerator(int seed) {
        this.seed = seed;
        random = new Random(seed);
    }

    public RandomGenerator() {
        seed = 0;
        random = new Random(seed);
    }

    @Override
    public double getValue(double x, double y) {
        return random.nextDouble();
    }
}
