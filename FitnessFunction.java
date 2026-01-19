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

        //variabel untuk menghitung sel angka yang jumlah hitam di sekitarnya sudah benar
        int cellYangSudahBenar = 0;

        //variabel untuk menghitung ada berapa sel yang memiliki angka
        int cellAngka = 0;

        //loop melalui setiap sel pada papan
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int target = puzzle.getNumber(row, col);
                if (target == -1){
                    continue;
                }
                //jika sel ini memiliki angka, hitung
                cellAngka++;
                //hitung jumlah sel hitam di sekitarnya saat ini
                int actual = countBlackNeighbors(kromosom, row, col, size);

                //cek apakah sudah sesuai
                if (target == actual) {
                    cellYangSudahBenar++; //sel yang sudah benar bertambah
                } else {
                    //hitung error kuadratnya
                    totalEror += Math.pow(target - actual, 2);
                }
            }
        }
        //variabel untuk menghitung berapa sel yang sudah benar dibagi total sel angka
        double accuracy = (double) cellYangSudahBenar / cellAngka;

        //menghitung fitness dengan rumus gabungan accuracy dan penalti error
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
        //variabel menghitung jumlah sel hitam
        int count = 0;

        //loop melalui area 3x3 di sekitar (row, col)
        for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
            int newRow = row + deltaRow;
            //cek batas baris
            if (newRow < 0 || newRow >= size) continue;

            //loop kolom
            for (int deltaCol = -1; deltaCol <= 1; deltaCol++) {
                int newCol = col + deltaCol;
                //cek batas kolom
                if (newCol >= 0 && newCol < size) {
                    //cek apakah sel tersebut hitam
                    if (kromosom.getBit(newRow, newCol)) {
                        count++;
                    }
                }
            }
        }
        //return jumlah sel yang hitam
        return count;
    }
}
