/**
 * ConstantGenerator: Description
 *
 * @author Samuil Orlioglu
 * @version 4/23/2020
 */

package generation;

/**
 * A Generator that only returns a specific value given as an argument in constructor.
 */
public class ConstantGenerator implements IGenerator {
    double constant;

    public ConstantGenerator(double constant) {
        this.constant = constant;
    }

    @Override
    public double getValue(double x, double y) {
        return constant;
    }
}
