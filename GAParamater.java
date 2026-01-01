public class GAParamater {
    private final int totalGeneration; // jumlah generasi yang akan dijalankan
    private final int populationSize; // ukuran populasi dalam setiap generasi
    private final double crossoverRate; // probabilitas terjadinya crossover antara dua individu
    private final double mutationRate; // probabilitas terjadinya mutasi pada individu
    private final double elitismPercent; // persentase individu terbaik yang dipertahankan ke generasi berikutnya
    private final int SelectionSize; // ukuran turnamen untuk seleksi individu
    private final String selectionMethod; // metode seleksi yang digunakan (misalnya "tournament", "roulette wheel", dll)
    private final String crossoverMethod; // metode crossover yang digunakan (misalnya "one-point", "two-point", dll)
    private final double probabilitasHitam; //
    

    public GAParamater(int totalGeneration, int populationSize, double crossoverRate, double mutationRate, double elitismPercent, int SelectionSize, String selectionMethod, String crossoverMethod, double probabilitasHitam) {
        this.totalGeneration = totalGeneration; 
        this.populationSize = populationSize; 
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.elitismPercent = elitismPercent;
        this.SelectionSize = SelectionSize;
        this.selectionMethod = selectionMethod;
        this.crossoverMethod = crossoverMethod;
        this.probabilitasHitam = probabilitasHitam;
    }

    public int getTotalGeneration() {
        return totalGeneration;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public double getElitismPercent() {
        return elitismPercent;
    }

    public int getSelectionSize() {
        return SelectionSize;
    }

    public String getSelectionMethod() {
        return selectionMethod;
    }

    public String getCrossoverMethod() {
        return crossoverMethod;
    }

    public double getProbabilitasHitam() {
        return probabilitasHitam;
    }
}