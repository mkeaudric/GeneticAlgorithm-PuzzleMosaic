import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Populasi {
    private List<Individu> individuList; //daftar individu dalam populasi
    private int populationSize; //jumlah individu dalam populasi
    private double populationFitness; //total fitness populasi
    private int kromosomSize; //ukuran kromosom
    private Random random;
    private FitnessFunction fitnessFunction; //fungsi fitness untuk evaluasi individu
    private Individu bestIndividu; //individu terbaik dalam populasi

    public Populasi(int populationSize, int kromosomSize, double probabilitasHitam, FitnessFunction fitnessFunction) {
        this.populationSize = populationSize;
        this.kromosomSize = kromosomSize;
        this.fitnessFunction = fitnessFunction;
        this.random = new Random();
        this.individuList = new ArrayList<>();

        for(int i=0; i<populationSize; i++) {
            //buat kromosom acak
            Kromosom kromosom = Kromosom.createRandomKromosom(kromosomSize, probabilitasHitam);

            //buat individu dengan kromosom dan fungsi fitness
            Individu individu = new Individu(kromosom, fitnessFunction);

            //tambahkan individu ke populasi
            individuList.add(individu);
        }
        //hitung fitness populasi awal
        evaluatePopulation();
    }

    public void evaluatePopulation() {
        populationFitness = 0;
        bestIndividu = null;

        //hitung total fitness dan individu terbaik
        for(Individu individu : individuList) {
            //ambil fitness individu
            double fitness = individu.getFitness();

            //tambahkan fitness ke total fitness populasi   
            populationFitness += fitness;

            //update individu terbaik
            if(bestIndividu == null || fitness > bestIndividu.getFitness()) {
                bestIndividu = individu;
            }
        }
        
    }

    //getter dan setter
    public List<Individu> getIndividuList() {
        return individuList;
    }

    public void setIndividuList(List<Individu> newPopulation) {
        this.individuList = newPopulation;
        evaluatePopulation();
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public double getPopulationFitness() {
        return populationFitness;
    }

    public Individu getBestIndividu() {
        return bestIndividu;
    }

    public double getAverageFitness() {
        return populationFitness / populationSize;
    }
}
