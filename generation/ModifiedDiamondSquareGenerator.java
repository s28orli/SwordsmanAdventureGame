/**
 * ModifiedDiamondSquareGenerator: Description
 *
 * @author Samuil Orlioglu
 * @version 4/24/2020
 */

package generation;

import world.Chunk;

import java.util.Arrays;
import java.util.Random;

/* https://en.wikipedia.org/wiki/Diamond-square_algorithm */
/** DOES NOT WORK**/
public class ModifiedDiamondSquareGenerator implements IGenerator {
    int seed;
    int size;
    Random random;
    double[][] values;

    public ModifiedDiamondSquareGenerator(int seed, int size) {
        this.seed = seed;
        this.size = size;
        random = new Random(seed);
        values = new double[size + 1][size + 1];
        fillValues();

    }

    public ModifiedDiamondSquareGenerator(int size) {
        this.seed = 0;
        this.size = size;
        random = new Random(seed);
        values = new double[size + 1][size + 1];
        fillValues();
    }

    @Override
    public double getValue(double x, double y) {
//        System.out.println(values[(int) Math.abs(y / size)][(int) Math.abs(x % size)]);
        int indexY = (int) y / size;
        int indexX = (int) x / size;

        return values[Math.abs(indexY)][Math.abs(indexX)];
    }

    private void fillValues() {
        double influence = 1;
        // Set corners
        values[0][0] = random.nextDouble();
        values[size - 1][0] = random.nextDouble();
        values[0][size - 1] = random.nextDouble();
        values[size - 1][size - 1] = random.nextDouble();
        int step = size;
        while (step > 1) {
//            System.out.println(step);
            diamond(step, influence);

            square(step, influence);

            step /= 2;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            sb.append("\n" + Arrays.toString(values[i]));
        }
        System.out.println(sb.toString());
    }

    private void square(int length, double influence) {
        for (int y = length; y < size; y += length) {
            for (int x = length; x < size; x += length) {

                double center = values[y][x];
                double left = values[y][x - length];
                double right = values[y][x + length];
                double top = values[y - length][x];
                double bottom = values[y + length][x];

                double avg = (center + left + right + top + bottom) / 5.0;

                values[y][x] = avg + random.nextDouble() * influence;
                values[y][x + length] = avg + random.nextDouble() * influence;
                values[y + length][x] = avg + random.nextDouble() * influence;
                values[y + length][x + length] = clamp(avg + random.nextDouble() * influence, 0, 1);


            }
        }

    }

    private void diamond(int length, double influence) {

        int halfLength = length / 2;
        for (int y = 0; y < size; y += length) {
            for (int x = 0; x < size; x += length) {

                double topLeft = values[y][x];
                double topRight = values[y][x + length];
                double bottomLeft = values[y + length][x];
                double bottomRight = values[y + length][x + length];

                double avg = (topLeft + topRight + bottomLeft + bottomRight) / 4.0;

                values[y + halfLength][x + halfLength] = clamp(avg + random.nextDouble() * influence, 0, 1);

            }
        }

    }

    private double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }


}
