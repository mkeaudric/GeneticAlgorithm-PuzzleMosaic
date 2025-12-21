// sumber : https://natureofcode.com/genetic-algorithms/
public class MosaicSolverGA {
    public static void main(String[] args) throws Exception {
        // inisialisasi peta
        int size = Integer.parseInt(args[0]);
        int seed = Integer.parseInt(args[1]);
        RNG.initialize(seed);
        // males import package jadi gini dulu
        // ntar fix ini jadi baca nya baca .txt nya pake BacaInput
        MosaicPuzzle puzzle = InputReader.bacaPuzzle("mosaic-5x5-easy.txt");

        // bagian GA

        //import fitness function
        FitnessFunction fitnessFunction = new FitnessFunction(puzzle);

        // 1. inisialisasi populasi
        Populasi populasi = new Populasi(100, size, 0.5, fitnessFunction, puzzle);
        // 2. seleksi parent untuk populasi berikutnya
        // 3. crossover & mutasi
        // 4. ulang dari step 2 sampe fitness = 1 (ketemu solusi)
    }
    
}