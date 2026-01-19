// import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

public class FitnessFunction {
    private MosaicPuzzle puzzle;
    // Menyimpan koordinat sel yang berisi angka agar tidak looping seluruh grid berkali-kali
    private List<int[]> targetCells;
    private int totalCellAngka;

    public FitnessFunction(MosaicPuzzle puzzle) {
        this.puzzle = puzzle;
        this.precomputeTargetCells();
    }

    /**
     * Melakukan scan satu kali di awal untuk mencatat posisi angka.
     * Ini menghemat waktu eksekusi secara signifikan pada grid besar.
     */
    private void precomputeTargetCells() {
        this.targetCells = new ArrayList<>();
        int size = puzzle.getSize();
        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int target = puzzle.getNumber(row, col);
                if (target != -1) {
                    // Simpan row, col, dan target value
                    targetCells.add(new int[]{row, col, target});
                }
            }
        }
        this.totalCellAngka = targetCells.size();
    }

    public double calculateFitness(Kromosom kromosom) {
        double totalEror = 0;
        int cellYangSudahBenar = 0;
        int size = kromosom.getSize();
        //double penalti = 0.0;

        // Hanya iterasi pada sel yang memiliki angka (hasil precompute)
        for (int[] cell : targetCells) {
            int row = cell[0];
            int col = cell[1];
            int target = cell[2];

            int actual = countBlackNeighbors(kromosom, row, col, size);

            if (target == actual) {
                cellYangSudahBenar++;
            } else {
                // Menggunakan kuadrat sesuai logika Anda sebelumnya
                double diff = target - actual;
                totalEror += diff * diff;

                // if(actual > target){
                //     penalti += diff * 1.5; // Penalti lebih berat untuk kelebihan hitam
                // }
            }
        }

        // Jika tidak ada angka sama sekali di puzzle (kasus edge)
        if (totalCellAngka == 0) return 1.0;

        double accuracy = (double) cellYangSudahBenar / totalCellAngka;
        return (accuracy * 0.5) + (0.5 / (1 + totalEror));
    }

    private int countBlackNeighbors(Kromosom kromosom, int row, int col, int size) {
        int count = 0;
        for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
            int newRow = row + deltaRow;
            if (newRow < 0 || newRow >= size) continue;

            for (int deltaCol = -1; deltaCol <= 1; deltaCol++) {
                int newCol = col + deltaCol;
                if (newCol >= 0 && newCol < size) {
                    if (kromosom.getBit(newRow, newCol)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}