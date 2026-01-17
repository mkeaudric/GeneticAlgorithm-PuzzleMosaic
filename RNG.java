import java.util.Random;
/**
 * Random Number Generator
 * @author Kelompok AI Mosaic
 * @version 1.0
 */
public class RNG {
    public static Random rand; // rand ini bakal dipake di semua kelas

    public static void initialize(int seed){
        rand = new Random(seed);
    }
}
