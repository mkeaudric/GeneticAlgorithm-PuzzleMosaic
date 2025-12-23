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

        // cetak parameter GA untuk verifikasi
        System.out.println("=== GA Parameters ===");
        System.out.println("Total generations: " + param.getTotalGeneration());
        System.out.println("Population size: " + param.getPopulationSize());
        System.out.printf("Crossover rate: %.4f%n", param.getCrossoverRate());
        System.out.printf("Mutation rate: %.4f%n", param.getMutationRate());
        System.out.printf("Elitism percent: %.4f%n", param.getElitismPercent());
        System.out.println("Tournament size: " + param.getTournamentSize());
        System.out.println("Selection method: '" + param.getSelectionMethod() + "'");
        System.out.println("Crossover method: '" + param.getCrossoverMethod() + "'");

        // simple validation for crossover method (accept 'one-point' with hyphen)
        String cm = param.getCrossoverMethod() == null ? "" : param.getCrossoverMethod().trim().toLowerCase();
        if (cm.equals("single-point") || cm.equals("single_point") || cm.equals("singlepoint")) {
            System.out.println("Crossover method recognized as: single-point");
        } else if (cm.equals("uniform") || cm.equals("uniform-crossover") || cm.equals("uniform_crossover")) {
            System.out.println("Crossover method recognized as: uniform");
        } else if (cm.equals("two-point") || cm.equals("two_point") || cm.equals("two-point")) {
            System.out.println("Crossover method recognized as: two-point");
        } else if (cm.isEmpty()) {
            System.out.println("Crossover method is empty");
        } else {
            System.out.println("Warning: unknown crossover method '" + param.getCrossoverMethod() + "' — expected 'one-point' or 'uniform'");
        }

        // bagian GA

        //import fitness function
        FitnessFunction fitnessFunction = new FitnessFunction(puzzle);
        Selection seleksi = new Selection();

        // 1. inisialisasi populasi
        Populasi populasi = new Populasi(param.getPopulationSize(), param.getProbabilitasHitam(), fitnessFunction, puzzle);

        // tampilkan ringkasan populasi awal
        System.out.println("=== Inisialisasi Populasi ===");
        System.out.println("Ukuran populasi: " + populasi.getPopulationSize());
        System.out.printf("Best fitness awal: %.6f%n", populasi.getBestIndividu().getFitness());
        System.out.printf("Average fitness awal: %.6f%n", populasi.getAverageFitness());
        System.out.println("Best individu (visual):");
        printKromosom(populasi.getBestIndividu().getKromosom());

        Crossover crossover = new Crossover(fitnessFunction);
        
        // ... (Inside the generations loop)
        for(int generasi = 0; generasi < param.getTotalGeneration(); generasi++) {
            List<Individu> newPopulation = new ArrayList<>(param.getPopulationSize());

            // 1. ELITISM: Copy top performers directly
            int jumlahElitism = (int)(param.getElitismPercent() * param.getPopulationSize());
            jumlahElitism = Math.max(0, Math.min(jumlahElitism, populasi.getPopulationSize()));
            newPopulation.addAll(populasi.getTopElitism(jumlahElitism));

            // 2. FILL THE REST OF THE POPULATION
            while (newPopulation.size() < param.getPopulationSize()) {
                // SELECTION: Pick two parents
                Individu parent1 = seleksi.tournamentSelection(populasi, param.getTournamentSize());
                Individu parent2 = seleksi.tournamentSelection(populasi, param.getTournamentSize());

                // CROSSOVER: Create a child
                Individu[] offspringArray = crossover.crossover(parent1, parent2, param.getCrossoverMethod());
                Individu offspring = offspringArray[0];

                // MUTATION: Apply your mutation logic to the NEW child
                if (RNG.rand.nextDouble() < param.getMutationRate()) {
                    Kromosom k = offspring.getKromosom();
                    double someBitMutationRate = 1.0 / k.getlength(); 
                    
                    for (int i = 0; i < k.getlength(); i++) {
                        if (!k.getFixedAllele(i)) {
                            if (RNG.rand.nextDouble() < someBitMutationRate) { 
                                k.setBit(i, !k.getBit(i));
                                offspring.resetFitness(); 
                            }
                        }
                    }
                }
                newPopulation.add(offspring);
            }

            // 3. UPDATE THE POPULATION
            populasi.setIndividuList(newPopulation);

            // 4. PRINT PROGRESS
            System.out.printf("Generasi %d | Best Fitness: %.6f | Avg Fitness: %.6f%n", 
                            generasi, populasi.getBestIndividu().getFitness(), populasi.getAverageFitness());

            // Exit early if solution found
            if (populasi.getBestIndividu().getFitness() >= 1.0) {
                System.out.println("Solusi ditemukan pada generasi " + generasi);
                break;
            }
        }

        for(int generasi = 0; generasi < param.getTotalGeneration(); generasi++) {
            List<Individu> newPopulation = new ArrayList<>(param.getPopulationSize());

            // 1. Elitism
            int jumlahElitism = (int)(param.getElitismPercent() * param.getPopulationSize());

            // clamp jumlahElitism ke rentang yang valid
            jumlahElitism = Math.max(0, Math.min(jumlahElitism, populasi.getPopulationSize()));

            //masukin individu terbaik ke populasi baru
            newPopulation.addAll(populasi.getTopElitism(jumlahElitism));
            // 2. seleksi parent untuk populasi berikutnya

            // 3. crossover & mutasi
            
            for (Individu offspring : newPopulation) {
                if (RNG.rand.nextDouble() < param.getMutationRate()) {
                    Kromosom k = offspring.getKromosom();

                    double someBitMutationRate = 1.0 / k.getlength(); // 0.01; mutation rate
                    
                    for (int i = 0; i < k.getlength(); i++) {
                        // Check if this bit is allowed to change
                        if (!k.getFixedAllele(i)) {
                            // Apply mutation to individual bits
                            if (RNG.rand.nextDouble() < someBitMutationRate) { 
                                boolean currentBit = k.getBit(i);
                                k.setBit(i, !currentBit);
                                offspring.resetFitness(); // Ensure fitness is recalculated 
                            }
                        }
                    }
                }
            }
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