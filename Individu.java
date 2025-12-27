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

    
    //Copy utk elitism yg menyimpan fitness
    public Individu copyForElitism() {
        Kromosom kromCopy = this.kromosom.copy();
        Individu copy = new Individu(kromCopy, this.fitnessFunction);
        copy.fitness = this.fitness;
        copy.fitnessCalculated = this.fitnessCalculated;
        return copy;
    }

    //Copy utk menghasilkan anak yang akan di crossover/mutasi (fitness dihitung ulang)
    public Individu copyForOffspring() {
        Kromosom kromCopy = this.kromosom.copy();
        Individu copy = new Individu(kromCopy, this.fitnessFunction);
        copy.resetFitness();
        return copy;
    }

    // Backwards compatible alias: default copy preserves fitness (elitism behavior)
    // public Individu copy() {
    //     return copyForElitism();
    // }

    //reset fitness agar dihitung ulang
    public void resetFitness() {
        this.fitness = Double.NEGATIVE_INFINITY;
        this.fitnessCalculated = false;
    }

    @Override
    public int compareTo(Individu other) {
        // urutan menurun berdasarkan fitness (ensure fitness evaluated)
        return Double.compare(other.getFitness(), this.getFitness());
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
