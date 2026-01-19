import java.util.ArrayList;
import java.util.List;

/**
 * Kelas Utama, mengkoordinasikan seluruh proses Algoritma Genetik untuk menyelesaikan Mosaic
 * Menangani pembacaan input dan eksekusi.
 * @author Kelompok AI Mosaic
 * @version 1.0
 */

public class MosaicSolverGA {

    // Class kecil untuk menampung hasil run agar bisa dihitung rata-ratanya
    private static class RunResult {
        double fitness;
        int generations;
        double time;

        public RunResult(double fitness, int generations, double time) {
            this.fitness = fitness;
            this.generations = generations;
            this.time = time;
        }
    }

    /**
     * @param args Array string yang berisi path file peta dan file parameter.
     * @throws Exception Jika terjadi kesalahan I/O atau format file.
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java MosaicSolverGA <filePeta.txt> <fileParameter.txt>");
            return;
        }

        String filePeta = "puzzle/" + args[0];
        String fileParameter = "parameter/" + args[1];

        MosaicPuzzle puzzle = InputReader.bacaPuzzle(filePeta);
        GAParamater param = InputReader.bacaParameter(fileParameter);

        int[] seeds = {12345, 12346, 12347, 12348, 12349};
        
        // Variabel untuk akumulasi rata-rata
        double totalFitness = 0;
        long totalGenerations = 0;
        double totalTime = 0;
        int count = 0;

        for (int seed : seeds) {
            // Jalankan solve dan tangkap hasilnya
            RunResult result = solve(puzzle, param, seed);
            
            // Tambahkan ke total
            totalFitness += result.fitness;
            totalGenerations += result.generations;
            totalTime += result.time;
            count++;

            System.out.println();
            System.gc(); // Garbage collection antar run
        }

        // === CETAK RATA-RATA SETELAH SEMUA SEED SELESAI ===
        System.out.println("==================================================");
        System.out.println("AVERAGE STATISTICS (" + count + " RUNS)");
        System.out.println("==================================================");
        System.out.printf("Average Best Fitness : %.6f%n", totalFitness / count);
        System.out.printf("Average Generations  : %.2f%n", (double)totalGenerations / count);
        System.out.printf("Average Time         : %.6f detik%n", totalTime / count);
        System.out.println("==================================================");
    }

    // Return type diubah dari void ke RunResult biar bisa lapor balik ke main
    public static RunResult solve(MosaicPuzzle puzzle, GAParamater param, int seed) {
        RNG.initialize(seed);

        System.out.println("Seed: " + seed);
        System.out.println("=== GA Parameters ===");
        System.out.println("Total generations: " + param.getTotalGeneration());
        System.out.println("Population size: " + param.getPopulationSize());
        System.out.printf("Crossover rate: %.4f%n", param.getCrossoverRate());
        System.out.printf("Mutation rate: %.4f%n", param.getMutationRate());
        System.out.printf("Elitism percent: %.4f%n", param.getElitismPercent());
        System.out.println("Tournament size: " + param.getSelectionSize());
        System.out.println("Selection method: '" + param.getSelectionMethod() + "'");
        System.out.println("Crossover method: '" + param.getCrossoverMethod() + "'");

        // Setup Method Crossover
        String rawMethod = param.getCrossoverMethod() == null ? "" : param.getCrossoverMethod().trim().toLowerCase();
        String method = "single-point"; 
        int k = 1; 

        if (rawMethod.contains("single") || rawMethod.contains("one")) {
            method = "single-point";
        } else if (rawMethod.contains("uniform")) {
            method = "uniform";
        } else if (rawMethod.startsWith("k-point") || rawMethod.startsWith("k_point")) {
            method = "k-point";
            String[] parts = rawMethod.split("[-_]");
            if (parts.length >= 3) {
                try {
                    k = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    System.out.println("Format angka k salah. Default k=1 (sama aja kek single-point).");
                }
            }
        } else {
            System.out.println("Warning: unknown method '" + rawMethod + "'. Defaulting to single-point.");
        }

        // BAGIAN GA
        FitnessFunction fitnessFunction = new FitnessFunction(puzzle);
        Selection seleksi = new Selection();
        Populasi populasi = new Populasi(param.getPopulationSize(), param.getProbabilitasHitam(), fitnessFunction, puzzle);

        System.out.println("=== Inisialisasi Populasi ===");
        System.out.println("Ukuran populasi: " + populasi.getPopulationSize());
        System.out.printf("Best fitness awal: %.6f%n", populasi.getBestIndividu().getFitness());
        System.out.printf("Average fitness awal: %.6f%n", populasi.getAverageFitness());
        System.out.println("Best individu (visual):");

        //Ambil individu terbaik dan print kromosomnya
        populasi.getBestIndividu().getKromosom().printCurrentFixedKromosom(); 

        Crossover crossover = new Crossover(fitnessFunction);
        long startTime = System.nanoTime();
        boolean ditemukan = false;
        
        // Variabel untuk nyimpen hasil akhir buat return
        double finalFitness = 0;
        int finalGen = 0;
        double finalTime = 0;

        for(int generasi = 0; generasi < param.getTotalGeneration(); generasi++) {
            List<Individu> newPopulation = new ArrayList<>(param.getPopulationSize());

            // 1. Elitism
            int jumlahElitism = (int)(param.getElitismPercent() * param.getPopulationSize());
            jumlahElitism = Math.max(0, Math.min(jumlahElitism, populasi.getPopulationSize()));
            newPopulation.addAll(populasi.getTopElitism(jumlahElitism));

            // 2. Isi populasi berikutnya
            while (newPopulation.size() < param.getPopulationSize()) {
                // Seleksi dua parent
                Individu parent1 = seleksi.select(populasi, param);
                Individu parent2 = seleksi.select(populasi, param);

                //simpan hasil crossover
                Individu[] offsprings;

                // Crossover terjadi berdasarkan probabilitas
                if(RNG.rand.nextDouble() < param.getCrossoverRate()) {
                    // Melakukan crossover
                    offsprings = crossover.crossover(parent1, parent2, method, k);
                } else {
                    // Tidak ada crossover, langsung copy parents
                    offsprings = new Individu[] { parent1.copyForOffspring(), parent2.copyForOffspring()};
                }

                // Proses mutasi dan heuristik pada setiap offspring
                for (Individu offspring : offsprings) {
                    // Pastikan populasi tidak melebihi batas
                    if (newPopulation.size() < param.getPopulationSize()) {
                        //variabel untuk menandai perlu tidaknya reset fitness berdasarkan perubahan
                        boolean perluResetFitness = false;
                        //ambil kromosom anak
                        Kromosom kromosom = offspring.getKromosom();

                        //mutasi setiap bit yang tidak fixed
                        for (int i = 0; i < kromosom.getLength(); i++) {
                            if (!kromosom.getFixedAllele(i)) {
                                if (RNG.rand.nextDouble() < param.getMutationRate()) {
                                    //set bit ke-i ke nilai sebaliknya
                                    kromosom.setBit(i, !kromosom.getBit(i));
                                    // tandai perlu reset fitness
                                    perluResetFitness = true;
                                }
                            }
                        }
                        
                        //Scaling heuristic
                        int intervalHeuristik;
                        if (generasi < 100000) {
                            intervalHeuristik = 500; // Awal: Sangat jarang, fokus ke eksplorasi acak
                        } else if (generasi < 300000) {
                            intervalHeuristik = 200; // Menengah: Mulai sering merapikan
                        } else {
                            intervalHeuristik = 100;  // Akhir: Sangat rajin untuk finishing detail
                        }

                        // Eksekusi Heuristik berdasarkan Interval atau Peluang Kecil (5%)
                        if (generasi % intervalHeuristik == 0 || RNG.rand.nextDouble() < 0.05) {
                            boolean adaPerubahan;
                            int limit = 0;
                            do {
                                // Terapkan heuristik fill certain sebanyak maksimal 3 kali
                                adaPerubahan = PenandaHeuristik.heuristicFillCertain(offspring.getKromosom());
                                if (adaPerubahan) perluResetFitness = true;
                                limit++;
                            } while (adaPerubahan && limit < 3); // Limit 3 cukup untuk performa
                        }

                        //KHUSUS SELAIN20X20 HARD
                        // boolean adaPerubahan;
                        // int limit = 0;
                        // do {
                        //     // Terapkan heuristik fill certain sebanyak maksimal 3 kali
                        //     adaPerubahan = PenandaHeuristik.heuristicFillCertain(offspring.getKromosom());
                        //     if (adaPerubahan) perluResetFitness = true;
                        //     limit++;
                        // } while (adaPerubahan && limit < 3); // Limit 3 cukup untuk performa
                        
                        // Reset fitness jika ada perubahan pada kromosom
                        if (perluResetFitness) offspring.resetFitness();
                        // Tambahkan offspring ke populasi baru
                        newPopulation.add(offspring);
                    }
                }
            }

            // 3. Update populasi
            populasi.setIndividuList(newPopulation);

            // 4. Debug (per 10000 generasi)
            if (generasi % 10000 == 0) {
                System.out.printf("Generasi %d | Best Fitness: %.6f | Avg Fitness: %.6f%n", 
                                generasi, populasi.getBestIndividu().getFitness(), populasi.getAverageFitness());
            }

            // Cek solusi ditemukan
            if (Math.abs(populasi.getBestIndividu().getFitness() - 1.0) < 1e-6) {
                long endTime = System.nanoTime();
                double totalTime = (endTime - startTime) / 1_000_000_000.0;

                System.out.println("Solusi ditemukan pada generasi " + generasi);
                System.out.printf("Best Fitness: %.6f\n", populasi.getBestIndividu().getFitness());
                System.out.printf("Total running time: %.6f detik%n", totalTime);
                // Print kromosom terbaik yang ditemukan
                populasi.getBestIndividu().getKromosom().printCurrentKromosom();
                ditemukan = true;
                
                // Set result sukses
                finalFitness = populasi.getBestIndividu().getFitness();
                finalGen = generasi;
                finalTime = totalTime;
                
                // Return result langsung
                return new RunResult(finalFitness, finalGen, finalTime);
            }
        }
        // Jika sampai sini berarti tidak ditemukan solusi sempurna
        if (!ditemukan) {
            long endTime = System.nanoTime();
            double totalTime = (endTime - startTime) / 1_000_000_000.0;
            System.out.printf("Best Fitness: %.6f\n", populasi.getBestIndividu().getFitness());
            System.out.printf("Total running time: %.6f detik%n", totalTime);
            // Print kromosom terbaik yang ditemukan
            populasi.getBestIndividu().getKromosom().printCurrentKromosom();
            
            finalFitness = populasi.getBestIndividu().getFitness();
            finalGen = param.getTotalGeneration();
            finalTime = totalTime;
            
            return new RunResult(finalFitness, finalGen, finalTime);
        }

        return new RunResult(0, 0, 0);
    }
    
}
