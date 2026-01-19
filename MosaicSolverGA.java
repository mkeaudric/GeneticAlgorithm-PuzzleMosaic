// sumber : https://natureofcode.com/genetic-algorithms/

import java.util.ArrayList;
import java.util.List;

public class MosaicSolverGA {
    public static void main(String[] args) throws Exception {
        //System.out.println("java MosaicSolver <filePeta.txt> <fileParameter.txt>");

        String filePeta = args[0];
        String fileParameter = args[1];
        int seed = 12349; // hardcode seed nya
        RNG.initialize(seed);

        MosaicPuzzle puzzle = InputReader.bacaPuzzle(filePeta);
        GAParamater param = InputReader.bacaParameter(fileParameter);

        System.out.println("seed :" + seed);

        // cetak parameter GA untuk verifikasi
        System.out.println("=== GA Parameters ===");
        System.out.println("Total generations: " + param.getTotalGeneration());
        System.out.println("Population size: " + param.getPopulationSize());
        System.out.printf("Crossover rate: %.4f%n", param.getCrossoverRate());
        System.out.printf("Mutation rate: %.4f%n", param.getMutationRate());
        System.out.printf("Elitism percent: %.4f%n", param.getElitismPercent());
        System.out.println("Tournament size: " + param.getSelectionSize());
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
        populasi.getBestIndividu().getKromosom().printCurrentFixedKromosom();

        Crossover crossover = new Crossover(fitnessFunction);

        long startTime = System.nanoTime();

        boolean ditemukan = false;
        
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
                        boolean perluResetFitness = false;
                        Kromosom kromosom = offspring.getKromosom();
                        for (int i = 0; i < kromosom.getLength(); i++) {
                            if (!kromosom.getFixedAllele(i)) {
                                if (RNG.rand.nextDouble() < param.getMutationRate()) {
                                    kromosom.setBit(i, !kromosom.getBit(i));
                                    perluResetFitness = true;
                                }
                            }
                        }
                        boolean adaPerubahanHeuristik;
                        int limit = 0;
                        do {
                            adaPerubahanHeuristik = PenandaHeuristik.heuristicFillCertain(offspring.getKromosom());
                            if(adaPerubahanHeuristik){
                                perluResetFitness = true;
                            }
                            limit++;
                        } while(adaPerubahanHeuristik && limit < 3);

                        if (perluResetFitness) offspring.resetFitness();
                        newPopulation.add(offspring);
                    }
                }
            }

            // 3. update populasi
            populasi.setIndividuList(newPopulation);

            // 4. debug
            if(generasi % 5000 == 0){
                System.out.printf("Generasi %d | Best Fitness: %.6f | Avg Fitness: %.6f%n", 
                                generasi, populasi.getBestIndividu().getFitness(), populasi.getAverageFitness());
            }

            
            // kalo ada fitness yang == 1 (ketemu solusi), terminate
            if (Math.abs(populasi.getBestIndividu().getFitness() - 1.0) < 1e-6) {
                long endTime = System.nanoTime();
                double totalTime = (endTime - startTime) / 1_000_000_000.0;

                System.out.println("Solusi ditemukan pada generasi " + generasi);
                System.out.printf("Best Fitness: %.6f\n", populasi.getBestIndividu().getFitness());
                System.out.printf("Total running time: %.6f detik%n", totalTime);
                populasi.getBestIndividu().getKromosom().printCurrentKromosom();
                ditemukan = true;
                break;
                
            }
            
        }
        if(!ditemukan){
            long endTime = System.nanoTime();
            double totalTime = (endTime - startTime) / 1_000_000_000.0;
            System.out.printf("Best Fitness: %.6f\n", populasi.getBestIndividu().getFitness());
            System.out.printf("Total running time: %.6f detik%n", totalTime);
            populasi.getBestIndividu().getKromosom().printCurrentKromosom();
        }
    System.out.println();
    }
    
}

// import java.util.ArrayList;
// import java.util.List;

// public class MosaicSolverGA {

//     // Class kecil untuk menampung hasil run agar bisa dihitung rata-ratanya
//     private static class RunResult {
//         double fitness;
//         int generations;
//         double time;

//         public RunResult(double fitness, int generations, double time) {
//             this.fitness = fitness;
//             this.generations = generations;
//             this.time = time;
//         }
//     }

//     public static void main(String[] args) throws Exception {
//         if (args.length < 2) {
//             System.out.println("Usage: java MosaicSolverGA <filePeta.txt> <fileParameter.txt>");
//             return;
//         }

//         String filePeta = args[0];
//         String fileParameter = args[1];

//         MosaicPuzzle puzzle = InputReader.bacaPuzzle(filePeta);
//         GAParamater param = InputReader.bacaParameter(fileParameter);

//         int[] seeds = {12345, 12346, 12347, 12348, 12349};
        
//         // Variabel untuk akumulasi rata-rata
//         double totalFitness = 0;
//         long totalGenerations = 0;
//         double totalTime = 0;
//         int count = 0;

//         for (int seed : seeds) {
//             // Jalankan solve dan tangkap hasilnya
//             RunResult result = solve(puzzle, param, seed);
            
//             // Tambahkan ke total
//             totalFitness += result.fitness;
//             totalGenerations += result.generations;
//             totalTime += result.time;
//             count++;

//             System.out.println();
//             System.gc(); // Garbage collection antar run
//         }

//         // === CETAK RATA-RATA SETELAH SEMUA SEED SELESAI ===
//         System.out.println("==================================================");
//         System.out.println("AVERAGE STATISTICS (" + count + " RUNS)");
//         System.out.println("==================================================");
//         System.out.printf("Average Best Fitness : %.6f%n", totalFitness / count);
//         System.out.printf("Average Generations  : %.2f%n", (double)totalGenerations / count);
//         System.out.printf("Average Time         : %.6f detik%n", totalTime / count);
//         System.out.println("==================================================");
//     }

//     // Return type diubah dari void ke RunResult biar bisa lapor balik ke main
//     public static RunResult solve(MosaicPuzzle puzzle, GAParamater param, int seed) {
//         RNG.initialize(seed);

//         System.out.println("Seed: " + seed);
//         System.out.println("=== GA Parameters ===");
//         System.out.println("Total generations: " + param.getTotalGeneration());
//         System.out.println("Population size: " + param.getPopulationSize());
//         System.out.printf("Crossover rate: %.4f%n", param.getCrossoverRate());
//         System.out.printf("Mutation rate: %.4f%n", param.getMutationRate());
//         System.out.printf("Elitism percent: %.4f%n", param.getElitismPercent());
//         System.out.println("Tournament size: " + param.getSelectionSize());
//         System.out.println("Selection method: '" + param.getSelectionMethod() + "'");
//         System.out.println("Crossover method: '" + param.getCrossoverMethod() + "'");

//         // Setup Method Crossover
//         String rawMethod = param.getCrossoverMethod() == null ? "" : param.getCrossoverMethod().trim().toLowerCase();
//         String method = "single-point"; 
//         int k = 1; 

//         if (rawMethod.contains("single") || rawMethod.contains("one")) {
//             method = "single-point";
//         } else if (rawMethod.contains("uniform")) {
//             method = "uniform";
//         } else if (rawMethod.startsWith("k-point") || rawMethod.startsWith("k_point")) {
//             method = "k-point";
//             String[] parts = rawMethod.split("[-_]");
//             if (parts.length >= 3) {
//                 try {
//                     k = Integer.parseInt(parts[2]);
//                 } catch (NumberFormatException e) {
//                     System.out.println("Format angka k salah. Default k=1 (sama aja kek single-point).");
//                 }
//             }
//         } else {
//             System.out.println("Warning: unknown method '" + rawMethod + "'. Defaulting to single-point.");
//         }

//         // BAGIAN GA
//         FitnessFunction fitnessFunction = new FitnessFunction(puzzle);
//         Selection seleksi = new Selection();
//         Populasi populasi = new Populasi(param.getPopulationSize(), param.getProbabilitasHitam(), fitnessFunction, puzzle);

//         System.out.println("=== Inisialisasi Populasi ===");
//         System.out.println("Ukuran populasi: " + populasi.getPopulationSize());
//         System.out.printf("Best fitness awal: %.6f%n", populasi.getBestIndividu().getFitness());
//         System.out.printf("Average fitness awal: %.6f%n", populasi.getAverageFitness());
//         System.out.println("Best individu (visual):");
//         populasi.getBestIndividu().getKromosom().printCurrentFixedKromosom(); 

//         Crossover crossover = new Crossover(fitnessFunction);
//         long startTime = System.nanoTime();
//         boolean ditemukan = false;
        
//         // Variabel untuk nyimpen hasil akhir buat return
//         double finalFitness = 0;
//         int finalGen = 0;
//         double finalTime = 0;

//         for(int generasi = 0; generasi < param.getTotalGeneration(); generasi++) {
//             List<Individu> newPopulation = new ArrayList<>(param.getPopulationSize());

//             // 1. Elitism
//             int jumlahElitism = (int)(param.getElitismPercent() * param.getPopulationSize());
//             jumlahElitism = Math.max(0, Math.min(jumlahElitism, populasi.getPopulationSize()));
//             newPopulation.addAll(populasi.getTopElitism(jumlahElitism));

//             // 2. Isi populasi berikutnya
//             while (newPopulation.size() < param.getPopulationSize()) {
//                 Individu parent1 = seleksi.select(populasi, param);
//                 Individu parent2 = seleksi.select(populasi, param);

//                 Individu[] offsprings;
//                 if(RNG.rand.nextDouble() < param.getCrossoverRate()) {
//                     offsprings = crossover.crossover(parent1, parent2, method, k);
//                 } else {
//                     offsprings = new Individu[] { parent1.copyForOffspring(), parent2.copyForOffspring()};
//                 }

//                 for (Individu offspring : offsprings) {
//                     if (newPopulation.size() < param.getPopulationSize()) {
//                         boolean perluResetFitness = false;
//                         Kromosom kromosom = offspring.getKromosom();

//                         for (int i = 0; i < kromosom.getLength(); i++) {
//                             if (!kromosom.getFixedAllele(i)) {
//                                 if (RNG.rand.nextDouble() < param.getMutationRate()) {
//                                     kromosom.setBit(i, !kromosom.getBit(i));
//                                     perluResetFitness = true;
//                                 }
//                             }
//                         }
//                         // 2. LOGIKA SCALING HEURISTIK
//                         // Kita tentukan interval berdasarkan progres generasi
//                         int intervalHeuristik;
//                         if (generasi < 100000) {
//                             intervalHeuristik = 500; // Awal: Sangat jarang, fokus ke eksplorasi acak
//                         } else if (generasi < 300000) {
//                             intervalHeuristik = 200; // Menengah: Mulai sering merapikan
//                         } else {
//                             intervalHeuristik = 100;  // Akhir: Sangat rajin untuk finishing detail
//                         }

//                         // Eksekusi Heuristik berdasarkan Interval atau Peluang Kecil (5%)
//                         if (generasi % intervalHeuristik == 0 || RNG.rand.nextDouble() < 0.05) {
//                             boolean adaPerubahan;
//                             int limit = 0;
//                             do {
//                                 adaPerubahan = PenandaHeuristik.heuristicFillCertain(offspring.getKromosom());
//                                 if (adaPerubahan) perluResetFitness = true;
//                                 limit++;
//                             } while (adaPerubahan && limit < 3); // Limit 3 cukup untuk performa
//                         }

//                         if (perluResetFitness) offspring.resetFitness();
//                         newPopulation.add(offspring);
//                     }
//                 }
//             }

//             // 3. Update populasi
//             populasi.setIndividuList(newPopulation);

//             // 4. Debug (per 10000 generasi)
//             if (generasi % 10000 == 0) {
//                 System.out.printf("Generasi %d | Best Fitness: %.6f | Avg Fitness: %.6f%n", 
//                                 generasi, populasi.getBestIndividu().getFitness(), populasi.getAverageFitness());
//             }

//             if (Math.abs(populasi.getBestIndividu().getFitness() - 1.0) < 1e-6) {
//                 long endTime = System.nanoTime();
//                 double totalTime = (endTime - startTime) / 1_000_000_000.0;

//                 System.out.println("Solusi ditemukan pada generasi " + generasi);
//                 System.out.printf("Best Fitness: %.6f\n", populasi.getBestIndividu().getFitness());
//                 System.out.printf("Total running time: %.6f detik%n", totalTime);
//                 populasi.getBestIndividu().getKromosom().printCurrentKromosom();
//                 ditemukan = true;
                
//                 // Set result sukses
//                 finalFitness = populasi.getBestIndividu().getFitness();
//                 finalGen = generasi;
//                 finalTime = totalTime;
                
//                 // Return result langsung
//                 return new RunResult(finalFitness, finalGen, finalTime);
//             }
//         }
        
//         if (!ditemukan) {
//             long endTime = System.nanoTime();
//             double totalTime = (endTime - startTime) / 1_000_000_000.0;
//             System.out.printf("Best Fitness: %.6f\n", populasi.getBestIndividu().getFitness());
//             System.out.printf("Total running time: %.6f detik%n", totalTime);
//             populasi.getBestIndividu().getKromosom().printCurrentKromosom();
            
//             finalFitness = populasi.getBestIndividu().getFitness();
//             finalGen = param.getTotalGeneration();
//             finalTime = totalTime;
            
//             return new RunResult(finalFitness, finalGen, finalTime);
//         }

//         return new RunResult(0, 0, 0);
//     }
// }