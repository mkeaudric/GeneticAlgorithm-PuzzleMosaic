import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Populasi {
    private List<Individu> individuList;
    private int size;
    private Random random;

    public Populasi(int size){
        this.size = size;
        this.random = new Random();
        this.individuList = new ArrayList<>(size);
    }
}
