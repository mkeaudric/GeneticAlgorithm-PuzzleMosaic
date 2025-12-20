public class Individu implements Comparable<Individu> {
    private Kromosom kromosom;
    private double fitness;
    private boolean fitnessCalculated = false;
    private FitnessFunction fitnessFunction;

    public Individu(Kromosom kromosom, FitnessFunction fitnessFunction) {
        this.kromosom = kromosom;
        this.fitnessFunction = fitnessFunction;
        this.fitness = Double.NEGATIVE_INFINITY;
        this.fitnessCalculated = false;
    }

    //cek apakah fitness sudah dihitung
    public boolean isFitnessCalculated() {
        return fitnessCalculated;
    }

    //reset fitness agar dihitung ulang
    public void resetFitness() {
        this.fitness = Double.NEGATIVE_INFINITY;
        this.fitnessCalculated = false;
    }

    @Override
    public int compareTo(Individu other) {
        //urutan menurun berdasarkan fitness
        return Double.compare(other.fitness, this.fitness);
    }

    //hitung fitness menggunakan fungsi fitness
    public void calculateFitness() {
        fitness = fitnessFunction.calculateFitness(kromosom);
        fitnessCalculated = true;
    }

    //getter dan setter
    public Kromosom getKromosom() {
        return kromosom;
    }

    public double getFitness() {
        if (!isFitnessCalculated()) { // Cek cache
            this.fitness = fitnessFunction.calculateFitness(this.kromosom);
            this.fitnessCalculated = true;
        }
        return this.fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
        this.fitnessCalculated = true;
    }
    
}
