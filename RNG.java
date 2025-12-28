import java.util.Random;

// Random Number Generator
public class RNG {
    public static Random rand; // rand ini bakal dipake di semua kelas

    public static void initialize(int seed){
        rand = new Random(seed);
    }

}
