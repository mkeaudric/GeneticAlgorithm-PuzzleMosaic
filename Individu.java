/**
 * Kelas Individu merepresentasikan satu solusi kandidat dalam populasi.
 * Setiap individu memiliki kromosom (genetik) dan nilai fitness yang menentukan kualitas solusi.
 * @author Kelompok AI Mosaic
 * @version 1.0
 */
public class Individu implements Comparable<Individu> {
    private Kromosom kromosom;
    private double fitness;
    private boolean fitnessCalculated = false;
    private FitnessFunction fitnessFunction;

    /**
     * Konstruktor untuk membuat individu baru dengan kromosom tertentu.
     * @param kromosom Struktur data genetik individu.
     * @param fitnessFunction pengevaluasi kualitas individu.
     */
    public Individu(Kromosom kromosom, FitnessFunction fitnessFunction) {
        this.kromosom = kromosom;
        this.fitnessFunction = fitnessFunction;
        this.fitness = Double.NEGATIVE_INFINITY;
        this.fitnessCalculated = false;
    }

    /**
     * Mengecek status perhitungan fitness.
     * @return true jika fitness sudah dihitung dan tersimpan.
     */
    public boolean isFitnessCalculated() {
        return fitnessCalculated;
    }

    /**
     * Membuat salinan individu untuk keperluan Elitism.
     * Fitness tetap dipertahankan karena individu ini tidak mengalami perubahan genetik.
     * @return Objek individu baru yang identik secara genetik dan nilai fitness.
     */
    public Individu copyForElitism() {
        Kromosom kromCopy = this.kromosom.copy();
        Individu copy = new Individu(kromCopy, this.fitnessFunction);
        copy.fitness = this.fitness;
        copy.fitnessCalculated = this.fitnessCalculated;
        return copy;
    }

    /**
     * Membuat salinan individu untuk diproses sebagai keturunan (offspring).
     * Fitness direset karena individu ini akan mengalami modifikasi (crossover/mutasi).
     * @return Objek individu baru dengan fitness yang harus dihitung ulang.
     */
    public Individu copyForOffspring() {
        Kromosom kromCopy = this.kromosom.copy();
        Individu copy = new Individu(kromCopy, this.fitnessFunction);
        copy.resetFitness();
        return copy;
    }

    //reset fitness agar dihitung ulang
    public void resetFitness() {
        this.fitness = Double.NEGATIVE_INFINITY;
        this.fitnessCalculated = false;
    }

    /**
     * Membandingkan individu saat ini dengan individu lain berdasarkan nilai fitness.
     * @param other Individu pembanding.
     * @return Nilai negatif jika fitness individu ini lebih besar (urutan menurun/descending).
     */
    @Override
    public int compareTo(Individu other) {
        // urutan menurun berdasarkan fitness (ensure fitness evaluated)
        return Double.compare(other.getFitness(), this.getFitness());
    }

    /**
     * Mengambil objek kromosom.
     * @return Objek Kromosom.
     */
    public Kromosom getKromosom() {
        return kromosom;
    }

    /**
     * Mengambil nilai fitness individu. 
     * @return Nilai fitness akhir.
     */
    public double getFitness() {
        if (!isFitnessCalculated()) { // Cek cache
            this.fitness = fitnessFunction.calculateFitness(this.kromosom);
            this.fitnessCalculated = true;
        }
        return this.fitness;
    }

    /**
     * Mengatur nilai fitness secara manual (jika diperlukan oleh mekanisme eksternal).
     * @param fitness Nilai fitness baru.
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
        this.fitnessCalculated = true;
    }  
}
