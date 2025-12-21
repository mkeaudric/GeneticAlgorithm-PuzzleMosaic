// sumber : https://natureofcode.com/genetic-algorithms/
public class MosaicSolverGA {
    public static void main(String[] args) throws Exception {
        System.out.println("java MosaicSolver <filePeta.txt> <fileParameter.txt>");

        // Baca peta
        String filePeta = args[0];
        String parameter = args[1];
        int seed = 12345;
        RNG.initialize(seed);
        // males import package jadi gini dulu
        // ntar fix ini jadi baca nya baca .txt nya pake BacaInput
        MosaicPuzzle puzzle = InputReader.bacaPuzzle(filePeta);
        GaParam param = InputReader.bacaPuzzle(parameter);

        // bagian GA

        //import fitness function
        FitnessFunction fitnessFunction = new FitnessFunction(puzzle);

        // 1. inisialisasi populasi
        int size = puzzle.getSize() * puzzle.getSize();
        Populasi populasi = new Populasi(100, size, 0.5, fitnessFunction, puzzle);
        // 2. seleksi parent untuk populasi berikutnya
        // 3. crossover & mutasi
        // 4. ulang dari step 2 sampe fitness = 1 (ketemu solusi)
    }
    
}