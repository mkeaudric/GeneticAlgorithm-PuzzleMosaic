import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Populasi {
    private List<Individu> individuList; //daftar individu dalam populasi
    private int populationSize; //jumlah individu dalam populasi
    private double populationFitness; //total fitness populasi
    private FitnessFunction fitnessFunction; //fungsi fitness untuk evaluasi individu
    private Individu bestIndividu; //individu terbaik dalam populasi, ambil beberapa (elitism)
    private MosaicPuzzle puzzle;

    public Populasi(int populationSize, double probabilitasHitam, FitnessFunction fitnessFunction, MosaicPuzzle puzzle) {
        this.populationSize = populationSize;
        this.fitnessFunction = fitnessFunction;
        this.individuList = new ArrayList<>();
        this.puzzle = puzzle;

        for(int i=0; i<populationSize; i++) {
            //buat kromosom acak
            Kromosom kromosom = Kromosom.createRandomKromosom(probabilitasHitam, puzzle);

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
                bestIndividu = individu.copyForElitism();
            }
        }
        Collections.sort(individuList);
    }

    // public void updatePopulation(List<Individu> newPopulation) {
    //     this.individuList = newPopulation;
    //     this.populationSize = newPopulation.size();
    //     evaluatePopulation();
    // }

    //getter dan setter
    public List<Individu> getIndividuList() {
        return individuList;
    }

    // Mengembalikan top-k individu untuk elitism sebagai salinan (preserve fitness)
    public List<Individu> getTopElitism(int k) {
        List<Individu> elites = new ArrayList<>();
        if (k <= 0) return elites;
    
        k = Math.min(k, individuList.size());
        for (int i = 0; i < k; i++) {
            elites.add(individuList.get(i).copyForElitism());
        }
        return elites;
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
