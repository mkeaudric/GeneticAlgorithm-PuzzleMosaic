import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Kelas Populasi merepresentasikan kumpulan individu (solusi kandidat) dalam satu generasi.
 * Bertanggung jawab untuk inisialisasi, evaluasi, dan manajemen elitism.
 * @author Kelompok AI Mosaic
 * @version 1.0
 */

public class Populasi {
    private List<Individu> individuList; //daftar individu dalam populasi
    private int populationSize; //jumlah individu dalam populasi
    private double populationFitness; //total fitness populasi
    private FitnessFunction fitnessFunction; //fungsi fitness untuk evaluasi individu
    private Individu bestIndividu; //individu terbaik dalam populasi, ambil beberapa (elitism)

    /**
     * Konstruktor untuk membuat populasi baru.
     * * @param populationSize  Jumlah total individu yang akan dibuat.
     * @param probabilitasHitam Probabilitas awal kemunculan kotak hitam pada kromosom.
     * @param fitnessFunction   Fungsi untuk mengevaluasi kualitas individu.
     * @param puzzle            Data puzzle Mosaic yang menjadi acuan.
     */
    public Populasi(int populationSize, double probabilitasHitam, FitnessFunction fitnessFunction, MosaicPuzzle puzzle) {
        this.populationSize = populationSize;
        this.fitnessFunction = fitnessFunction;
        this.individuList = new ArrayList<>();

        // panggil PenandaHeuristik sekali aja disini, nanti pas bikin kromosom biar ga panggil berkali" tinggal copy aja template
        Kromosom heuristicTemplate = new Kromosom(puzzle.getSize(), puzzle);
        try {
            PenandaHeuristik.setFixedAllele(heuristicTemplate);
        } catch(Exception e){
            System.out.println("Tidak ada fixed allele yang diset");
        }

        //loop selama populationSize untuk buat individu
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

    //method untukmenghitung fitness populasi
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

    //getter
    public List<Individu> getIndividuList() {
        return individuList;
    }

    /**
     * Mengambil daftar individu terbaik sebanyak k (elitism) untuk diteruskan ke generasi berikutnya.
     * *@param k Jumlah individu elit yang ingin diambil.
     * @return top-k individu untuk elitism sebagai salinan (preserve fitness).
     */
    public List<Individu> getTopElitism(int k) {
        List<Individu> elites = new ArrayList<>();
        //kalau elistism = 0, return kosong
        if (k <= 0) return elites;

        //loop untuk ambil top k individu dengan memasukkan salinan individu ke daftar elites
        for (int i = 0; i < k; i++) {
            elites.add(individuList.get(i).copyForElitism());
        }
        //return daftar elites
        return elites;
    }

    //method untuk bikin populasi baru
    public void setIndividuList(List<Individu> newPopulation) {
        this.individuList = newPopulation;
        evaluatePopulation();
    }

    //getter untuk atribut populasi
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
