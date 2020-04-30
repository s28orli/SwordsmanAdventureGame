/**
 * PerlinGenerator: Description
 *
 * @author Samuil Orlioglu
 * @version 4/27/2020
 */

package generation;


/**
 * A simple implementation of Perlin noise based on the explanations of the following site.
 * https://flafla2.github.io/2014/08/09/perlinnoise.html
 * **/

public class PerlinGenerator implements IGenerator {
    private static int[] permutation = {151, 160, 137, 91, 90, 15,
            131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23,
            190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33,
            88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166,
            77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244,
            102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196,
            135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123,
            5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
            223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9,
            129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228,
            251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107,
            49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254,
            138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180
    };

    private static int[] p;


    double persistence;
    int numOctaves;
    double amplitude;
    double frequency;

    public PerlinGenerator(int numOctaves) {
        this.numOctaves = numOctaves;
        persistence = 1;
        amplitude = 1;
        frequency = 1;
        p = new int[512];
        for(int x=0;x<512;x++) {
            p[x] = permutation[x%256];
        }
    }

    public double getPersistence() {
        return persistence;
    }

    public void setPersistence(double persistence) {
        this.persistence = persistence;
    }

    public int getNumOctaves() {
        return numOctaves;
    }

    public void setNumOctaves(int numOctaves) {
        this.numOctaves = numOctaves;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public PerlinGenerator(int numOctaves, double persistence, double amplitude, double frequency) {
        this.persistence = persistence;
        this.numOctaves = numOctaves;
        this.amplitude = amplitude;
        this.frequency = frequency;
        p = new int[512];
        for(int x=0;x<512;x++) {
            p[x] = permutation[x%256];
        }
    }


    @Override
    public double getValue(double x, double y) {
        double total = 0;
        double freq = frequency;
        double amp = amplitude;
        double maxValue = 0;  // Used for normalizing result to 0.0 - 1.0
        for (int i = 0; i < numOctaves; i++) {
            total += perlin(x * freq, y * freq) * amp;

            maxValue += amp;

            amp *= persistence;
            freq *= 2;
        }

        return total / maxValue;
    }

    private double perlin(double x, double y) {
        int cellXMin = (int) Math.floor(x);
        int cellYMin = (int) Math.floor(y);
        double xf = Math.abs(x - (int)x);
        double yf = Math.abs(y - (int)y);

        double u = fade(xf);
        double v = fade(yf);

        // Hash
        int aaa, aba, aab, abb, baa, bba, bab, bbb;
        int indexX = cellXMin & 255;
        int indexY = cellYMin & 255;

        aaa = p[p[p[indexX] + indexY]];
        aba = p[p[p[indexX] + indexY + 1]];
        aab = p[p[p[indexX] + indexY]];
        abb = p[p[p[indexX] + indexY + 1]];
        baa = p[p[p[indexX + 1] + indexY]];
        bba = p[p[p[indexX + 1] + indexY + 1]];
        bab = p[p[p[indexX + 1] + indexY]];
        bbb = p[p[p[indexX + 1] + indexY + 1]];

        double x1, x2, y1, y2;
        x1 = lerp(grad(aaa, xf, yf),
                grad(baa, xf - 1, yf),
                u);
        x2 = lerp(grad(aba, xf, yf - 1),
                grad(bba, xf - 1, yf - 1),
                u);
        y1 = lerp(x1, x2, v);

        x1 = lerp(grad(aab, xf, yf),
                grad(bab, xf - 1, yf),
                u);
        x2 = lerp(grad(abb, xf, yf - 1),
                grad(bbb, xf - 1, yf - 1),
                u);
        y2 = lerp(x1, x2, v);

        return (lerp(y1, y2, v) + 1) / 2;


    }


    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);         // 6t^5 - 15t^4 + 10t^3
    }

    private double grad(int hash, double x, double y) {
        switch (hash & 7) {
            case 0x0:
                return x + y;
            case 1:
                return -x + y;
            case 2:
                return x - y;
            case 3:
                return -x - y;
            case 4:
                return y + x;
            case 5:
                return y - x;
            case 6:
                return -y - x;
            case 7:
                return -y + x;
            default:
                return 0;
        }
    }

    private double lerp(double a, double b, double x) {
        return a + x * (b - a);
    }


}
