import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Kelas Crossover bertanggung jawab untuk melakukan operasi perkawinan silang 
 * antara dua individu (parent) untuk menghasilkan keturunan (offspring).
 * Mendukung metode Single-Point, Uniform, dan K-Point Crossover.
 * Referensi Utama: 
 * - Konsep Crossover: http://ijcs.net/ijcs/index.php/ijcs/article/view/4596/934
 * - Random Range: https://www.baeldung.com/java-generating-random-numbers-in-range
 * @author Kelompok AI Mosaic
 * @version 1.0
 */

public class Crossover {
    private FitnessFunction fitnessFunction;

    /**
     * Konstruktor untuk menginisialisasi Crossover dengan referensi fungsi fitness.
     * @param fitnessFunction Objek fungsi fitness untuk mengevaluasi individu baru.
     */
    public Crossover(FitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
    }

    /**
     * Metode utama untuk melakukan crossover berdasarkan parameter yang dipilih.
     * @param parent1 Individu orang tua pertama.
     * @param parent2 Individu orang tua kedua.
     * @param crossoverMethod Nama metode crossover ("single-point", "uniform", "k-point").
     * @param k Jumlah titik potong (hanya digunakan untuk k-point).
     * @return Array berisi dua individu baru sebagai keturunan.
     */
    public Individu[] crossover(Individu parent1, Individu parent2, String crossoverMethod, int k) {
        switch (crossoverMethod) {
            case "single-point":
                    return singlePointCrossover(parent1, parent2);
            case "uniform":
                    return uniformCrossover(parent1, parent2);
            case "k-point":
                    return KPointCrossover(parent1, parent2, k);
            default:
                System.err.println("Tidak ada metode crossover " +  crossoverMethod + ". Balik ke default : single-point");
                return singlePointCrossover(parent1, parent2);
        }
    }
    
    /**
     * Melakukan Single-Point Crossover.
     * Menentukan satu titik acak dan menukar seluruh segmen setelah titik tersebut.
     * @param parent1 Orang tua 1.
     * @param parent2 Orang tua 2.
     * @return Array berisi dua keturunan.
     */
    private Individu[] singlePointCrossover(Individu parent1, Individu parent2) {
        int i, length = parent1.getKromosom().getLength();
        int crossoverPoint = RNG.rand.nextInt(length-1) + 1; 
        // kalo dari 0 (nextInt(length-1) aja gapake + 1), dia jadi sama persis sama satu parentnya
        
        Kromosom firstChild = parent1.getKromosom().copy();
        Kromosom secondChild = parent2.getKromosom().copy();

        for(i=crossoverPoint; i < length; i++){
            // ini karena kita udah setFixedAllele untuk satu populasi, kita gaperlu cek sebenernya karena pasti sama warnanya untuk yang fixed
            // tapi kita pake aja biar hemat resource komputasi (kalo udah pasti sama gaperlu nuker)
            // karena pasti sama yang fixed, saya cuma cek 1 anak aja
            if(!firstChild.getFixedAllele(i)){
                // tuker allele
                boolean temp = firstChild.getBit(i);
                firstChild.setBit(i, secondChild.getBit(i));
                secondChild.setBit(i, temp);
            }
        }

        return new Individu[] {new Individu(firstChild, fitnessFunction), new Individu(secondChild, fitnessFunction)};
    }

    /**
     * Melakukan Uniform Crossover.
     * Setiap bit memiliki peluang 50% untuk ditukar antar orang tua.
     * @param parent1 Orang tua 1.
     * @param parent2 Orang tua 2.
     * @return Array berisi dua keturunan.
     */
    private Individu[] uniformCrossover(Individu parent1, Individu parent2) {
        int i, length = parent1.getKromosom().getLength();

        Kromosom firstChild = parent1.getKromosom().copy();
        Kromosom secondChild = parent2.getKromosom().copy();

        for(i=0; i < length; i++){
            if(!firstChild.getFixedAllele(i)){
                // ada probabilitas 50% ketuker, ato engga ketuker
                if(RNG.rand.nextDouble() < 0.5){
                    // tuker allele
                    boolean temp = firstChild.getBit(i);
                    firstChild.setBit(i, secondChild.getBit(i));
                    secondChild.setBit(i, temp);
                }
            }
        }

        return new Individu[] {new Individu(firstChild, fitnessFunction), new Individu(secondChild, fitnessFunction)};
    }

    /**
     * Melakukan K-Point Crossover.
     * Menentukan 'k' titik potong dan menukar segmen di antara titik-titik tersebut.
     * @param parent1 Orang tua 1.
     * @param parent2 Orang tua 2.
     * @param k Jumlah titik potong.
     * @return Array berisi dua keturunan.
     */
    private Individu[] KPointCrossover(Individu parent1, Individu parent2, int k) {
        int i, length = parent1.getKromosom().getLength();

        // simpen di list biar ga ada yg nabrak (2 point sama pas di random)
        List<Integer> cutPoints = new ArrayList<>();
        while(cutPoints.size() < k){
            int point = RNG.rand.nextInt(length-1) + 1;
            if(!cutPoints.contains(point)) cutPoints.add(point);
        }
        Collections.sort(cutPoints);

        Kromosom firstChild = parent1.getKromosom().copy();
        Kromosom secondChild = parent2.getKromosom().copy();

        boolean swap = false; // kalo swap nya true, baru tuker, kalo false gausah tuker
        int cutIdx = 0;

        for(i=0; i < length; i++){
            if(cutIdx < cutPoints.size() && i == cutPoints.get(cutIdx)){
                swap = !swap;
                cutIdx++;
            }
            if(swap && !firstChild.getFixedAllele(i)){
                // tuker allele
                boolean temp = firstChild.getBit(i);
                firstChild.setBit(i, secondChild.getBit(i));
                secondChild.setBit(i, temp);
            }
        }

        return new Individu[] {new Individu(firstChild, fitnessFunction), new Individu(secondChild, fitnessFunction)};
    }
}
