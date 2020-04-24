package generation;

/**
 * Tile Generator interface. Defines actions that generator must have for a World.
 */
public interface IGenerator {
    /**
     * Get a 2d height map value
     * @param x X position
     * @param y Y position
     * @return value of the height at (x, y) normalized to 0.0 - 1.0
     */
    double getValue(double x, double y);

}
