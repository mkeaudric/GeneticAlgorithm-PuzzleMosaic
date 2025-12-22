// sumber : https://natureofcode.com/genetic-algorithms/

import java.util.ArrayList;
import java.util.List;

public class MosaicSolverGA {
    public static void main(String[] args) throws Exception {
        System.out.println("java MosaicSolver <filePeta.txt> <fileParameter.txt>");

        String filePeta = args[0];
        String fileParameter = args[1];
        int seed = 12345; // hardcode seed nya
        RNG.initialize(seed);

        MosaicPuzzle puzzle = InputReader.bacaPuzzle(filePeta);
        GAParamater param = InputReader.bacaParameter(fileParameter);

        // bagian GA

        //import fitness function
        FitnessFunction fitnessFunction = new FitnessFunction(puzzle);
        Selection seleksi = new Selection();
        Crossover crossover = new Crossover();

        // 1. inisialisasi populasi
        Populasi populasi = new Populasi(param.getPopulationSize(), param.getProbabilitasHitam(), fitnessFunction, puzzle);

        // tampilkan ringkasan populasi awal
        System.out.println("=== Inisialisasi Populasi ===");
        System.out.println("Ukuran populasi: " + populasi.getPopulationSize());
        System.out.printf("Best fitness awal: %.6f%n", populasi.getBestIndividu().getFitness());
        System.out.printf("Average fitness awal: %.6f%n", populasi.getAverageFitness());
        System.out.println("Best individu (visual):");
        printKromosom(populasi.getBestIndividu().getKromosom());

        for(int generasi = 0; generasi < param.getTotalGeneration(); generasi++) {
            List<Individu> newPopulation = new ArrayList<>(param.getPopulationSize());

            // 1. Elitism
            int jumlahElitism = (int)(param.getElitismPercent() * param.getPopulationSize());
            // clamp jumlahElitism ke rentang yang valid
            jumlahElitism = Math.max(0, Math.min(jumlahElitism, populasi.getPopulationSize()));
            newPopulation.addAll(populasi.getTopElitism(jumlahElitism));
            // 2. seleksi parent untuk populasi berikutnya

            // 3. crossover & mutasi
            // 4. ulang dari step 2 sampe fitness = 1 (ketemu solusi)
        }
        
       
    }

    // helper: print kromosom sebagai grid (# = item/black, . = white)
    private static void printKromosom(Kromosom k) {
        int size = k.getSize();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                System.out.print(k.getBit(r, c) ? "#" : ".");
            }
            System.out.println();
        }
    }

}