public class FitnessFunction {
    public static double calculateFitness(MosaicPuzzle puzzle, MosaicSolution solution) {
        int error = totalError(puzzle, solution);
        return 1.0 / (1 + error);
    }

    //method menghitung total error antara solusi dan puzzle
    private static int totalError(MosaicPuzzle puzzle, MosaicSolution solution) {
        int totalError = 0;
        int rows = puzzle.getRow();
        int cols = puzzle.getCol();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int expectedBlackCells = puzzle.getNumber(r, c);
                if(expectedBlackCells == -1) continue; 
                int actualBlackCells = countAdjacentBlackCells(solution, r, c);
                totalError += Math.abs(expectedBlackCells - actualBlackCells);
            }
        }
        return totalError;
    }

    //method menghitung jumlah sel hitam di sekitar sel tertentu
    private static int countAdjacentBlackCells(MosaicSolution solution, int r, int c) {
        int count = 0;
        int rows = solution.getRow();
        int cols = solution.getCol();

        //loop melalui sel di sekitar (r, c)
        for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
            for (int deltaCol = -1; deltaCol <= 1; deltaCol++) {
                int newRow = r + deltaRow;
                int nCol = c + deltaCol;

                //cek batas
                if (newRow >= 0 && newRow < rows && nCol >= 0 && nCol < cols) {
                    if (solution.isBlack(newRow, nCol)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
