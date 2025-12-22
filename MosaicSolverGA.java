// sumber : https://natureofcode.com/genetic-algorithms/
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

        // 1. inisialisasi populasi
        int size = puzzle.getSize() * puzzle.getSize();
        Populasi populasi = new Populasi(param.getPopulationSize(), size, param.getProbabilitasHitam(), fitnessFunction, puzzle);
        // 2. seleksi parent untuk populasi berikutnya
        // 3. crossover & mutasi
        // 4. ulang dari step 2 sampe fitness = 1 (ketemu solusi)
    }
    
}