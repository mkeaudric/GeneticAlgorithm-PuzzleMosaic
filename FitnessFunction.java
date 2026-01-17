/**
 * Kelas FitnessFunction bertanggung jawab untuk menghitung nilai kualitas (fitness) dari sebuah solusi atau individu.
 * Nilai fitness menentukan seberapa dekat solusi tersebut dengan aturan permainan.
 * @author Kelompok AI Mosaic
 * @version 1.0
 */
public class FitnessFunction {
    private MosaicPuzzle puzzle;
    
    /**
     * Konstruktor untuk menghubungkan fungsi fitness dengan data puzzle utama.
     * @param puzzle Objek MosaicPuzzle.
     */
    public FitnessFunction(MosaicPuzzle puzzle) {
        this.puzzle = puzzle;
    }

    /**
     * Hitung fitness dengan melihat sel yang memiliki angka dan membandingkannya dengan jumlah sel hitam di sekitarnya.
     * Menggunakan dua metrik: akurasi (rasio sel benar) dan penalti kesalahan kuadrat.
     * @param kromosom Individu yang akan dievaluasi representasi biner-nya.
     * @return Nilai double antara 0.0 (buruk) hingga 1.0 (solusi sempurna).
     */
    public double calculateFitness(Kromosom kromosom) {
        double totalEror = 0;

        int size = kromosom.getSize();

        int cellYangSudahBenar = 0;

        int cellAngka = 0;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int target = puzzle.getNumber(row, col);
                if (target == -1){
                    continue;
                }
                cellAngka++;

                int actual = countBlackNeighbors(kromosom, row, col, size);

                if (target == actual) {
                    cellYangSudahBenar++; // hitung sel yang sudah benar
                } else {
                    // eror pake kuadrat tidak lagi linear
                    totalEror += Math.pow(target - actual, 2);
                }
            }
        }
        double accuracy = (double) cellYangSudahBenar / cellAngka;

        return (accuracy * 0.5) + (0.5 / (1 + totalEror));
    }

   /**
     * Menghitung jumlah sel hitam di sekitar koordinat tertentu (termasuk sel itu sendiri).
     * Area pengecekan adalah grid 3x3.
     * @param kromosom Data individu yang diperiksa.
     * @param row Indeks baris sel pusat.
     * @param col Indeks kolom sel pusat.
     * @param size Batas ukuran papan untuk validasi koordinat.
     * @return Jumlah total sel bernilai true (hitam) dalam jangkauan.
     */ 
   private int countBlackNeighbors(Kromosom kromosom, int row, int col, int size) {
       int count = 0;

       //cek 8 tetangga dan sel itu sendiri
       for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
           for (int deltaCol = -1; deltaCol <= 1; deltaCol++) {
               int newRow = row + deltaRow;
               int newCol = col + deltaCol;

               //cek batas papan
               if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size) {
                   if (kromosom.getBit(newRow, newCol)) {
                       count++;
                   }
               }
           }
       }
       return count;
   }

   
}
