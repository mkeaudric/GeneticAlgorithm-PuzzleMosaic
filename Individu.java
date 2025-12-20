public class Individu implements Comparable<Individu> {
    private Kromosom kromosom;
    private double fitness;

    public Individu(Kromosom kromosom) {
        this.kromosom = kromosom;
        this.fitness = Double.NEGATIVE_INFINITY;
    }

    public Kromosom getKromosom() {
        return kromosom;
    }

    public double getFitness() {
        return fitness;
    }

    @Override
    public int compareTo(Individu other) {
        //urutan menurun berdasarkan fitness
        return Double.compare(other.fitness, this.fitness);
    }
    
}
