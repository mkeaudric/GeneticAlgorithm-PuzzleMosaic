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

        // panggil PenandaHeuristik sekali aja disini, nanti pas bikin kromosom biar ga panggil berkali" tinggal copy aja template
        Kromosom heuristicTemplate = new Kromosom(puzzle.getSize(), puzzle);
        try {
            PenandaHeuristik.setFixedAllele(heuristicTemplate);
        } catch(Exception e){
            System.out.println("Tidak ada fixed allele yang diset");
        }

        for(int i=0; i<populationSize; i++) {
            //buat kromosom acak
            Kromosom kromosom = Kromosom.createRandomKromosom(probabilitasHitam, puzzle, heuristicTemplate);

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

        for(Individu individu : individuList) {
            populationFitness += individu.getFitness();
        }

        //Sort individu berdasarkan fitness
        Collections.sort(individuList);

        //Ambil individu terbaik
        if (!individuList.isEmpty()) {
            bestIndividu = individuList.get(0).copyForElitism();
        }
    }

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

    public Individu getBestIndividu() {
        return bestIndividu;
    }

    public double getAverageFitness() {
        return populationFitness / populationSize;
    }
}
