/**
 * RandomSeedGenerator: Description
 *
 * @author Samuil Orlioglu
 * @version 4/24/2020
 */

package generation;

import java.util.Random;

public class RandomSeedGenerator implements IGenerator {
    Random seedRandom;
    Random valueRandom;
    public RandomSeedGenerator(){
        seedRandom = new Random();
        valueRandom = new Random(seedRandom.nextInt());
    }
    @Override
    public double getValue(double x, double y) {
        valueRandom.setSeed(seedRandom.nextInt());
        return valueRandom.nextDouble();
    }
}
