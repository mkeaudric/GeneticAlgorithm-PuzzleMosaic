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

        String rawMethod = param.getCrossoverMethod() == null ? "" : param.getCrossoverMethod().trim().toLowerCase();

        // method lama gw ganti, jadi kalo mau k-point pake "k-point-n"
        // Default nya
        String method = "single-point"; 
        int k = 1; 

        if (rawMethod.contains("single") || rawMethod.contains("one")) {
            method = "single-point";
            System.out.println("Crossover method recognized as: single-point");
        } else if (rawMethod.contains("uniform")) {
            method = "uniform";
            System.out.println("Crossover method recognized as: uniform");
        } else if (rawMethod.startsWith("k-point") || rawMethod.startsWith("k_point")) {
            method = "k-point";
            // Parsing angka di akhirannya
            String[] parts = rawMethod.split("[-_]"); // regex gajelas njir, sumber : https://stackoverflow.com/questions/2911005/how-to-cut-string-with-two-regular-expression-and
            // isi parts nya jadi k, point, n 
            if (parts.length >= 3) {
                try {
                    k = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    System.out.println("Format angka k salah. Default k=1 (sama aja kek single-point).");
                }
            }
            System.out.println("Crossover method recognized as: k-point with k = " + k);
        } else {
            System.out.println("Warning: unknown method '" + rawMethod + "'. Defaulting to single-point.");
        }

        // BAGIAN GA

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
        // debug, print gridnya
        populasi.getBestIndividu().getKromosom().printCurrentFixedKromosomAsGrid();

        Crossover crossover = new Crossover(fitnessFunction);
        
        for(int generasi = 0; generasi < param.getTotalGeneration(); generasi++) {
            List<Individu> newPopulation = new ArrayList<>(param.getPopulationSize());

            // 1. elitism
            int jumlahElitism = (int)(param.getElitismPercent() * param.getPopulationSize());
            jumlahElitism = Math.max(0, Math.min(jumlahElitism, populasi.getPopulationSize()));
            newPopulation.addAll(populasi.getTopElitism(jumlahElitism));

            // 2. isi populasi berikutnya
            while (newPopulation.size() < param.getPopulationSize()) {
                Individu parent1 = seleksi.select(populasi, param);
                Individu parent2 = seleksi.select(populasi, param);

                // Crossover
                Individu[] offsprings;

                if(RNG.rand.nextDouble() < param.getCrossoverRate()) {
                    offsprings = crossover.crossover(parent1, parent2, method, k);
                } else {
                    // ga crossover, anaknya sama aja kaya parentnya (tapi harus copy for offspring biar fitness nya direset)
                    offsprings = new Individu[] { parent1.copyForOffspring(), parent2.copyForOffspring()};
                }

                // mutasi
                for (Individu offspring : offsprings) {
                    if (newPopulation.size() < param.getPopulationSize()) {
                        // Mutasi per bit
                        boolean mutated = false;
                        Kromosom kromosom = offspring.getKromosom();
                        for (int i = 0; i < kromosom.getLength(); i++) {
                            if (!kromosom.getFixedAllele(i)) {
                                if (RNG.rand.nextDouble() < param.getMutationRate()) {
                                    kromosom.setBit(i, !kromosom.getBit(i));
                                    mutated = true;
                                }
                            }
                        }
                        // PenandaHeuristik.heuristicFillCertain(offspring.getKromosom());
                        if (mutated) offspring.resetFitness();
                        newPopulation.add(offspring);
                    }
                }
            }

            // 3. update populasi
            populasi.setIndividuList(newPopulation);

            // 4. debug
            System.out.printf("Generasi %d | Best Fitness: %.6f | Avg Fitness: %.6f%n", 
                            generasi, populasi.getBestIndividu().getFitness(), populasi.getAverageFitness());

            // kalo ada fitness yang == 1 (ketemu solusi), terminate
            if (Math.abs(populasi.getBestIndividu().getFitness() - 1.0) < 1e-6) {
                System.out.println("Solusi ditemukan pada generasi " + generasi);
                populasi.getBestIndividu().getKromosom().printCurrentKromosomAsGrid();
                break;
            }
        }
    }
}