// sumber : https://natureofcode.com/genetic-algorithms/

public class MosaicSolverGA {
    public static void main(String[] args) {
        // inisialisasi peta
        int size = Integer.parseInt(args[0]);
        int seed = Integer.parseInt(args[1]);
        // males import package jadi gini dulu
        MosaicPuzzle puzzle = GenerateProblem.generatePuzzle(size, seed);

        // bagian GA
        // 1. inisialisasi populasi
        // 2. seleksi parent untuk populasi berikutnya
        // 3. crossover & mutasi
        // 4. ulang dari step 2 sampe fitness = 1 (ketemu solusi)
    }
    
}