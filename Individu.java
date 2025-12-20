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

    public void calculateFitness() {
        fitness = fitnessFunction.calculateFitness(kromosom);
        fitnessCalculated = true;
    }
    public Kromosom getKromosom() {
        return kromosom;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
        this.fitnessCalculated = true;
    }

    public boolean isFitnessCalculated() {
        return fitnessCalculated;
    }

    public void resetFitness() {
        this.fitness = Double.NEGATIVE_INFINITY;
        this.fitnessCalculated = false;
    }

    @Override
    public int compareTo(Individu other) {
        //urutan menurun berdasarkan fitness
        return Double.compare(other.fitness, this.fitness);
    }
    
}
